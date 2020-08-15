package com.example.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "customer")
public class Customer {

    /**
      *  @Author Liu Haonan
      *  @Date 2020/8/14 19:27
      *  @Description 客户实体类，一个客户实体类对应多个联系人
      */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_id")
    private Long cust_id;
    @Column(name = "cust_address")
    private String cust_address;
    @Column(name = "cust_industry")
    private String cust_industry;
    @Column(name = "cust_level")
    private String cust_level;
    @Column(name = "cust_name")
    private String cust_name;
    @Column(name = "cust_phone")
    private String cust_phone;
    @Column(name = "cust_source")
    private String cust_source;

    @OneToMany(targetEntity = LinkMan.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id",referencedColumnName = "cust_id")
    private Set<LinkMan> linkManSet = new HashSet<>();//注意集合对象要实例化
}
