
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import utilities.AbstractTest;
import domain.Article;
import domain.CreditCard;
import domain.Newspaper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class FollowUPServiceTest extends AbstractTest {

	@Autowired
	public ActorService			actorService;
	@Autowired
	public UserService			userService;
	@Autowired
	public FollowUpService		followUpService;
	@Autowired
	public NewspaperService		newspaperService;
	@Autowired
	public ArticleService		articleService;
	@Autowired
	public ConfigurationService	configurationService;


	//******************************************Positive Methods*******************************************************************

	//******************************************Negative Methods*******************************************************************

	//******************************************Private Methods**************************

	@SuppressWarnings("unused")
	private Newspaper createStandarNewspaper() throws ParseException {
		Newspaper newspaper;
		Newspaper savedNewspaper;
		final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		final Date date = format.parse("21/12/2015");

		final Collection<Article> articles = new ArrayList<Article>();
		final Collection<CreditCard> creditCards = new ArrayList<CreditCard>();

		newspaper = this.newspaperService.create();
		newspaper.setArticles(articles);
		newspaper.setCreditCards(creditCards);
		newspaper.setDescription("New Description");
		newspaper.setPublicationDate(date);
		newspaper.setPictureUrl("https://www.realbetisbalompie.es/img1");
		newspaper.setPublicNewspaper(true);
		newspaper.setTaboo(false);
		newspaper.setTitle("title 1");
		savedNewspaper = this.newspaperService.save(newspaper);

		return savedNewspaper;

	}

	//	private Article createStandarsArticle(){
	//		Article article;
	//		
	//		article= this.articleService.create();
	//		article.setFinalMode(true);
	//		article.set;
	//		article.set;
	//		article.set;
	//		article.set;
	//		
	//	}

}
