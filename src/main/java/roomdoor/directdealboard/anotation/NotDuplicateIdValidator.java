package roomdoor.directdealboard.anotation;

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
		if (id == null) {
			return true;
		}
		return !userRepository.existsById(id);
	}
}
