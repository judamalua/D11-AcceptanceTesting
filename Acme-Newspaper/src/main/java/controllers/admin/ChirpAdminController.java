
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChirpService;
import services.UserService;
import controllers.AbstractController;
import domain.Chirp;
import domain.User;

@Controller
@RequestMapping("/chirp/admin")
public class ChirpAdminController extends AbstractController {

	// Services -------------------------------------------------------
	@Autowired
	ChirpService	chirpService;

	@Autowired
	UserService		userService;


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
			result = new ModelAndView("misc/403");
		}

		return result;
	}

	// List taboo ---------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Collection<Chirp> chirps;

		try {
			chirps = this.chirpService.getAllTabooChirps();

			result = new ModelAndView("chirp/list");

			result.addObject("chirps", chirps);
			result.addObject("requestUri", "chirp/admin/list.do?");

		} catch (final Throwable oops) {
			result = new ModelAndView("rediect:/misc/403");
		}

		return result;
	}
}
