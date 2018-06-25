
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
import domain.Lusit;
import domain.Newspaper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class LusitServiceTest extends AbstractTest {

	@Autowired
	public ActorService		actorService;
	@Autowired
	public LusitService		lusitService;
	@Autowired
	public AdminService		adminService;
	@Autowired
	public NewspaperService	newspaperService;


	@Test
	public void testCreate() {
		super.authenticate("admin1");
		Lusit lusit;
		lusit = this.lusitService.create();

		Assert.notNull(lusit);
		Assert.notNull(lusit.getTicker());
		Assert.isNull(lusit.getDescription());
		Assert.isTrue(lusit.getFinalMode() == false);
		Assert.isNull(lusit.getGauge());
		Assert.isNull(lusit.getPublicationDate());
		Assert.isNull(lusit.getTitle());
		super.unauthenticate();

	}

	@Test
	public void testSave() {
		super.authenticate("admin1");
		final Lusit lusit;
		final Newspaper newspaper;
		Integer newspaperId;

		newspaperId = super.getEntityId("Newspaper1");
		newspaper = this.newspaperService.findOne(newspaperId);

		lusit = this.lusitService.create();
		lusit.setTitle("Title test");
		lusit.setDescription("Description test");
		lusit.setFinalMode(false);
		lusit.setPublicationDate(new Date(System.currentTimeMillis() + 60000));
		lusit.setGauge(2);

		this.lusitService.save(lusit, newspaper);
		super.unauthenticate();
	}

	@Test
	public void testEditToFinal() {
		super.authenticate("admin");
		final Lusit lusit;
		final Newspaper newspaper;
		Integer newspaperId, lusitId;

		newspaperId = super.getEntityId("Newspaper1");
		lusitId = super.getEntityId("Lusit2");
		newspaper = this.newspaperService.findOne(newspaperId);
		lusit = this.lusitService.findOne(lusitId);

		lusit.setFinalMode(true);
		lusit.setPublicationDate(new Date(System.currentTimeMillis() + 60000));

		this.lusitService.save(lusit, newspaper);
		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveNotLoggedNegative() {
		final Lusit lusit, savedLusit;
		final Newspaper newspaper;
		Integer newspaperId;

		newspaperId = super.getEntityId("Newspaper1");
		newspaper = this.newspaperService.findOne(newspaperId);

		lusit = this.lusitService.create();
		lusit.setTitle("Title test");
		lusit.setDescription("Description test");
		lusit.setFinalMode(false);
		lusit.setPublicationDate(new Date(System.currentTimeMillis() + 60000));
		lusit.setGauge(2);

		savedLusit = this.lusitService.save(lusit, newspaper);
		Assert.isTrue(this.lusitService.findAll().contains(savedLusit));
	}

	@Test
	public void deleteLusit() {
		super.authenticate("admin1");
		final Lusit lusit;
		Integer lusitId;

		lusitId = super.getEntityId("Lusit2");
		lusit = this.lusitService.findOne(lusitId);

		this.lusitService.delete(lusit);
		Assert.isTrue(!this.lusitService.findAll().contains(lusit));
		super.unauthenticate();
	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteLusitNotLoggedNegative() {
		final Lusit lusit;
		Integer lusitId;

		lusitId = super.getEntityId("Lusit2");
		lusit = this.lusitService.findOne(lusitId);

		this.lusitService.delete(lusit);
		Assert.isTrue(!this.lusitService.findAll().contains(lusit));
	}
}
