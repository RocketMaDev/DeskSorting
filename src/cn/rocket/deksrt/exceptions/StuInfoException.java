package cn.rocket.deksrt.exceptions;

/**
 * @author Rocket
 * @version 1.0
 */
public class StuInfoException extends Exception{
    public StuInfoException() {
        super();
    }

    public StuInfoException(String message) {
        super(message);
    }

    public StuInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public StuInfoException(Throwable cause) {
        super(cause);
    }
}
