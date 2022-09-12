package roomdoor.directdealboard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import roomdoor.directdealboard.entity.User;
import roomdoor.directdealboard.repository.UserRepository;
import roomdoor.directdealboard.requestDto.UserCreateRequestDto;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public boolean userCreate(UserCreateRequestDto userCreateRequestDto) {
		Optional<User> optionalUser = userRepository.findById(userCreateRequestDto.getId());
		if (optionalUser.isPresent()) {
			return false;
		}

		userRepository.save(User.DtoToUser(userCreateRequestDto));

		return true;
	}

	public Map<String, String> validateHandler(Errors errors) {
		Map<String, String> result = new HashMap<>();

		for (FieldError error : errors.getFieldErrors()) {
			System.out.println(error.getField());
			System.out.println(error.getDefaultMessage());

			result.put(error.getField(), error.getDefaultMessage());
		}

		return result;
	}
}
