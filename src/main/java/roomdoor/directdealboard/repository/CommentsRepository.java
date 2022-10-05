package roomdoor.directdealboard.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomdoor.directdealboard.entity.Comments;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {

	List<Comments> findAllByPostsId(Long postsId);

	Optional<Comments> findByPostsIdAndId(Long postsId, Long Id);

	void deleteByPostsIdAndId(Long postsId, Long id);

	Page<Comments> findAllByPostsId(Long id, Pageable pageable);
}
