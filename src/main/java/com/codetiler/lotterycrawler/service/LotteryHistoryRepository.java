package com.codetiler.lotterycrawler.service;

import com.codetiler.lotterycrawler.domain.LottoryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotteryHistoryRepository extends JpaRepository<LottoryHistory,Long> {
}
