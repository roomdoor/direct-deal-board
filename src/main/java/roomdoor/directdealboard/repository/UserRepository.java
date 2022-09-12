package roomdoor.directdealboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomdoor.directdealboard.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	boolean existsById(String id);

	boolean existsByNickName(String nickName);
}
