package com.codetiler.lotterycrawler.task;

import com.codetiler.lotterycrawler.domain.*;
import com.codetiler.lotterycrawler.service.*;
import com.codetiler.lotterycrawler.utils.CheckLotteryInfoUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 定时任务
 * 每周二、四、日21：30爬取开奖信息
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TimedTask {

    private final LotteryInfoService lotteryInfoSubscriber;

    private final BettingInfoRepository bettingInfoRepository;

    private final UserRepository userRepository;

    private final LotteryHistoryRepository lotteryHistoryRepository;

    private final GameInfoService gameInfoSubscriber;

    private final EmailSender emailSender;

    private final DateTimeFormatter sdf= DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

//    @Scheduled(cron="0 0 18 ? * 1,2,3,4,5,6,7")
        @Scheduled(cron="*/5 * * * * ?")
    public void getLotteryInfo(){
        Lottery lottery = lotteryInfoSubscriber.getLotteryInfo();

        List<SysUser> userList=userRepository.findAll();
        userList.forEach(user->{
            String email=user.getEmail();
            String name = user.getName();
            String userId = user.getId();
            BettingInfo bettingInfo=new BettingInfo();
            bettingInfo.setUserId(userId);
            Example<BettingInfo> example=Example.of(bettingInfo);
            List<BettingInfo> bettingInfoList = bettingInfoRepository.findAll(example);
            StringBuffer sb =new StringBuffer();
            sb.append(name).append(",你好！\n");
            sb.append("本期").append(lottery.getIssue()).append("开奖信息：").append(lottery.getReds()).append("-").append(lottery.getBlue());
            sb.append("中奖信息如下：\n");
            bettingInfoList.forEach(betting->{
                String message=CheckLotteryInfoUtils.check(lottery,betting);
                sb.append(betting.getReds()).append("-").append(betting.getBlue()).append(":").append(message).append("\n");
            });
            emailSender.send(email,"中奖信息",sb.toString());
        });
    }

//    @Scheduled(cron="0 38 23 ? * 1,2,3,4,5,6,7")
//    @Scheduled(cron="*/5 * * * * ?")
    public void getGameInfo(){
        List<GameInfo> gameInfoList=gameInfoSubscriber.getGameInfoList();


        for (GameInfo gameInfo:gameInfoList) {
            log.info("gameInfo:{}",gameInfo.toString());
            List<Map<String,List<Map<String,String>>>> list= gameInfo.getPromotions().get("upcomingPromotionalOffers");
            if(list.isEmpty()){
                list=gameInfo.getPromotions().get("promotionalOffers");
            }
            List<Map<String,String>> list2= list.get(0).get("promotionalOffers");
            String startDate= String.valueOf(list2.get(0).get("startDate"));
            LocalDateTime startTime = LocalDateTime.parse(startDate,sdf);
            String endDate= String.valueOf(list2.get(0).get("endDate"));
            LocalDateTime endTime = LocalDateTime.parse(endDate,sdf);

            LocalDateTime currentTime = LocalDateTime.now();


            if(currentTime.isAfter(startTime)&&currentTime.isBefore(endTime)){
                Duration duration=Duration.between(currentTime,endTime);
                long days = duration.toDays();
                log.info("title:{},startDate:{},endDate:{},距离活动结束还剩：{}天",gameInfo.getTitle(),startDate,endDate,days);
            }else if(currentTime.isBefore(startTime)){
                Duration duration=Duration.between(currentTime,startTime);
                long days = duration.toDays();
                log.info("title:{},startDate:{},endDate:{},距离活动开始还剩：{}天",gameInfo.getTitle(),startDate,endDate,days);
            }

        }
    }



}
