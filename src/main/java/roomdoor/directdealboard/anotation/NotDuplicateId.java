package roomdoor.directdealboard.anotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = NotDuplicateIdValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotDuplicateId {
	String message() default "중복된 아이디 입니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
