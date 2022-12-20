package com.i3e3.mindlet.global.constant.message;

public enum ErrorMessage {

    DUPLICATE_ID("이미 사용 중인 아이디입니다."),
    PASSWORD_CONTAIN_ID("패스워드에 아이디가 포함되어 있습니다."),
    INVALID_REGISTER_KEY("유효하지 않은 회원가입 키입니다."),
    INVALID_ID("유효하지 않은 아이디입니다."),
    INVALID_REQUEST("잘못된 요청입니다."),
    MORE_THAN_MAX_COUNT("최대 개수를 초과하였습니다."),
    FILE_CONVERT_FAIL("파일 저장에 실패하였습니다."),
    FILE_DELETE_FAIL("파일 삭제에 실패하였습니다."),
    INVALID_DATE_REQUEST("잘못된 날짜 요청입니다.");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
