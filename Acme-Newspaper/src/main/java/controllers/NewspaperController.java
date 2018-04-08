/*
 * RendezvousController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.ConfigurationService;
import services.NewspaperService;
import services.UserService;
import domain.Actor;
import domain.Article;
import domain.Configuration;
import domain.CreditCard;
import domain.Customer;
import domain.DomainEntity;
import domain.Newspaper;
import domain.User;

@Controller
@RequestMapping("/newspaper")
public class NewspaperController extends AbstractController {

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private UserService				userService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public NewspaperController() {
		super();
	}

	// Listing  ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(defaultValue = "0") final int page) throws IllegalArgumentException, IllegalAccessException, IOException {
		final ModelAndView result;
		final Page<Newspaper> newspapers;
		final Pageable pageable;
		final Configuration configuration;
		final Collection<Boolean> ownNewspapers;
		Actor actor;
		User publisher;

		configuration = this.configurationService.findConfiguration();
		pageable = new PageRequest(page, configuration.getPageSize());
		ownNewspapers = new ArrayList<>();
		result = new ModelAndView("newspaper/list");

		newspapers = this.newspaperService.findPublicPublicatedNewspapers(pageable);

		if (this.actorService.getLogged()) {
			actor = this.actorService.findActorByPrincipal();
			for (final Newspaper newspaper : newspapers.getContent()) {
				publisher = this.userService.findUserByNewspaper(newspaper.getId());
				ownNewspapers.add(actor.equals(publisher));
			}

			result.addObject("ownNewspaper", ownNewspapers);
		}
		result.addObject("newspapers", newspapers.getContent());
		result.addObject("page", page);
		result.addObject("pageNum", newspapers.getTotalPages());
		result.addObject("requestUri", "newspaper/user/list.do?");

		//SCRIPT ===============================

		final HashMap<Integer, String> idMap = new HashMap<Integer, String>(); //Integer: Id del domain entity, String: Nombre de la clase para referenciarla
		final HashMap<String, Integer> countDomainEntityNames = new HashMap<String, Integer>(); //String: clase , Integer: id actual para generar nuevo nombre
		final HashMap<String, List<Object>> objects = new HashMap<String, List<Object>>(); //Lista de todos los objetos en memoria
		final HashMap<Object, String> datatypes = new HashMap<Object, String>(); //Object: Datatype, String: Su nombre al que se referencia

		final HashMap<String, Integer> countDataTypesName = new HashMap<String, Integer>(); //String clase

		final EntityManager et = Persistence.createEntityManagerFactory("Acme-Newspaper").createEntityManager();

		for (final EntityType<?> entity : et.getMetamodel().getEntities()) {

			final String className = entity.getName();
			//Debe ignorar las clases abstractas
			if (!className.equalsIgnoreCase("actor") && !className.equalsIgnoreCase("domainentity")) {
				final Query q = et.createQuery("from " + className + " c");

				for (final Object o : q.getResultList()) {
					if (!objects.containsKey(o.getClass().getName()))
						objects.put(o.getClass().getName(), new ArrayList<Object>());
					objects.get(o.getClass().getName()).add(o);
					final String classN = o.getClass().getName().replaceFirst("domain.", "").replaceFirst("security.", "");
					if (!countDomainEntityNames.containsKey(classN))
						countDomainEntityNames.put(classN, 0);
					countDomainEntityNames.put(classN, countDomainEntityNames.get(classN) + 1);

					final String name = classN + countDomainEntityNames.get(classN);

					if (o instanceof DomainEntity)
						idMap.put(((DomainEntity) o).getId(), name);

				}
			}
		}

		String xml = "<beans xmlns=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd\"> \n";
		for (final String clase : objects.keySet()) {
			xml = xml + "\n\n <!-- %%%%%%  " + clase + " %%%%%% --> \n";
			final List<Object> subobjects = objects.get(clase);
			for (final Object o : subobjects) {
				final Integer id = ((DomainEntity) o).getId();
				xml = xml + "<bean id=\"" + idMap.get(id) + "\" class=\"" + o.getClass().getName() + "\"> \n";

				final List<Field> fields = new ArrayList<Field>(Arrays.asList(o.getClass().getDeclaredFields()));

				if (o.getClass().getSuperclass().getName().contains("Actor"))
					fields.addAll(Arrays.asList(o.getClass().getSuperclass().getDeclaredFields()));
				for (final Field field : fields)
					if (!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
						field.setAccessible(true); // You might want to set modifier to public first.
						Object value = field.get(o);
						if (value != null) {
							if (value instanceof Date) {
								final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
								value = df.format((Date) value);
							}
							if (value instanceof String || value instanceof Date || value instanceof Boolean || value instanceof Double || value instanceof Integer || value instanceof Long)
								xml = xml + "<property name=\"" + field.getName() + "\" value=\"" + value + "\" /> \n";
							else if (value instanceof Collection<?>) {
								xml = xml + "<property name=\"" + field.getName() + "\" > \n";
								xml = xml + "<list> \n";

								final Collection<?> col = (Collection<?>) value;
								for (final Object obColl : col)
									if (obColl instanceof String || obColl instanceof Date || obColl instanceof Boolean || obColl instanceof Double || obColl instanceof Integer || obColl instanceof Long)
										xml = xml + "<value>" + obColl + "</value> \n";
									else if (obColl instanceof Authority)
										xml = xml + "<bean class=\"security.Authority\"> \n <property name=\"authority\" value=\"" + obColl + "\" /> \n </bean>";
									else
										xml = xml + "<ref bean=\"" + idMap.get(((DomainEntity) obColl).getId()) + "\" /> \n";
								xml = xml + "</list> \n";
								xml = xml + "</property> \n";
							} else if (value instanceof DomainEntity)
								xml = xml + "<property name=\"" + field.getName() + "\" ref=\"" + idMap.get(((DomainEntity) value).getId()) + "\" /> \n";
							else {
								//Datatypes
								Integer valAct = countDataTypesName.get(value.getClass().getSimpleName());
								if (valAct == null)
									valAct = 0;
								valAct++;
								countDataTypesName.put(value.getClass().getSimpleName(), valAct);
								final String dtName = value.getClass().getSimpleName() + valAct;
								if (!datatypes.containsKey(value))
									datatypes.put(value, dtName);

								xml = xml + "<property name=\"" + field.getName() + "\" ref=\"" + dtName + "\" /> \n";

							}

						}
					}
				xml = xml + "</bean>\n";
			}

		}

		xml = xml + "\n\n <!-- = = = = = = DATATYPES = = = = = = = --> \n";
		for (final Entry<Object, String> entry : datatypes.entrySet()) {
			xml = xml + "<bean id=\"" + entry.getValue() + "\" class=\"" + entry.getKey().getClass().getName() + "\"> \n";
			for (final Field field : entry.getKey().getClass().getDeclaredFields()) {
				field.setAccessible(true); // You might want to set modifier to public first.
				final Object value = field.get(entry.getKey());
				if (value != null)
					xml = xml + "<property name=\"" + field.getName() + "\" value=\"" + value + "\" /> \n";
			}
			xml = xml + "</bean>\n";
		}
		xml = xml + "</beans>";

		final String path = "C:/Users/corchu/Desktop/populate.xml";
		Files.write(Paths.get(path), xml.getBytes(), StandardOpenOption.CREATE);
		//Hacer bucle para incorporar los datatypes
		System.out.println(xml);

		//FIN DEL SCRIPT ========================

		return result;
	}
	@RequestMapping("/display")
	public ModelAndView display(@RequestParam final Integer newspaperId, @RequestParam(required = true, defaultValue = "0") final Integer pageArticle) {
		ModelAndView result;
		Newspaper newspaper;
		Actor actor;
		User writer;
		final Collection<Boolean> ownArticles;
		final Page<Article> articles;
		Pageable pageable;
		Configuration configuration;
		Boolean subscriber;

		try {

			result = new ModelAndView("newspaper/display");
			subscriber = false;
			newspaper = this.newspaperService.findOne(newspaperId);
			Assert.notNull(newspaper);

			configuration = this.configurationService.findConfiguration();
			pageable = new PageRequest(pageArticle, configuration.getPageSize());

			ownArticles = new ArrayList<>();
			articles = this.newspaperService.findArticlesByNewspaper(newspaperId, pageable);

			if (this.actorService.getLogged()) {
				actor = this.actorService.findActorByPrincipal();

				for (final Article article : articles.getContent()) {
					writer = this.userService.findUserByArticle(article.getId());
					ownArticles.add(writer.equals(actor));
				}

				if (actor instanceof Customer)
					for (final CreditCard creditCard : newspaper.getCreditCards()) {
						subscriber = creditCard.getCustomer().equals(actor);
						if (subscriber)
							break;
					}
				result.addObject("ownArticle", ownArticles);
			}

			result.addObject("subscriber", subscriber);
			result.addObject("newspaper", newspaper);
			result.addObject("articles", articles.getContent());
			result.addObject("page", pageArticle);
			result.addObject("pageNum", articles.getTotalPages());

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
}
