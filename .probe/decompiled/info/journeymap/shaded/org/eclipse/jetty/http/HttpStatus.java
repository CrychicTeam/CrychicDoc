package info.journeymap.shaded.org.eclipse.jetty.http;

public class HttpStatus {

    public static final int CONTINUE_100 = 100;

    public static final int SWITCHING_PROTOCOLS_101 = 101;

    public static final int PROCESSING_102 = 102;

    public static final int OK_200 = 200;

    public static final int CREATED_201 = 201;

    public static final int ACCEPTED_202 = 202;

    public static final int NON_AUTHORITATIVE_INFORMATION_203 = 203;

    public static final int NO_CONTENT_204 = 204;

    public static final int RESET_CONTENT_205 = 205;

    public static final int PARTIAL_CONTENT_206 = 206;

    public static final int MULTI_STATUS_207 = 207;

    public static final int MULTIPLE_CHOICES_300 = 300;

    public static final int MOVED_PERMANENTLY_301 = 301;

    public static final int MOVED_TEMPORARILY_302 = 302;

    public static final int FOUND_302 = 302;

    public static final int SEE_OTHER_303 = 303;

    public static final int NOT_MODIFIED_304 = 304;

    public static final int USE_PROXY_305 = 305;

    public static final int TEMPORARY_REDIRECT_307 = 307;

    public static final int PERMANENT_REDIRECT_308 = 308;

    public static final int BAD_REQUEST_400 = 400;

    public static final int UNAUTHORIZED_401 = 401;

    public static final int PAYMENT_REQUIRED_402 = 402;

    public static final int FORBIDDEN_403 = 403;

    public static final int NOT_FOUND_404 = 404;

    public static final int METHOD_NOT_ALLOWED_405 = 405;

    public static final int NOT_ACCEPTABLE_406 = 406;

    public static final int PROXY_AUTHENTICATION_REQUIRED_407 = 407;

    public static final int REQUEST_TIMEOUT_408 = 408;

    public static final int CONFLICT_409 = 409;

    public static final int GONE_410 = 410;

    public static final int LENGTH_REQUIRED_411 = 411;

    public static final int PRECONDITION_FAILED_412 = 412;

    @Deprecated
    public static final int REQUEST_ENTITY_TOO_LARGE_413 = 413;

    public static final int PAYLOAD_TOO_LARGE_413 = 413;

    @Deprecated
    public static final int REQUEST_URI_TOO_LONG_414 = 414;

    public static final int URI_TOO_LONG_414 = 414;

    public static final int UNSUPPORTED_MEDIA_TYPE_415 = 415;

    @Deprecated
    public static final int REQUESTED_RANGE_NOT_SATISFIABLE_416 = 416;

    public static final int RANGE_NOT_SATISFIABLE_416 = 416;

    public static final int EXPECTATION_FAILED_417 = 417;

    public static final int IM_A_TEAPOT_418 = 418;

    public static final int ENHANCE_YOUR_CALM_420 = 420;

    public static final int MISDIRECTED_REQUEST_421 = 421;

    public static final int UNPROCESSABLE_ENTITY_422 = 422;

    public static final int LOCKED_423 = 423;

    public static final int FAILED_DEPENDENCY_424 = 424;

    public static final int UPGRADE_REQUIRED_426 = 426;

    public static final int PRECONDITION_REQUIRED_428 = 428;

    public static final int TOO_MANY_REQUESTS_429 = 429;

    public static final int REQUEST_HEADER_FIELDS_TOO_LARGE_431 = 431;

    public static final int UNAVAILABLE_FOR_LEGAL_REASONS_451 = 451;

    public static final int INTERNAL_SERVER_ERROR_500 = 500;

    public static final int NOT_IMPLEMENTED_501 = 501;

    public static final int BAD_GATEWAY_502 = 502;

    public static final int SERVICE_UNAVAILABLE_503 = 503;

    public static final int GATEWAY_TIMEOUT_504 = 504;

    public static final int HTTP_VERSION_NOT_SUPPORTED_505 = 505;

    public static final int INSUFFICIENT_STORAGE_507 = 507;

    public static final int LOOP_DETECTED_508 = 508;

    public static final int NOT_EXTENDED_510 = 510;

    public static final int NETWORK_AUTHENTICATION_REQUIRED_511 = 511;

    public static final int MAX_CODE = 511;

    private static final HttpStatus.Code[] codeMap = new HttpStatus.Code[512];

    public static HttpStatus.Code getCode(int code) {
        return code <= 511 ? codeMap[code] : null;
    }

    public static String getMessage(int code) {
        HttpStatus.Code codeEnum = getCode(code);
        return codeEnum != null ? codeEnum.getMessage() : Integer.toString(code);
    }

    public static boolean isInformational(int code) {
        return 100 <= code && code <= 199;
    }

    public static boolean isSuccess(int code) {
        return 200 <= code && code <= 299;
    }

    public static boolean isRedirection(int code) {
        return 300 <= code && code <= 399;
    }

    public static boolean isClientError(int code) {
        return 400 <= code && code <= 499;
    }

    public static boolean isServerError(int code) {
        return 500 <= code && code <= 599;
    }

    static {
        for (HttpStatus.Code code : HttpStatus.Code.values()) {
            codeMap[code._code] = code;
        }
    }

    public static enum Code {

        CONTINUE(100, "Continue"),
        SWITCHING_PROTOCOLS(101, "Switching Protocols"),
        PROCESSING(102, "Processing"),
        OK(200, "OK"),
        CREATED(201, "Created"),
        ACCEPTED(202, "Accepted"),
        NON_AUTHORITATIVE_INFORMATION(203, "Non Authoritative Information"),
        NO_CONTENT(204, "No Content"),
        RESET_CONTENT(205, "Reset Content"),
        PARTIAL_CONTENT(206, "Partial Content"),
        MULTI_STATUS(207, "Multi-Status"),
        MULTIPLE_CHOICES(300, "Multiple Choices"),
        MOVED_PERMANENTLY(301, "Moved Permanently"),
        MOVED_TEMPORARILY(302, "Moved Temporarily"),
        FOUND(302, "Found"),
        SEE_OTHER(303, "See Other"),
        NOT_MODIFIED(304, "Not Modified"),
        USE_PROXY(305, "Use Proxy"),
        TEMPORARY_REDIRECT(307, "Temporary Redirect"),
        PERMANET_REDIRECT(308, "Permanent Redirect"),
        BAD_REQUEST(400, "Bad Request"),
        UNAUTHORIZED(401, "Unauthorized"),
        PAYMENT_REQUIRED(402, "Payment Required"),
        FORBIDDEN(403, "Forbidden"),
        NOT_FOUND(404, "Not Found"),
        METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
        NOT_ACCEPTABLE(406, "Not Acceptable"),
        PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
        REQUEST_TIMEOUT(408, "Request Timeout"),
        CONFLICT(409, "Conflict"),
        GONE(410, "Gone"),
        LENGTH_REQUIRED(411, "Length Required"),
        PRECONDITION_FAILED(412, "Precondition Failed"),
        PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
        URI_TOO_LONG(414, "URI Too Long"),
        UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
        RANGE_NOT_SATISFIABLE(416, "Range Not Satisfiable"),
        EXPECTATION_FAILED(417, "Expectation Failed"),
        IM_A_TEAPOT(418, "I'm a Teapot"),
        ENHANCE_YOUR_CALM(420, "Enhance your Calm"),
        MISDIRECTED_REQUEST(421, "Misdirected Request"),
        UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
        LOCKED(423, "Locked"),
        FAILED_DEPENDENCY(424, "Failed Dependency"),
        UPGRADE_REQUIRED(426, "Upgrade Required"),
        PRECONDITION_REQUIRED(428, "Precondition Required"),
        TOO_MANY_REQUESTS(429, "Too Many Requests"),
        REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
        UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable for Legal Reason"),
        INTERNAL_SERVER_ERROR(500, "Server Error"),
        NOT_IMPLEMENTED(501, "Not Implemented"),
        BAD_GATEWAY(502, "Bad Gateway"),
        SERVICE_UNAVAILABLE(503, "Service Unavailable"),
        GATEWAY_TIMEOUT(504, "Gateway Timeout"),
        HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported"),
        INSUFFICIENT_STORAGE(507, "Insufficient Storage"),
        LOOP_DETECTED(508, "Loop Detected"),
        NOT_EXTENDED(510, "Not Extended"),
        NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required");

        private final int _code;

        private final String _message;

        private Code(int code, String message) {
            this._code = code;
            this._message = message;
        }

        public int getCode() {
            return this._code;
        }

        public String getMessage() {
            return this._message;
        }

        public boolean equals(int code) {
            return this._code == code;
        }

        public String toString() {
            return String.format("[%03d %s]", this._code, this.getMessage());
        }

        public boolean isInformational() {
            return HttpStatus.isInformational(this._code);
        }

        public boolean isSuccess() {
            return HttpStatus.isSuccess(this._code);
        }

        public boolean isRedirection() {
            return HttpStatus.isRedirection(this._code);
        }

        public boolean isClientError() {
            return HttpStatus.isClientError(this._code);
        }

        public boolean isServerError() {
            return HttpStatus.isServerError(this._code);
        }
    }
}