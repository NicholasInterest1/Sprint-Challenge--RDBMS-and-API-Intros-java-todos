package com.lambdaschool.todo.repos;

import com.lambdaschool.todo.models.Todo;
import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<Todo, Long> {
}