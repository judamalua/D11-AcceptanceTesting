package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ChirpRepository;
import domain.Chirp;

@Service
@Transactional
public class ChirpService {

	// Managed repository --------------------------------------------------

	@Autowired
	private ChirpRepository	chirpRepository;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------

	public Chirp create() {
		Chirp result;

		result = new Chirp();

		return result;
	}

	public Collection<Chirp> findAll() {

		Collection<Chirp> result;

		Assert.notNull(this.chirpRepository);
		result = this.chirpRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Chirp findOne(final int chirpId) {

		Chirp result;

		result = this.chirpRepository.findOne(chirpId);

		return result;

	}

	public Chirp save(final Chirp chirp) {

		assert chirp != null;

		Chirp result;

		result = this.chirpRepository.save(chirp);

		return result;

	}

	public void delete(final Chirp chirp) {

		assert chirp != null;
		assert chirp.getId() != 0;

		Assert.isTrue(this.chirpRepository.exists(chirp.getId()));

		this.chirpRepository.delete(chirp);

	}
}

