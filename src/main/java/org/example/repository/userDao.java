package org.example.repository;

import org.example.model.DAOUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userDao extends JpaRepository<DAOUser,Long> {
     DAOUser findByUsername(String username) ;
}
