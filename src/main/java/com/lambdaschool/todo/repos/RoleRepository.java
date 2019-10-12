package com.lambdaschool.todo.repos;

import com.lambdaschool.todo.models.Role;
import com.lambdaschool.todo.views.JustTheCount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RoleRepository extends CrudRepository<Role, Long> {

    @Query(value = "SELECT COUNT(*) as count FROM userroles WHERE userid = :userid AND roleid = :roleid", nativeQuery = true)
    JustTheCount checkUserRolesCombo(long userid, long roleid);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM UserRoles WHERE userid = :userid AND roleid = :roleid")
    void deleteUserRoles(long userid, long roleid);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO UserRoles(userid, roleid) VALUES (:userid, :roleid)", nativeQuery = true)
    void insertUserRoles(long userid, long roleid);

    Role findByNameIgnoreCase(String name);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Role SET NAME = :name WHERE roleid = :roleid")
    void updateRoleName(long roleid, String name);
}