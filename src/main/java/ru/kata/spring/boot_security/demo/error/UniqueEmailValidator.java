package ru.kata.spring.boot_security.demo.error;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, User> {

    private UserService userService;

    @Autowired
    void setUniqueEmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if (userService == null) {
                return true;
            }
            if (Objects.equals(user.getEmail(), userService.findById(user.getId()).getEmail())) {
                return true;
            }
            return !userService.existsByEmail(user.getEmail());

        } catch (EntityNotFoundException e) {
            return !userService.existsByEmail(user.getEmail());
        }

    }
}
