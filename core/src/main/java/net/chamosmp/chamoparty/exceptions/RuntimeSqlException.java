package net.chamosmp.chamoparty.exceptions;

import java.io.Serial;

public class RuntimeSqlException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5224696788505678598L;

    public RuntimeSqlException() {
        super();
    }

    public RuntimeSqlException(String message) {
        super(message);
    }

    public RuntimeSqlException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeSqlException(Throwable cause) {
        super(cause);
    }

}