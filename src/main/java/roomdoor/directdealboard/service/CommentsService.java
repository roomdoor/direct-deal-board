package roomdoor.directdealboard.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import roomdoor.directdealboard.dto.CommentsDto.CreateRequest;
import roomdoor.directdealboard.dto.CommentsDto.DeleteRequest;
import roomdoor.directdealboard.dto.CommentsDto.UpdateRequest;
import roomdoor.directdealboard.entity.Comments;
import roomdoor.directdealboard.entity.User;
import roomdoor.directdealboard.exception.exception.CommentsException;
import roomdoor.directdealboard.exception.exception.UserException;
import roomdoor.directdealboard.repository.CommentsRepository;
import roomdoor.directdealboard.repository.UserRepository;
import roomdoor.directdealboard.type.ErrorCode;

@Service
@RequiredArgsConstructor
public class CommentsService {

	private final CommentsRepository commentsRepository;

	private final UserRepository userRepository;

	private final BCryptPasswordEncoder passwordEncoder;

	public Comments getComment(Long id) {
		return commentsRepository.findById(id)
			.orElseThrow(() -> new CommentsException(ErrorCode.NOT_FOUND_COMMENTS));
	}

	public List<Comments> list(Long id) {
		return commentsRepository.findAllByPostsId(id);
	}

	public Comments create(CreateRequest createRequest) {
		User user = userRepository.findById(createRequest.getUserId())
			.orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

		return commentsRepository.save(Comments.builder()
			.postsId(createRequest.getPostsId())
			.writerNickName(createRequest.getWriterNickName())
			.userId(createRequest.getUserId())
			.comments(createRequest.getComments())
			.likeCount(0L)
			.build());
	}

	public Comments update(UpdateRequest updateRequest) {
		Comments comments = commentsRepository.findByPostsIdAndId(updateRequest.getPostsId(),
				updateRequest.getId())
			.orElseThrow(() -> new CommentsException(ErrorCode.NOT_FOUND_COMMENTS));

		User user = userRepository.findById(updateRequest.getUserId())
			.orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

		if (!passwordEncoder.matches(updateRequest.getUserPassword(), user.getPassword())) {
			throw new UserException(ErrorCode.PASSWORD_MISMATCH);
		}

		if (!ObjectUtils.isEmpty(updateRequest.getComments())) {
			comments.setComments(updateRequest.getComments());
		}

		return commentsRepository.save(comments);
	}

	public boolean delete(DeleteRequest deleteRequest) {
		Comments comments = commentsRepository.findByPostsIdAndId(deleteRequest.getPostsId(),
				deleteRequest.getId())
			.orElseThrow(() -> new CommentsException(ErrorCode.NOT_FOUND_COMMENTS));

		if (!comments.getWriterNickName().equals(deleteRequest.getWriterNickName())) {
			throw new CommentsException(ErrorCode.MISMATCH_WRITER);
		}

		User user = userRepository.findById(comments.getWriterNickName())
			.orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));

		if (!user.getPassword().equals(deleteRequest.getUserPassword())) {
			throw new CommentsException(ErrorCode.PASSWORD_MISMATCH);
		}

		commentsRepository.deleteByPostsIdAndId(deleteRequest.getPostsId(), deleteRequest.getId());

		return true;
	}
}
