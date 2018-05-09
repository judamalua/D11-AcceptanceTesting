
package controllers.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdvertisementService;
import services.ConfigurationService;
import services.NewspaperService;
import controllers.AbstractController;
import domain.Advertisement;
import domain.Configuration;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper/agent")
public class NewspaperAgentController extends AbstractController {

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private AdvertisementService	advertisementService;


	// Constructors -----------------------------------------------------------

	public NewspaperAgentController() {
		super();
	}

	// Listing  ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(defaultValue = "0") final int page, final int advertisementId, final boolean hasAdvertisement) {
		ModelAndView result;
		Page<Newspaper> newspapers;
		Pageable pageable;
		Configuration configuration;
		Advertisement advertisement;
		try {
			advertisement = this.advertisementService.findOne(advertisementId);
			Assert.isTrue(advertisement.getAgent().getId() == this.actorService.findActorByPrincipal().getId());
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());
			result = new ModelAndView("newspaper/list");

			newspapers = this.newspaperService.findNewspapersWithAdvertisements(advertisementId, hasAdvertisement, pageable);

			result.addObject("newspapers", newspapers.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", newspapers.getTotalPages());
			result.addObject("requestUri", "newspaper/agent/list.do?advertisementId=" + advertisementId + "&hasAdvertisement=" + hasAdvertisement + "&");
		} catch (final Throwable throwable) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	// Listing  ---------------------------------------------------------------		

	@RequestMapping("/list-newspaper")
	public ModelAndView list(@RequestParam(defaultValue = "0") final int page, final boolean hasAdvertisement) {
		ModelAndView result;
		Page<Newspaper> newspapers;
		Pageable pageable;
		Configuration configuration;

		try {

			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());
			result = new ModelAndView("newspaper/list");

			if (hasAdvertisement)
				newspapers = this.newspaperService.findNewspapersWithAnyOwnAdvertisement(pageable);
			else
				newspapers = this.newspaperService.findNewspapersWithoutOwnAdvertisement(pageable);

			result.addObject("newspapers", newspapers.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", newspapers.getTotalPages());
			result.addObject("requestUri", "newspaper/agent/list-newspaper.do?hasAdvertisement=" + hasAdvertisement + "&");

		} catch (final Throwable throwable) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
}
