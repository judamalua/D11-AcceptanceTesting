
package controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import services.TagService;
import controllers.AbstractController;
import domain.Configuration;
import domain.Tag;

@Controller
@RequestMapping("/tag/admin")
public class TagAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private TagService				tagService;


	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(defaultValue = "0", required = false) final Integer page) {
		ModelAndView result;
		Page<Tag> tags;
		final Configuration configuration;
		Pageable pageable;

		result = new ModelAndView("tag/list");
		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(page, configuration.getPageSize());
		tags = this.tagService.findAll(pageable);

		result.addObject("tags", tags.getContent());
		result.addObject("page", page);
		result.addObject("pageNum", tags.getTotalPages());

		return result;
	}

	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final Integer tagId) {
		ModelAndView result;
		Tag tag;
		Assert.isTrue(tagId != 0);

		tag = this.tagService.findOne(tagId);

		result = this.createEditModelAndView(tag);

		return result;
	}

	// Creating ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Tag tag;

		tag = this.tagService.create();

		result = this.createEditModelAndView(tag);

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Tag tag, final BindingResult binding) {
		ModelAndView result;
		try {
			tag = this.tagService.reconstruct(tag, binding);
		} catch (final Throwable oops) {
		}
		if (binding.hasErrors())
			result = this.createEditModelAndView(tag, "tag.params.error");
		else
			try {
				this.tagService.save(tag);
				result = new ModelAndView("redirect:/tag/admin/list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(tag, "tag.commit.error");
			}

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView deelte(Tag tag, final BindingResult binding) {
		ModelAndView result;

		try {
			tag = this.tagService.findOne(tag.getId());
			this.tagService.delete(tag);
			result = new ModelAndView("redirect:/tag/admin/list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(tag, "tag.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Tag tag) {
		ModelAndView result;

		result = this.createEditModelAndView(tag, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Tag tag, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("tag/edit");
		result.addObject("tag", tag);

		result.addObject("message", messageCode);

		return result;

	}
}
