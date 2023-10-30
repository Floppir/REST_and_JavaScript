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
import java.security.Principal;
import java.util.List;

@RestController
public class RESTController {

    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public RESTController(UserServiceImpl userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
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
        userService.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/edit_user/{id}")
    public ResponseEntity<?> editUser(@RequestBody User user) {
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