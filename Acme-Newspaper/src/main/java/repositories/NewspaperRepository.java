
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Article;
import domain.Newspaper;

@Repository
public interface NewspaperRepository extends JpaRepository<Newspaper, Integer> {

	@Query("select n from Newspaper n join n.articles a where a.id=?1")
	Newspaper findNewspaperByArticle(int articleId);

	@Query("select n from Newspaper n where (n.publicationDate!=null or n.publicationDate!='')")
	Page<Newspaper> findPublicPublicatedNewspapers(Pageable pageable);

	@Query("select n from Newspaper n join n.creditCards c where c.customer.id=?1")
	Page<Newspaper> findSubscribeNewspapers(int customerId, Pageable pageable);

	@Query("select a from Newspaper n join n.articles a where n.id=?1")
	Page<Article> findArticlesByNewspaper(int newspaperId, Pageable pageable);

	@Query("select n from Newspaper n join n.creditCards c where c.customer.id = ?1")
	Page<Newspaper> findNotSubscribedNewspapersByCustomer(int customerId, Pageable pageable);

	@Query("select n from Newspaper n where n.taboo = TRUE and n.publicNewspaper = TRUE and (n.publicationDate!=null and n.publicationDate!='')")
	Collection<Newspaper> getAllTabooNewspapers();

	/**
	 * Level C query 3
	 * 
	 * @return The average and the standard deviation of articles per newspaper.
	 * @author Antonio
	 */
	@Query("select avg(n.articles.size), sqrt(sum(n.articles.size * n.articles.size) / count(n.articles.size) - (avg(n.articles.size) * avg(n.articles.size))) from Newspaper n")
	String getArticlesInfoFromNewspapers();

	/**
	 * Level C query 4
	 * 
	 * @return The newspapers that have at least 10% more articles than the average.
	 * @author Antonio
	 */
	@Query("select n from Newspaper n where n.articles.size > (select (avg(ne.articles.size)*1.1) from Newspaper ne)")
	Collection<Newspaper> getNewspaperWith10PercentMoreArticlesThanAverage();

	/**
	 * Level C query 5
	 * 
	 * @return The newspapers that have at least 10% fewer articles than the average.
	 * @author Antonio
	 */
	@Query("select n from Newspaper n where n.articles.size < (select (avg(ne.articles.size)*0.9) from Newspaper ne)")
	Collection<Newspaper> getNewspaperWith10PercentLessArticlesThanAverage();

	/**
	 * Level A query 1
	 * 
	 * @return The ratio of public versus private newspapers.
	 * @author Antonio
	 */
	@Query("select (select count(n1) from Newspaper n1 where n1.publicNewspaper = TRUE)/(count(n)*1.0) from Newspaper n")
	String getRatioPublicNewspapers();

	/**
	 * Level A query 2
	 * 
	 * @return The average number of articles per private newspapers.
	 * @author Antonio
	 */
	@Query("select avg(n.articles.size) from Newspaper n where n.publicNewspaper = FALSE")
	String getAverageArticlesPerPrivateNewspapers();

	/**
	 * Level A query 3
	 * 
	 * @return The average number of articles per public newspapers.
	 * @author Antonio
	 */
	@Query("select avg(n.articles.size) from Newspaper n where n.publicNewspaper = TRUE")
	String getAverageArticlesPerPublicNewspapers();

	/**
	 * Level A query 5
	 * 
	 * @return The average ratio of private versus public newspapers per publisher
	 * @author Antonio
	 */
	@Query("select count(n1)*1.0/(select count(n)*1.0 from Newspaper n) from Newspaper n1 where n1.publicNewspaper = FALSE")
	String getAverageRatioPrivateVSPublicNewspaperPublisher();

	/**
	 * Level C query 1
	 * 
	 * @return The ratio of newspapers that have at least one advertisement versus the newspapers that haven’t any.
	 * @author MJ
	 */
	@Query("select (count(na)*1.0/(select count(n) from Newspaper n)*1.0),((select count(n) from Newspaper n)-count(na))*1.0/(select count(n) from Newspaper n)*1.0  from Newspaper na where na.advertisements.size>0")
	String getRatioNewspapersAtLeastOneAdvertisementVsNoOne();

	@Query("select n from Newspaper n where ((n.publicationDate!=null or n.publicationDate!='') and (n.title like ?1 or n.description like ?1))")
	Page<Newspaper> findPublicPublicatedNewspapersWithSearch(String search, Pageable pageable);

	@Query("select n from Volume v join v.newspapers n where v.id = ?1")
	Page<Newspaper> findNewspapersByVolume(Integer volumeId, Pageable pageable);

	@Query("select n from Volume v join v.newspapers n where v.id = ?1")
	Collection<Newspaper> findNewspapersByVolume(Integer volumeId);

	@Query("select n from Newspaper n join n.advertisements a where a.id=?1")
	Collection<Newspaper> findNewspaperByAdvertisement(int advertisementId);

	@Query("select n from Newspaper n join n.advertisements a where a.id=?1")
	Page<Newspaper> findNewspaperByAdvertisementPage(int advertisementId, Pageable pageable);

	@Query("select distinct n from Newspaper n join n.advertisements a where (a.id!=?1) and (n.publicationDate!=null and n.publicationDate!='')")
	Page<Newspaper> findNewspaperByNoAdvertisementPage(int advertisementId, Pageable pageable);
}
