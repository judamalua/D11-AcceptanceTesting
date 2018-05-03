
package controllers.user;

import java.util.Collection;
import java.util.HashSet;

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
import services.ConfigurationService;
import services.CreditCardService;
import services.NewspaperService;
import services.VolumeService;
import controllers.AbstractController;
import domain.Configuration;
import domain.CreditCard;
import domain.Newspaper;
import domain.User;
import domain.Volume;

@Controller
@RequestMapping("/volume/user")
public class VolumeUserController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	private VolumeService			volumeService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private ConfigurationService	configurationService;


	// List created volumes ---------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(defaultValue = "0") final int page) {
		ModelAndView result;
		Page<Volume> volumes;
		Pageable pageable;
		Configuration configuration;
		User user;

		try {
			user = (User) this.actorService.findActorByPrincipal();
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());
			result = new ModelAndView("volume/list");

			volumes = this.volumeService.findVolumesByUser(user.getId(), pageable);

			result.addObject("volumes", volumes.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", volumes.getTotalPages());
			result.addObject("requestUri", "volume/user/list.do?");
			result.addObject("userId", user.getId());
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;

	}

	// Create volume ---------------------------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Volume volume;

		try {
			volume = this.volumeService.create();
			result = this.createEditModelAndView(volume);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	//Edit volume ----------------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(final int volumeId) {
		ModelAndView result;
		Volume volume;
		User principal, volumeCreator;

		try {
			volume = this.volumeService.findOne(volumeId);

			Assert.notNull(volume);

			principal = (User) this.actorService.findActorByPrincipal();
			volumeCreator = volume.getUser();

			Assert.isTrue(principal.equals(volumeCreator));

			result = this.createEditModelAndView(volume);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@ModelAttribute("volume") Volume volume, final BindingResult binding) {
		ModelAndView result;
		Volume savedVolume;
		User user;
		final Collection<CreditCard> creditCards;
		final Collection<Newspaper> newspapers;

		try {
			volume = this.volumeService.reconstruct(volume, binding);
		} catch (final Throwable oops) {
		}

		if (binding.hasErrors())
			result = this.createEditModelAndView(volume, "volume.params.error");
		else
			try {
				user = (User) this.actorService.findActorByPrincipal();

				Assert.isTrue(user.equals(volume.getUser()), "Not owner");

				Assert.isTrue(user.getNewspapers().containsAll(volume.getNewspapers()));

				if (volume.getId() != 0) {
					//If newspapers are added, the subscribers (customers) must be automatically subscribed to these new newspapers.
					creditCards = this.creditCardService.getCreditCardsByVolume(volume.getId());
					newspapers = volume.getNewspapers();

					for (final Newspaper n : new HashSet<>(newspapers)) {
						for (final CreditCard c : creditCards)
							if (!n.getCreditCards().contains(c))
								n.getCreditCards().add(c);
						this.newspaperService.save(n);
					}
				}

				savedVolume = this.volumeService.save(volume);

				result = new ModelAndView("redirect:/volume/display.do?volumeId=" + savedVolume.getId());
			} catch (final Throwable oops) {
				if (oops.getMessage().contains("Not owner"))
					result = new ModelAndView("redirect:/misc/403");
				else
					result = this.createEditModelAndView(volume, "volume.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(@ModelAttribute("volume") final Volume volume, final BindingResult binding) {
		ModelAndView result;

		try {
			this.volumeService.delete(volume);

			result = new ModelAndView("redirect:/volume/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(volume, "volume.commit.error");
		}

		return result;
	}
	// Ancilliary Methods ---------------------------------------------------------

	private ModelAndView createEditModelAndView(final Volume volume) {
		ModelAndView result;

		result = this.createEditModelAndView(volume, null);

		return result;
	}

	private ModelAndView createEditModelAndView(final Volume volume, final String message) {
		ModelAndView result;
		Collection<Newspaper> elegibleNewspapers;
		User user;

		user = (User) this.actorService.findActorByPrincipal();
		elegibleNewspapers = this.volumeService.getElegibleNewspaperForVolume(user);

		result = new ModelAndView("volume/edit");
		result.addObject("volume", volume);
		result.addObject("message", message);
		result.addObject("elegibleNewspapers", elegibleNewspapers);

		return result;
	}

}
