
package controllers;

import java.util.ArrayList;
import java.util.Collection;

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

import services.ActorService;
import services.ConfigurationService;
import services.MessageFolderService;
import domain.Actor;
import domain.Configuration;
import domain.MessageFolder;

@Controller
@RequestMapping("/messageFolder")
public class MessageFolderController extends AbstractController {

	// Services -------------------------------------------------------
	@Autowired
	private MessageFolderService	messageFolderService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public MessageFolderController() {
		super();
	}

	// listing ---------------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) final Integer messageFolderId, @RequestParam(required = false, defaultValue = "0") final Integer page) {
		ModelAndView result;
		MessageFolder messageFolder = null;
		Page<MessageFolder> messageFolders;
		final Pageable pageable;
		Configuration configuration;
		Actor actor;

		try {
			result = new ModelAndView("messageFolder/list");
			configuration = this.configurationService.findConfiguration();

			pageable = new PageRequest(page, configuration.getPageSize());

			if (messageFolderId != null) {
				actor = this.actorService.findActorByPrincipal();
				messageFolder = this.messageFolderService.findOne(messageFolderId);
				Assert.isTrue(actor.getMessageFolders().contains(messageFolder));
				messageFolders = this.messageFolderService.findMessageFolderChildren(messageFolderId, pageable);
			} else
				messageFolders = this.messageFolderService.findRootMessageFolders(pageable);

			result.addObject("messageFolders", messageFolders.getContent());
			result.addObject("father", messageFolder);
			result.addObject("page", page);
			result.addObject("pageNum", messageFolders.getTotalPages());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
	// Editing -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int messageFolderId) {
		ModelAndView result;
		MessageFolder messageFolder;
		Actor actor;

		try {
			actor = this.actorService.findActorByPrincipal();
			messageFolder = this.messageFolderService.findOne(messageFolderId);
			Assert.isTrue(!messageFolder.getIsDefault());
			Assert.isTrue(actor.getMessageFolders().contains(messageFolder));
			result = this.createEditModelAndView(messageFolder);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(MessageFolder messageFolder, final BindingResult binding) {
		ModelAndView result;
		try {
			messageFolder = this.messageFolderService.reconstruct(messageFolder, binding);
		} catch (final Throwable oops) {
			oops.printStackTrace();
		}
		if (binding.hasErrors())
			result = this.createEditModelAndView(messageFolder, "messageFolder.params.error");
		else
			try {
				this.messageFolderService.save(messageFolder);

				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(messageFolder, "messageFolder.commit.error");
			}

		return result;
	}

	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(MessageFolder messageFolder, final BindingResult binding) {
		ModelAndView result;

		try {
			messageFolder = this.messageFolderService.findOne(messageFolder.getId());
			Assert.isTrue(this.actorService.findActorByPrincipal().getMessageFolders().contains(messageFolder));
			this.messageFolderService.delete(messageFolder);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(messageFolder, "messageFolder.commit.error");
		}

		return result;
	}

	// Creating -----------------------------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		MessageFolder messageFolder;
		try {
			messageFolder = this.messageFolderService.create();

			result = this.createEditModelAndView(messageFolder);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}

	// Ancillary methods ---------------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final MessageFolder messageFolder) {
		ModelAndView result;

		result = this.createEditModelAndView(messageFolder, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final MessageFolder messageFolder, final String messageCode) {
		ModelAndView result;
		Actor actor;
		Collection<MessageFolder> messageFolders;
		try {
			actor = this.actorService.findActorByPrincipal();
			result = new ModelAndView("messageFolder/edit");
			messageFolders = new ArrayList<>(actor.getMessageFolders());
			messageFolders.removeAll(this.messageFolderService.getHerency(messageFolder));
			messageFolders.remove(messageFolder);

			result.addObject("messageFolder", messageFolder);
			result.addObject("messageFolders", messageFolders);

			result.addObject("message", messageCode);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
}
