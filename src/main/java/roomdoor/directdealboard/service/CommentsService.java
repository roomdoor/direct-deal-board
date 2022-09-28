package roomdoor.directdealboard.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomdoor.directdealboard.dto.CommentsDto;
import roomdoor.directdealboard.dto.CommentsDto.CreateRequest;
import roomdoor.directdealboard.dto.CommentsDto.DeleteRequest;
import roomdoor.directdealboard.dto.CommentsDto.Response;
import roomdoor.directdealboard.dto.CommentsDto.UpdateRequest;
import roomdoor.directdealboard.entity.Comments;
import roomdoor.directdealboard.entity.Posts;
import roomdoor.directdealboard.entity.User;
import roomdoor.directdealboard.exception.CommentsException;
import roomdoor.directdealboard.exception.UserException;
import roomdoor.directdealboard.repository.CommentsRepository;
import roomdoor.directdealboard.repository.UserRepository;
import roomdoor.directdealboard.type.ErrorCode;

@Service
@RequiredArgsConstructor
public class CommentsService {

	private final CommentsRepository commentsRepository;

	private final UserRepository userRepository;

	public Comments getComment(Long id) {
		return commentsRepository.findById(id)
			.orElseThrow(() -> new CommentsException(ErrorCode.NOT_FOUND_COMMENTS));
	}

//	public List<Comments> list(Long id) {
//		return commentsRepository.findAllByPostsId(id);
//	}

	public Comments create(CreateRequest createRequest) {

		return commentsRepository.save(Comments.builder()
			.postsId(createRequest.getPostsId())
			.writer(createRequest.getWriter())
			.comments(createRequest.getComments())
			.likeCount(0L)
			.build());
	}

	public Comments update(UpdateRequest updateRequest) {
		Comments comments = commentsRepository.findByPostsIdAndId(updateRequest.getPostsId(),
				updateRequest.getId())
			.orElseThrow(() -> new CommentsException(ErrorCode.NOT_FOUND_COMMENTS));

		comments.setComments(updateRequest.getComments());
		return commentsRepository.save(comments);
	}

	public boolean delete(DeleteRequest deleteRequest) {
		Comments comments = commentsRepository.findByPostsIdAndId(deleteRequest.getPostsId(),
				deleteRequest.getId())
			.orElseThrow(() -> new CommentsException(ErrorCode.NOT_FOUND_COMMENTS));

		if (!comments.getWriter().equals(deleteRequest.getWriter())) {
			throw new CommentsException(ErrorCode.MISMATCH_WRITER);
		}

		User user = userRepository.findById(comments.getWriter())
			.orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

		if (!user.getPassword().equals(deleteRequest.getWriterPassword())) {
			throw new CommentsException(ErrorCode.PASSWORD_MISMATCH);
		}

		commentsRepository.deleteByPostsIdAndId(deleteRequest.getPostsId(), deleteRequest.getId());

		return true;
	}
}
