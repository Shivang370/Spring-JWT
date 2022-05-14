package org.example.repository;

import org.example.model.DAOUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface userDao extends JpaRepository<DAOUser,Long> {
     DAOUser findByUsername(String username) ;

     @Query("select d from DAOUser d where d.email =:email")
     DAOUser findByEmail(@Param(value = "email") String email);
}
