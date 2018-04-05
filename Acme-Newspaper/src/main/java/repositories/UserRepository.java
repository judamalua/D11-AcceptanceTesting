
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Article;
import domain.Newspaper;
import domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u join u.newspapers n where n.id=?1")
	User findUserByNewspaper(int newspaperId);

	@Query("select a from User u join u.newspapers n join n.articles a where u.id = ?1 and n.publicationDate != null")
	Page<Article> findUserPublishedArticles(int userId, Pageable pageable);

	@Query("select n from User u join u.newspapers n where u.id=?1")
	Page<Newspaper> findNewspapersByUser(int userId, Pageable pageable);

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
}
