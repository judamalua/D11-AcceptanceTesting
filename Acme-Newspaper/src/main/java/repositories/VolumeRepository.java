
package repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Volume;

@Repository
public interface VolumeRepository extends JpaRepository<Volume, Integer> {

	@Query("select v from Volume v")
	Page<Volume> findVolumes(Pageable pageable);

	/**
	 * Level B query 1
	 * 
	 * @return The average number of newspapers per volume.
	 * @author MJ
	 */
	@Query("select avg(v.newspapers.size)  from Volume v ")
	String getAverageNewspapersPerVolume();

	/**
	 * Level B query 1
	 * 
	 * @return The ratio of subscriptions to volumes versus subscriptions to newspapers.
	 * @author MJ
	 */
	@Query("select count(cd)/(select count(nw) from Newspaper nw), (count(v)/select count(vl) from Volume vl) from Newspaper n join n.creditCards cd, CreditCard c join c.volumes v ")
	String getRatioSubscriptionsToVolumesVsRatioSubscriptiosNewspapers();

}
