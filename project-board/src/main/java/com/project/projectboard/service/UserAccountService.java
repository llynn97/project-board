package com.project.projectboard.service;

import com.project.projectboard.domain.UserAccount;
import com.project.projectboard.dto.UserAccountDto;
import com.project.projectboard.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public Optional<UserAccountDto> searchUser(String username){
        return userAccountRepository.findById(username)
                .map(UserAccountDto::from);
    }

    public UserAccountDto saveUser(String username, String password, String email, String nickname, String memo){
        return UserAccountDto.from(userAccountRepository.save(UserAccount.of(username,password,email,nickname,memo,username)));
    }
}
