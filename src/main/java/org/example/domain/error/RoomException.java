package org.example.domain.error;

public class RoomException extends Exception {
    private RoomException.ErrorCode errorCode = RoomException.ErrorCode.OK;

    public RoomException(String message) {
        super(message);
    }

    public RoomException(RoomException.ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    public String errorMessage() {
        switch (errorCode) {
            case OK :
                return "TILT: Should not get here.";
            case NOT_ENOUGH_PLAYER:
                return "Player should be more than 1 player to play";
            case TOO_MANY_PLAYER:
                return "Player should be less than 11 players.";
        }
        return "";
    }
    public enum ErrorCode {
        OK, NOT_ENOUGH_PLAYER, TOO_MANY_PLAYER;
    }
}
