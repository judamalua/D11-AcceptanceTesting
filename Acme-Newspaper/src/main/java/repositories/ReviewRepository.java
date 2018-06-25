
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

	@Query("select r from Review r where r.newspaper.id = ?1")
	Collection<Review> getReviewsByNewspaper(int newsPaperId);

	@Query("select r from Review r where r.admin.id = ?1")
	Collection<Review> getReviewsWrotenByAdmin(int adminid);

	@Query("select r.ticker from Review r")
	Collection<String> getReviewTickers();

	@Query("select r from Review r where r.newspaper.id = ?1 and r.draf = false")
	Page<Review> findReviewsByNewspaper(int newspaperId, Pageable pageable);

	@Query("select r from Review r where r.admin.id = ?1")
	Page<Review> findReviewsWrotenByAdmin(int adminId, Pageable pageable);

}
