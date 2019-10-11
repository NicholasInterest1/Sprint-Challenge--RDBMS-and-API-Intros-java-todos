package com.lambdaschool.todo.repos;

import com.lambdaschool.todo.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}