package com.icetea.project.MonStu.enums;

/*
    EmailManager를 통해 메일을 보낼때 사용할 메세지를 보관합니다
*/
public enum EmailMsg {

    SIGNUP_FAILURE("이메일을 전송하는데 실패했습니다. 다시 시도해주세요."),
    SIGNUP_SUCCESS("전송되었습니다. 이메일을 확인해주세요."),

    SIGNUP_SUBJECT("MonStu 회원가입 이메일 확인 코드 입니다."),
    SIGNUP_CONTENT("<div>MonStu Email Authentication Code</div>" +
                            "<h3> %s </h3>");

    private final String message;

    EmailMsg(String message) {
        this.message = message;
    }

    /*Object... args는 Java의 가변 인자(Varargs) 기능을 사용,메서드에 인자를 전달할 때, 개수가 정해지지 않은 여러 개의 인자를 배열로 전달할 수 있게*/
    public String getMessage(Object... args) {
        if (args.length > 0) {
            return String.format(message, args);
        }
        return message;
    }
}
