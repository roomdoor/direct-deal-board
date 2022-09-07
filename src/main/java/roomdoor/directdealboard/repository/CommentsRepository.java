package roomdoor.directdealboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomdoor.directdealboard.entity.Comments;
import roomdoor.directdealboard.entity.Posts;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

}
