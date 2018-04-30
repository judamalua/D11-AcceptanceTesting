
package controllers.agent;

import java.util.ArrayList;
import java.util.Collection;

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
import controllers.AbstractController;
import domain.Actor;
import domain.Advertisement;
import domain.Configuration;
import domain.Newspaper;

@Controller
@RequestMapping("/advertisement/agent")
public class AdvertisementAgentController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	private AdvertisementService	advertisementService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;


	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(required = false) final Integer newspaperId, @RequestParam(required = false, defaultValue = "0") final Integer page) {
		ModelAndView result;
		final Page<Advertisement> advertisements;
		final Pageable pageable;
		Configuration configuration;
		Collection<Boolean> isAdvertised;
		Newspaper newspaper;

		try {
			result = new ModelAndView("advertisement/list");
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());

			advertisements = this.advertisementService.findByPrincipalPage(pageable);

			result.addObject("advertisements", advertisements.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", advertisements.getTotalPages());
			result.addObject("requestUri", "advertisement/agent/list.do");

			if (newspaperId != null) {
				newspaper = this.newspaperService.findOne(newspaperId);
				Assert.isTrue(newspaper.getPublicationDate() != null);
				result.addObject("newspaper", newspaper);
				isAdvertised = new ArrayList<Boolean>();
				for (final Advertisement ad : advertisements.getContent())
					isAdvertised.add(newspaper.getAdvertisements().contains(ad));
				result.addObject("isAdvertised", isAdvertised);
			}

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	// Creating -----------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Advertisement advertisement;

		try {
			advertisement = this.advertisementService.create();
			result = this.createEditModelAndView(advertisement);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final Integer advertisementId) {
		ModelAndView result;
		final Advertisement advertisement;
		Actor actor;

		try {
			advertisement = this.advertisementService.findOne(advertisementId);
			actor = this.actorService.findActorByPrincipal();
			Assert.isTrue(actor.getId() == advertisement.getAgent().getId());

			result = this.createEditModelAndView(advertisement);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("advertisement") Advertisement advertisement, final BindingResult binding) {
		ModelAndView result;
		Actor actor;
		try {
			advertisement = this.advertisementService.reconstruct(advertisement, binding);
		} catch (final Throwable oops) {
		}
		if (binding.hasErrors())
			result = this.createEditModelAndView(advertisement, "advertisement.params.error");
		else
			try {
				actor = this.actorService.findActorByPrincipal();

				Assert.isTrue(actor.getId() == advertisement.getAgent().getId());
				this.advertisementService.save(advertisement);
				result = new ModelAndView("redirect:/advertisement/agent/list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(advertisement, "advertisement.commit.error");
			}

		return result;
	}

	// Deleting -----------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@ModelAttribute("advertisement") Advertisement advertisement, final BindingResult binding) {
		ModelAndView result;
		final Actor actor;
		try {
			advertisement = this.advertisementService.reconstruct(advertisement, binding);
		} catch (final Throwable oops) {
		}

		try {
			actor = this.actorService.findActorByPrincipal();

			Assert.isTrue(actor.getId() == advertisement.getAgent().getId());

			this.advertisementService.delete(advertisement);

			result = new ModelAndView("redirect:/advertisement/agent/list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(advertisement, "advertisement.commit.error");
		}

		return result;
	}

	// Advertise ---------------------------------------------------------------		

	@RequestMapping("/advertise")
	public ModelAndView advertise(@RequestParam(required = true) final Integer newspaperId, @RequestParam(required = true) final Integer advertisementId, @RequestParam(required = false, defaultValue = "0") final Integer page) {
		ModelAndView result;
		Newspaper newspaper;
		Advertisement advertisement;

		try {
			newspaper = this.newspaperService.findOne(newspaperId);
			advertisement = this.advertisementService.findOne(advertisementId);
			this.advertisementService.advertise(advertisement, newspaper);
			result = new ModelAndView("redirect:/advertisement/agent/list.do?newspaperId=" + newspaperId);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	@RequestMapping("/unadvertise")
	public ModelAndView unadvertise(@RequestParam(required = true) final Integer newspaperId, @RequestParam(required = true) final Integer advertisementId, @RequestParam(required = false, defaultValue = "0") final Integer page) {
		ModelAndView result;
		Newspaper newspaper;
		Advertisement advertisement;

		try {
			newspaper = this.newspaperService.findOne(newspaperId);
			advertisement = this.advertisementService.findOne(advertisementId);
			this.advertisementService.unadvertise(advertisement, newspaper);
			result = new ModelAndView("redirect:/advertisement/agent/list.do?newspaperId=" + newspaperId);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Advertisement advertisement) {
		ModelAndView result;

		result = this.createEditModelAndView(advertisement, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Advertisement advertisement, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("advertisement/edit");
		result.addObject("advertisement", advertisement);
		result.addObject("message", messageCode);

		return result;

	}
}
