
package controllers.customer;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CreditCardService;
import services.VolumeService;
import controllers.AbstractController;
import domain.CreditCard;
import domain.Customer;
import domain.Volume;

@Controller
@RequestMapping("/volume/customer")
public class VolumeCustomerController extends AbstractController {

	// Services -------------------------------------------------------
	@Autowired
	private VolumeService		volumeService;

	@Autowired
	private CreditCardService	creditCardService;

	@Autowired
	private ActorService		actorService;


	// Subscribe ---------------------------------------------------------

	@RequestMapping(value = "/subscribe", method = RequestMethod.GET)
	public ModelAndView subscibe(@RequestParam final Integer volumeId) {
		ModelAndView result;
		Volume volume;
		CreditCard creditCard;
		Customer customer;

		try {
			customer = (Customer) this.actorService.findActorByPrincipal();
			volume = this.volumeService.findOne(volumeId);
			creditCard = this.creditCardService.getCreditCardUserVolume(volumeId, customer.getId());

			Assert.isNull(creditCard); //Asserts that the Customer hasn't subscribed to this Volume

			creditCard = this.creditCardService.create();

			result = this.createEditModelAndView(creditCard, volume);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;

	}

	@RequestMapping(value = "/subscribe", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid @ModelAttribute("creditCard") final CreditCard creditCard, final BindingResult binding, final int volumeId) {
		ModelAndView result;
		Volume volume;

		volume = this.volumeService.findOne(volumeId);

		if (binding.hasErrors())
			result = this.createEditModelAndView(creditCard, volume, "request.params.error");
		else
			try {
				this.volumeService.subscribe(creditCard, volume);
				result = new ModelAndView("redirect:/volume/list.do");
			} catch (final Throwable oops) {
				if (oops.getMessage() == "CreditCard expiration Date error")
					result = this.createEditModelAndView(creditCard, volume, "request.creditcard.expiration.error");
				else
					result = this.createEditModelAndView(creditCard, volume, "request.commit.error");

			}

		return result;
	}

	// Ancilliary methods ---------------------------------------------------------

	private ModelAndView createEditModelAndView(final CreditCard creditCard, final Volume volume) {
		ModelAndView result;

		result = this.createEditModelAndView(creditCard, volume, null);

		return result;
	}

	private ModelAndView createEditModelAndView(final CreditCard creditCard, final Volume volume, final String message) {
		ModelAndView result;

		result = new ModelAndView("volume/subscribe");
		result.addObject("creditCard", creditCard);
		result.addObject("volume", volume);
		result.addObject("message", message);

		return result;
	}

}
