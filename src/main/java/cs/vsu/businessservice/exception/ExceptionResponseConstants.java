package cs.vsu.businessservice.exception;

public final class ExceptionResponseConstants {
    public static final String SIGNATURE_EXCEPTION_RESPONSE = "No user with this username";
    public static final String TOKEN_EXPIRED_EXCEPTION_RESPONSE = "Authentication token is expired";
    public static final String BAD_CREDENTIALS_EXCEPTION_RESPONSE = "Received user credentials are not correct";
    public static final String MALFORMED_JWT_EXCEPTION_RESPONSE = "Authentication token is corrupted";
}