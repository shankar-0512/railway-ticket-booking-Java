package com.space.app.exception;

public class SpaceException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message;

	@Override
	public String getMessage() {
		return message;
	}

	public SpaceException(String message, Throwable cause) {
		super(message, cause);
		this.message = message;
	}

	public SpaceException(String message) {
		super(message);
		this.message = message;
	}

	public SpaceException(Throwable cause) {
		super(cause);
		this.message = cause.getMessage();
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("Space-Ticket-Booking-Exception [message=").append(message).append("]");
		return builder.toString();
	}
}
