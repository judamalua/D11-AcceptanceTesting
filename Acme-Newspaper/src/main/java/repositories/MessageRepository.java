
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
	//
	//	@Query("select m from Actor a  join a.messageFolders mf join mf.messages m where a.id = ?1")
	//	Collection<Message> findMessagesByActorId(Integer id);
}
