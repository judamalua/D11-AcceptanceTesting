
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.FollowUp;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUp, Integer> {

	@Query("select f from FollowUp f where f.user.id=?1")
	Page<FollowUp> findCreatedFollowUps(int userId, Pageable pageable);

	/**
	 * Level B query 2
	 * 
	 * @return The average number of follow-ups per article up to one week after the corresponding newspaper's been published.
	 * @author Antonio
	 */
	@Query("select ((count(f)*1.0)/count(a)*1.0) from Newspaper n join n.articles a join a.followUps f where DATEDIFF(n.publicationDate, f.publicationDate) < 7")
	String getAverageFollowUpPerArticleOneWeek();

	/**
	 * Level B query 3
	 * 
	 * @return The average number of follow-ups per article up to two weeks after the corresponding newspaper's been published.
	 * @author Antonio
	 */
	@Query("select ((count(f)*1.0)/count(a)*1.0) from Newspaper n join n.articles a join a.followUps f where DATEDIFF(n.publicationDate, f.publicationDate) < 14")
	String getAverageFollowUpPerArticleTwoWeek();
}
