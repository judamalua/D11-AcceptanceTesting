
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Advertisment;

@Repository
public interface AdvertismentRepository extends JpaRepository<Advertisment, Integer> {

}
