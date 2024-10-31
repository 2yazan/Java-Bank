package main.java.bank.service;

import main.java.bank.entity.BankOffice;
import main.java.bank.entity.Employee;
import main.java.bank.exception.UniquenessException;

import java.util.List;

public interface EmployeeService {
    Employee create(Employee employee) throws UniquenessException;
    public Employee getEmployeeById(long id);
    public List<Employee> getAllEmployees();
    public boolean isEmployeeSuitable(Employee employee);
}
