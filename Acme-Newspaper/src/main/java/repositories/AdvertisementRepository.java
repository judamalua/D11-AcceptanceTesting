
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Advertisement;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

	/**
	 * Level C query 2
	 * 
	 * @return The ratio of advertisements that have taboo words
	 * @author MJ
	 */
	@Query("select count(a)/(select count(ad) from Advertisement ad) from Advertisement a where a.taboo=true")
	String getRatioTabooAdvertisements();

	@Query("select a from Advertisement a where a.taboo=true")
	Page<Advertisement> findTabooAdvertisements(Pageable pageable);

	@Query("select a from Advertisement a where a.taboo=false")
	Page<Advertisement> findNotTaboo(Pageable pageable);

	@Query("select a from Advertisement a where a.agent.id=?1")
	Page<Advertisement> findByAgentPage(int agentId, Pageable pageable);

}
