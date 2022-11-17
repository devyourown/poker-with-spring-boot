package org.example.domain.error;

public class BetException extends Exception {
    private ErrorCode errorCode = ErrorCode.OK;

    public BetException(String message) {
        super(message);
    }

    public BetException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    public String errorMessage() {
        switch (errorCode) {
            case OK :
                return "TILT: Should not get here.";
            case INVALID_BET_SIZE:
                return "Bet size should be 100 unit.";
            case TOO_SMALL_BET_SIZE:
                return "Bet size should upper than 100.";
            case MONEY_NOT_ENOUGH:
                return "Money is not enough to bet.";

        }
        return "";
    }
    public enum ErrorCode {
        OK, INVALID_BET_SIZE, TOO_SMALL_BET_SIZE,
        MONEY_NOT_ENOUGH;
    }
}
