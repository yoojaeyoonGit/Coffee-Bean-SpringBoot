package jpabook.jpashop.controller;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LoginForm {
        @NotNull
        private String member_login_id;

        @NotEmpty
        private String password;
}
