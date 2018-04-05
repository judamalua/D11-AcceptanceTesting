
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Article;
import domain.Chirp;
import domain.Newspaper;
import domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u join u.newspapers n where n.id=?1")
	User findUserByNewspaper(int newspaperId);

	@Query("select u from User u join u.newspapers n join n.articles a where a.id=?1")
	User findUserByArticle(int articleId);

	@Query("select a from User u join u.newspapers n join n.articles a where u.id = ?1 and n.publicationDate != null")
	Page<Article> findUserPublishedArticles(int userId, Pageable pageable);

	@Query("select n from User u join u.newspapers n where u.id=?1 and (n.publicationDate!=null or n.publicationDate!='')")
	Page<Newspaper> findPublishedNewspapersByUser(int userId, Pageable pageable);

	@Query("select n from User u join u.newspapers n where u.id=?1 and (n.publicationDate=null or n.publicationDate='')")
	Page<Newspaper> findNotPublishedNewspapersByUser(int userId, Pageable pageable);

	@Query("select u1 from User u join u.users u1 where u.id = ?1")
	Page<User> findFollowedUsers(int userId, Pageable pageable);

	@Query("select u from User u join u.users u1 where u1.id = ?1")
	Page<User> findFollowers(int userId, Pageable pageable);

	@Query("select c from User u join u.users u1 join u1.chirps c where u.id = ?1")
	Page<Chirp> findFollowedUsersChirps(int userId, Pageable pageable);

	@Query("select n from User u join u.newspapers n where u.id=?1 and (n.publicationDate=null or n.publicationDate='')")
	Collection<Newspaper> findNotPublishedNewspapersByUser(int userId);

	/**
	 * Level C query 1
	 * 
	 * @return The average and the standard deviation of newspapers created per user.
	 * @author Antonio
	 */
	@Query("select avg(u.newspapers.size), sqrt(sum(u.newspapers.size * u.newspapers.size) / count(u.newspapers.size) - (avg(u.newspapers.size) * avg(u.newspapers.size))) from User u")
	String getNewspapersInfoFromUsers();

	/**
	 * Level C query 2
	 * 
	 * @return The average and the standard deviation of articles written by writer.
	 * @author Antonio
	 */
	@Query("select avg(u.newspapers.size), sqrt(sum(u.newspapers.size * u.newspapers.size) / count(u.newspapers.size) - (avg(u.newspapers.size) * avg(u.newspapers.size))) from User u")
	String getArticlesInfoFromUsers();

	/**
	 * Level C query 6
	 * 
	 * @return The ratio of users who have ever created a newspaper.
	 * @author Antonio
	 */
	@Query("select (select count(u1) from User u1 where u1.newspapers.size>0)/(count(u)*1.0) from User u")
	String getRatioCreatedNewspapers();

	/**
	 * Level C query 7
	 * 
	 * @return The ratio of users who have ever written an article.
	 * @author Antonio
	 */
	@Query("select (select count(u1) from User u1 join u1.newspapers n where n.articles.size>0)/(count(u)*1.0) from User u")
	String getRatioCreatedArticles();

	/**
	 * Level B query 4
	 * 
	 * @return The average and the standard deviation of the number of chirps per user.
	 * @author Antonio
	 */
	@Query("select avg(u.chirps.size), sqrt(sum(u.chirps.size * u.chirps.size) / count(u.chirps.size) - (avg(u.chirps.size) * avg(u.chirps.size))) from User u")
	String getChirpsInfoFromUsers();

	/**
	 * Level B query 5
	 * 
	 * @return The ratio of users who have posted above 75% the average number of chirps per user.
	 * @author Antonio
	 */
	@Query("select sum(case when(u.chirps.size>(select (avg(us.chirps.size)*0.75) from User us)) then 1.0 else 0.0 end)/count(u) from User u")
	String getRatioUsersPostedAbove75PercentAverageChirpsPerUser();

	/**
	 * Level A query 5
	 * 
	 * @return The average ratio of private versus public newspapers per publisher.
	 * @author Antonio
	 */
	@Query("select avg(sum(case when(n.publicNewspaper = FALSE) then 1.0 else 0.0 end)/count(n)) from User u join u.newspapers n ")
	String getAverageRatioPrivateNewspaperPerPublisher();
}
