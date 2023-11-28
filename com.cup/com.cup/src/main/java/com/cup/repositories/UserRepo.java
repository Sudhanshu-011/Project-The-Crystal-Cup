package com.cup.repositories;

import com.cup.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

// 7) From here findByEmailId method runs whose implementation is given in the User in entities and hibernate write the query
public interface UserRepo extends JpaRepository<User, Integer> {

    User findByEmailId(@Param("email") String email);
}
