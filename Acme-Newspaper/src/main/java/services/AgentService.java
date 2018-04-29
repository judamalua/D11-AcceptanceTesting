
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AgentRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Agent;
import domain.MessageFolder;
import forms.UserCustomerAdminForm;

@Service
@Transactional
public class AgentService {

	// Managed repository --------------------------------------------------

	@Autowired
	private AgentRepository			agentRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService			actorService;

	@Autowired
	private MessageFolderService	messageFolderService;

	@Autowired
	private Validator				validator;


	// Simple CRUD methods --------------------------------------------------

	public Agent create() {
		Agent result;
		UserAccount userAccount;
		Collection<Authority> authorities;
		Authority authority;

		result = new Agent();
		userAccount = new UserAccount();
		authorities = new HashSet<Authority>();
		authority = new Authority();
		authority.setAuthority(Authority.AGENT);
		authorities.add(authority);
		userAccount.setAuthorities(authorities);

		result.setUserAccount(userAccount);

		return result;
	}

	public Collection<Agent> findAll() {
		Collection<Agent> result;

		result = this.agentRepository.findAll();

		return result;
	}

	public Agent findOne(final int agentId) {

		Agent result;

		result = this.agentRepository.findOne(agentId);

		return result;

	}

	public Agent save(final Agent agent) {

		assert agent != null;
		Actor actor = null;
		Agent result;

		if (agent.getId() != 0)
			actor = this.actorService.findActorByPrincipal();

		if (actor instanceof Agent && agent.getId() != 0)
			Assert.isTrue(agent.equals(actor));

		result = this.agentRepository.save(agent);

		return result;

	}

	public void delete(final Agent agent) {

		assert agent != null;
		assert agent.getId() != 0;

		Assert.isTrue(this.agentRepository.exists(agent.getId()));

		this.agentRepository.delete(agent);

	}

	// Other business methods
	public void flush() {
		this.agentRepository.flush();
	}

	public Agent reconstruct(final UserCustomerAdminForm userAdminForm, final BindingResult binding) {
		Agent result;
		Actor actor;
		final MessageFolder inbox, outbox, notificationbox, trashbox, spambox;
		final Collection<MessageFolder> messageFolders, savedMessageFolders;

		if (userAdminForm.getId() == 0) {

			result = this.create();

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
			actor = this.actorService.findActorByPrincipal();
			result = this.agentRepository.findOne(userAdminForm.getId());

			Assert.isTrue(result.getId() == actor.getId());

			result.setName(userAdminForm.getName());
			result.setSurname(userAdminForm.getSurname());
			result.setPostalAddress(userAdminForm.getPostalAddress());
			result.setPhoneNumber(userAdminForm.getPhoneNumber());
			result.setEmail(userAdminForm.getEmail());
			result.setBirthDate(userAdminForm.getBirthDate());

		}
		this.validator.validate(result, binding);
		this.agentRepository.flush();

		return result;
	}

}
