
package controllers.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ConfigurationService;
import services.CreditCardService;
import services.NewspaperService;
import controllers.AbstractController;
import domain.Actor;
import domain.Configuration;
import domain.CreditCard;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper/customer")
public class NewspaperCustomerController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private CreditCardService		creditCardService;


	// Subscribe controller -----------------------------------------------

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(required = false, defaultValue = "0") final Integer page) {
		ModelAndView result;
		Page<Newspaper> newspapers;
		final Pageable pageable;
		Configuration configuration;
		Actor actor;
		try {
			result = new ModelAndView("newspaper/list");
			actor = this.actorService.findActorByPrincipal();
			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(page, configuration.getPageSize());

			newspapers = this.newspaperService.findSubscribedNewspapersByUser(actor.getId(), pageable);

			result.addObject("owner", true);
			result.addObject("newspapers", newspapers.getContent());
			result.addObject("page", page);
			result.addObject("pageNum", newspapers.getTotalPages());
			result.addObject("requestUri", "newspaper/customer/list.do?");

		} catch (final Throwable oops) {
			result = new ModelAndView("rediect:/misc/403");
		}
		return result;
	}

	@RequestMapping(value = "/subscribe", method = RequestMethod.GET)
	public ModelAndView subscibe(@RequestParam final Integer newspaperId) {
		ModelAndView result;
		Newspaper newspaper;
		final CreditCard creditCard;

		try {
			newspaper = this.newspaperService.findOne(newspaperId);
			Assert.isTrue(newspaper.getPublicationDate() != null); //Tests that the newspaper is published
			Assert.isTrue(newspaper.getPublicNewspaper() == false); //Tests that the newspaper is private
			Assert.isTrue(this.creditCardService.creditCardSubscribed(newspaper.getId(), this.actorService.findActorByPrincipal().getId()) == null);//Tests that the user is not already subscribed
			creditCard = this.creditCardService.create();
			result = this.createEditModelAndView(creditCard, newspaper);
		} catch (final Exception e) {
			result = new ModelAndView("rediect:/misc/403");
		}

		return result;
	}

	@RequestMapping(value = "/subscribe", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("creditCard") final CreditCard creditCard, final BindingResult binding, final int newspaperId) {
		ModelAndView result;
		Newspaper newspaper;

		newspaper = this.newspaperService.findOne(newspaperId);

		if (binding.hasErrors())
			result = this.createEditModelAndView(creditCard, newspaper, "request.params.error");
		else
			try {
				this.creditCardService.subscribe(creditCard, newspaper);
				result = new ModelAndView("redirect:/newspaper/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(creditCard, newspaper, "request.commit.error");
			}

		return result;
	}

	//AJAX Credit Card---------------------------------------
	/**
	 * This method receives a cookie token and sends a string with the last four numbers of a credit card and the credit card number, if something fails returns a null string.
	 * 
	 * @param cookieToken
	 *            The token to test
	 * @author Daniel Diment
	 * @return
	 *         The string
	 */
	@RequestMapping(value = "/ajaxCard", method = RequestMethod.GET)
	public @ResponseBody
	String ajaxCard(@RequestParam final String cookieToken) {
		String result = "null";
		CreditCard creditCard;
		try {
			creditCard = this.creditCardService.findByCookieToken(cookieToken);
			result = creditCard.getNumber().substring(creditCard.getNumber().length() - 4) + creditCard.getId();
		} catch (final Throwable e) {
		}
		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final CreditCard creditCard, final Newspaper newspaper) {
		ModelAndView result;

		result = this.createEditModelAndView(creditCard, newspaper, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final CreditCard creditCard, final Newspaper newspaper, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("creditCard/edit");
		result.addObject("creditCard", creditCard);
		result.addObject("newspaper", newspaper);
		result.addObject("message", messageCode);

		return result;

	}
}
