
package services;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.LusitRepository;
import domain.Admin;
import domain.Lusit;
import domain.Newspaper;

@Service
@Transactional
public class LusitService {

	// Managed repository --------------------------------------------------

	@Autowired
	private LusitRepository		lusitRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private AdminService		adminService;

	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private Validator			validator;


	// Simple CRUD methods --------------------------------------------------

	public Lusit create() {
		Lusit result;

		result = new Lusit();
		result.setTicker(this.generateTicker());

		return result;
	}

	public Collection<Lusit> findAll() {

		Collection<Lusit> result;

		Assert.notNull(this.lusitRepository);
		result = this.lusitRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Lusit findOne(final int lusitId) {

		Lusit result;

		result = this.lusitRepository.findOne(lusitId);

		return result;

	}

	public Lusit save(final Lusit lusit, final Newspaper newspaper) {

		Assert.notNull(lusit);

		Lusit result;
		Admin admin;

		admin = (Admin) this.actorService.findActorByPrincipal();

		result = this.lusitRepository.save(lusit);

		admin.getLusits().remove(lusit);
		admin.getLusits().add(result);
		this.actorService.save(admin);

		if (lusit.getFinalMode()) {
			newspaper.getLusits().remove(lusit);
			newspaper.getLusits().add(result);
			this.newspaperService.save(newspaper);
		}

		return result;

	}
	public void delete(final Lusit lusit) {

		Assert.notNull(lusit);
		Assert.isTrue(lusit.getId() != 0);

		Assert.isTrue(this.lusitRepository.exists(lusit.getId()));
		this.actorService.checkUserLogin();

		final Admin admin;
		Newspaper newspaper;

		if (lusit.getFinalMode()) {
			newspaper = this.newspaperService.findNewspaperByLusit(lusit.getId());
			newspaper.getLusits().remove(lusit);
			this.newspaperService.save(newspaper);
		}

		admin = this.findAdminByLusit(lusit.getId());
		admin.getLusits().remove(lusit);
		this.adminService.save(admin);

		this.lusitRepository.delete(lusit);

	}

	public Admin findAdminByLusit(final Integer lusitId) {
		final Admin result;

		result = this.lusitRepository.findAdminByLusit(lusitId);

		return result;
	}

	// Other business methods --------------------------------------------------------------

	public Lusit reconstruct(final Lusit lusit, final BindingResult binding) {
		Lusit result;

		if (lusit.getId() == 0) {

			result = lusit;

			if (result.getPublicationDate() == null) {
				result.setPublicationDate(new Date(System.currentTimeMillis() + 1000));
			}

			result.setTicker(this.generateTicker());

		} else {
			result = this.lusitRepository.findOne(lusit.getId());

			result.setTitle(lusit.getTitle());
			result.setDescription(lusit.getDescription());
			result.setGauge(lusit.getGauge());
			if (lusit.getPublicationDate() == null) {
				result.setPublicationDate(new Date(System.currentTimeMillis() + 1000));
			} else {
				result.setPublicationDate(lusit.getPublicationDate());
			}
			result.setFinalMode(lusit.getFinalMode());

		}
		this.validator.validate(result, binding);
		this.lusitRepository.flush();

		return result;
	}
	private String generateTicker() {
		String result;
		String alphabet;
		LocalDate now;
		int year, month, day;
		final Random random;

		result = "";
		alphabet = "abcdefghijklmnopqrstuvwyzABCDEFGHIJKLMNOPQRSTUVWYZ0123456789_";
		now = new LocalDate();

		year = now.getYear() % 100;
		month = now.getMonthOfYear();
		day = now.getDayOfMonth();
		random = new Random();
		result += ((year < 10) ? 0 + "" + year : year) + "" + ((month < 10) ? 0 + "" + month : month) + "" + ((day < 10) ? 0 + "" + day : day) + "-";

		for (int i = 0; i < 4; i++) {
			result += alphabet.charAt(random.nextInt(alphabet.length()));
		}

		return result;
	}

	public Page<Lusit> findLusitsByAdmin(final Integer adminId, final Boolean finalMode, final Pageable pageable) {
		Page<Lusit> result;

		result = this.lusitRepository.findLusitsByAdmin(adminId, finalMode, pageable);

		return result;
	}

	public Page<Lusit> findLusitsByNewspaper(final Integer newspaperId, final Pageable pageable) {
		Page<Lusit> result;

		result = this.lusitRepository.findLusitsByNewspaper(newspaperId, pageable);

		return result;
	}

	public Collection<Lusit> findLusitsByAdmin(final Integer adminId) {
		Collection<Lusit> result;

		result = this.lusitRepository.findLusitsByAdmin(adminId);

		return result;
	}
}
