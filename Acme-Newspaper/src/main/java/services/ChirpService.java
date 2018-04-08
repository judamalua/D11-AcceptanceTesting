package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ChirpRepository;
import domain.Chirp;
import domain.User;

@Service
@Transactional
public class ChirpService {

	// Managed repository --------------------------------------------------

	@Autowired
	private ChirpRepository	chirpRepository;
	
	@Autowired
	private UserService userService;

	@Autowired
	private Validator				validator;

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

	public Chirp save(final Chirp chirp, final User user) {

		assert chirp != null;

		Chirp result;
		chirp.setMoment(new Date(System.currentTimeMillis()-10));
		

		result = this.chirpRepository.save(chirp);
		
		user.getChirps().add(result);
		this.userService.save(user);

		return result;

	}

	public void delete(final Chirp chirp) {

		assert chirp != null;
		assert chirp.getId() != 0;

		Assert.isTrue(this.chirpRepository.exists(chirp.getId()));

		this.chirpRepository.delete(chirp);

	}
	
	/**
	 * Reconstruct the Chirp passed as parameter
	 * 
	 * @param chirp
	 * @param binding
	 * 
	 * @return The reconstructed Chirp
	 * @author MJ
	 */
	public Chirp reconstruct(final Chirp chirp, final BindingResult binding) {
		Chirp result;

		if (chirp.getId() == 0) {
			result = chirp;
			result.setMoment(new Date(System.currentTimeMillis() - 1000));
		} else {
			result = this.chirpRepository.findOne(chirp.getId());
			result.setDescription(chirp.getDescription());
			result.setTitle(chirp.getTitle());
			result.setMoment(chirp.getMoment());
			result.setTaboo(chirp.isTaboo());
		}

		this.validator.validate(result, binding);
		return result;
	}
}

