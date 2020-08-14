package com.example.dao;

import com.example.domain.LinkMan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LinkManDao extends JpaRepository<LinkMan,Long>, JpaSpecificationExecutor<LinkMan> {
    @Query(value = "delete from linkman where customer_id=?1")
    @Modifying
    void deleteLinkmanByCustomer_id(long l);
}
