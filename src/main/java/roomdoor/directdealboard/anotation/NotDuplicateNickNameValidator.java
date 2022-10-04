package roomdoor.directdealboard.anotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import roomdoor.directdealboard.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class NotDuplicateNickNameValidator implements
	ConstraintValidator<NotDuplicateNickName, String> {

	private final UserRepository userRepository;

	@Override
	public void initialize(NotDuplicateNickName constraintAnnotation) {
	}

	@Override
	public boolean isValid(String nickName, ConstraintValidatorContext context) {
		if (nickName == null) {
			return true;
		}

		return !userRepository.existsByNickName(nickName);
	}
}
