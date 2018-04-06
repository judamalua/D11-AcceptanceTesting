
package controllers.admin;

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
		final User user;

		try {
			chirp = this.chirpService.findOne(chirpId);
			//TODO user = this.userService.findUserByChirp(chirpId);
			this.chirpService.delete(chirp);

			result = new ModelAndView("redirect:/");

		} catch (final Throwable oops) {
			result = new ModelAndView("rediect:/misc/403");
		}

		return result;
	}

}
