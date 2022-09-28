package roomdoor.directdealboard.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomdoor.directdealboard.dto.PostsDto;
import roomdoor.directdealboard.dto.PostsDto.CreateRequest;
import roomdoor.directdealboard.dto.PostsDto.DeleteRequest;
import roomdoor.directdealboard.dto.PostsDto.Response;
import roomdoor.directdealboard.dto.PostsDto.UpdateRequest;
import roomdoor.directdealboard.entity.Posts;
import roomdoor.directdealboard.entity.User;
import roomdoor.directdealboard.exception.exception.PostsException;
import roomdoor.directdealboard.exception.exception.UserException;
import roomdoor.directdealboard.repository.CommentsRepository;
import roomdoor.directdealboard.repository.PostsRepository;
import roomdoor.directdealboard.repository.UserRepository;
import roomdoor.directdealboard.type.ErrorCode;

@Service
@RequiredArgsConstructor
public class PostsService {

	private final PostsRepository postsRepository;

	private final UserRepository userRepository;

	private final CommentsRepository commentsRepository;

	public Response getPosts(Long id) {
		Optional<Posts> optionalPosts = postsRepository.findById(id);

		if (!optionalPosts.isPresent()) {
			throw new PostsException(ErrorCode.NOT_FOUND_POSTS);
		}


		Posts posts = optionalPosts.get();
		posts.setViews(posts.getViews() + 1);
		postsRepository.save(posts);

		return PostsDto.Response.of(posts);
	}

	public List<PostsDto.Response> list() {
		return PostsDto.Response.of(postsRepository.findAll());
	}


	public PostsDto.Response create(CreateRequest createRequest) {
		Posts posts = postsRepository.save(Posts.builder()
			.writerNickName(createRequest.getWriterNickName())
			.userId(createRequest.getUserId())
			.title(createRequest.getTitle())
			.text(createRequest.getText())
			.category(createRequest.getCategory())
			.views(0L)
			.likeCount(0L)
			.isSailed(false)
			.build());

		return PostsDto.Response.of(posts);
	}

	public PostsDto.Response update(UpdateRequest updateRequest) {
		Optional<Posts> optionalPosts = postsRepository.findById(updateRequest.getId());

		if (!optionalPosts.isPresent()) {
			throw new PostsException(ErrorCode.NOT_FOUND_POSTS);
		}

		Posts posts = optionalPosts.get();

		if (posts.getUserId().equals(updateRequest.getWriterNickName())) {
			throw new PostsException(ErrorCode.MISMATCH_WRITER);
		}

		posts.setTitle(updateRequest.getTitle());
		posts.setText(updateRequest.getText());

		return PostsDto.Response.of(postsRepository.save(posts));
	}


	public boolean delete(DeleteRequest deleteRequest) {
		Optional<Posts> optionalPosts = postsRepository.findById(deleteRequest.getId());

		if (!optionalPosts.isPresent()) {
			throw new PostsException(ErrorCode.NOT_FOUND_POSTS);
		}

		Optional<User> optionalUser = userRepository.findById(deleteRequest.getUserId());
		if (!optionalUser.isPresent()) {
			throw new UserException(ErrorCode.NOT_FOUND_USER);
		}

		User user = optionalUser.get();
		if (!user.getPassword().equals(deleteRequest.getUserPassword())) {
			throw new UserException(ErrorCode.PASSWORD_MISMATCH);
		}


		postsRepository.delete(optionalPosts.get());
		return true;
	}
}
