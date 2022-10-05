package roomdoor.directdealboard.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import roomdoor.directdealboard.dto.CommentsDto.CreateRequest;
import roomdoor.directdealboard.dto.CommentsDto.DeleteRequest;
import roomdoor.directdealboard.dto.CommentsDto.Response;
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

	public Response getComment(Long id) {
		return Response.of(commentsRepository.findById(id)
			.orElseThrow(() -> new CommentsException(ErrorCode.NOT_FOUND_COMMENTS)));
	}

	public List<Response> list(Long id) {
		return Response.of(commentsRepository.findAllByPostsId(id));
	}

	public Page<Response> list(Long id, int pageNumber) {
		Pageable pageable = PageRequest.of(pageNumber, 10);
		return new PageImpl<>(Response.of(commentsRepository.findAllByPostsId(id, pageable).getContent()));
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

	public Response update(UpdateRequest updateRequest) {
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

		return Response.of(commentsRepository.save(comments));
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
