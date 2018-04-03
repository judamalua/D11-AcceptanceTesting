package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CreditCardRepository;
import domain.CreditCard;

@Service
@Transactional
public class CreditCardService {

	// Managed repository --------------------------------------------------

	@Autowired
	private CreditCardRepository	creditCardRepository;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------

	public CreditCard create() {
		CreditCard result;

		result = new CreditCard();

		return result;
	}

	public Collection<CreditCard> findAll() {

		Collection<CreditCard> result;

		Assert.notNull(this.creditCardRepository);
		result = this.creditCardRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public CreditCard findOne(final int creditCardId) {

		CreditCard result;

		result = this.creditCardRepository.findOne(creditCardId);

		return result;

	}

	public CreditCard save(final CreditCard creditCard) {

		assert creditCard != null;

		CreditCard result;

		result = this.creditCardRepository.save(creditCard);

		return result;

	}

	public void delete(final CreditCard creditCard) {

		assert creditCard != null;
		assert creditCard.getId() != 0;

		Assert.isTrue(this.creditCardRepository.exists(creditCard.getId()));

		this.creditCardRepository.delete(creditCard);

	}
}

