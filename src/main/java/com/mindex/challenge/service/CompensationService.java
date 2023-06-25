package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;

public interface CompensationService {
    Compensation create(final Compensation compensation);
    Compensation read(final String employeeId);
}
