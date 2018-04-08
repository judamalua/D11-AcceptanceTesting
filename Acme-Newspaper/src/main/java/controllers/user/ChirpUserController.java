package controllers.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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
import domain.User;

@Controller
@RequestMapping("/chirp/user")
public class ChirpUserController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	ChirpService		chirpService;
	@Autowired
	ActorService			actorService;
	@Autowired
	ConfigurationService	configurationService;
	@Autowired
	UserService				userService;



	/**
	 * List the chirps of created Rendezvouses
	 * 
	 * @return a ModelAndView object with all the chirps of the created Rendezvouses
	 * @author Alejandro
	 */
	@RequestMapping(value = "/stream")
	public ModelAndView streamChirps() {
		ModelAndView result;
		HashMap<Chirp, String> chirps = new HashMap<Chirp,String>();
		User user;
		try {

			result = new ModelAndView("chirp/stream");
			user = (User) this.actorService.findActorByPrincipal();
			for (User follow: user.getUsers()){
				for(Chirp chirpF : follow.getChirps()){
					chirps.put(chirpF, follow.getName());
				}
			}
			chirps = sortByChirps(chirps);

			/**
			 * Adding the associated rendezvouses to an chirp
			 */

			result.addObject("chirps", chirps.keySet());
			result.addObject("authors", chirps.values().toArray());
			result.addObject("requestURI", "chirp/user/stream.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	
	/**
	 * List the chirps of created Rendezvouses
	 * 
	 * @return a ModelAndView object with all the chirps of the created Rendezvouses
	 * @author Alejandro
	 */
	@RequestMapping(value = "/list")
	public ModelAndView listChirps() {
		ModelAndView result;
		HashMap<Chirp, String> chirps = new HashMap<Chirp,String>();
		User user;
		try {

			result = new ModelAndView("chirp/stream");
			user = (User) this.actorService.findActorByPrincipal();
			
				for(Chirp chirpF : user.getChirps()){
					chirps.put(chirpF, user.getName());
				}
			
			chirps = sortByChirps(chirps);

			/**
			 * Adding the associated rendezvouses to an chirp
			 */

			result.addObject("chirps", chirps.keySet());
			result.addObject("authors", Arrays.asList(chirps.values().toArray()));
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
		
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(chirp, "chirp.params.error");
		} else
			try {
				user = (User) this.actorService.findActorByPrincipal();
				this.chirpService.save(chirp, user);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(chirp, "chirp.commit.error");
			}

		return result;
	}
	
	
	private static HashMap<Chirp,String> sortByChirps(HashMap<Chirp,String> map) { 
	       List<Entry<Chirp,String>> list = new LinkedList<Entry<Chirp,String>>(map.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator<Object>() {
	            public int compare(Object o1, Object o2) {
	               return ((Comparable<Date>) ((Chirp)((Map.Entry) (o1)).getKey()).getMoment())
	                  .compareTo(((Chirp)((Map.Entry) (o1)).getKey()).getMoment());
	            }
	       });

	       // Here I am copying the sorted list in HashMap
	       // using LinkedHashMap to preserve the insertion order
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
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
