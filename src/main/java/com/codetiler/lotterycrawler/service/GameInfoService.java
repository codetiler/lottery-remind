package com.codetiler.lotterycrawler.service;

import com.codetiler.lotterycrawler.domain.GameInfo;
import org.apache.catalina.LifecycleState;

import java.util.List;

public interface GameInfoService {

    List<GameInfo> getGameInfoList();

}
