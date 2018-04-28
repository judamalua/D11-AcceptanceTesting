
package controllers.user;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.VolumeService;
import controllers.AbstractController;
import domain.Newspaper;
import domain.User;
import domain.Volume;

@Controller
@RequestMapping("/volume/user")
public class VolumeUserController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	private VolumeService	volumeService;

	@Autowired
	private ActorService	actorService;


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

		try {
			volume = this.volumeService.reconstruct(volume, binding);
		} catch (final Throwable oops) {
		}

		if (binding.hasErrors())
			result = this.createEditModelAndView(volume, "volume.params.error");
		else
			try {
				user = (User) this.actorService.findActorByPrincipal();

				Assert.isTrue(user.equals(volume.getUser()));

				savedVolume = this.volumeService.save(volume);

				result = new ModelAndView("redirect:/volume/display.do?volumeId=" + savedVolume.getId());
			} catch (final Throwable oops) {
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