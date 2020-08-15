package com.example;

import com.example.dao.CustomerDao;
import com.example.dao.LinkManDao;
import com.example.dao.RoleDao;
import com.example.dao.UserDao;
import com.example.domain.Customer;
import com.example.domain.LinkMan;
import com.example.domain.Role;
import com.example.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpaTestApplication.class)
public class TestJPA {

    @Autowired
    private UserDao userDao;
    @Autowired
    private LinkManDao linkManDao;
    @Autowired
    private CustomerDao customerDao;
    @Autowired
    private RoleDao roleDao;

    @Test
    public void testFindAllUser(){
        List<User> userList  = userDao.findAll();
        for (Object user : userList) {
            System.out.println(user);

        }

    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/8/12 20:21
      *  @Description 使用模糊查询，查询id大于等于5的所有user记录
      */
    @Test
    public void testFindByIds(){
        List<User> userList  = userDao.findAll((root,criteriaQuery,criteriaBuilder)->{//重写toPredicate方法
            Path<Object> id = root.get("id");
            return criteriaBuilder.ge(id.as(Long.class), 5);//
        });
        for (User user : userList) {
            System.out.println(user);
        }

    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/8/12 22:24
      *  @Description 查询出所有姓张的，性别为男的用户。通过方法名称自定义查询
      */
    @Test
    public void testFindByMethodName(){//
        String username = "张%";
        String sex = "男";
        List<User> resultList = userDao.findByUsernameLikeAndSex(username, sex);
        for (User user : resultList) {

            System.out.println(user);
        }
    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/8/12 22:39
      *  @Description 使用JPQL查询，查询性别为男的所有user
      */
    @Test
    public void testFindUserBySexUseJPQL(){
        String sex = "男";
        List<User> bySex = userDao.findBySex(sex);
        for (User user : bySex) {
            System.out.println(user);
        }
    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/8/12 22:53
      *  @Description 使用JPQL完成更新密码操作
      */
    @Test
    public void testUpdatePasswordUseJPQL(){
        String password = "666";
        String username="王五";
        userDao.updatePassword(username,password);
    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/8/12 22:55
      *  @Description 使用原生SQL语句完成查询操作
      */
    @Test
    public void testFindFemaleUseSQL(){
        String sex = "女";
        List<User> femaleUsers = userDao.findFemaleUsers(sex);
        for (User femaleUser : femaleUsers) {
            System.out.println(femaleUser);
        }
    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/8/13 18:17
      *  @Description 一对多添加。
     *      使用级联更新，保存customer的同时保存它的linkman
      */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testOneToManySaveCascade(){
        Customer customer= new Customer();
        customer.setCust_name("小白");

        LinkMan linkMan = new LinkMan();
        linkMan.setLkm_address("北京");
        linkMan.setLkm_name("小红");

//        linkMan.setCustomer(customer);
        customer.getLinkManSet().add(linkMan);

        customerDao.save(customer);
//        linkManDao.save(linkMan);
    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/8/14 17:01
      *  @Description 一对多自定义删除
      */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testOneToManyDelete(){
        Specification<Customer>spec = new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path<Object> cust_name = root.get("cust_name");
                return criteriaBuilder.equal(cust_name,"小白");
            }
        };
        Optional<Customer>optional = customerDao.findOne(spec);

        Long cust_id = optional.get().getCust_id();


        customerDao.deleteCustomerByCust_id(cust_id);
        linkManDao.deleteLinkmanByCustomer_id(cust_id);
    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/8/15 1:07
      *  @Description 一对多级联删除，效果和上面的自定义删除等同。
      */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testOneToManyDeleteCascade(){
        Specification<Customer>spec = new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path<Object> cust_name = root.get("cust_name");
                return criteriaBuilder.equal(cust_name,"小白");
            }
        };
        Optional<Customer>optional = customerDao.findOne(spec);
        Customer customer = optional.get();
        customerDao.delete(customer);

    }

    /**
      *  @Author Liu Haonan
      *  @Date 2020/8/15 0:34
      *  @Description 多对多，被动的一方（角色role）放弃关联关系
      */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testManyToMany(){
        User user = new User();
        user.setUsername("赵六");
        Role role = new Role();
        role.setRoleName("Java开发工程师");

        user.getRoleSet().add(role);
//        role.getUserSet().add(user);
        roleDao.save(role);
        userDao.save(user);
    }
    /**
      *  @Author Liu Haonan
      *  @Date 2020/8/15 0:42
      *  @Description 测试多对多的级联添加
      */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testManyToManyCascadeAdd(){

        User user = new User();
        user.setUsername("赵六");
        Role role = new Role();
        role.setRoleName("Java开发工程师");

        user.getRoleSet().add(role);
//        role.getUserSet().add(user);
//        roleDao.save(role);
        userDao.save(user);
    }
    /**
     *  @Author Liu Haonan
     *  @Date 2020/8/15 0:42
     *  @Description 测试多对多的级联删除
     *               注意实体类互相包含集合，不能带有toString方法，会陷入死循环导致栈溢出异常
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testManyToManyCascadeRemove(){
        Optional<User> userOptional = userDao.findOne((root, criteriaQuery, criteriaBuilder) ->{
            Path<Object> username = root.get("username");
            return criteriaBuilder.equal(username, "赵六");
        });
        User user = userOptional.get();
        userDao.delete(user);

    }

    /**
      *  @Author Liu Haonan
      *  @Date 2020/8/15 17:45
      *  @Description 使用对象导航查询，查询customer和与他相关的所有联系人
     *                  直接调用get方法即可
      */

    @Test
    @Transactional
    @Rollback(value = false)
    public void testOGNQuery(){
        Customer customer = customerDao.getOne(3l);//查询id为3的客户
        //使用对象导航查询，调用get方法就可以获取到下面所有的里联系人
        Set<LinkMan> linkManSet = customer.getLinkManSet();
        for (LinkMan linkMan : linkManSet) {
            System.out.println(linkMan);
        }

    }
}
