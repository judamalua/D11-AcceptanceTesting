
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Article;
import domain.FollowUp;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

	/**
	 * Returns the article witch the followUp belongs to
	 * 
	 * @author Luis
	 **/

	@Query("select a from Article a join a.followUps f where f = ?1")
	public Article getArticleByFollowUp(FollowUp followUp);

}
