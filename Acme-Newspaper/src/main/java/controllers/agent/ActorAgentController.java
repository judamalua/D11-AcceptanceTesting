
package controllers.agent;

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
import services.AgentService;
import controllers.AbstractController;
import domain.Agent;
import forms.UserCustomerAdminForm;

@Controller
@RequestMapping("/actor/agent")
public class ActorAgentController extends AbstractController {

	@Autowired
	private ActorService	actorService;
	@Autowired
	private AgentService	agentService;


	// Constructors -----------------------------------------------------------

	public ActorAgentController() {
		super();
	}

	// Registering customer ------------------------------------------------------------
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerAgent() {
		ModelAndView result;
		UserCustomerAdminForm agent;

		agent = new UserCustomerAdminForm();

		result = this.createEditModelAndViewRegister(agent);

		return result;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView registerAdministrator(@ModelAttribute("agent") final UserCustomerAdminForm actor, final BindingResult binding) {
		ModelAndView result;
		Authority auth;
		Agent agent = null;
		try {
			agent = this.agentService.reconstruct(actor, binding);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		if (binding.hasErrors())
			result = this.createEditModelAndViewRegister(actor, "agent.params.error");
		else
			try {
				auth = new Authority();
				auth.setAuthority(Authority.AGENT);
				Assert.isTrue(agent.getUserAccount().getAuthorities().contains(auth));
				Assert.isTrue(actor.getConfirmPassword().equals(agent.getUserAccount().getPassword()), "Passwords do not match");
				this.actorService.registerActor(agent);
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

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndViewRegister(final UserCustomerAdminForm agent) {
		ModelAndView result;

		result = this.createEditModelAndViewRegister(agent, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewRegister(final UserCustomerAdminForm agent, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("agent/register");
		result.addObject("message", messageCode);
		result.addObject("agent", agent);

		return result;

	}

}
