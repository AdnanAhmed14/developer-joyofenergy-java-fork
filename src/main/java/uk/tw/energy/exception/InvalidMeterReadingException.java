package uk.tw.energy.exception;

public class InvalidMeterReadingException extends RuntimeException {

    public InvalidMeterReadingException(String message) {
        super(message);
    }
}

