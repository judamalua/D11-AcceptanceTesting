package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdminRepository;
import domain.Admin;

@Service
@Transactional
public class AdminService {

	// Managed repository --------------------------------------------------

	@Autowired
	private AdminRepository	adminRepository;


	// Supporting services --------------------------------------------------

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

	public Admin findOne(final int adminId) {

		Admin result;

		result = this.adminRepository.findOne(adminId);

		return result;

	}

	public Admin save(final Admin admin) {

		assert admin != null;

		Admin result;

		result = this.adminRepository.save(admin);

		return result;

	}

	public void delete(final Admin admin) {

		assert admin != null;
		assert admin.getId() != 0;

		Assert.isTrue(this.adminRepository.exists(admin.getId()));

		this.adminRepository.delete(admin);

	}
}

