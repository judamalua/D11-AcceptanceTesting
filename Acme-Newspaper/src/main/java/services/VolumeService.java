
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.VolumeRepository;
import domain.CreditCard;
import domain.Newspaper;
import domain.User;
import domain.Volume;

@Service
@Transactional
public class VolumeService {

	// Managed repository --------------------------------------------------
	@Autowired
	private VolumeRepository	volumeRepository;

	// Supporting services --------------------------------------------------
	@Autowired
	private Validator			validator;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserService			userService;

	@Autowired
	private CreditCardService	creditCardService;

	@Autowired
	private NewspaperService	newspaperService;


	// Simple CRUD methods --------------------------------------------------

	public Volume create() {
		Volume result;

		result = new Volume();

		return result;
	}

	public Collection<Volume> findAll() {
		Collection<Volume> result;

		result = this.volumeRepository.findAll();

		return result;
	}

	public Volume findOne(final int volumeId) {
		Volume result;

		result = this.volumeRepository.findOne(volumeId);

		return result;
	}

	public Volume save(final Volume volume) {
		Assert.notNull(volume);

		Volume result;

		result = this.volumeRepository.save(volume);

		return result;
	}

	public void delete(final Volume volume) {
		Assert.notNull(volume);
		Assert.isTrue(volume.getId() != 0);

		User user;
		Volume savedVolume;

		savedVolume = this.findOne(volume.getId());
		user = (User) this.actorService.findActorByPrincipal();

		Assert.isTrue(savedVolume.getUser().equals(user));

		this.volumeRepository.delete(volume);
	}

	public Volume reconstruct(final Volume volume, final BindingResult binding) {
		Volume result;
		User user;

		if (volume.getId() == 0) {
			Collection<Newspaper> newspapers;

			user = (User) this.actorService.findActorByPrincipal();
			result = volume;
			newspapers = new HashSet<Newspaper>();

			result.setNewspapers(newspapers);
			result.setUser(user);

		} else {
			result = this.findOne(volume.getId());
			result.setDescription(volume.getDescription());
			result.setTitle(volume.getTitle());
			result.setYear(volume.getYear());
			result.setNewspapers(volume.getNewspapers());
		}

		this.validator.validate(result, binding);
		this.volumeRepository.flush();

		return result;
	}

	public Page<Volume> findVolumes(final Pageable pageable) {
		Page<Volume> result;

		Assert.notNull(pageable);

		result = this.volumeRepository.findVolumes(pageable);

		return result;
	}

	public String getRatioSubscriptionsToVolumesVsRatioSubscriptiosNewspapers() {
		String result;

		result = this.volumeRepository.getRatioSubscriptionsToVolumesVsRatioSubscriptiosNewspapers();

		return result;
	}

	public String getAverageNewspapersPerVolume() {

		String result;

		result = this.volumeRepository.getAverageNewspapersPerVolume();

		return result;
	}

	public Collection<Newspaper> getElegibleNewspaperForVolume(final User user) {
		Collection<Newspaper> result;

		result = this.userService.findPublishedNewspapersByUser(user.getId());

		return result;
	}

	public Page<Volume> findVolumesByCustomer(final int customerId, final Pageable pageable) {
		Page<Volume> result;

		Assert.notNull(pageable);

		result = this.volumeRepository.findVolumesByCustomer(customerId, pageable);

		return result;
	}

	public Page<Volume> findVolumesByUser(final int customerId, final Pageable pageable) {
		Page<Volume> result;

		Assert.notNull(pageable);

		result = this.volumeRepository.findVolumesByUser(customerId, pageable);

		return result;
	}

	public CreditCard subscribe(final CreditCard creditCard, final Volume volume) {
		Assert.notNull(volume);

		Collection<Newspaper> newspapers;
		Collection<Volume> volumes;
		CreditCard result;

		newspapers = this.volumeRepository.getSubscribableNewspapersFromVolume(volume.getId());

		if (creditCard.getVolumes() != null)
			creditCard.getVolumes().add(volume);
		else {
			volumes = new ArrayList<Volume>();
			volumes.add(volume);
			creditCard.setVolumes(volumes);
		}

		result = this.creditCardService.save(creditCard);

		for (final Newspaper n : newspapers) {
			n.getCreditCards().add(result);
			this.newspaperService.save(n);
		}

		return result;

	}

}
