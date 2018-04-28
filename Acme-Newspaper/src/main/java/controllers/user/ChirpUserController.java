
package controllers.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ChirpService;
import services.ConfigurationService;
import services.UserService;
import controllers.AbstractController;
import domain.Chirp;
import domain.Configuration;
import domain.User;

@Controller
@RequestMapping("/chirp/user")
public class ChirpUserController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	ChirpService			chirpService;
	@Autowired
	ActorService			actorService;
	@Autowired
	ConfigurationService	configurationService;
	@Autowired
	UserService				userService;


	/**
	 * List the chirps
	 * 
	 * @return a ModelAndView object with all the chirps
	 * @author Alejandro
	 */
	@RequestMapping(value = "/stream")
	public ModelAndView streamChirps(@RequestParam(defaultValue = "0") final int page) {
		ModelAndView result;
		//HashMap<Chirp, String> chirps = new HashMap<Chirp,String>();
		User user;
		Pageable pageable;
		Page<Chirp> chirpList;
		Configuration configuration;
		List<String> authors;
		try {

			result = new ModelAndView("chirp/stream");
			user = (User) this.actorService.findActorByPrincipal();
			configuration = this.configurationService.findConfiguration();

			pageable = new PageRequest(page, configuration.getPageSize());
			chirpList = this.chirpService.findFollowedUsersChirps(user.getId(), pageable);

			authors = this.mapUsers(chirpList.getContent());

			/**
			 * Adding the associated rendezvouses to an chirp
			 */

			result.addObject("chirps", chirpList.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", chirpList.getTotalPages());

			result.addObject("authors", authors);
			result.addObject("requestURI", "chirp/user/stream.do");
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

	/**
	 * List the chirps of created Rendezvouses
	 * 
	 * @return a ModelAndView object with all the chirps of the created Rendezvouses
	 * @author Alejandro
	 */
	@RequestMapping(value = "/list")
	public ModelAndView listChirps(@RequestParam(defaultValue = "0") final int page) {
		ModelAndView result;
		//HashMap<Chirp, String> chirps = new HashMap<Chirp,String>();
		User user;
		Pageable pageable;
		Page<Chirp> chirpList;
		Configuration configuration;
		List<String> authors;
		try {

			result = new ModelAndView("chirp/list");
			user = (User) this.actorService.findActorByPrincipal();
			//			for (User follow: user.getUsers()){
			//				for(Chirp chirpF : follow.getChirps()){
			//					chirps.put(chirpF, follow.getName());
			//				}
			//			}
			configuration = this.configurationService.findConfiguration();

			pageable = new PageRequest(page, configuration.getPageSize());
			chirpList = this.chirpService.findUserChirps(user.getId(), pageable);

			authors = this.mapUsers(chirpList.getContent());

			/**
			 * Adding the associated rendezvouses to an chirp
			 */

			result.addObject("chirps", chirpList.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", chirpList.getTotalPages());

			result.addObject("authors", authors);
			result.addObject("requestURI", "chirp/user/list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Creating ---------------------------------------------------------
	/**
	 * 
	 * Gets the form to create a new Chirp associated to the Rendezvous with id rendezvousId
	 * 
	 * @param rendezvousId
	 * @return a ModelAndView object with a form to create a new Chirp associated to
	 *         the Rendezvous with id rendezvousId
	 * @author Alejandro
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Chirp chirp;

		try {
			chirp = this.chirpService.create();
			result = this.createEditModelAndView(chirp);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Saving -------------------------------------------------------------------
	/**
	 * Saves the Chirp passed as parameter
	 * 
	 * @param chirp
	 * @param binding
	 * 
	 * @return a ModelAndView object with an error if exists or with a list of Chirps
	 * @author Ale
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Chirp chirp, final BindingResult binding) {
		ModelAndView result;
		User user;
		try {
			chirp = this.chirpService.reconstruct(chirp, binding);
		} catch (final Throwable oops) {//Not delete
		}
		if (binding.hasErrors())
			result = this.createEditModelAndView(chirp, "chirp.params.error");
		else
			try {
				user = (User) this.actorService.findActorByPrincipal();
				this.chirpService.save(chirp, user);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(chirp, "chirp.commit.error");
			}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Chirp chirp) {
		ModelAndView result;

		result = this.createEditModelAndView(chirp, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Chirp chirp, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("chirp/edit");
		result.addObject("chirp", chirp);

		result.addObject("message", messageCode);

		return result;

	}
}
