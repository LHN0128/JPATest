package com.example.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
//弃用@Table注解，使用@Entity(name=“表名”)
/**
  *  @Author Liu Haonan
  *  @Date 2020/8/14 19:39
  *  @Description 用户类，用户和角色多对多
  */
@Entity(name = "user")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自动确定数据库（mysql为IDENTIFY）
    private Long user_id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "sex")
    private String sex;
    @ManyToMany(targetEntity = Role.class,cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_key",referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_key",referencedColumnName = "role_id")}
    )
    private Set<Role> roleSet = new HashSet<>();

}
