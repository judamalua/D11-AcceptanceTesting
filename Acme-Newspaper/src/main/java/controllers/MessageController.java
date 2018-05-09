
package controllers;

import java.util.Collection;
import java.util.HashSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.MessageFolderService;
import services.MessageService;
import domain.Actor;
import domain.Configuration;
import domain.Message;
import domain.MessageFolder;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController {

	// Services -------------------------------------------------------
	@Autowired
	private MessageService			messageService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private MessageFolderService	messageFolderService;

	@Autowired
	private ConfigurationService	configurationService;


	// Listing ----------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET, params = "messageFolderId")
	public ModelAndView list(@RequestParam final int messageFolderId, @RequestParam(required = false, defaultValue = "0") final Integer page) {
		ModelAndView result;
		final MessageFolder messageFolder;
		Page<Message> messages;
		final Pageable pageable;
		Configuration configuration;
		Actor actor;

		try {
			messageFolder = this.messageFolderService.findOne(messageFolderId);

			actor = this.actorService.findActorByPrincipal();
			Assert.isTrue(actor.getMessageFolders().contains(messageFolder));
			configuration = this.configurationService.findConfiguration();

			pageable = new PageRequest(page, configuration.getPageSize());

			messages = this.messageService.findMessagesByMessageFolderId(messageFolderId, pageable);

			result = new ModelAndView("message/list");
			result.addObject("messageFolder", messageFolder);
			result.addObject("messages", messages.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", messages.getTotalPages());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = {
		"save"
	})
	public ModelAndView save(@RequestParam(value = "broadcast", required = false, defaultValue = "false") final Boolean broadcast, @ModelAttribute("modelMessage") Message message, final BindingResult binding) {
		ModelAndView result;
		MessageFolder messageFolderIn, outBox;
		Actor actor;

		try {
			message = this.messageService.reconstruct(message, binding);
		} catch (final Throwable oops) {
		}
		if (binding.hasErrors())
			result = this.createEditModelAndView(message, "message.params.error");
		else
			try {
				Assert.isTrue(message.getId() == 0);
				actor = this.actorService.findActorByPrincipal();
				if (broadcast == true) {
					message.setReceiver(null);
					this.messageService.broadcastNotification(message);
				} else {
					messageFolderIn = this.messageFolderService.findMessageFolder("in box", message.getReceiver());
					this.actorService.sendMessage(message, message.getSender(), message.getReceiver(), messageFolderIn);
				}
				outBox = this.messageFolderService.findMessageFolder("out box", actor);
				result = new ModelAndView("redirect:list.do?messageFolderId=" + outBox.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(message, "message.commit.error");
			}

		return result;
	}
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam("messageId") final int messageId, @RequestParam("messageFolderId") final int messageFolderId) {
		ModelAndView result;
		Message message;

		try {
			message = this.messageService.findOne(messageId);
			this.messageService.delete(message);

			result = new ModelAndView("redirect:/message/list.do?messageFolderId=" + messageFolderId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:misc/403");
		}

		return result;
	}
	@RequestMapping(value = "/move", method = RequestMethod.GET)
	public ModelAndView move(@RequestParam final int messageId) {
		ModelAndView result;
		Actor actor;
		Collection<MessageFolder> messageFolders;
		try {
			actor = this.actorService.findActorByPrincipal();
			messageFolders = actor.getMessageFolders();

			result = new ModelAndView("message/move");
			result.addObject("messageFolders", messageFolders);
			result.addObject("messageId", messageId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:misc/403");
		}
		return result;
	}

	@RequestMapping(value = "/move", method = RequestMethod.POST, params = {
		"selectedMessageFolder", "messageId", "save"
	})
	public ModelAndView SaveMove(@ModelAttribute("messageId") final int messageId, @ModelAttribute("selectedMessageFolder") @Valid final MessageFolder selectedMessageFolder, final BindingResult binding) {
		ModelAndView result;
		Message message;

		if (binding.hasErrors())
			result = new ModelAndView("redirect:move.do");
		else
			try {
				message = this.messageService.findOne(messageId);
				this.actorService.moveMessage(message, selectedMessageFolder);
				result = new ModelAndView("redirect:/message/list.do?messageFolderId=" + message.getMessageFolder().getId());
			} catch (final Throwable oops) {
				result = new ModelAndView("redirect:move.do");
			}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Message message;
		try {
			message = this.messageService.create();

			result = this.createEditModelAndView(message);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:misc/403");
		}
		return result;
	}
	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Message message) {
		ModelAndView result;

		result = this.createEditModelAndView(message, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Message message, final String messageCode) {
		ModelAndView result;
		final Collection<Actor> actors;
		Collection<String> priorities;

		actors = this.actorService.findAll();

		priorities = new HashSet<>();
		priorities.add("HIGH");
		priorities.add("LOW");
		priorities.add("NEUTRAL");

		result = new ModelAndView("message/edit");
		result.addObject("modelMessage", message);
		result.addObject("actors", actors);
		result.addObject("message", messageCode);

		return result;

	}
}
