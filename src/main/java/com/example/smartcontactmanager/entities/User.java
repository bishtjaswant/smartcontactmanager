package com.example.smartcontactmanager.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private long id;
//    @NotBlank(message = "name is required")
//    @Size(min = 4, max=10, message = "Name must be b/w 4 to 10")
    private String name;
    @Column(unique = true)
//    @NotBlank(message = "email is required")
//    @Email(regexp = "^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$")
    private String email;
//    @NotBlank(message = "name is required")
//    @Size(min = 4, max=15, message = "Name must be b/w 4 to 15")
    private String password;
    private String role;
    private boolean enabled;
    private String imgUrl;
    @Column(length = 500)
    private String about_yourself;
    @Transient
//    @AssertTrue(message = "Email must be valid")
    private boolean terms;


    @OneToMany(cascade =  CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Contact> contacts;


}
