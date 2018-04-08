
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdminRepository;
import domain.Admin;
import forms.UserCustomerAdminForm;

@Service
@Transactional
public class AdminService {

	// Managed repository --------------------------------------------------

	@Autowired
	private AdminRepository	adminRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService	actorService;

	@Autowired
	private Validator		validator;


	// Simple CRUD methods --------------------------------------------------

	public Admin create() {
		Admin result;

		result = new Admin();

		return result;
	}

	public Collection<Admin> findAll() {

		Collection<Admin> result;

		Assert.notNull(this.adminRepository);
		result = this.adminRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Admin findOne(final int AdminId) {

		Admin result;

		result = this.adminRepository.findOne(AdminId);

		return result;

	}

	public Admin save(final Admin Admin) {

		assert Admin != null;

		Admin result;

		result = this.adminRepository.save(Admin);

		return result;

	}

	public void delete(final Admin Admin) {

		assert Admin != null;
		assert Admin.getId() != 0;

		Assert.isTrue(this.adminRepository.exists(Admin.getId()));

		this.adminRepository.delete(Admin);

	}

	// Other business methods
	public Admin reconstruct(final UserCustomerAdminForm userAdminForm, final BindingResult binding) {
		Admin result;

		if (userAdminForm.getId() == 0) {

			result = this.actorService.createAdmin();

			result.getUserAccount().setUsername(userAdminForm.getUserAccount().getUsername());
			result.getUserAccount().setPassword(userAdminForm.getUserAccount().getPassword());
			result.setName(userAdminForm.getName());
			result.setSurname(userAdminForm.getSurname());
			result.setPostalAddress(userAdminForm.getPostalAddress());
			result.setPhoneNumber(userAdminForm.getPhoneNumber());
			result.setEmail(userAdminForm.getEmail());
			result.setBirthDate(userAdminForm.getBirthDate());

		} else {
			result = this.adminRepository.findOne(userAdminForm.getId());

			result.setName(userAdminForm.getName());
			result.setSurname(userAdminForm.getSurname());
			result.setPostalAddress(userAdminForm.getPostalAddress());
			result.setPhoneNumber(userAdminForm.getPhoneNumber());
			result.setEmail(userAdminForm.getEmail());
			result.setBirthDate(userAdminForm.getBirthDate());
		}
		this.validator.validate(result, binding);

		return result;
	}
}
