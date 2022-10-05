package roomdoor.directdealboard.service;

import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomdoor.directdealboard.dto.HeartDto;
import roomdoor.directdealboard.entity.Heart;
import roomdoor.directdealboard.entity.Posts;
import roomdoor.directdealboard.entity.User;
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

	public void heart(HeartDto heartDto) {
//		validateToken(heartDto, jwtToken);

		if (findHeartWithUserAndCampaignId(heartDto).isPresent()) {
			throw new HeartException(ErrorCode.ALREADY_HEARTED);
		}

		Heart heart = Heart.builder()
			.postsId(postsRepository.findById(heartDto.getPostsId())
				.orElseThrow(() -> new PostsException(ErrorCode.NOT_FOUND_POSTS)).getId())
			.userId(userRepository.findById(heartDto.getUserId())
				.orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER)).getId())
			.build();
		heartRepository.save(heart);

		updateHeartCount(heartDto.getPostsId(), 1);

	}

	public void unHeart(HeartDto heartDto){
//		validateToken(heartDto, jwtToken);

		Heart heart = findHeartWithUserAndCampaignId(heartDto).orElseThrow(
			() -> new HeartException(ErrorCode.NOT_FOUND_HEART));

		heartRepository.deleteById(heart.getId());

		updateHeartCount(heartDto.getPostsId(), -1);
	}

//	public void validateToken(HeartDto heartDto, String jwtToken) {
//		// 생략 ... 유효한 토큰인지 검증하는 부분
//	}

	public Optional<Heart> findHeartWithUserAndCampaignId(HeartDto heartDto) {
		return heartRepository
			.findHeartByUserIdAndPostsId(heartDto.getUserId(), heartDto.getPostsId());
	}

	public void updateHeartCount(Long postsId, Integer plusOrMinus) {
		Posts posts = postsRepository.findById(postsId)
			.orElseThrow(() -> new PostsException(ErrorCode.NOT_FOUND_POSTS));
		posts.setLikeCount(posts.getLikeCount() + plusOrMinus);
		postsRepository.save(posts);
	}
}
