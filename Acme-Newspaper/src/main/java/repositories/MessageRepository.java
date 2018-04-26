
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select m from Message m, Actor a join a.messageFolders mf where m.messageFolder.id = mf.id and a.id=?1")
	Collection<Message> findMessagesByActorId(Integer id);

	@Query("select m from Message m  where m.messageFolder.id = ?1")
	Page<Message> findMessagesByMessageFolderId(Integer messageFolderId, Pageable pageable);

	@Query("select m from Message m  where m.messageFolder.id = ?1")
	Collection<Message> findMessagesByMessageFolderId(Integer messageFolderId);

}
