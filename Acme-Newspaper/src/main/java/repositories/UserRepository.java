
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Newspaper;
import domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u join u.newspapers n where n.id=?1")
	User findUserByNewspaper(int newspaperId);

	@Query("select u from User u join u.articles a where a.id=?1")
	User findUserByArticle(int articleId);

	//	@Query("select a from User u join u.articles a where u.id = 19 and a member of (select a1 from Newspaper n join n.articles a1 where n.publicationDate != null)")
	//	Page<Article> findUserArticles(int userId);
	//
	//	@Query("select a from Newspaper n join n.articles a where n.publicationDate != null")
	//	Page<Article> findAllNewspaperArticles();

	@Query("select n from User u join u.newspapers n where u.id=?1")
	Page<Newspaper> findNewspapersByUser(int userId, Pageable pageable);

}
