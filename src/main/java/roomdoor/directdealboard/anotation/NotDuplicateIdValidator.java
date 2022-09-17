package roomdoor.directdealboard.anotation;

import java.text.MessageFormat;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import roomdoor.directdealboard.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class NotDuplicateIdValidator implements ConstraintValidator<NotDuplicateId, String> {

	private final UserRepository userRepository;

	@Override
	public void initialize(NotDuplicateId constraintAnnotation) {
	}

	@Override
	public boolean isValid(String id, ConstraintValidatorContext context) {
		boolean result = userRepository.existsById(id);
		if (!result) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(
					MessageFormat.format("Id {0} 가 이미 존재합니다!", id))
				.addConstraintViolation();
		}

		return !result;
	}
}
