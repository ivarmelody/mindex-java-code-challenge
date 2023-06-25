package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    @Autowired
    private CompensationService compensationService;

    private String compensationUrl;
    private String compensationByEmployeeIdUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationByEmployeeIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void create_compensation_createsCompensationSuccessfully() {
        final Compensation compensation = new Compensation();
        compensation.setEmployeeId("testEmpId");
        compensation.setSalary("100");
        compensation.setEffectiveDate("06-01-2023");

        Compensation resultComp = restTemplate.postForEntity(compensationUrl, compensation, Compensation.class).getBody();
        assertNotNull(resultComp);
        assertEquals("100", resultComp.getSalary());
        assertEquals("testEmpId", resultComp.getEmployeeId());
    }

    @Test
    public void read_compensationByEmployeeId_returnsCompensationSuccessfully() {
        final Compensation compensation = new Compensation();
        compensation.setEmployeeId("testEmpId");
        compensation.setSalary("100");
        compensation.setEffectiveDate("06-01-2023");

        Compensation createComp = restTemplate.postForEntity(compensationUrl, compensation, Compensation.class).getBody();
        assertNotNull(createComp);
        Compensation getComp = restTemplate.getForEntity(compensationByEmployeeIdUrl, Compensation.class,
                createComp.getEmployeeId()).getBody();
        assertNotNull(getComp);
        assertEquals("100", getComp.getSalary());
        assertEquals("06-01-2023", getComp.getEffectiveDate());
    }
}
