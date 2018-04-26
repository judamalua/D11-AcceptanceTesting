
package services;

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
import domain.Newspaper;
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

		this.volumeRepository.delete(volume);
	}

	public Volume reconstruct(final Volume volume, final BindingResult binding) {
		Volume result;

		if (volume.getId() == 0) {
			Collection<Newspaper> newspapers;

			result = volume;
			newspapers = new HashSet<Newspaper>();
			result.setNewspapers(newspapers);

		} else {
			result = this.findOne(volume.getId());
			result.setDescription(volume.getDescription());
			result.setTitle(volume.getTitle());
			result.setYear(volume.getYear());
		}

		this.validator.validate(result, binding);

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
}
