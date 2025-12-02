package com.om.demo.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@Entity
@Table(name = "users")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String username;



    @Column(unique = true)
    private String email;


    @Column(unique = true)
    private String phone;


    private String password;

    private boolean isProfileUpdated = false;


    private Double latitude;
    private Double longitude;


    private String pincode;

    private String country;
    private String state;
    private String district;
    private String city;
    private String area;





    @ManyToMany
    @JoinTable(
            name = "user_interests",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    private List<Interest> interests;


}