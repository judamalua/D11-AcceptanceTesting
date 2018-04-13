
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Article;
import domain.FollowUp;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

	@Query("select a from Article a where a.taboo = TRUE and a.finalMode = TRUE")
	Collection<Article> getAllTabooArticles();

	/**
	 * Level B query 1
	 * 
	 * @return The average number of follow-ups per article.
	 * @author Antonio
	 */
	@Query("select avg(a.followUps.size) from Article a")
	String getAverageFollowUpsPerArticle();

	@Query("select f from Article a join a.followUps f")
	Page<FollowUp> findFollowUpsByArticle(Pageable pageable);

	/**
	 * 
	 * 
	 * @author Luis
	 */
	@Query("select a from Article a join a.followUps fs where fs =?1")
	Article getArticleByFollowUp(FollowUp followUp);

	@Query("select a from Newspaper n join n.articles a where a.title like ?1 or a.summary like ?1 or a.body like ?1 and n.publicNewspaper=true and (n.publicationDate!='' or n.publicationDate!=null)")
	Page<Article> findPublicPublicatedArticlesWithSearch(String search, Pageable pageable);
}
