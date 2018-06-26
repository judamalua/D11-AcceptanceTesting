
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

	@Query("select r from Review r where r.newspaper.id = ?1 and r.draf = false and r.moment < NOW()")
	Page<Review> findReviewsByNewspaper(int newspaperId, Pageable pageable);

	@Query("select r from Review r where r.admin.id = ?1")
	Page<Review> findReviewsWrotenByAdmin(int adminId, Pageable pageable);

	//The ratio of reviews per newspaper
	@Query("select 1.0*((select count(n) from Newspaper n where (select count(r) from Review r where r.newspaper.id=n.id)>0/count(p))) from Newspaper p")
	Double getRatioReviewsNewspapers();

	//The admin with more tremites

	//		@Query("select a from Admin a where (select count(r) from Review r where r.admin.id=a.id)>= (select max(1.0*(select count(r) from Review r where r.admin.id=a.id)) from Admin a)")
	//		Administrator getMaxTremitesAdmin();

}
