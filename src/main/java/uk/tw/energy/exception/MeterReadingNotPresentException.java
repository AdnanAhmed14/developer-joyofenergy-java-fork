package uk.tw.energy.exception;

public class MeterReadingNotPresentException extends RuntimeException{
    public MeterReadingNotPresentException(String message){
        super(message);
    }
}
