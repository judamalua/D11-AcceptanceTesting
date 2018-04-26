
package controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.VolumeService;
import controllers.AbstractController;
import domain.Volume;

@Controller
@RequestMapping("/volume/user")
public class VolumeUserController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	private VolumeService	volumeService;


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
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@ModelAttribute("volume") Volume volume, final BindingResult binding) {
		ModelAndView result;
		Volume savedVolume;

		try {
			volume = this.volumeService.reconstruct(volume, binding);
		} catch (final Throwable oops) {
		}

		if (binding.hasErrors())
			result = this.createEditModelAndView(volume, "volume.params.error");
		else
			try {
				savedVolume = this.volumeService.save(volume);

				result = new ModelAndView("redirect:/volume/display.do?volumeId=" + savedVolume.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(volume, "newspaper.commit.error");
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

		result = new ModelAndView("volume/edit");
		result.addObject("volume", volume);
		result.addObject("message", message);

		return result;
	}

}
