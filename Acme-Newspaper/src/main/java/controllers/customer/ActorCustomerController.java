/*
 * RendezvousController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.CustomerService;
import controllers.AbstractController;
import domain.Customer;
import forms.UserCustomerAdminForm;

@Controller
@RequestMapping("/actor/customer")
public class ActorCustomerController extends AbstractController {

	@Autowired
	private ActorService	actorService;
	@Autowired
	private CustomerService	customerService;


	// Constructors -----------------------------------------------------------

	public ActorCustomerController() {
		super();
	}

	// Registering customer ------------------------------------------------------------
	/**
	 * That method registers a customer in the system and saves it.
	 * 
	 * @param
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerCustomer() {
		ModelAndView result;
		UserCustomerAdminForm customer;

		customer = new UserCustomerAdminForm();

		result = this.createEditModelAndViewRegister(customer);

		return result;
	}

	//Edit a customer
	/**
	 * That method edits the profile of a customer
	 * 
	 * @param
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView editCustomer() {
		ModelAndView result;
		Customer customer;
		final UserCustomerAdminForm customerForm;

		customer = (Customer) this.actorService.findActorByPrincipal();
		Assert.notNull(customer);
		customerForm = this.actorService.deconstruct(customer);
		result = this.createEditModelAndView(customerForm);

		return result;
	}

	//Saving customer ---------------------------------------------------------------------
	/**
	 * That method saves a customer in the system
	 * 
	 * @param save
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView registerAdministrator(@ModelAttribute("customer") final UserCustomerAdminForm actor, final BindingResult binding) {
		ModelAndView result;
		Authority auth;
		Customer customer = null;
		try {
			customer = this.customerService.reconstruct(actor, binding);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		if (binding.hasErrors())
			result = this.createEditModelAndViewRegister(actor, "admin.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.CUSTOMER);
				Assert.isTrue(customer.getUserAccount().getAuthorities().contains(auth));
				Assert.isTrue(actor.getConfirmPassword().equals(customer.getUserAccount().getPassword()), "Passwords do not match");
				this.actorService.registerActor(customer);
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final DataIntegrityViolationException oops) {
				result = this.createEditModelAndViewRegister(actor, "customer.username.error");
			} catch (final Throwable oops) {
				if (oops.getMessage().contains("Passwords do not match"))
					result = this.createEditModelAndViewRegister(actor, "customer.password.error");
				else
					result = this.createEditModelAndViewRegister(actor, "customer.commit.error");
			}

		return result;
	}

	//Updating profile of a customer ---------------------------------------------------------------------
	/**
	 * This method update the profile of a customer.
	 * 
	 * @param save
	 * @return ModelandView
	 * @author Luis
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView updateAdministrator(@ModelAttribute("actor") final UserCustomerAdminForm actor, final BindingResult binding) {
		ModelAndView result;
		Customer customer = null;

		try {
			customer = this.customerService.reconstruct(actor, binding);
		} catch (final Throwable oops) { //Not delete
		}
		if (binding.hasErrors())
			result = this.createEditModelAndView(actor, "actor.params.error");
		else
			try {
				this.actorService.save(customer);
				result = new ModelAndView("redirect:/actor/display.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(actor, "actor.commit.error");
			}

		return result;
	}
	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final UserCustomerAdminForm admin) {
		ModelAndView result;

		result = this.createEditModelAndView(admin, null);

		return result;
	}
	protected ModelAndView createEditModelAndViewRegister(final UserCustomerAdminForm admin) {
		ModelAndView result;

		result = this.createEditModelAndViewRegister(admin, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final UserCustomerAdminForm customer, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject("message", messageCode);
		result.addObject("actor", customer);

		return result;

	}

	protected ModelAndView createEditModelAndViewRegister(final UserCustomerAdminForm customer, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("customer/register");
		result.addObject("message", messageCode);
		result.addObject("customer", customer);

		return result;

	}
}
