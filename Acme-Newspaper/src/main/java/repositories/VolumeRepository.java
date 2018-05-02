
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Newspaper;
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
	@Query("select avg(v.newspapers.size) from Volume v ")
	String getAverageNewspapersPerVolume();

	/**
	 * Level B query 1
	 * 
	 * @return The ratio of subscriptions to volumes versus subscriptions to newspapers.
	 * @author MJ
	 */
	@Query("select count(cd)*1.0/(select count(nw)*1.0 from Newspaper nw), (count(v)*1.0/(select count(vl)*1.0 from Volume vl)) from Newspaper n join n.creditCards cd, CreditCard c join c.volumes v ")
	String getRatioSubscriptionsToVolumesVsRatioSubscriptiosNewspapers();

	@Query("select n from Volume v join v.newspapers n where v.id = ?1 and n.publicNewspaper = FALSE")
	Collection<Newspaper> getSubscribableNewspapersFromVolume(int volumeId);

	@Query("select v from CreditCard c join c.volumes v where c.customer.id = ?1 ")
	Page<Volume> findVolumesByCustomer(int customerId, Pageable pageable);

	@Query("select v from Volume v where v.user.id = ?1")
	Page<Volume> findVolumesByUser(int customerId, Pageable pageable);

}
