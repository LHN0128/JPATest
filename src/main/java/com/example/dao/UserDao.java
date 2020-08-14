package com.example.dao;

import com.example.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDao extends JpaRepository<User,Long>, JpaSpecificationExecutor<User>{
    //测试根据方法名称自定义查询
    List<User> findByUsernameLikeAndSex(String username, String sex);

    //测试使用JPQL语句查询,一个参数也要写序号
    @Query(value="from user where sex = ?1")
    List<User> findBySex(String sex);


    //测试使用JPQL语句更新
    @Query(value = "update user set password=?2 where username=?1")
    @Modifying
    void updatePassword(String username,String password);

    //测试使用SQL完成查询
    @Query(value = "select * from user where sex = ?1",nativeQuery = true)
    List<User> findFemaleUsers(String sex);

}
