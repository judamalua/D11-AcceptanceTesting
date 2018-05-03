
package controllers.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChirpService;
import services.ConfigurationService;
import services.UserService;
import controllers.AbstractController;
import domain.Chirp;
import domain.Configuration;

@Controller
@RequestMapping("/chirp/admin")
public class ChirpAdminController extends AbstractController {

	// Services -------------------------------------------------------
	@Autowired
	private ChirpService			chirpService;

	@Autowired
	private UserService				userService;

	@Autowired
	private ConfigurationService	configurationService;


	// Delete ---------------------------------------------------------
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int chirpId) {
		ModelAndView result;
		Chirp chirp;

		try {
			chirp = this.chirpService.findOne(chirpId);
			this.chirpService.delete(chirp);

			result = new ModelAndView("redirect:list.do");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// List taboo ---------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam(required = false, defaultValue = "0") final Integer page) {
		ModelAndView result;
		Page<Chirp> chirps;
		List<String> authors;
		Configuration configuration;
		Pageable pageable;

		try {
			configuration = this.configurationService.findConfiguration();

			pageable = new PageRequest(page, configuration.getPageSize());

			chirps = this.chirpService.getAllTabooChirps(pageable);

			authors = this.mapUsers(chirps.getContent());
			result = new ModelAndView("chirp/list");

			result.addObject("chirps", chirps.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", chirps.getTotalPages());
			result.addObject("authors", authors);
			result.addObject("requestURI", "chirp/admin/list.do?");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	private List<String> mapUsers(final List<Chirp> chirpList) {
		final List<String> result = new ArrayList<String>();
		for (final Chirp c : chirpList)
			result.add(this.userService.findUserByChirp(c.getId()).getName());
		return result;
	}
}
