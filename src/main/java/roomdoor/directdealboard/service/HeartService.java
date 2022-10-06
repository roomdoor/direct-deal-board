package roomdoor.directdealboard.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomdoor.directdealboard.dto.HeartDto;
import roomdoor.directdealboard.entity.Heart;
import roomdoor.directdealboard.entity.Posts;
import roomdoor.directdealboard.exception.exception.HeartException;
import roomdoor.directdealboard.exception.exception.PostsException;
import roomdoor.directdealboard.exception.exception.UserException;
import roomdoor.directdealboard.repository.HeartRepository;
import roomdoor.directdealboard.repository.PostsRepository;
import roomdoor.directdealboard.repository.UserRepository;
import roomdoor.directdealboard.type.ErrorCode;

@RequiredArgsConstructor
@Service
public class HeartService {

	private final HeartRepository heartRepository;
	private final UserRepository userRepository;
	private final PostsRepository postsRepository;
//	private final RestHighLevelClient elasticsearchClient;
//	private final JwtTokenProvider jwtTokenProvider;
//	private User user;

	public HeartDto heart(HeartDto heartDto) {
//		validateToken(heartDto, jwtToken);

		if (findHeartWithUserIdAndPostsId(heartDto).isPresent()) {
			throw new HeartException(ErrorCode.ALREADY_HEARTED);
		}

		Heart heart = Heart.builder()
			.postsId(postsRepository.findById(heartDto.getPostsId())
				.orElseThrow(() -> new PostsException(ErrorCode.NOT_FOUND_POSTS)).getId())
			.userId(userRepository.findById(heartDto.getUserId())
				.orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER)).getId())
			.build();

		Heart save = heartRepository.save(heart);

		Long heartCount = updateHeartCount(heartDto.getPostsId(), 1);

		HeartDto result = HeartDto.of(save);
		result.setNowHeartCount(heartCount);

		return result;
	}

	public HeartDto unHeart(HeartDto heartDto) {
//		validateToken(heartDto, jwtToken);

		Heart heart = findHeartWithUserIdAndPostsId(heartDto).orElseThrow(
			() -> new HeartException(ErrorCode.NOT_FOUND_HEART));

		heartRepository.deleteById(heart.getId());

		Long heartCount = updateHeartCount(heartDto.getPostsId(), -1);


		heartDto.setNowHeartCount(heartCount);
		return heartDto;
	}

//	public void validateToken(HeartDto heartDto, String jwtToken) {
//		// 생략 ... 유효한 토큰인지 검증하는 부분
//	}

	public Optional<Heart> findHeartWithUserIdAndPostsId(HeartDto heartDto) {
		return heartRepository
			.findHeartByUserIdAndPostsId(heartDto.getUserId(), heartDto.getPostsId());
	}

	public Long updateHeartCount(Long postsId, Integer plusOrMinus) {
		Posts posts = postsRepository.findById(postsId)
			.orElseThrow(() -> new PostsException(ErrorCode.NOT_FOUND_POSTS));
		long result = posts.getLikeCount() + plusOrMinus;
		posts.setLikeCount(result);
		postsRepository.save(posts);

		return result;
	}
}
