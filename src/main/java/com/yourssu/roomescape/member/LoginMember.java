package com.yourssu.roomescape.member;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER) // 파라미터에 붙일 수 있음
@Retention(RetentionPolicy.RUNTIME) // 런타임에도 유지되어야 함
public @interface LoginMember {
}
