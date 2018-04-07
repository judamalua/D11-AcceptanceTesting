
package controllers.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.NewspaperService;
import services.UserService;
import controllers.AbstractController;
import domain.Actor;
import domain.Configuration;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper/customer")
public class NewspaperCustomerController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	NewspaperService		newspaperService;

	@Autowired
	ActorService			actorService;

	@Autowired
	UserService				userService;

	@Autowired
	ConfigurationService	configurationService;


	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(required = false, defaultValue = "0") final Integer page) {
		ModelAndView result;
		Page<Newspaper> newspapers;
		final Pageable pageable;
		Configuration configuration;
		Actor actor;
		try {
			result = new ModelAndView("newspaper/list");
			actor = this.actorService.findActorByPrincipal();
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());

			newspapers = this.newspaperService.findSubscribedNewspapersByUser(actor.getId(), pageable);

			result.addObject("owner", true);
			result.addObject("newspapers", newspapers.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", newspapers.getTotalPages());
			result.addObject("requestUri", "newspaper/customer/list.do?");

		} catch (final Throwable oops) {
			result = new ModelAndView("rediect:/misc/403");
		}
		return result;
	}

}
