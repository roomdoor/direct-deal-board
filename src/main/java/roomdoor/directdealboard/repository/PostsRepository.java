package roomdoor.directdealboard.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomdoor.directdealboard.entity.Posts;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

}
