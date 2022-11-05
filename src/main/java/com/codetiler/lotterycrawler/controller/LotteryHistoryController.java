package com.codetiler.lotterycrawler.controller;

import com.codetiler.lotterycrawler.domain.Lottery;
import com.codetiler.lotterycrawler.domain.LottoryHistory;
import com.codetiler.lotterycrawler.service.LotteryHistoryRepository;
import com.codetiler.lotterycrawler.service.LotteryInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/lotteryHistory")
public class LotteryHistoryController {

    private  final LotteryHistoryRepository lotteryHistoryRepository;

    private final LotteryInfoService lotteryInfoSubscriber;

    @GetMapping("/sync")
    public void sync(){
        for (int i=2013;i<=2022;i++){
            String startIssue=i+"001";
            String endIssue=i+"100";
            String uri="http://www.cwl.gov.cn/cwl_admin/front/cwlkj/search/kjxx/findDrawNotice?name=ssq&issueCount=&issueStart="+startIssue+"&issueEnd="+endIssue+"&dayStart=&dayEnd=";
            save(uri);

            startIssue=i+"100";
            endIssue=i+"200";
            uri="http://www.cwl.gov.cn/cwl_admin/front/cwlkj/search/kjxx/findDrawNotice?name=ssq&issueCount=&issueStart="+startIssue+"&issueEnd="+endIssue+"&dayStart=&dayEnd=";
            save(uri);

        }
    }
    @GetMapping("/sync2")
    public void sync2(){
//        String issue="03001";
        String uri="https://kaijiang.500.com/shtml/ssq/03001.shtml";
//        lotteryInfoSubscriber.getLotteryInfoFrom500(uri);

        List<Map<String,String>> hrefList=lotteryInfoSubscriber.getAllLink(uri);
        for (Map<String,String> map:hrefList) {
            Set<String> keys=map.keySet();
            String key= String.valueOf(keys.toArray()[0]);
            if(key.startsWith("22")||key.startsWith("21")||key.startsWith("20")||key.startsWith("19")||key.startsWith("18")||key.startsWith("17")
                    ||key.startsWith("16")||key.startsWith("15")
                    ||key.startsWith("14")||key.startsWith("13")){
                System.out.println("跳过："+key);
                continue;
            }
            Lottery lottery=lotteryInfoSubscriber.getLotteryInfoFrom500(String.valueOf(map.get(key)));
            LottoryHistory lottoryHistory=new LottoryHistory();
            String issue="20"+key;
            lottoryHistory.setIssue(issue);
            Example<LottoryHistory> example=Example.of(lottoryHistory);
            Optional<LottoryHistory> lotteryHistoryOptional= lotteryHistoryRepository.findOne(example);
            if(lotteryHistoryOptional.isEmpty()){
                lottoryHistory.setReds(lottery.getReds());
                lottoryHistory.setBlue(lottery.getBlue());
                lotteryHistoryRepository.save(lottoryHistory);
                System.out.println("插入："+issue);
            }
        }
        System.out.println("执行结束.........");

    }

    public void save(String uri){
        List<Lottery> lotteryList = lotteryInfoSubscriber.getLotteryInfo(uri);

        for (Lottery lottery:lotteryList) {

            LottoryHistory lottoryHistory=new LottoryHistory();
            lottoryHistory.setIssue(lottery.getIssue());
            Example<LottoryHistory> example=Example.of(lottoryHistory);
            List<LottoryHistory> list=lotteryHistoryRepository.findAll(example);

            if(list.size()<=0){
                lottoryHistory.setBlue(lottery.getBlue());
                lottoryHistory.setReds(lottery.getReds());
                lotteryHistoryRepository.save(lottoryHistory);
            }


        }

    }
}
