
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.MessageFolder;

@Repository
public interface MessageFolderRepository extends JpaRepository<MessageFolder, Integer> {

	@Query("select mf from Actor a join a.messageFolders mf where mf.name=?1 and a.id=?2 and mf.isDefault=true")
	MessageFolder findMessageFolder(String name, Integer id);

	@Query("select mf from Actor a join a.messageFolders mf where mf.messageFolderFather = null and a.id=?1")
	Collection<MessageFolder> findRootMessageFolders(int actorId);

	@Query("select mf from Actor a join a.messageFolders mf where mf.messageFolderFather = null and a.id=?1")
	Page<MessageFolder> findRootMessageFolders(int actorId, Pageable pageable);

	@Query("select mf from MessageFolder mf where mf.messageFolderFather.id=?1")
	Page<MessageFolder> findMessageFolderChildren(int messageFolderId, Pageable pageable);

	@Query("select mf from MessageFolder mf where mf.messageFolderFather.id=?1")
	Collection<MessageFolder> findMessageFolderChildren(int messageFolderId);
}
