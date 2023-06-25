package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure get(final String employeeId) {
        LOG.debug("Getting reportingStructure for employeeId: [{}]", employeeId);
        final Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + employeeId);
        }
        final int reporteeCount = employee.getDirectReports() == null || employee.getDirectReports().isEmpty() ?
                0 : getReporteeCount(employee);
        return new ReportingStructure(employee, reporteeCount);
    }

    private synchronized int getReporteeCount(final Employee employee) {
        final Queue<Employee> queue = new LinkedList<>();
        final Set<String> uniqueSet = new HashSet<>();
        if (employee.getEmployeeId() != null) {
            queue.add(employee);
            uniqueSet.add(employee.getEmployeeId());
        }
        while (!queue.isEmpty()) {
            Employee reportee = queue.poll();
            if (reportee.getDirectReports() != null) {
                for (Employee e : reportee.getDirectReports()) {
                    if (! uniqueSet.contains(e.getEmployeeId())) {
                        final Employee empById = employeeRepository.findByEmployeeId(e.getEmployeeId());
                        queue.add(empById);
                        uniqueSet.add(empById.getEmployeeId());
                    }
                }
            }
        }
        return uniqueSet.isEmpty() ? 0 : uniqueSet.size() - 1;
    }
}
