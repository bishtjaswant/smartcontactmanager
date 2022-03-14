package com.example.smartcontactmanager.dao;


import com.example.smartcontactmanager.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepo extends JpaRepository<User,Integer> {

    @Query("select u from User as u where u.email=:email")
  User getUserByUsername(@Param("email") String email);
}
