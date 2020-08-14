package com.example.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "linkman")
@Getter
@Setter
@ToString
public class LinkMan {
    /**
      *  @Author Liu Haonan
      *  @Date 2020/8/14 19:27
      *  @Description 联系人实体类，多个联系人对应一个客户
      */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lkm_id;
    @Column(name = "lkm_name")
    private String lkm_name;
    @Column(name = "lkm_gender")
    private String lkm_gender;
    @Column(name = "lkm_phone")
    private String lkm_phone;
    @Column(name = "lkm_address")
    private String lkm_address;

    @ManyToOne(targetEntity = Customer.class)
    @JoinColumn(name = "customer_id",referencedColumnName = "cust_id")
    private Customer customer;


}
