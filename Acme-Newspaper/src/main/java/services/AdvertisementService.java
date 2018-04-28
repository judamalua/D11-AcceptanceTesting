
package services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdvertisementRepository;
import domain.Advertisement;
import domain.Agent;
import domain.Newspaper;

@Service
@Transactional
public class AdvertisementService {

	// Managed repository --------------------------------------------------

	@Autowired
	private AdvertisementRepository	advertismentRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private NewspaperService		newspaperService;


	// Simple CRUD methods --------------------------------------------------

	public Advertisement create() {
		Advertisement result;

		result = new Advertisement();

		result.setAgent((Agent) this.actorService.findActorByPrincipal());

		return result;
	}

	public Collection<Advertisement> findAll() {

		Collection<Advertisement> result;

		Assert.notNull(this.advertismentRepository);
		result = this.advertismentRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Advertisement findOne(final int advertismentId) {

		Advertisement result;

		result = this.advertismentRepository.findOne(advertismentId);

		return result;

	}

	public Advertisement save(final Advertisement advertisment) {
		Advertisement result;

		//Checks that the CreditCard hasn't expired
		this.checkCreditCardExpired(advertisment);

		advertisment.setAgent((Agent) this.actorService.findActorByPrincipal());
		result = this.save(advertisment);

		return result;

	}

	public void delete(final Advertisement advertisment) {

		Assert.isTrue(advertisment != null);
		Assert.isTrue(advertisment.getId() != 0);

		Assert.isTrue(this.advertismentRepository.exists(advertisment.getId()));

		this.advertismentRepository.delete(advertisment);

	}

	/**
	 * This method checks that the Credit Card of the Request hasn't expired, checking its expiration
	 * year and expiration month.
	 * 
	 * @param creditCard
	 * @author Antonio and Daniel Diment
	 */
	public void checkCreditCardExpired(final Advertisement advertisment) {
		Integer actualMonth, actualYear, ccMonth, ccYear;
		DateFormat dfYear, dfMonth;
		String formattedYear, formattedMonth;

		ccMonth = advertisment.getExpirationMonth();
		ccYear = advertisment.getExpirationYear();

		dfYear = new SimpleDateFormat("yy"); // Just the year, with 2 digits
		formattedYear = dfYear.format(Calendar.getInstance().getTime());
		actualYear = Integer.valueOf(formattedYear);

		dfMonth = new SimpleDateFormat("MM"); //Just the month
		formattedMonth = dfMonth.format(Calendar.getInstance().getTime());
		actualMonth = Integer.valueOf(formattedMonth);

		//Asserts that the CreditCard expiration Year is greater than the actual year
		Assert.isTrue(ccYear >= actualYear, "CreditCard expiration Date error");

		//If the CreditCard expiration Year is the same that the actual Year, 
		//Asserts that the CreditCard expiration Month is greater than the actual Month.
		if (ccYear == actualYear)
			Assert.isTrue(ccMonth > actualMonth, "CreditCard expiration Date error");

	}

	public void advertise(final Advertisement advertisement, final Newspaper newspaper) {
		Assert.isTrue(newspaper.getPublicationDate() != null); //Tests that the newspaper is published
		final Advertisement savedAdvertisement;

		savedAdvertisement = this.save(advertisement);
		newspaper.getAdvertisements().add(savedAdvertisement);
		this.newspaperService.save(newspaper);
	}

	public String getRatioTabooAdvertisements() {
		String result;

		result = this.advertismentRepository.getRatioTabooAdvertisements();

		return result;
	}

	public Page<Advertisement> findTabooAdvertisements(final Pageable pageable) {
		Page<Advertisement> result;

		result = this.advertismentRepository.findTabooAdvertisements(pageable);

		return result;
	}
}
