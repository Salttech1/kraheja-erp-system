package kraheja.constant;

/**
 * <p>Application response constant.</p>
 * 
 * @author sazzad.alom
 * @since 1.0.0
 */
public class ApiResponseMessage {

	private ApiResponseMessage() {}

	public static final String AUTHORIZATION_FAILED = "message authentication failed.";
	public static final String INVALID_HEADER_FIELDS = "invalid request header fields.";
	public static final String BAD_REQUEST = "bad request, check request fields and re-try.";
	public static final String TECHNICAL_ERROR = "technical error occured.";
	public static final String INETNAL_SERVER_ERROR = "internal server error";
	public static final String UNRECOGNIZED_PROPERTY = "un-recognized property in request fields.";
	public static final String MANDATORY_FIELDS_MISSING = "mandatory fields missing in request.";
	public static final String INCHEQUE_SAVE_SUCCESS = "incheque details save successfully.";
	//400 - BAD REQUEST
	public static final String MESSAGE_NOT_REDABLE = "message not readable.";
	public static final String JSON_PARSE_ERROR = "json parse error.";
	public static final String JSON_MAPPING_ERROR = "json mapping error";
	public static final String UN_SUPPORTED_MEDIA_TYPE = "un-supported media type.";//415 - UNSUPPORTED MEDIA TYPE
	public static final String METHOD_NOT_ALLLOWED = "method not allowed.";//405 - METHOD NOT ALLOWED

	public static final String EXCEPTION_OCCURE = "exception occure.";
	
	public static final String INCHEQ_DETAIL_FAILED_TO_SAVE = "incheq detail failed to save.";
	
}