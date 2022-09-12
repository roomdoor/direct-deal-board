package roomdoor.directdealboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomdoor.directdealboard.entity.Posts;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

}
