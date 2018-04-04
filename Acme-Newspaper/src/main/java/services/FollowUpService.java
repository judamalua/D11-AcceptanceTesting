package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.FollowUpRepository;
import domain.FollowUp;

@Service
@Transactional
public class FollowUpService {

	// Managed repository --------------------------------------------------

	@Autowired
	private FollowUpRepository	followUpRepository;


	// Supporting services --------------------------------------------------

	// Simple CRUD methods --------------------------------------------------

	public FollowUp create() {
		FollowUp result;

		result = new FollowUp();

		return result;
	}

	public Collection<FollowUp> findAll() {

		Collection<FollowUp> result;

		Assert.notNull(this.followUpRepository);
		result = this.followUpRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public FollowUp findOne(final int followUpId) {

		FollowUp result;

		result = this.followUpRepository.findOne(followUpId);

		return result;

	}

	public FollowUp save(final FollowUp followUp) {

		assert followUp != null;

		FollowUp result;

		result = this.followUpRepository.save(followUp);

		return result;

	}

	public void delete(final FollowUp followUp) {

		assert followUp != null;
		assert followUp.getId() != 0;

		Assert.isTrue(this.followUpRepository.exists(followUp.getId()));

		this.followUpRepository.delete(followUp);

	}
}

