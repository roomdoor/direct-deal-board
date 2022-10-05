package roomdoor.directdealboard.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import roomdoor.directdealboard.entity.Heart;
import roomdoor.directdealboard.entity.User;

public interface HeartRepository extends JpaRepository<Heart, Long> {

	Optional<Heart> findHeartByUserIdAndPostsId(String userId, Long postsId);
}