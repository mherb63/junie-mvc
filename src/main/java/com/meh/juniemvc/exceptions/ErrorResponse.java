package com.meh.juniemvc.exceptions;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response format for API errors.
 * Follows RFC 9457 ProblemDetails format.
 */
public record ErrorResponse(
    LocalDateTime timestamp,
    int status,
    String error,
    String message,
    List<String> details
) {}