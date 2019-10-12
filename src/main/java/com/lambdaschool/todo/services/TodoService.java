package com.lambdaschool.todo.services;

import com.lambdaschool.todo.models.Todo;

import java.util.List;

public interface TodoService {
    Todo updateTodo(Todo todo, long todoid);
    List<Todo> findAllTodos();
}