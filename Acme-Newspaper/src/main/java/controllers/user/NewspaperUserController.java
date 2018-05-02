
package controllers.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdvertisementService;
import services.ConfigurationService;
import services.NewspaperService;
import services.UserService;

import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.Entailment;
import com.textrazor.annotations.Word;

import controllers.AbstractController;
import domain.Actor;
import domain.Advertisement;
import domain.Article;
import domain.Configuration;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping("/newspaper/user")
public class NewspaperUserController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	public NewspaperService		newspaperService;

	@Autowired
	public ActorService			actorService;

	@Autowired
	public UserService			userService;

	@Autowired
	public AdvertisementService	advertisementService;

	@Autowired
	public ConfigurationService	configurationService;


	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam final Boolean published, @RequestParam(required = false, defaultValue = "0") final Integer page) {
		ModelAndView result;
		Page<Newspaper> newspapers;
		final Pageable pageable;
		Configuration configuration;
		Actor actor;
		Collection<Boolean> canPublicate;
		boolean allFinalMode = true;

		try {
			result = new ModelAndView("newspaper/list");
			actor = this.actorService.findActorByPrincipal();
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());
			canPublicate = new ArrayList<>();

			if (published)
				newspapers = this.userService.findPublishedNewspapersByUser(actor.getId(), pageable);
			else
				newspapers = this.userService.findNotPublishedNewspapersByUser(actor.getId(), pageable);

			for (final Newspaper newspaper : newspapers.getContent()) {
				allFinalMode = true;
				if (newspaper.getArticles().size() == 0)
					allFinalMode = false;
				for (final Article article : newspaper.getArticles())
					allFinalMode &= article.getFinalMode();
				canPublicate.add(allFinalMode);
			}
			result.addObject("owner", true);
			result.addObject("canPublicate", canPublicate);
			result.addObject("newspapers", newspapers.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", newspapers.getTotalPages());
			result.addObject("requestUri", "newspaper/user/list.do?published=" + published + "&");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final Integer newspaperId) {
		ModelAndView result;
		Newspaper newspaper;
		final User publisher;
		Actor actor;
		try {
			actor = this.actorService.findActorByPrincipal();

			newspaper = this.newspaperService.findOne(newspaperId);
			publisher = this.userService.findUserByNewspaper(newspaper.getId());

			Assert.isTrue(actor.equals(publisher));
			Assert.isTrue(newspaper.getPublicationDate() == null);

			result = this.createEditModelAndView(newspaper);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Newspaper newspaper;

		this.actorService.checkUserLogin();

		newspaper = this.newspaperService.create();

		result = this.createEditModelAndView(newspaper);

		return result;
	}

	@RequestMapping(value = "/publish", method = RequestMethod.GET)
	public ModelAndView publish(@RequestParam final Integer newspaperId) {
		ModelAndView result;
		Newspaper newspaper;
		Actor actor;
		User publisher;
		final TextRazor client;
		AnalyzedText response, responseNewspaper;
		final Collection<Advertisement> advertisements;
		Collection<Entailment> entailments;
		Collection<Word> words;
		Map<String, Double> entailmentScore;
		Map<String, Double> wordScore;
		Collection<String> entailedWords;
		String allStrings;
		Double maxScore;
		Advertisement maxAdvertisement = null;

		try {
			actor = this.actorService.findActorByPrincipal();

			newspaper = this.newspaperService.findOne(newspaperId);
			publisher = this.userService.findUserByNewspaper(newspaperId);

			Assert.isTrue(actor.equals(publisher));
			Assert.isTrue(newspaper.getPublicationDate() == null);
			Assert.isTrue(newspaper.getArticles().size() > 0);

			client = new TextRazor("2a7775eb6f2695154d305aad841da8856f2c633f5a0541d3208dad40");

			client.addExtractor("words");
			client.addExtractor("entailments");
			client.setCleanupHTML(true);

			entailmentScore = new HashMap<>();
			wordScore = new HashMap<>();
			entailedWords = new HashSet<>();
			maxScore = Double.MIN_VALUE;
			allStrings = "";

			newspaper.setPublicationDate(new Date(System.currentTimeMillis() - 1));

			for (final Article article : newspaper.getArticles())
				Assert.isTrue(article.getFinalMode());

			advertisements = this.advertisementService.findAll();

			for (final Advertisement advertisement : advertisements)
				try {
					response = client.analyzeUrl(advertisement.getAdditionalInfoLink());
					entailments = response.getResponse().getEntailments();

					if (entailments != null)
						for (final Entailment entailment : entailments) {
							System.out.println("Matched Entailments: " + entailment.getEntailedWords() + " score:" + entailment.getScore());
							for (final String entailedWord : entailedWords)
								if (entailmentScore.containsKey(entailedWord))
									entailmentScore.put(entailedWord, entailmentScore.get(entailedWord) + entailment.getScore());
								else
									entailmentScore.put(entailedWord, entailment.getScore());
						}
					else {
						words = response.getResponse().getWords();
						for (final Word word : words) {
							System.out.println("Matched word: " + word.getLemma() + " score:" + word.getLemma());
							if (wordScore.containsKey(word.getLemma()))
								wordScore.put(word.getLemma(), wordScore.get(word.getLemma()) + 1.);
							else
								wordScore.put(word.getLemma(), 1.);
						}
					}
					allStrings += newspaper.getTitle() + " " + newspaper.getDescription();

					for (final Article article : newspaper.getArticles())
						allStrings += " " + article.getTitle() + " " + article.getSummary() + " " + article.getBody();

					responseNewspaper = client.analyze(allStrings);

					if (entailments != null)
						for (final Entailment entailment : responseNewspaper.getResponse().getEntailments()) {
							System.out.println("Matched Entailments: " + entailment.getEntailedWords() + " score:" + entailment.getScore());
							for (final String entailedWord : entailedWords)
								if (entailmentScore.containsKey(entailedWord) && maxScore < entailmentScore.get(entailedWord)) {
									maxScore = entailmentScore.get(entailedWord);
									maxAdvertisement = advertisement;
								}
						}
					else
						for (final Word word : responseNewspaper.getResponse().getWords()) {
							System.out.println("Matched word: " + word.getLemma() + " score:" + word.getLemma());

							if (wordScore.containsKey(word.getLemma()) && maxScore < entailmentScore.get(word.getLemma())) {
								maxScore = wordScore.get(word.getLemma());
								maxAdvertisement = advertisement;
							}
						}

				} catch (final NetworkException e) {
				} catch (final AnalysisException e) {
				}

			newspaper.getAdvertisements().add(maxAdvertisement);

			this.newspaperService.save(newspaper);

			result = new ModelAndView("redirect:/newspaper/user/list.do?published=true");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("newspaper") Newspaper newspaper, final BindingResult binding) {
		ModelAndView result;
		final User publisher;
		final Actor actor;
		Newspaper savedNewspaper;
		try {
			newspaper = this.newspaperService.reconstruct(newspaper, binding);
		} catch (final Throwable oops) {
		}
		if (binding.hasErrors())
			result = this.createEditModelAndView(newspaper, "newspaper.params.error");
		else
			try {

				savedNewspaper = this.newspaperService.save(newspaper);

				publisher = this.userService.findUserByNewspaper(savedNewspaper.getId());
				actor = this.actorService.findActorByPrincipal();

				Assert.isTrue(actor.equals(publisher));
				result = new ModelAndView("redirect:/newspaper/user/list.do?published=false");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(newspaper, "newspaper.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@ModelAttribute("newspaper") Newspaper newspaper, final BindingResult binding) {
		ModelAndView result;
		final User publisher;
		final Actor actor;
		try {
			newspaper = this.newspaperService.reconstruct(newspaper, binding);
		} catch (final Throwable oops) {
		}

		try {

			publisher = this.userService.findUserByNewspaper(newspaper.getId());
			actor = this.actorService.findActorByPrincipal();

			Assert.isTrue(actor.equals(publisher));

			this.newspaperService.delete(newspaper);

			result = new ModelAndView("redirect:/newspaper/user/list.do?published=false");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(newspaper, "newspaper.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Newspaper newspaper) {
		ModelAndView result;

		result = this.createEditModelAndView(newspaper, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Newspaper newspaper, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("newspaper/edit");
		result.addObject("newspaper", newspaper);
		result.addObject("message", messageCode);

		return result;

	}
}
