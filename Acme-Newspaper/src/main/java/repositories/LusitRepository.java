
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Admin;
import domain.Lusit;

@Repository
public interface LusitRepository extends JpaRepository<Lusit, Integer> {

	@Query("select l from Admin a join a.lusits l where a.id = ?1 and l.finalMode = ?2")
	Page<Lusit> findLusitsByAdmin(Integer adminId, Boolean finalMode, Pageable pageable);

	@Query("select l from Admin a join a.lusits l where a.id = ?1")
	Collection<Lusit> findLusitsByAdmin(Integer adminId);

	@Query("select a from Admin a join a.lusits l where l.id = ?1")
	Admin findAdminByLusit(Integer lusitId);

	@Query("select l from Newspaper n join n.lusits l where n.id = ?1 and l.finalMode = true and (l.publicationDate=null or l.publicationDate='' or l.publicationDate<=CURRENT_TIMESTAMP)")
	Page<Lusit> findLusitsByNewspaper(Integer newspaperId, Pageable pageable);

	@Query("select count(n)*1.0/(select count(ne) from Newspaper ne where (ne.publicationDate!=null or ne.publicationDate!='')) from Newspaper n where n.lusits.size>0")
	String ratioNewspapersAtLeastOneLusit();

	@Query("select a from Admin a where a.lusits.size = (select max(a.lusits.size) from Admin a)")
	Page<Admin> findAdminWithMoreLusits(Pageable pageable);

}
