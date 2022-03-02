package com.example.springboot.testing.tutorial.repository;


import com.example.springboot.testing.tutorial.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Long> {

    Optional<Employee> findByEmail(String email);

    //custom query using JPQL using index  param
    @Query("select e from Employee e where e.firstName = ?1 and  e.lastName = ?2 ")
    Employee findByJPQL(String firstName,String lastName);

    //custom query using JPQL using named  param
    @Query("select e from Employee e where e.firstName =:firstName  and  e.lastName =:lastName ")
    Employee findByJPQLNamedParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

    //custom query using native sql using index  params
    @Query(value = "select  * from  employees e where  e.first_name =?1 and e.last_name =?2",nativeQuery = true)
    Employee findByNativeSQL(String firstName, String lastName);

    //custom query using native sql using named  params
    @Query(value = "select  * from  employees e where  e.first_name =:firstName and e.last_name =:lastName",nativeQuery = true)
    Employee findByNativeSQLNamed(@Param("firstName") String firstName,@Param("lastName") String lastName);
}
