package spring.boot.constant;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum HttpMessage {
        UNCATEGORIZED(0, "uncategorized exception, service can not response.", HttpStatus.INTERNAL_SERVER_ERROR),
        METHOD_NOT_ALLOWED(1, "method not allowed on this endpoint.", HttpStatus.METHOD_NOT_ALLOWED),
        NOT_FOUND(2, "apis resource not found.", HttpStatus.NOT_FOUND),
        ILL_LEGAL_PAYLOAD(3, "payload is missing or not readable.", HttpStatus.BAD_REQUEST),
        ILL_LEGAL_PARAMETER(4, "parameter is missing or not readable.", HttpStatus.BAD_REQUEST),
        ILL_LEGAL_QUERY(5, "query is missing not readable.", HttpStatus.BAD_REQUEST),
        ILL_LEGAL_HEADER(6, "header is missing not readable.", HttpStatus.BAD_REQUEST),
        UNAUTHORIZED(7, "unauthorized: your identity can not be verified.", HttpStatus.UNAUTHORIZED),
        FORBIDDEN(10, "forbidden: do not has right authority, do not f*ck with cat.", HttpStatus.FORBIDDEN),
        TOO_MANY_REQUEST(10, "too Many Request: try again after a few minutes.", HttpStatus.TOO_MANY_REQUESTS),
        BAD_SIGNATURE(11, "signature does not match, your payload may be edited or damaged.", HttpStatus.UNAUTHORIZED),
        URI_EXPIRED(12, "url expired, you cannot use this url anymore.", HttpStatus.BAD_REQUEST),

        SAVE(0, "{resource} has been saved.", HttpStatus.CREATED),
        FIND_BY_ID(0, "query {resource} by id success.", HttpStatus.OK),
        FIND_ALL(0, "query {resource} success.", HttpStatus.OK),
        FIND_ALL_BY_ID(0, "query {resource} by id success.", HttpStatus.OK),
        FIND_ALL_BY(0, "query {resource} by {criteria} success.", HttpStatus.OK),
        FIND_ALL_ARCHIVED(0, "query archived {resource} success.", HttpStatus.OK),
        UPDATE(0, "{resource} has been updated.", HttpStatus.OK),
        DELETE(0, "{resource} has been deleted.", HttpStatus.OK),
        SIGN_UP(0, "sign up success, enjoy.", HttpStatus.CREATED),
        SIGN_IN(0, "sign in success, enjoy.", HttpStatus.OK),
        RETRIEVE_PROFILE(0, "retrieve profile success, enjoy.", HttpStatus.OK),
        SIGN_OUT(0, "sign out success, enjoy.", HttpStatus.OK),
        REFRESH_TOKEN(0, "refresh token success, enjoy.", HttpStatus.OK),

        ALREADY_EXISTED(200, "{resource} already existed, {identity} must be unique.", HttpStatus.BAD_REQUEST),
        OWNING_SIDE_NOT_EXISTED(201, "{owning side} does not exist.", HttpStatus.BAD_REQUEST),
        OWNING_SIDE_NOT_AVAILABLE(202, "{owning side} is not available.", HttpStatus.BAD_REQUEST),
        ILL_LEGAL_RESOURCE_ID(203, "{resource} id must be in valid string format.", HttpStatus.BAD_REQUEST),
        FIND_BY_ID_NO_CONTENT(204, "retrieve {resource} by id return no content.", HttpStatus.NO_CONTENT),
        FIND_ALL_NO_CONTENT(205, "retrieve {resource} return no content.", HttpStatus.NO_CONTENT),
        FIND_ALL_BY_ID_NO_CONTENT(206, "retrieve {resource} return no content.", HttpStatus.NO_CONTENT),
        FIND_ALL_BY_NO_CONTENT(207, "retrieve {resource} by {criteria} return no content.", HttpStatus.NO_CONTENT),
        FIND_ALL_ARCHIVED_NO_CONTENT(208, "retrieve archived {resource} return no content.", HttpStatus.NO_CONTENT),
        NOT_EXISTED(209, "{resource} does not exist.", HttpStatus.BAD_REQUEST),

        USER_EXISTED(210, "user already existed, username must be unique.", HttpStatus.BAD_REQUEST),
        SIGN_TOKEN(211, "can not sign token: illegal claims or encrypt algorithm.", HttpStatus.INTERNAL_SERVER_ERROR),
        // @formatter:off
        PARSE_TOKEN(212, "can not parse token: token has been edited, expired or not published by us.", HttpStatus.INTERNAL_SERVER_ERROR), // @formatter:on
        // @formatter:off
        ILL_LEGAL_TOKEN(213, "illegal token: token has been edited, expired or not published by us.", HttpStatus.UNAUTHORIZED), // @formatter:on
        TOKEN_EXPIRED(214, "illegal token: token has been expired.", HttpStatus.UNAUTHORIZED),
        BAD_CREDENTIAL(215, "bad credentials: username or password does not match.", HttpStatus.UNAUTHORIZED),
        // @formatter:off
        MISSING_AUTHORIZATION(216, "unauthorized: missing authorization or x-refresh-token.", HttpStatus.UNAUTHORIZED), // @formatter:on
        TOKEN_BLOCKED(217, "token has been recalled: can not use this anymore.", HttpStatus.UNAUTHORIZED),
        // @formatter:off
        DECODE_RESOURCE_ID(218, "can not decode resource id: make sure you provide a valid one.", HttpStatus.BAD_REQUEST), // @formatter:on
        ENSURE_NOT_BAD_CREDENTIAL(219, "can not ensure token is not recalled.", HttpStatus.UNAUTHORIZED),
        TOKEN_NOT_SUITABLE(220, "access token and refresh token are not suitable.", HttpStatus.UNAUTHORIZED),
        RECALL_TOKEN(221, "refresh token not success: token not be recalled.", HttpStatus.UNAUTHORIZED),
        MISSING_HMAC_SIGNATURE(222, "hmac signature is missing, can not verify payload.", HttpStatus.BAD_REQUEST),
        VERIFY_HMAC_SIGNATURE(223, "can not verify hmac signature.", HttpStatus.INTERNAL_SERVER_ERROR),
        MISSING_NONCE_OR_TIMESTAMP(224, "missing nonce or timestamp in request.", HttpStatus.BAD_REQUEST),
        NONCE_BLOCKED(225, "nonce has been used: your process can not start with this nonce.", HttpStatus.BAD_REQUEST),
        ENSURE_NOT_BAD_NONCE(226, "can not ensure nonce is not blocked.", HttpStatus.INTERNAL_SERVER_ERROR);
        ;

        int code;
        String message;
        HttpStatus httpStatus;
}
