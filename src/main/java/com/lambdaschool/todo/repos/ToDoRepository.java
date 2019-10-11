package com.lambdaschool.todo.repos;

import com.lambdaschool.todo.models.Todo;
import org.springframework.data.repository.CrudRepository;

public interface ToDoRepository extends CrudRepository<Todo, Long> {
}