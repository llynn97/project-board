package com.project.projectboard.config;

import com.project.projectboard.domain.UserAccount;
import com.project.projectboard.dto.UserAccountDto;
import com.project.projectboard.repository.UserAccountRepository;
import com.project.projectboard.service.UserAccountService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean private UserAccountRepository userAccountRepository;
    @MockBean private UserAccountService userAccountService;

    @BeforeTestMethod
    public void securitySetup(){
        given(userAccountService.searchUser(anyString())).willReturn(Optional.of(createUserAccountDto()));
        given(userAccountService.saveUser(anyString(), anyString(), anyString(), anyString(), anyString()))
                .willReturn(createUserAccountDto());
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "unoTest",
                "pw",
                "uno-test@email.com",
                "uno-test",
                "test memo"
        );
    }



}
