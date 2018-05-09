
package services;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;
import domain.Message;
import domain.MessageFolder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MessageServiceTest extends AbstractTest {

	@Autowired
	public ActorService			actorService;
	@Autowired
	public MessageService		messageService;
	@Autowired
	public MessageFolderService	messageFolderService;
	@Autowired
	public AdminService			adminService;


	/**
	 * Functional requirement number 13: An actor who is authenticated must be able to:
	 * Exchange messages with other actors and manage them, which includes deleting and moving them from
	 * one folder to another folder.
	 * 
	 * Manage his or her message folders, except for the system folders
	 * 
	 * @author MJ
	 */
	@Test
	public void testCreate() {
		super.authenticate("admin1");
		Message message;
		message = this.messageService.create();

		Assert.notNull(message);
		Assert.isNull(message.getBody());
		Assert.isNull(message.getPriority());
		Assert.isNull(message.getReceiver());
		super.unauthenticate();

	}

	/**
	 * Functional requirement number 13: An actor who is authenticated must be able to:
	 * Exchange messages with other actors and manage them, which includes deleting and moving them from
	 * one folder to another folder.
	 * 
	 * Manage his or her message folders, except for the system folders
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCreateNotLoggedNegative() {
		Message message;
		message = this.messageService.create();

		Assert.notNull(message);
		Assert.isNull(message.getBody());
		Assert.isNull(message.getPriority());
		Assert.isNull(message.getReceiver());

	}

	/**
	 * Functional requirement number 13: An actor who is authenticated must be able to:
	 * Exchange messages with other actors and manage them, which includes deleting and moving them from
	 * one folder to another folder.
	 * 
	 * Manage his or her message folders, except for the system folders
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFindOneNotExistingNegative() {
		super.authenticate("admin");
		this.messageService.findOne(0);
		super.unauthenticate();
	}

	/**
	 * Functional requirement number 13: An actor who is authenticated must be able to:
	 * Exchange messages with other actors and manage them, which includes deleting and moving them from
	 * one folder to another folder.
	 * 
	 * Manage his or her message folders, except for the system folders
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFindOneNotOwner() {
		super.authenticate("user2");
		final Integer messageId;
		final Message result;

		messageId = super.getEntityId("Message1");

		result = this.messageService.findOne(messageId);
		Assert.notNull(result);
		super.unauthenticate();
	}

	/**
	 * Functional requirement number 13: An actor who is authenticated must be able to:
	 * Exchange messages with other actors and manage them, which includes deleting and moving them from
	 * one folder to another folder.
	 * 
	 * Manage his or her message folders, except for the system folders
	 * 
	 * @author MJ
	 */
	@Test
	public void testSave() {
		super.authenticate("user1");
		final Actor actor;
		final MessageFolder messageFolder;
		final Message message, savedMessage;

		actor = this.actorService.findActorByPrincipal();

		messageFolder = (MessageFolder) actor.getMessageFolders().toArray()[0];
		message = this.messageService.create();
		message.setBody("Body 1");
		message.setMessageFolder(messageFolder);
		message.setPriority("LOW");
		message.setReceiver((Actor) this.actorService.findAll().toArray()[0]);
		message.setReceptionDate(new Date());
		message.setSender(actor);
		message.setSubject("Subject 1");

		savedMessage = this.messageService.save(message);
		Assert.isTrue(this.messageService.findAll().contains(savedMessage));
		Assert.isTrue(actor.getMessageFolders().contains(message.getMessageFolder()));
		super.unauthenticate();
	}

	/**
	 * Functional requirement number 13: An actor who is authenticated must be able to:
	 * Exchange messages with other actors and manage them, which includes deleting and moving them from
	 * one folder to another folder.
	 * 
	 * Manage his or her message folders, except for the system folders
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSaveNotLoggedNegative() {
		super.unauthenticate();
		final Actor actor;
		final MessageFolder messageFolder;
		final Message message, savedMessage;

		actor = this.actorService.findActorByPrincipal();

		messageFolder = (MessageFolder) actor.getMessageFolders().toArray()[0];
		message = this.messageService.create();
		message.setBody("Body 1");
		message.setMessageFolder(messageFolder);
		message.setPriority("LOW");
		message.setReceiver((Actor) this.actorService.findAll().toArray()[0]);
		message.setReceptionDate(new Date());
		message.setSender(actor);
		message.setSubject("Subject 1");

		savedMessage = this.messageService.save(message);
		Assert.isTrue(this.messageService.findAll().contains(savedMessage));
		Assert.isTrue(actor.getMessageFolders().contains(message.getMessageFolder()));
	}

	/**
	 * Functional requirement number 13: An actor who is authenticated must be able to:
	 * Exchange messages with other actors and manage them, which includes deleting and moving them from
	 * one folder to another folder.
	 * 
	 * Manage his or her message folders, except for the system folders
	 * 
	 * @author MJ
	 */
	@Test
	public void deleteMessage() {
		super.authenticate("admin");
		final Message message;

		message = this.messageService.findOne(super.getEntityId("Message1"));
		Assert.notNull(message);
		this.messageService.delete(message);
		Assert.isTrue(message.getMessageFolder().getName().equals("trash box") || !this.messageFolderService.findAll().contains(message));

		super.unauthenticate();
	}

	/**
	 * Functional requirement number 13: An actor who is authenticated must be able to:
	 * Exchange messages with other actors and manage them, which includes deleting and moving them from
	 * one folder to another folder.
	 * 
	 * Manage his or her message folders, except for the system folders
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteMessageNotLoggedNegative() {
		final Message message;

		message = this.messageService.findOne(super.getEntityId("Message1"));
		Assert.notNull(message);
		this.messageService.delete(message);
		Assert.isTrue(message.getMessageFolder().getName().equals("trash box") || !this.messageFolderService.findAll().contains(message));

	}
	/**
	 * Functional requirement number 13: An actor who is authenticated must be able to:
	 * Exchange messages with other actors and manage them, which includes deleting and moving them from
	 * one folder to another folder.
	 * 
	 * Manage his or her message folders, except for the system folders
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void deleteNotExistingMessage() {
		super.authenticate("admin1");
		final Message message;

		message = this.messageService.findOne(0);
		Assert.notNull(message);
		this.messageService.delete(message);
		Assert.isTrue(message.getMessageFolder().getName().equals("trash box") || !this.messageFolderService.findAll().contains(message));

		super.unauthenticate();
	}

	/**
	 * Functional requirement number 13: An actor who is authenticated must be able to:
	 * Exchange messages with other actors and manage them, which includes deleting and moving them from
	 * one folder to another folder.
	 * 
	 * Manage his or her message folders, except for the system folders
	 * 
	 * @author MJ
	 */
	@Test
	public void moveMessage() {
		super.authenticate("admin");
		final Actor actor;
		MessageFolder trashBox;
		Message message;

		actor = this.actorService.findActorByPrincipal();
		message = this.messageService.findOne(super.getEntityId("Message1"));
		trashBox = this.messageFolderService.findMessageFolder("trash box", actor);
		Assert.notNull(message);
		message = this.actorService.moveMessage(message, trashBox);

		Assert.isTrue(message.getMessageFolder().equals(trashBox));
		super.unauthenticate();
	}

	/**
	 * Functional requirement number 13: An actor who is authenticated must be able to:
	 * Exchange messages with other actors and manage them, which includes deleting and moving them from
	 * one folder to another folder.
	 * 
	 * Manage his or her message folders, except for the system folders
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void moveMessageNotLoggedNegative() {
		final Actor actor;
		MessageFolder trashBox;
		Message message;

		actor = this.actorService.findActorByPrincipal();
		message = this.messageService.findOne(super.getEntityId("Message1"));
		trashBox = this.messageFolderService.findMessageFolder("trash box", actor);
		Assert.notNull(message);
		message = this.actorService.moveMessage(message, trashBox);

		Assert.isTrue(message.getMessageFolder().equals(trashBox));
	}

	/**
	 * Functional requirement number 13: An actor who is authenticated must be able to:
	 * Exchange messages with other actors and manage them, which includes deleting and moving them from
	 * one folder to another folder.
	 * 
	 * Manage his or her message folders, except for the system folders
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void moveMessageNotOwnDestiny() {
		super.authenticate("admin1");
		final Actor otherActor;
		MessageFolder trashBox;
		Message message;

		otherActor = this.actorService.findOne(super.getEntityId("User1"));
		message = this.messageService.findOne(super.getEntityId("Message1"));
		trashBox = this.messageFolderService.findMessageFolder("trash box", otherActor);
		Assert.notNull(message);
		message = this.actorService.moveMessage(message, trashBox);

		Assert.isTrue(message.getMessageFolder().equals(trashBox));
		super.unauthenticate();
	}

	/**
	 * Functional requirement number 13: An actor who is authenticated must be able to:
	 * Exchange messages with other actors and manage them, which includes deleting and moving them from
	 * one folder to another folder.
	 * 
	 * Manage his or her message folders, except for the system folders
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void moveMessageNotOwn() {
		super.authenticate("admin1");
		final Actor actor;
		MessageFolder trashBox;
		Message message;

		actor = this.actorService.findActorByPrincipal();
		message = this.messageService.findOne(super.getEntityId("Message8"));
		trashBox = this.messageFolderService.findMessageFolder("trash box", actor);
		Assert.notNull(message);
		message = this.actorService.moveMessage(message, trashBox);

		Assert.isTrue(message.getMessageFolder().equals(trashBox));
		super.unauthenticate();
	}

	/**
	 * Functional requirement number 14: An actor who is authenticated as administrator must be able to:
	 * Broadcast a message to the actors of the system
	 * 
	 * @author MJ
	 */
	@Test
	public void testBroadcastNotification() {
		super.authenticate("admin1");

		final Actor actor;
		final Message message;

		actor = this.actorService.findActorByPrincipal();

		message = this.messageService.create();
		message.setBody("Body 1");
		message.setPriority("LOW");
		message.setReceiver((Actor) this.actorService.findAll().toArray()[0]);
		message.setReceptionDate(new Date(System.currentTimeMillis() - 1));
		message.setSender(actor);
		message.setSubject("Subject 1");
		this.messageService.broadcastNotification(message);

		super.unauthenticate();
	}

	/**
	 * Functional requirement number 14: An actor who is authenticated as administrator must be able to:
	 * Broadcast a message to the actors of the system
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testBroadcastNotificationNotLoggedNegative() {
		super.unauthenticate();
		final Actor actor;
		final Message message;

		actor = this.actorService.findActorByPrincipal();

		message = this.messageService.create();
		message.setBody("Body 1");
		message.setPriority("LOW");
		message.setReceiver((Actor) this.actorService.findAll().toArray()[0]);
		message.setReceptionDate(new Date(System.currentTimeMillis() - 1));
		message.setSender(actor);
		message.setSubject("Subject 1");
		this.messageService.broadcastNotification(message);

	}

	/**
	 * Functional requirement number 13: An actor who is authenticated must be able to:
	 * Exchange messages with other actors and manage them, which includes deleting and moving them from
	 * one folder to another folder.
	 * 
	 * Manage his or her message folders, except for the system folders
	 * 
	 * @author MJ
	 */
	@Test
	public void testSendMessage() {

		super.authenticate("admin1");
		final Actor actor, receiver;
		final Message message;
		MessageFolder messageFolderReceiver;

		actor = this.actorService.findActorByPrincipal();
		receiver = (Actor) this.actorService.findAll().toArray()[0];
		message = this.messageService.create();
		message.setBody("Body 1");
		message.setPriority("LOW");
		message.setReceiver(receiver);
		message.setReceptionDate(new Date(System.currentTimeMillis() - 1));
		message.setSender(actor);
		message.setSubject("Subject 1");

		messageFolderReceiver = this.messageFolderService.findMessageFolder("in box", receiver);

		this.actorService.sendMessage(message, actor, receiver, messageFolderReceiver);

		super.unauthenticate();
	}

	/**
	 * Functional requirement number 13: An actor who is authenticated must be able to:
	 * Exchange messages with other actors and manage them, which includes deleting and moving them from
	 * one folder to another folder.
	 * 
	 * Manage his or her message folders, except for the system folders
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSendMessageNotLoggedNegative() {

		final Actor actor, receiver;
		final Message message;
		MessageFolder messageFolderReceiver;

		actor = this.actorService.findActorByPrincipal();
		receiver = (Actor) this.actorService.findAll().toArray()[0];
		message = this.messageService.create();
		message.setBody("Body 1");
		message.setPriority("LOW");
		message.setReceiver(receiver);
		message.setReceptionDate(new Date(System.currentTimeMillis() - 1));
		message.setSender(actor);
		message.setSubject("Subject 1");

		messageFolderReceiver = this.messageFolderService.findMessageFolder("in box", receiver);

		this.actorService.sendMessage(message, actor, receiver, messageFolderReceiver);

	}

	/**
	 * Functional requirement number 13: An actor who is authenticated must be able to:
	 * Exchange messages with other actors and manage them, which includes deleting and moving them from
	 * one folder to another folder.
	 * 
	 * Manage his or her message folders, except for the system folders
	 * 
	 * @author MJ
	 */
	@Test
	public void testFindByActorId() {
		super.authenticate("admin1");
		final Integer id;

		id = super.getEntityId("User1");
		Assert.notNull(this.messageService.findMessagesByActorId(id));

		super.unauthenticate();
	}

	/**
	 * Functional requirement number 13: An actor who is authenticated must be able to:
	 * Exchange messages with other actors and manage them, which includes deleting and moving them from
	 * one folder to another folder.
	 * 
	 * Manage his or her message folders, except for the system folders
	 * 
	 * @author MJ
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFindByActorIdNotLoggedNegative() {
		final Integer id;
		super.unauthenticate();
		id = super.getEntityId("User1");
		Assert.notNull(this.messageService.findMessagesByActorId(id));
	}
}
