package com.example.smartcontactmanager.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "contacts")

@Data
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "contact_id")
    private long id;
    private String name;
    private String email;
    private String nick_name;
    private String number;
    private String work;

    private String imgUrl;
    @Column(length = 500)
    private String description;

    @ManyToOne( fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @Override
    public String toString() {
        return "Contact{}";
    }
}
