
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Article;
import domain.FollowUp;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

	/**
	 * Level B query 1
	 * 
	 * @return The average number of follow-ups per article.
	 * @author Antonio
	 */
	@Query("select avg(a.followUps.size) from Article a")
	String getAverageFollowUpsPerArticle();

	/**
	 * 
	 * 
	 * @author Luis
	 */
	@Query("select a from Article a join a.followUps fs where fs =?1")
	Article getArticleByFollowUp(FollowUp followUp);
}
