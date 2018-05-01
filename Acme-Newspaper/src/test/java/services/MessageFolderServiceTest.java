
package services;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Actor;
import domain.MessageFolder;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class MessageFolderServiceTest extends AbstractTest {

	@Autowired
	public ActorService			actorService;
	@Autowired
	public MessageService		messageService;
	@Autowired
	public MessageFolderService	messageFolderService;
	@Autowired
	public AdminService			adminService;


	@Test
	public void testCreate() {
		super.authenticate("user1");
		final MessageFolder messageFolder;

		messageFolder = this.messageFolderService.create();
		Assert.notNull(messageFolder);
		Assert.isNull(messageFolder.getMessageFolderFather());
		Assert.isTrue(!messageFolder.getIsDefault());
		Assert.isNull(messageFolder.getName());
		super.unauthenticate();
	}

	@Test
	public void testFindAll() {
		super.authenticate("admin1");
		final Collection<MessageFolder> messageFolders;

		messageFolders = this.messageFolderService.findAll();

		Assert.notNull(messageFolders);
		super.unauthenticate();
	}

	@Test
	public void testFindOne() {
		super.authenticate("admin1");
		final MessageFolder messageFolder;

		messageFolder = this.messageFolderService.findOne(super.getEntityId("MessageFolder1"));
		Assert.notNull(messageFolder);
		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindOneNotExistNegative() {
		super.authenticate("admin1");
		final MessageFolder messageFolder;

		messageFolder = this.messageFolderService.findOne(0);
		Assert.notNull(messageFolder);
		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindOneNotLoggedNegative() {
		final MessageFolder messageFolder;

		messageFolder = this.messageFolderService.findOne(super.getEntityId("MessageFolder1"));
		Assert.notNull(messageFolder);
	}

	@Test
	public void testSave() {
		super.authenticate("user1");
		final Actor actor;
		final MessageFolder messageFolder, savedMessageFolder;

		actor = this.actorService.findActorByPrincipal();

		messageFolder = this.messageFolderService.create();
		messageFolder.setName("new box");
		messageFolder.setMessageFolderFather(null);

		savedMessageFolder = this.messageFolderService.save(messageFolder);

		Assert.isTrue(this.messageFolderService.findAll().contains(savedMessageFolder));
		Assert.isTrue(actor.getMessageFolders().contains(savedMessageFolder));

		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveNotLoggedNegative() {
		final Actor actor;
		final MessageFolder messageFolder, savedMessageFolder;

		actor = this.actorService.findActorByPrincipal();

		messageFolder = this.messageFolderService.create();
		messageFolder.setName("new box");
		messageFolder.setMessageFolderFather(null);

		savedMessageFolder = this.messageFolderService.save(messageFolder);

		Assert.isTrue(this.messageFolderService.findAll().contains(savedMessageFolder));
		Assert.isTrue(actor.getMessageFolders().contains(savedMessageFolder));

	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveNotValidFatherNegative() {
		super.authenticate("user1");
		final Actor actor;
		final MessageFolder messageFolder, savedMessageFolder;

		actor = this.actorService.findActorByPrincipal();

		messageFolder = this.messageFolderService.create();
		messageFolder.setName("new box");
		messageFolder.setMessageFolderFather(messageFolder);

		savedMessageFolder = this.messageFolderService.save(messageFolder);

		Assert.isTrue(this.messageFolderService.findAll().contains(savedMessageFolder));
		Assert.isTrue(actor.getMessageFolders().contains(savedMessageFolder));

		super.unauthenticate();
	}

	@Test
	public void testDelete() {
		super.authenticate("user1");
		final Actor actor;
		final MessageFolder messageFolder, savedMessageFolder;

		actor = this.actorService.findActorByPrincipal();

		messageFolder = this.messageFolderService.create();
		messageFolder.setName("new box");
		savedMessageFolder = this.messageFolderService.save(messageFolder);

		this.messageFolderService.delete(savedMessageFolder);
		Assert.isTrue(!this.messageFolderService.findAll().contains(savedMessageFolder));
		Assert.isTrue(!actor.getMessageFolders().contains(savedMessageFolder));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteNotValidMessageFolder() {
		super.authenticate("user1");
		final MessageFolder messageFolder;

		messageFolder = this.messageFolderService.create();
		messageFolder.setName("new box");

		this.messageFolderService.delete(messageFolder);
	}

	@Test
	public void testFindMessageFolder() {
		super.authenticate("admin1");
		Assert.notNull(this.messageFolderService.findMessageFolder("in box", (Actor) this.adminService.findAll().toArray()[0]));
		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindNotValidMessageFolder() {
		super.authenticate("admin1");
		Assert.notNull(this.messageFolderService.findMessageFolder("none box", (Actor) this.adminService.findAll().toArray()[0]));
		super.unauthenticate();
	}
}
