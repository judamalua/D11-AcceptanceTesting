
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.TagRepository;
import domain.Admin;
import domain.Advertisement;
import domain.Newspaper;
import domain.Tag;

@Service
@Transactional
public class TagService {

	// Managed repository --------------------------------------------------

	@Autowired
	private TagRepository			tagRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private AdvertisementService	advertisementService;

	@Autowired
	private Validator				validator;


	// Simple CRUD methods --------------------------------------------------

	public Tag create() {
		Tag result;

		result = new Tag();

		return result;
	}

	public Collection<Tag> findAll() {
		Collection<Tag> result;

		result = this.tagRepository.findAll();

		return result;
	}

	public Page<Tag> findAll(final Pageable pageable) {
		Page<Tag> result;

		result = this.tagRepository.findAll(pageable);

		return result;
	}

	public Tag findOne(final int tagId) {

		Tag result;

		result = this.tagRepository.findOne(tagId);

		return result;

	}

	public Tag save(final Tag tag) {

		Assert.notNull(tag);

		Tag result;
		Collection<Newspaper> newspapers;
		Collection<Advertisement> advertisements;

		Assert.isTrue(this.actorService.findActorByPrincipal() instanceof Admin);

		result = this.tagRepository.save(tag);

		if (tag.getId() != 0) {
			newspapers = this.findNewspapersByTag(tag.getId());
			advertisements = this.advertisementService.findAdvertisementsTag(tag.getId());

			for (final Newspaper newspaper : newspapers) {
				newspaper.setTag(result);
				this.newspaperService.save(newspaper);
			}
			for (final Advertisement advertisement : advertisements) {
				advertisement.getTags().remove(tag);
				this.advertisementService.save(advertisement);
			}
		}

		return result;

	}
	public void delete(final Tag tag) {

		Assert.notNull(tag);
		Assert.isTrue(tag.getId() != 0);
		Assert.isTrue(this.tagRepository.exists(tag.getId()));
		Assert.isTrue(this.actorService.findActorByPrincipal() instanceof Admin);

		Collection<Newspaper> newspapers;
		Collection<Advertisement> advertisements;

		newspapers = this.findNewspapersByTag(tag.getId());
		advertisements = this.advertisementService.findAdvertisementsTag(tag.getId());

		for (final Newspaper newspaper : newspapers) {
			newspaper.setTag(null);
			this.newspaperService.save(newspaper);
		}

		for (final Advertisement advertisement : advertisements) {
			advertisement.getTags().remove(tag);
			this.advertisementService.save(advertisement);
		}

		this.tagRepository.delete(tag);

	}
	public Collection<Newspaper> findNewspapersByTag(final Integer tagId) {
		Collection<Newspaper> result;

		result = this.tagRepository.findNewspapersByTag(tagId);

		return result;
	}

	// Other business methods
	public void flush() {
		this.tagRepository.flush();
	}

	public Tag reconstruct(final Tag tag, final BindingResult binding) {
		Tag result;

		if (tag.getId() == 0) {

			result = this.create();

			result.setName(tag.getName());
			result.setKeywords(tag.getKeywords());

		} else {
			result = this.tagRepository.findOne(tag.getId());

			result.setName(tag.getName());
			result.setKeywords(tag.getKeywords());

		}
		this.validator.validate(result, binding);
		this.tagRepository.flush();

		return result;
	}

}
