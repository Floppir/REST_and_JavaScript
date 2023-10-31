package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
public class RESTController {

    private final UserService userService;

    private final RoleService roleService;

    private final Validator validator;

    @Autowired
    public RESTController(UserServiceImpl userService, RoleService roleService, Validator validator) {
        this.userService = userService;
        this.roleService = roleService;
        this.validator = validator;
    }

    @GetMapping(value = "/userInfo")
    public ResponseEntity<User> getUserInfo(Principal principal) {
        User user = userService.findByEmail(principal.getName());

        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<?> getUserByID(@PathVariable Long id) {
        User user = userService.findById(id);
        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/save_user")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        userService.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/edit_user/{id}")
    public ResponseEntity<?> editUser(@RequestBody User user) {
        System.out.println(user.getRoles());
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        if (user.getPassword() == null) {
            user.setPassword(userService.findById(user.getId()).getPassword());
        }
        userService.update(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/getAllRoles")
    public List<Role> getAllRoles() {

        return roleService.findAll();
    }

    @GetMapping(value = "/allUsers")
    public ResponseEntity<List<User>> allUsers() {
        List<User> users = userService.findAll();

        return users != null && !users.isEmpty()
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete_user/{id}")
    public String deleteUser(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            throw new EntityNotFoundException("User ID = " + id + " not found");
        }
        userService.deleteById(id);
        return "User ID = " + id + " deleted";
    }

}