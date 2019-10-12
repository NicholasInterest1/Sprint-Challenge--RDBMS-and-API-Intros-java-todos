package com.lambdaschool.todo.services;

import com.lambdaschool.todo.models.Todo;
import com.lambdaschool.todo.repos.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "todoService")
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todorepos;

    @Transactional
    @Override
    public Todo updateTodo(Todo todo, long todoid) {

        Todo currentTodo = todorepos.findById(todoid).orElseThrow(() -> new EntityNotFoundException(Long.toString(todoid)));

        if (todo.getDatestarted() != null) {
            currentTodo.setDatestarted(todo.getDatestarted());
        }

        if (todo.getDescription() != null) {
            currentTodo.setDescription(todo.getDescription());
        }

        currentTodo.setCompleted(todo.isCompleted());

        return todorepos.save(currentTodo);
    }

    @Override
    public List<Todo> findAllTodos() {
        List<Todo> rtnList = new ArrayList<>();
        todorepos.findAll().iterator().forEachRemaining(rtnList::add);

        return rtnList;
    }
}