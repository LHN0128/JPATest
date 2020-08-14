package com.example.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
  *  @Author Liu Haonan
  *  @Date 2020/8/14 19:29
  *  @Description 角色实体类，多个用户对应多个角色
  */

@Entity(name = "role")
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long role_id;
    @Column(name = "role_name")
    private String roleName;

//    @ManyToMany(targetEntity = User.class)
//    @JoinTable(name = "user_role",
//            joinColumns = {@JoinColumn(name = "role_key",referencedColumnName = "role_id")},
//            inverseJoinColumns = {@JoinColumn(name = "user_key",referencedColumnName = "user_id")}
//                )
    @ManyToMany(mappedBy = "roleSet")
    private Set<User> userSet = new HashSet<>();

}
