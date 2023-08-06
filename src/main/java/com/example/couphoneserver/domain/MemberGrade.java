package com.example.couphoneserver.domain;

/**
 * ROLE_MEMBER : 일반 사용자
 * ROLE_ADMIN : 관리자
 */
public enum MemberGrade {
    ROLE_MEMBER("ROLE_MEMBER"),
    ROLE_ADMIN("ROLE_ADMIN");

    String grade;

    MemberGrade(String grade) {
        this.grade = grade;
    }
    public String getValue(){
        return grade;
    }
}
