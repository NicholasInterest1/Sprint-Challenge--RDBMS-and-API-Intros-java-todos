package com.lambdaschool.todo.controllers;

import com.lambdaschool.todo.models.Todo;
import com.lambdaschool.todo.models.User;
import com.lambdaschool.todo.services.TodoService;
import com.lambdaschool.todo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoController {

    @Autowired
    private TodoService todoService;

    @Autowired
    private UserService userService;

    @PutMapping(value = "/todos/todoid/{todoid}",
            produces = {"application/json"},
            consumes = {"application/json"})
    public ResponseEntity<?> updateTodo(Authentication authentication, @RequestBody Todo updatedTodo, @PathVariable long todoid) {

        User currentUser = userService.findUserByName(authentication.getName());

        for(Todo t: currentUser.getTodos()) {
            if(t.getTodoid()==todoid) {
                todoService.update(updatedTodo, todoid);
                return new ResponseEntity<>("UPDATE SUCCESS", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}