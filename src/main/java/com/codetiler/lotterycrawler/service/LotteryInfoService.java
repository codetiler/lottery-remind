package com.codetiler.lotterycrawler.service;

import com.codetiler.lotterycrawler.domain.Lottery;

import java.util.List;
import java.util.Map;

public interface LotteryInfoService {

    Lottery getLotteryInfo();

    List<Lottery> getLotteryInfo(String uri);

    Lottery getLotteryInfoFrom500(String uri);

    List<Map<String,String>> getAllLink(String url);

}
