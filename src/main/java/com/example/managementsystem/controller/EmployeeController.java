package com.example.managementsystem.controller;

import com.example.managementsystem.domain.Employee;
import com.example.managementsystem.exception.NotFoundException;
import com.example.managementsystem.model.EmployeeDTO;
import com.example.managementsystem.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


@Controller
@Slf4j
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public String viewHomePage(Model model){
        model.addAttribute("employee", employeeService.getAllEmployee());

        return "home";
    }

    @GetMapping("/newEmployeeForm")
    public String ViewNewEmployeeForm(Model model){
        model.addAttribute("newEmployee", new Employee());
        return "addForm";
    }

    @PostMapping("/saveEmployee") //when clicking a button
    public String saveEmployee(@Valid @ModelAttribute("newEmployee") EmployeeDTO employeeDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){

            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });

            return "addForm";
        }
        // save employee to database
        employeeService.saveEmployee(employeeDTO);
        return "redirect:/";
    }

    @PostMapping("/updateEmployee") //when clicking a button
    public String UpdateEmployee(@Valid @ModelAttribute("employee") EmployeeDTO employeeDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {

            bindingResult.getAllErrors().forEach(objectError -> {
                log.debug(objectError.toString());
            });

            return "update_employeeForm";
        }

        // save employee to database
        employeeService.saveEmployee(employeeDTO);
        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable Long id, Model model){
        //set employee AS A MODEL ATTRIBUTE TO PRE-POPULATE THE FORM
        model.addAttribute("employee", employeeService.getEmployeeCommandByID(id));

        return "update_employeeForm";
    }
    @GetMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployeeById(id);
        return "redirect:/";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception exception){
        log.error("not found exception");
        log.error(exception.getMessage());

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("exception", exception);
        modelAndView.setViewName("error404");

        return modelAndView;
    }
}
