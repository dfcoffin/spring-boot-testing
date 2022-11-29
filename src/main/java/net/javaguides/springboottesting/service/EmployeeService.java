package net.javaguides.springboottesting.service;

import net.javaguides.springboottesting.model.Employee;

import java.util.List;
import java.util.Optional;

/**
 * @author Donald F. Coffin
 */
public interface EmployeeService {

    Employee saveEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Optional<Employee> getEmployeeById(long id);

    Employee updateEmployee(Employee employee);

    void deleteEmployee(long id);
}
