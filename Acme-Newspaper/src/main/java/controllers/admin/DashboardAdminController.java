
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.AdvertisementService;
import services.ArticleService;
import services.CustomerService;
import services.FollowUpService;
import services.NewspaperService;
import services.UserService;
import services.VolumeService;
import controllers.AbstractController;
import domain.Newspaper;

@Controller
@RequestMapping("/dashboard/admin")
public class DashboardAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	UserService				userService;

	@Autowired
	NewspaperService		newspaperService;

	@Autowired
	ArticleService			articleService;

	@Autowired
	CustomerService			customerService;

	@Autowired
	FollowUpService			followUpService;

	@Autowired
	AdvertisementService	advertisementService;

	@Autowired
	VolumeService			volumeService;


	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;
		String newspapersInfoFromUsers, articlesInfoFromUsers, articlesInfoFromNewspapers, ratioCreatedNewspapers, ratioCreatedArticles, chirpsInfoFromUsers, ratioUsersPostedAbove75PercentAverageChirpsPerUser, ratioPublicNewspapers, averageArticlesPerPrivateNewspapers, averageArticlesPerPublicNewspapers;
		String averageFollowUpsPerArticle, averageFollowUpPerArticleOneWeek, averageFollowUpPerArticleTwoWeek, ratioSubscribersPrivateNewspaperVSTotalCustomers;
		String averageRatioPrivateVSPublicNewspaperPublisher;
		final String ratioSubscriptionsToVolumesVsRatioSubscriptionsNewspapers, averageNewspapersPerVolume, ratioTabooAdvertisements, ratioNewspapersAtLeastOneAdvertisementVsNoOne;
		Collection<Newspaper> newspaperWith10PercentMoreArticlesThanAverage, newspaperWith10PercentLessArticlesThanAverage;

		try {
			//C queries
			newspapersInfoFromUsers = this.userService.getNewspapersInfoFromUsers();
			articlesInfoFromUsers = this.userService.getArticlesInfoFromUsers();
			articlesInfoFromNewspapers = this.newspaperService.getArticlesInfoFromNewspapers();
			newspaperWith10PercentMoreArticlesThanAverage = this.newspaperService.getNewspaperWith10PercentMoreArticlesThanAverage();
			newspaperWith10PercentLessArticlesThanAverage = this.newspaperService.getNewspaperWith10PercentLessArticlesThanAverage();
			ratioCreatedNewspapers = this.userService.getRatioCreatedNewspapers();
			ratioCreatedArticles = this.userService.getRatioCreatedArticles();
			ratioNewspapersAtLeastOneAdvertisementVsNoOne = this.newspaperService.getRatioNewspapersAtLeastOneAdvertisementVsNoOne();
			ratioTabooAdvertisements = this.advertisementService.getRatioTabooAdvertisements();

			//B queries
			averageFollowUpsPerArticle = this.articleService.getAverageFollowUpsPerArticle();
			averageFollowUpPerArticleOneWeek = this.followUpService.getAverageFollowUpPerArticleOneWeek();
			averageFollowUpPerArticleTwoWeek = this.followUpService.getAverageFollowUpPerArticleTwoWeek();
			chirpsInfoFromUsers = this.userService.getChirpsInfoFromUsers();
			ratioUsersPostedAbove75PercentAverageChirpsPerUser = this.userService.getRatioUsersPostedAbove75PercentAverageChirpsPerUser();
			averageNewspapersPerVolume = this.volumeService.getAverageNewspapersPerVolume();
			ratioSubscriptionsToVolumesVsRatioSubscriptionsNewspapers = this.volumeService.getRatioSubscriptionsToVolumesVsRatioSubscriptiosNewspapers();

			//A queries
			ratioPublicNewspapers = this.newspaperService.getRatioPublicNewspapers();
			averageArticlesPerPrivateNewspapers = this.newspaperService.getAverageArticlesPerPrivateNewspapers();
			averageArticlesPerPublicNewspapers = this.newspaperService.getAverageArticlesPerPublicNewspapers();
			ratioSubscribersPrivateNewspaperVSTotalCustomers = this.customerService.getRatioSubscribersPrivateNewspaperVSTotalCustomers();
			averageRatioPrivateVSPublicNewspaperPublisher = this.newspaperService.getAverageRatioPrivateVSPublicNewspaperPublisher();

			result = new ModelAndView("dashboard/list");

			//C queries
			result.addObject("newspapersInfoFromUsersAverage", newspapersInfoFromUsers.split(",")[0]);
			result.addObject("newspapersInfoFromUsersDeviation", newspapersInfoFromUsers.split(",")[1]);

			result.addObject("articlesInfoFromUsersAverage", articlesInfoFromUsers.split(",")[0]);
			result.addObject("articlesInfoFromUsersDeviation", articlesInfoFromUsers.split(",")[1]);

			result.addObject("articlesInfoFromNewspapersAverage", articlesInfoFromNewspapers.split(",")[0]);
			result.addObject("articlesInfoFromNewspapersDeviation", articlesInfoFromNewspapers.split(",")[1]);

			result.addObject("newspaperWith10PercentMoreArticlesThanAverage", newspaperWith10PercentMoreArticlesThanAverage);
			result.addObject("newspaperWith10PercentLessArticlesThanAverage", newspaperWith10PercentLessArticlesThanAverage);

			result.addObject("ratioCreatedNewspapers", ratioCreatedNewspapers);
			result.addObject("ratioCreatedArticles", ratioCreatedArticles);

			result.addObject("ratioNewspapersAtLeastOneAdvertisementVsNoOne", ratioNewspapersAtLeastOneAdvertisementVsNoOne.split(","));
			result.addObject("ratioTabooAdvertisements", ratioTabooAdvertisements);

			//B queries
			result.addObject("averageFollowUpsPerArticle", averageFollowUpsPerArticle);

			result.addObject("averageFollowUpPerArticleOneWeek", averageFollowUpPerArticleOneWeek);
			result.addObject("averageFollowUpPerArticleTwoWeek", averageFollowUpPerArticleTwoWeek);

			result.addObject("chirpsInfoFromUsersAverage", chirpsInfoFromUsers.split(",")[0]);
			result.addObject("chirpsInfoFromUsersDeviation", chirpsInfoFromUsers.split(",")[1]);

			result.addObject("ratioUsersPostedAbove75PercentAverageChirpsPerUser", ratioUsersPostedAbove75PercentAverageChirpsPerUser);

			result.addObject("averageNewspapersPerVolume", averageNewspapersPerVolume);
			result.addObject("ratioSubscriptionsToVolumesVsRatioSubscriptiosNewspapers", ratioSubscriptionsToVolumesVsRatioSubscriptionsNewspapers.split(","));

			//A queries
			result.addObject("ratioPublicNewspapers", ratioPublicNewspapers);

			result.addObject("averageArticlesPerPrivateNewspapers", averageArticlesPerPrivateNewspapers);
			result.addObject("averageArticlesPerPublicNewspapers", averageArticlesPerPublicNewspapers);

			result.addObject("ratioSubscribersPrivateNewspaperVSTotalCustomers", ratioSubscribersPrivateNewspaperVSTotalCustomers);

			result.addObject("averageRatioPrivateVSPublicNewspaperPublisher", averageRatioPrivateVSPublicNewspaperPublisher);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}
		return result;
	}
}
