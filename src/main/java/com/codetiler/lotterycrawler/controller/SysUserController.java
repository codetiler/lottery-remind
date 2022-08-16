package com.codetiler.lotterycrawler.controller;

import com.codetiler.lotterycrawler.domain.SysUser;
import com.codetiler.lotterycrawler.service.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/sysUser")
public class SysUserController {

    private final UserRepository userRepository;

    @PostMapping("/add")
    public void add(@RequestBody SysUser sysUser){
        userRepository.save(sysUser);
    }
}
