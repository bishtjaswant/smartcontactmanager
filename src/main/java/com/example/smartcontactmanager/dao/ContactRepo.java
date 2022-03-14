package com.example.smartcontactmanager.dao;

import com.example.smartcontactmanager.entities.Contact;
import com.example.smartcontactmanager.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ContactRepo extends JpaRepository<Contact, Long> {
    @Query("FROM Contact c where c.user.id = :uid ORDER BY c.id DESC")
    Page<Contact> findAllContactsByUser(@Param("uid") long uid, Pageable pageable);

   //find contact by id
    Contact findContactById(long id);

//    search query
    List<Contact> findByNameContainingAndUser(String name, User user);

}
