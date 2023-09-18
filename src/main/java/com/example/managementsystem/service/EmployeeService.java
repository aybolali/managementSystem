package com.example.managementsystem.service;

import com.example.managementsystem.domain.Employee;
import com.example.managementsystem.model.EmployeeDTO;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getAllEmployee();
    Employee getEmployeeByID(Long id);

    EmployeeDTO getEmployeeCommandByID(Long id); //for access with validations
    EmployeeDTO saveEmployee(EmployeeDTO employeeDTO);
    void deleteEmployeeById(Long id);
}
