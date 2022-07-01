package tw.niq.app.exception;

public class UserServiceException extends RuntimeException {

	private static final long serialVersionUID = 4257145680867444715L;

	public UserServiceException(String message) {
		super(message);
	}

}
