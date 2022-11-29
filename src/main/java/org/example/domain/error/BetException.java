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
            case NOT_INTEGER:
                return "Money should be number.";
            case NOT_YOUR_TURN:
                return "Now is not your turn.";
            case NOT_POSSIBLE_CHECK:
                return "Cannot check now.";
        }
        return "";
    }
    public enum ErrorCode {
        OK, INVALID_BET_SIZE, TOO_SMALL_BET_SIZE, NOT_POSSIBLE_CHECK,
        MONEY_NOT_ENOUGH, NOT_INTEGER, NOT_YOUR_TURN;
    }
}
