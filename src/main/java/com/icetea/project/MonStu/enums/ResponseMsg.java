package com.icetea.project.MonStu.enums;

/*
     ResponseEntity<>()와 함께 보낼 메세지를 보관합니다.
*/
public enum ResponseMsg {

    SAVE_FAILURE("저장하는데 실패했습니다. 다시 시도해주세요."),
    SAVE_SUCCESS("저장되었습니다."),

    SIGNUP_FAILURE("가입하는데 실패했습니다. 다시 시도해주세요."),
    SIGNUP_SUCCESS("가입되었습니다."),
    SIGNUP_EXISTS_EMAIL("이미 가입되어 있는 이메일입니다."),
    SIGNUP_NONEXISTS_EMAIL("사용 가능한 이메일입니다."),

    CREATED_SUCCESS("성공적으로 생성되었습니다."),

    USER_NOT_FOUNDED_EX("유저 정보를 찾을 수 없습니다."),
    ILLEGAL_ARGUMENT_EX("에러가 발생했습니다."),
    RUNTIME_EX("API 호출 중 에러 발생.");

    private final String message;

    ResponseMsg(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
