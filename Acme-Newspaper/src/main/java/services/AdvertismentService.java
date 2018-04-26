
package services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdvertismentRepository;
import domain.Advertisment;
import domain.Agent;
import domain.Newspaper;

@Service
@Transactional
public class AdvertismentService {

	// Managed repository --------------------------------------------------

	@Autowired
	private AdvertismentRepository	advertismentRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private NewspaperService		newspaperService;


	// Simple CRUD methods --------------------------------------------------

	public Advertisment create() {
		Advertisment result;

		result = new Advertisment();

		result.setAgent((Agent) this.actorService.findActorByPrincipal());

		return result;
	}

	public Collection<Advertisment> findAll() {

		Collection<Advertisment> result;

		Assert.notNull(this.advertismentRepository);
		result = this.advertismentRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Advertisment findOne(final int advertismentId) {

		Advertisment result;

		result = this.advertismentRepository.findOne(advertismentId);

		return result;

	}

	public Advertisment save(final Advertisment advertisment) {
		Advertisment result;

		//Checks that the CreditCard hasn't expired
		this.checkCreditCardExpired(advertisment);

		advertisment.setAgent((Agent) this.actorService.findActorByPrincipal());
		result = this.save(advertisment);

		return result;

	}

	public void delete(final Advertisment advertisment) {

		assert advertisment != null;
		assert advertisment.getId() != 0;

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
	public void checkCreditCardExpired(final Advertisment advertisment) {
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

	public void advertise(final Advertisment advertisment, final Newspaper newspaper) {
		Assert.isTrue(newspaper.getPublicationDate() != null); //Tests that the newspaper is published
		final Advertisment savedAdvertisment = this.save(advertisment);
		newspaper.getAdvetisments().add(savedAdvertisment);
		this.newspaperService.save(newspaper);
	}
}
