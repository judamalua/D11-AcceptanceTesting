
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chirp;

@Repository
public interface ChirpRepository extends JpaRepository<Chirp, Integer> {

	@Query("select c from Chirp c where c.taboo = TRUE")
	Collection<Chirp> getAllTabooChirps();
	
	@Query("select c from User u join u.users u1 join u1.chirps c where u.id = ?1 order by c.moment DESC")
	Page<Chirp> findFollowedUsersChirps(int userId, Pageable pageable);
	
	@Query("select c from User u join u.chirps c where u.id= ?1 order by c.moment DESC")
	Page<Chirp> findUserChirps(int userId, Pageable pageable);

}
