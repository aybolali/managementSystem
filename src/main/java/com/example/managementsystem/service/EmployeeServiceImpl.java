package com.example.managementsystem.service;

import com.example.managementsystem.domain.Employee;
import com.example.managementsystem.exception.NotFoundException;
import com.example.managementsystem.mapper.EmployeeMapperImpl;
import com.example.managementsystem.model.EmployeeDTO;
import com.example.managementsystem.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapperImpl employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapperImpl employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public List<EmployeeDTO> getAllEmployee() {
        return employeeRepository.findAll()
                .stream()
                .map(employeeMapper::employeeToEmployeeDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Employee getEmployeeByID(Long id) {
        Optional<Employee> optional = employeeRepository.findById(id);

        Employee employee = null;
        if(optional.isPresent())
            employee = optional.get();
        else log.error("employee not found for " + id);
        return employee;
    }

    @Override
    @Transactional
    public EmployeeDTO getEmployeeCommandByID(Long id){
        return employeeRepository.findById(id)
                .map(employeeMapper::employeeToEmployeeDTO)
                .map(employeeDTO -> {
                    employeeDTO.setId(employeeDTO.getId());
                    return employeeDTO;
                })
                .orElseThrow(NotFoundException::new);
    }


    @Override
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee detachedEmployee = employeeMapper.employeeDTOToEmployee(employeeDTO);

        Employee savedEmployee = employeeRepository.save(detachedEmployee);
        log.debug("Saved Employee id: " + savedEmployee.getId());
        return employeeMapper.employeeToEmployeeDTO(savedEmployee);
    }

    @Override
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }
}
