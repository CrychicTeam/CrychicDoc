package snownee.kiwi.shadowed.com.ezylang.evalex;

import lombok.Generated;

public class BaseException extends Exception {

    private final int startPosition;

    private final int endPosition;

    private final String tokenString;

    private final String message;

    public BaseException(int startPosition, int endPosition, String tokenString, String message) {
        super(message);
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.tokenString = tokenString;
        this.message = super.getMessage();
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BaseException)) {
            return false;
        } else {
            BaseException other = (BaseException) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getStartPosition() != other.getStartPosition()) {
                return false;
            } else if (this.getEndPosition() != other.getEndPosition()) {
                return false;
            } else {
                Object this$tokenString = this.getTokenString();
                Object other$tokenString = other.getTokenString();
                if (this$tokenString == null ? other$tokenString == null : this$tokenString.equals(other$tokenString)) {
                    Object this$message = this.getMessage();
                    Object other$message = other.getMessage();
                    return this$message == null ? other$message == null : this$message.equals(other$message);
                } else {
                    return false;
                }
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof BaseException;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getStartPosition();
        result = result * 59 + this.getEndPosition();
        Object $tokenString = this.getTokenString();
        result = result * 59 + ($tokenString == null ? 43 : $tokenString.hashCode());
        Object $message = this.getMessage();
        return result * 59 + ($message == null ? 43 : $message.hashCode());
    }

    @Generated
    public String toString() {
        return "BaseException(startPosition=" + this.getStartPosition() + ", endPosition=" + this.getEndPosition() + ", tokenString=" + this.getTokenString() + ", message=" + this.getMessage() + ")";
    }

    @Generated
    public int getStartPosition() {
        return this.startPosition;
    }

    @Generated
    public int getEndPosition() {
        return this.endPosition;
    }

    @Generated
    public String getTokenString() {
        return this.tokenString;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }
}