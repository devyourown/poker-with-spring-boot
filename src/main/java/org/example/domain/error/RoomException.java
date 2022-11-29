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
            case DUPLICATED_ROOM:
                return "Player Should have only one room.";
            case ID_NOT_EXIST:
                return "The ID does not existed.";
            case NOT_REMOVABLE:
                return "The room is used by someone.";
        }
        return "";
    }
    public enum ErrorCode {
        OK, NOT_ENOUGH_PLAYER, TOO_MANY_PLAYER, DUPLICATED_ROOM,
        ID_NOT_EXIST, NOT_REMOVABLE;
    }
}
