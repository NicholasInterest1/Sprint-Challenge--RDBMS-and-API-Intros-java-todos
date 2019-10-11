package com.lambdaschool.todo.services;

import com.lambdaschool.todo.models.Todo;
import com.lambdaschool.todo.models.User;
import com.lambdaschool.todo.models.UserRoles;
import com.lambdaschool.todo.models.Useremail;
import com.lambdaschool.todo.repos.RoleRepository;
import com.lambdaschool.todo.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userrepos;

    @Autowired
    private RoleRepository rolerepos;

    @Transactional
    public User findUserById(long id) throws EntityNotFoundException {

        return userrepos.findById(id).orElseThrow(() -> new EntityNotFoundException(Long.toString(id)));
    }

    public List<User> findAll() {

        List<User> list = new ArrayList<>();

        userrepos.findAll().iterator().forEachRemaining(list::add);

        return list;
    }

    @Override
    public void delete(long id) {

        if (userrepos.findById(id).isPresent()) {
            userrepos.deleteById(id);

        } else {
            throw new EntityNotFoundException(Long.toString(id));
        }
    }

    @Transactional
    @Override
    public User save(User user) {

        User newUser = new User();

        newUser.setUsername(user.getUsername());

        newUser.setPasswordNoEncrypt(user.getPassword());

        newUser.setPrimaryemail(user.getPrimaryemail().toLowerCase());

        ArrayList<UserRoles> newRoles = new ArrayList<>();

        for (UserRoles ur : user.getUserRoles()) {
            System.out.println(ur.getRole().getRolename());
            newRoles.add(new UserRoles(newUser, ur.getRole()));
        }
        newUser.setUserRoles(newRoles);

        for (Useremail ue : user.getUseremails()) {
            newUser.getUseremails()
                    .add(new Useremail(newUser,
                            ue.getUseremail()));
        }

        for (Todo t : user.getTodos()) {
            newUser.getTodos().add(new Todo(t.getDescription(), t.getDatestarted(), newUser));
        }
        return userrepos.save(newUser);
    }

    @Override
    public User findUserByName(String name) {

        System.out.println("HERE");
        User currentUser = userrepos.findByUsername(name);

        if (currentUser != null) {
            return currentUser;
        } else {
            throw new EntityNotFoundException(name);
        }
    }

    @Transactional
    @Override
    public User update(User user, long id) {

        User currentUser = userrepos.findById(id).orElseThrow(() -> new EntityNotFoundException(Long.toString(id)));

        if (user.getUsername() != null) {
            currentUser.setUsername(user.getUsername());
        }

        if (user.getPassword() != null) {
            currentUser.setPasswordNoEncrypt(user.getPassword());
        }

        if (user.getUserRoles().size() > 0) {
            rolerepos.deleteUserRolesByUserId(currentUser.getUserid());
            for (UserRoles ur : user.getUserRoles()) {
                rolerepos.insertUserRoles(id, ur.getRole().getRoleid());
            }
        }

        if (user.getTodos().size() > 0) {
            for (Todo t : user.getTodos()) {
                currentUser.getTodos().add(new Todo(t.getDescription(), t.getDatestarted(), currentUser));
            }
        }
        return userrepos.save(currentUser);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = userrepos.findByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthority()); //we have to make a change in user for getAuthority
    }
}