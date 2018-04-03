package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.NewspaperRepository;
import domain.Newspaper;

@Service
@Transactional
public class NewspaperService {

	// Managed repository --------------------------------------------------

	@Autowired
	private NewspaperRepository	newspaperRepository;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------

	public Newspaper create() {
		Newspaper result;

		result = new Newspaper();

		return result;
	}

	public Collection<Newspaper> findAll() {

		Collection<Newspaper> result;

		Assert.notNull(this.newspaperRepository);
		result = this.newspaperRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Newspaper findOne(final int newspaperId) {

		Newspaper result;

		result = this.newspaperRepository.findOne(newspaperId);

		return result;

	}

	public Newspaper save(final Newspaper newspaper) {

		assert newspaper != null;

		Newspaper result;

		result = this.newspaperRepository.save(newspaper);

		return result;

	}

	public void delete(final Newspaper newspaper) {

		assert newspaper != null;
		assert newspaper.getId() != 0;

		Assert.isTrue(this.newspaperRepository.exists(newspaper.getId()));

		this.newspaperRepository.delete(newspaper);

	}
}

