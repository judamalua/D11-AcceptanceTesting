
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u join u.newspapers n where n.id=?1")
	User findUserByNewspaper(int newspaperId);

	@Query("select u from User u join u.articles a where a.id=?1")
	User findUserByArticle(int articleId);
}
