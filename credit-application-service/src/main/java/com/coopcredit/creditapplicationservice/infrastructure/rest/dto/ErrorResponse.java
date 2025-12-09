package com.coopcredit.creditapplicationservice.infrastructure.rest.dto;

import org.springframework.http.ProblemDetail;

import java.net.URI;
import java.time.Instant;
import java.util.UUID;

public class ErrorResponse extends ProblemDetail {

    private final Instant timestamp;
    private final String traceId;

    public ErrorResponse(int status, String title, String detail, URI instance) {
        this.setStatus(status);
        this.setTitle(title);
        this.setDetail(detail);
        this.setInstance(instance);
        this.timestamp = Instant.now();
        this.traceId = UUID.randomUUID().toString(); // Generate a unique trace ID
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getTraceId() {
        return traceId;
    }
}
