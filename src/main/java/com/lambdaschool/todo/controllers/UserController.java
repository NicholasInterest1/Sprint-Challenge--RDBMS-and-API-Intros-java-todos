package com.lambdaschool.todo.controllers;

import com.lambdaschool.todo.models.Todo;
import com.lambdaschool.todo.models.User;
import com.lambdaschool.todo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;



@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET -- http://localhost:2019/users/mine

    @GetMapping(value = "/mine",
            produces = {"application/json"})
    public ResponseEntity<?> getCurrentUserInfo(Authentication authentication) {

        User u = userService.findByName(authentication.getName());
        return new ResponseEntity<>(u,
                HttpStatus.OK);
    }

    // http://localhost:2019/users/users/

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/users",
            produces = {"application/json"})
    public ResponseEntity<?> listAllUsers() {

        List<User> myUsers = userService.findAll();
        return new ResponseEntity<>(myUsers,
                HttpStatus.OK);
    }

    // POST -- http://localhost:2019/users/user
    // adds a user

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/user",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<?> addNewUser(@Valid @RequestBody User newuser) throws URISyntaxException {

        newuser = userService.save(newuser);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userid}").buildAndExpand(newuser.getUserid()).toUri();
        responseHeaders.setLocation(newUserURI);
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    // POST -- http://localhost:2019/users/todo/{userid}
    // adds a to-do to the assigned user

    @PostMapping(value = "/todo/{userid}",
            consumes = {"application/json"})
    public ResponseEntity<?> addTodo(@Valid @RequestBody Todo todo, @PathVariable long userid) {

        userService.addTodo(todo, userid);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // DELETE - http://localhost:2019/users/user/{id}

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/userid/{userid}")
    public ResponseEntity<?> deleteUserById(@PathVariable long userid) {

        userService.delete(userid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}