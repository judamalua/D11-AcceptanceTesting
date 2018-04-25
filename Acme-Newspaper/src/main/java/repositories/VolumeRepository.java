
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
}
