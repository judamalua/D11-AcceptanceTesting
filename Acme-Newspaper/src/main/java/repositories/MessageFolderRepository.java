
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.MessageFolder;

@Repository
public interface MessageFolderRepository extends JpaRepository<MessageFolder, Integer> {

	//	@Query("select mf from Actor a join a.messageFolders mf where mf.name=?1 and a.id=?2 and mf.isDefault=true")
	//	MessageFolder findMessageFolder(String name, Integer id);
	//
	//	@Query("select mf from Actor a join a.messageFolders mf where mf.messageFolderFather = null and a.id=?1")
	//	Collection<MessageFolder> findRootMessageFolders(int actorId);
}
