
package controllers.agent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AgentService;
import controllers.AbstractController;
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
	public ModelAndView registerCustomer() {
		ModelAndView result;
		UserCustomerAdminForm customer;

		customer = new UserCustomerAdminForm();

		result = this.createEditModelAndViewRegister(customer);

		return result;
	}

}
