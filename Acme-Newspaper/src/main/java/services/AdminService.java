
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdminRepository;
import domain.Admin;
import domain.MessageFolder;
import forms.UserCustomerAdminForm;

@Service
@Transactional
public class AdminService {

	// Managed repository --------------------------------------------------

	@Autowired
	private AdminRepository			adminRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private MessageFolderService	messageFolderService;

	@Autowired
	private Validator				validator;


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
		final MessageFolder inbox, outbox, notificationbox, trashbox, spambox;
		final Collection<MessageFolder> messageFolders, savedMessageFolders;

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

			inbox = this.messageFolderService.create();
			inbox.setIsDefault(true);
			inbox.setMessageFolderFather(null);
			inbox.setName("in box");
			outbox = this.messageFolderService.create();
			outbox.setIsDefault(true);
			outbox.setMessageFolderFather(null);
			outbox.setName("out box");
			notificationbox = this.messageFolderService.create();
			notificationbox.setIsDefault(true);
			notificationbox.setMessageFolderFather(null);
			notificationbox.setName("notification box");
			trashbox = this.messageFolderService.create();
			trashbox.setIsDefault(true);
			trashbox.setMessageFolderFather(null);
			trashbox.setName("trash box");
			spambox = this.messageFolderService.create();
			spambox.setIsDefault(true);
			spambox.setMessageFolderFather(null);
			spambox.setName("spam box");

			messageFolders = new ArrayList<MessageFolder>();
			messageFolders.add(inbox);
			messageFolders.add(outbox);
			messageFolders.add(trashbox);
			messageFolders.add(spambox);
			messageFolders.add(notificationbox);

			savedMessageFolders = new ArrayList<MessageFolder>();

			for (final MessageFolder mf : messageFolders)
				savedMessageFolders.add(this.messageFolderService.saveDefaultMessageFolder(mf));

			result.setMessageFolders(savedMessageFolders);

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
		this.adminRepository.flush();

		return result;
	}

	public void flush() {
		this.adminRepository.flush();
	}
}
