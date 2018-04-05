
package repositories;

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
}
