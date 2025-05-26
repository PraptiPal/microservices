package com.icress.emp_api.repository;

import com.icress.emp_api.entity.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @DisplayName("Junit for create employee operation")
    @Test
    public void givenEmployee_whenSave_thenReturnSavedEmployee(){

        //1. given or arrange
        Employee employee = Employee.builder()
                .name("Prapti")
                .age(25)
                .salary(25000)
                .address("BLR-25")
                .build();

        //2. when or action
        Employee savedEmployee = employeeRepository.save(employee);

        //3. then or assertion
        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);
    }


    @DisplayName("Junit for get All Employees Operation")
    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeeList(){

        Employee aliEmployee = Employee.builder()
                .name("ali")
                .age(25)
                .salary(25000)
                .address("BLR-25")
                .build();

        Employee maliEmployee = Employee.builder()
                .name("mali")
                .age(27)
                .salary(27000)
                .address("BLR-27")
                .build();

        employeeRepository.save(aliEmployee);
        employeeRepository.save(maliEmployee);

        List<Employee> employeeList = employeeRepository.findAll();

        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("Junit for get employee by id")
    @Test
    public void givenEmployee_whenFindById_thenReturnEmployee(){
        Employee aliEmployee = Employee.builder()
                .name("ali")
                .age(25)
                .salary(25000)
                .address("BLR-25")
                .build();

        employeeRepository.save(aliEmployee);

        Employee aliEmployeeDB = employeeRepository.findById(aliEmployee.getId()).get();

        Assertions.assertThat(aliEmployeeDB).isNotNull();
        Assertions.assertThat(aliEmployeeDB.getName()).isEqualTo("ali");
    }

    @DisplayName("Junit for update employee by id")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        Employee aliEmployee = Employee.builder()
                .name("ali")
                .age(25)
                .salary(25000)
                .address("BLR-25")
                .build();

        employeeRepository.save(aliEmployee);

        Employee aliEmployeeDB = employeeRepository.findById(aliEmployee.getId()).get();
        aliEmployeeDB.setName("mohd ali");
        Employee updatedAliEmployeeDB = employeeRepository.save(aliEmployeeDB);

        Assertions.assertThat(updatedAliEmployeeDB).isNotNull();
        Assertions.assertThat(updatedAliEmployeeDB.getName()).isEqualTo("mohd ali");
    }

    @DisplayName("Junit for delete employee by id")
    @Test
    public void givenEmployeeObject_whenDeleteEmployee_thenDeleteEmployee(){

        Employee aliEmployee = Employee.builder()
                .name("ali")
                .age(25)
                .salary(25000)
                .address("BLR-25")
                .build();

        Employee createdEmployee = employeeRepository.save(aliEmployee);

        employeeRepository.deleteById(createdEmployee.getId());
        Optional<Employee> aliEmployeeDB = employeeRepository.findById(createdEmployee.getId());

        Assertions.assertThat(aliEmployeeDB).isEmpty();
    }
}
