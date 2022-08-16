package com.codetiler.lotterycrawler.controller;

import com.codetiler.lotterycrawler.domain.BettingInfo;
import com.codetiler.lotterycrawler.service.BettingInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/betting")
public class BettingInfoController {

    private final BettingInfoRepository bettingInfoRepository;

    @PostMapping("/add")
    public void add(@RequestBody BettingInfo bettingInfo){
        bettingInfoRepository.save(bettingInfo);
    }

}
