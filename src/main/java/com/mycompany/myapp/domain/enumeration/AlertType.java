package com.mycompany.myapp.domain.enumeration;

/**
 * The AlertType enumeration.
 */
public enum AlertType {
    INFO("Info"),
    WARN("Warn"),
    ERROR("Error"),
    DEBUG("Debug"),
    UNKNOWN("Unknown");

    private final String value;


    AlertType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
