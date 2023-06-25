package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {
    private String employeeUrl;
    private String reportingStructureUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        reportingStructureUrl = "http://localhost:" + port + "/employee/{id}/reportingStructure";
    }

    @Test
    public void get_withNoDirectReports_returnsZeroReporteeCount() {
        Employee employee = new Employee();
        employee.setFirstName("A");
        employee.setLastName("B");
        Employee createdEmp = restTemplate.postForEntity(employeeUrl, employee, Employee.class).getBody();
        final ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureUrl,
                ReportingStructure.class, createdEmp.getEmployeeId()).getBody();
        assertEquals(0, reportingStructure.getNumberOfReports());
    }

    @Test
    public void get_withDirectReports_returnsNonZeroReporteeCount() {
        Employee employee1 = new Employee();
        employee1.setFirstName("A");
        employee1.setLastName("B");

        Employee employee2 = new Employee();
        employee2.setFirstName("C");
        employee2.setLastName("D");

        List<Employee> employeeList = new ArrayList<>();

        employee1.setDirectReports(employeeList);

        Employee createdEmp2 = restTemplate.postForEntity(employeeUrl, employee2, Employee.class).getBody();
        employeeList.add(createdEmp2);

        Employee createdEmp1 = restTemplate.postForEntity(employeeUrl, employee1, Employee.class).getBody();

        final ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureUrl,
                ReportingStructure.class, createdEmp1.getEmployeeId()).getBody();

        assertEquals(1, reportingStructure.getNumberOfReports());
    }

    @Test
    public void get_withLoopDirectReports_returnsZeroReporteeCount() {
        Employee employee1 = new Employee();
        employee1.setFirstName("A");
        employee1.setLastName("B");

        List<Employee> employeeList = new ArrayList<>();

        employee1.setDirectReports(employeeList);

        Employee createdEmp1 = restTemplate.postForEntity(employeeUrl, employee1, Employee.class).getBody();
        employeeList.add(createdEmp1);

        final ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureUrl,
                ReportingStructure.class, createdEmp1.getEmployeeId()).getBody();

        assertEquals(0, reportingStructure.getNumberOfReports());
    }

    @Test
    public void get_withMultipleDirectReports_returnsNonZeroReporteeCount() {
        Employee employee5 = new Employee();
        employee5.setFirstName("K");
        employee5.setLastName("L");
        Employee createdEmp5 = restTemplate.postForEntity(employeeUrl, employee5, Employee.class).getBody();

        Employee employee4 = new Employee();
        employee4.setFirstName("G");
        employee4.setLastName("H");
        employee4.setDirectReports(Arrays.asList(createdEmp5));
        Employee createdEmp4 = restTemplate.postForEntity(employeeUrl, employee4, Employee.class).getBody();

        Employee employee3 = new Employee();
        employee3.setFirstName("E");
        employee3.setLastName("F");
        employee3.setDirectReports(Arrays.asList(createdEmp4));
        Employee createdEmp3 = restTemplate.postForEntity(employeeUrl, employee3, Employee.class).getBody();

        Employee employee2 = new Employee();
        employee2.setFirstName("C");
        employee2.setLastName("D");
        Employee createdEmp2 = restTemplate.postForEntity(employeeUrl, employee2, Employee.class).getBody();

        Employee employee1 = new Employee();
        employee1.setFirstName("A");
        employee1.setLastName("B");
        employee1.setDirectReports(Arrays.asList(createdEmp2, createdEmp3));
        Employee createdEmp1 = restTemplate.postForEntity(employeeUrl, employee1, Employee.class).getBody();

        final ReportingStructure reportingStructure = restTemplate.getForEntity(reportingStructureUrl,
                ReportingStructure.class, createdEmp1.getEmployeeId()).getBody();

        assertEquals(4, reportingStructure.getNumberOfReports());
    }

}
