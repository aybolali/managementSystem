package com.example.managementsystem.controller;

import com.example.managementsystem.domain.Employee;
import com.example.managementsystem.exception.NotFoundException;
import com.example.managementsystem.model.EmployeeDTO;
import com.example.managementsystem.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EmployeeControllerTest {
    @Mock
    EmployeeService employeeService;

    EmployeeController controller;

    MockMvc mockMvc;

    @Before
    public void set() {
        MockitoAnnotations.initMocks(this);

        controller = new EmployeeController(employeeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }
    @Test
    public void viewHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("employee"));

    }

    @Test
    public void viewNewEmployeeForm() throws Exception{
        EmployeeDTO employeeDTO = new EmployeeDTO();

        mockMvc.perform(get("/newEmployeeForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("addForm"))
                .andExpect(model().attributeExists("newEmployee"));
    }

    @Test
    public void saveEmployee() throws Exception {
        EmployeeDTO command = new EmployeeDTO();
        command.setId(2L);

        when(employeeService.saveEmployee(any())).thenReturn(command);

        mockMvc.perform(
                post("/saveEmployee")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("firstName", "some name")
                        .param("lastName", "some noname")
                        .param("email", "some email")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    public void updateEmployee() throws Exception {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("aybolali");
        employee.setLastName("sartbay");
        employee.setEmail("asartbai7@gmail.com");

        when(employeeService.getEmployeeByID(employee.getId())).thenReturn(employee);

        ArgumentCaptor<Employee> argumentCaptor =
                ArgumentCaptor.forClass(Employee.class);

        employee.setFirstName("a");


    }

    @Test
    public void showFormForUpdate() throws Exception {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(2L);

        when(employeeService.getEmployeeCommandByID(anyLong())).thenReturn(employeeDTO); //for model (inner some object from employee class)
                                                                                           // for not being null
        mockMvc.perform(
                get("/showFormForUpdate/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("update_employeeForm"))
                .andExpect(model().attributeExists("employee"));
    }

    @Test
    public void deleteEmployee() throws Exception {
        EmployeeDTO command = new EmployeeDTO();
        command.setId(1L);

        mockMvc.perform(get("/deleteEmployee/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));


        verify(employeeService, times(1)).deleteEmployeeById(anyLong());
    }

}