
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

}
