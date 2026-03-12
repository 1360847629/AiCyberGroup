package org.cyberwarriors.domain.enumeration;

/**
 * The Status enumeration.
 */
public enum Status {
    PENDING,
    QUEUED,
    SANITIZING,
    SANITIZED,
    FAILED_SANITIZATION,
    IN_PROGRESS,
    RETRYING,
    COMPLETED,
    COMPLETED_WITH_WARNINGS,
    ERROR,
    CANCELLED,
}
