
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.CreditCardService;
import services.NewspaperService;
import services.VolumeService;
import domain.Actor;
import domain.Configuration;
import domain.CreditCard;
import domain.Customer;
import domain.Newspaper;
import domain.User;
import domain.Volume;

@Controller
@RequestMapping("/volume")
public class VolumeController extends AbstractController {

	@Autowired
	private VolumeService			volumeService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private CreditCardService		creditCardService;


	//Constructors ------------------------

	public VolumeController() {
		super();
	}

	// Listing  ---------------------------------------------------------------	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(defaultValue = "0") final int page) {
		ModelAndView result;
		Configuration configuration;
		Pageable pageable;
		Page<Volume> volumes;
		Actor actor;

		try {
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());

			result = new ModelAndView("volume/list");

			volumes = this.volumeService.findVolumes(pageable);

			if (this.actorService.getLogged()) {
				actor = this.actorService.findActorByPrincipal();

				if (actor instanceof User)
					result.addObject("userId", actor.getId());
			}

			result.addObject("volumes", volumes.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", volumes.getTotalPages());
			result.addObject("requestUri", "volume/list.do?");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Display  ---------------------------------------------------------------	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final Integer volumeId, @RequestParam(required = true, defaultValue = "0") final Integer pageNewspaper) {
		ModelAndView result;
		Volume volume;
		Pageable pageable;
		Configuration configuration;
		Page<Newspaper> newspapers;
		Actor actor;
		User user;
		Boolean subscriber, userIsCreator;
		Collection<CreditCard> creditCards;

		try {
			result = new ModelAndView("volume/display");

			volume = this.volumeService.findOne(volumeId);
			Assert.notNull(volume);

			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(pageNewspaper, configuration.getPageSize());

			newspapers = this.newspaperService.findNewspapersByVolume(volumeId, pageable);
			creditCards = this.creditCardService.getCreditCardsByVolume(volumeId);

			subscriber = false;

			if (this.actorService.getLogged()) {
				actor = this.actorService.findActorByPrincipal();

				if (actor instanceof Customer)
					for (final CreditCard creditCard : creditCards) {
						subscriber = creditCard.getCustomer().equals(actor);
						if (subscriber)
							break;
					}
				else if (actor instanceof User) {
					user = (User) actor;
					userIsCreator = false;

					if (user.equals(volume.getUser()))
						userIsCreator = true;

					result.addObject("userIsCreator", userIsCreator);
				}
			}

			result.addObject("subscriber", subscriber);
			result.addObject("volume", volume);
			result.addObject("newspapers", newspapers.getContent());
			result.addObject("page", pageNewspaper);
			result.addObject("pageNum", newspapers.getTotalPages());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
}
