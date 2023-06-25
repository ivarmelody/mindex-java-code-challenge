package com.mindex.challenge.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Compensation {
    private static final Logger LOG = LoggerFactory.getLogger(Compensation.class);

    private String employeeId;
    private BigDecimal salary;
    private Date effectiveDate;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getSalary() {
        return salary.toPlainString();
    }

    public void setSalary(String salary) {
        this.salary = new BigDecimal(salary);
    }

    @Override
    public String toString() {
        return "Compensation{" +
                "employeeId='" + employeeId + '\'' +
                '}';
    }

    public String getEffectiveDate() {
        return new SimpleDateFormat("mm-dd-yyyy").format(effectiveDate);
    }

    public void setEffectiveDate(String effectiveDate) {
        try {
            this.effectiveDate = new SimpleDateFormat("mm-dd-yyyy").parse(effectiveDate);
        } catch (Exception e) {
            LOG.error("Caught Parsing Exception", e);
        }
    }
}
