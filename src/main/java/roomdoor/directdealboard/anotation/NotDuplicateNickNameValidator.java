package roomdoor.directdealboard.anotation;

import java.text.MessageFormat;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import roomdoor.directdealboard.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class NotDuplicateNickNameValidator implements ConstraintValidator<NotDuplicateNickName, String> {

	private final UserRepository userRepository;

	@Override
	public void initialize(NotDuplicateNickName constraintAnnotation) {
	}

	@Override
	public boolean isValid(String nickName, ConstraintValidatorContext context) {
		boolean result = userRepository.existsByNickName(nickName);
		if (!result) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					MessageFormat.format("닉네임 {0} 가 이미 존재합니다!", nickName))
				.addConstraintViolation();
		}

		return !result;
	}
}
