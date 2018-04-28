
package controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertisementService;
import services.ConfigurationService;
import controllers.AbstractController;
import domain.Advertisement;
import domain.Configuration;

@Controller
@RequestMapping("/advertisement/admin")
public class AdvertisementAdminController extends AbstractController {

	// Services -------------------------------------------------------
	@Autowired
	private AdvertisementService	advertisementService;

	@Autowired
	private ConfigurationService	configurationService;


	// Delete ---------------------------------------------------------
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int advertisementId) {
		ModelAndView result;
		Advertisement advertisement;

		try {
			advertisement = this.advertisementService.findOne(advertisementId);
			this.advertisementService.delete(advertisement);

			result = new ModelAndView("redirect:list.do");

		} catch (final Throwable oops) {
			result = new ModelAndView("misc/403");
		}

		return result;
	}

	// List taboo ---------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam(required = false, defaultValue = "0") final Integer page) {
		ModelAndView result;
		Page<Advertisement> advertisements;
		Configuration configuration;
		Pageable pageable;

		try {
			configuration = this.configurationService.findConfiguration();

			pageable = new PageRequest(page, configuration.getPageSize());

			advertisements = this.advertisementService.findTabooAdvertisements(pageable);

			result = new ModelAndView("chirp/list");

			result.addObject("advertisements", advertisements.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", advertisements.getTotalPages());
			result.addObject("requestUri", "advertisement/admin/list.do?");

		} catch (final Throwable oops) {
			result = new ModelAndView("rediect:/misc/403");
		}

		return result;
	}
}
