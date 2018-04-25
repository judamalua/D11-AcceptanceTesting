
package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import services.VolumeService;
import domain.Configuration;
import domain.Volume;

@Controller
@RequestMapping("/volume")
public class VolumeController extends AbstractController {

	@Autowired
	private VolumeService			volumeService;

	@Autowired
	private ConfigurationService	configurationService;


	//Constructors ------------------------

	public VolumeController() {
		super();
	}

	// Listing  ---------------------------------------------------------------	
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(defaultValue = "0") final int page) {
		ModelAndView result;
		Configuration configuration;
		Pageable pageable;
		Page<Volume> volumes;

		try {
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());

			result = new ModelAndView("volume/list");

			volumes = this.volumeService.findVolumes(pageable);

			result.addObject("volumes", volumes.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", volumes.getTotalPages());
			result.addObject("requestUri", "volume/list.do?");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

}
