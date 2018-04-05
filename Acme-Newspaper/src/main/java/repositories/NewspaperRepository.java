
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

	@Query("select n from Newspaper n where n.publicNewspaper=true")
	Page<Newspaper> findPublicNewspapers(Pageable pageable);

	@Query("select n from Newspaper n join n.creditCards c where c.customer.id=?1")
	Page<Newspaper> findSubscribeNewspapers(int customerId, Pageable pageable);

	@Query("select a from Newspaper n join n.articles a where n.id=?1")
	Page<Article> findArticlesByNewspaper(int newspaperId, Pageable pageable);

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
	@Query("select n from Newspaper n where n.articles.size > (select (avg(ne.articles.size)*0.9) from Newspaper ne)")
	Collection<Newspaper> getNewspaperWith10PercentLessArticlesThanAverage();
}
