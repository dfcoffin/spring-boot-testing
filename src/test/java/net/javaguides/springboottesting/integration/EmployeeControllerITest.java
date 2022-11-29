package net.javaguides.springboottesting.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.javaguides.springboottesting.model.Employee;
import net.javaguides.springboottesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Donald F. Coffin
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();
    }


    // JUnit Integration test for create Employee REST API
    @DisplayName("JUnit Integration test for create Employee REST API")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the results or output using assert statements
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));

    }

    // JUnit Integration test for Get All employees REST API
    @DisplayName("JUnit Integration test for Get All employees REST API")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        // given - precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Ramesh").lastName("Fadatare").email("ramesh@gmail.com")
                .build());
        listOfEmployees.add(Employee.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com")
                .build());
        employeeRepository.saveAll(listOfEmployees);

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the result or output using assert statements
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfEmployees.size())));
    }

    // Positive scenario - valid employee id
    // JUnit Integration test for GET employee by id REST API (Positive scenario)
    @DisplayName("JUnit Integration test for GET employee by id REST API (Positive scenario)")
    @Test
    public void givenValidEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;

        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        // then - verify the result or output using assert statements
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    // Negative scenario - invalid employee id
    // JUnit Integration test for GET employee by id REST API (Negative scenario)
    @DisplayName("JUnit Integration test for GET employee by id REST API (Negative scenario)")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;

        Employee employee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then - verify the result or output using assert statements
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // Positive scenario - valid employee id
    // JUnit Integration test for update employee REST API (Positive scenario)
    @DisplayName("JUnit Integration test for update employee REST API (Positive scenario)")
    @Test
    public void givenValidUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        // given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Ram")
                .lastName("Jadhav")
                .email("ram@gmail.com")
                .build();


        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the result or output using assert statements
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    // Negative scenario - invalid employee id
    // JUnit Integration test for update employee REST API (Negative scenario)
    @DisplayName("JUnit Integration test for update employee REST API (Negative scenario)")
    @Test
    public void givenInvalidUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;

        Employee savedEmployee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Ram")
                .lastName("Jadhav")
                .email("ram@gmail.com")
                .build();

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then - verify the result or output using assert statements
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // JUnit Integration test for delete employee REST API
    @DisplayName("JUnit Integration test for delete employee REST API")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        // given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Ramesh")
                .lastName("Fadatare")
                .email("ramesh@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

        // then - verify the result or output using assert statements
        response.andExpect(status().isOk())
                .andDo(print());
    }
}
