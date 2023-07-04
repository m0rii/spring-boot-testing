package mori.java.springboottesting.repository;

import java.util.Optional;
import mori.java.springboottesting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    //define custom query using JPQL with index params
    @Query("select e from Employee e where e.firstName =?1 and e.lastName=?2")
    Employee findByJPQL(String firstName, String lastName);

    //define custom query using JPQL with named params
    @Query("select e from Employee e where e.firstName =:firstname and e.lastName=:lastName")
    Employee findByJPQLNamedParams(@Param("firstname") String firstName, @Param("lastName") String lastName);

    //define custom query using Native SQL  with index params
    @Query(value = "select * from employees e where e.first_name =?1 and e.last_name=?2", nativeQuery = true)
    Employee findByNativeSql(String firstName, String lastName);

    //define custom query using Native SQL with named params
    @Query(value = "select * from employees e where e.first_name =:firstname and e.last_name=:lastName", nativeQuery = true)
    Employee findByNativeSqlNamedParams(@Param("firstname") String firstName, @Param("lastName") String lastName);

}
