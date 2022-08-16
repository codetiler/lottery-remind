package com.codetiler.lotterycrawler.task;

import com.codetiler.lotterycrawler.domain.BettingInfo;
import com.codetiler.lotterycrawler.domain.Lottery;
import com.codetiler.lotterycrawler.domain.SysUser;
import com.codetiler.lotterycrawler.service.BettingInfoRepository;
import com.codetiler.lotterycrawler.service.EmailSender;
import com.codetiler.lotterycrawler.service.LotteryInfoService;
import com.codetiler.lotterycrawler.service.UserRepository;
import com.codetiler.lotterycrawler.utils.CheckLotteryInfoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 定时任务
 * 每周二、四、日21：30爬取开奖信息
 */
@Component
@RequiredArgsConstructor
public class TimedTask {

    private final LotteryInfoService lotteryInfoSubscriber;

    private final BettingInfoRepository bettingInfoRepository;

    private final UserRepository userRepository;
    private final EmailSender emailSender;

    @Scheduled(cron="0 30 21 ? * 2,4,7")
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
            sb.append("中奖信息如下：\n");
            bettingInfoList.forEach(betting->{
                String message=CheckLotteryInfoUtils.check(lottery,betting);
                sb.append(message).append("\n");
            });
            emailSender.send(email,"中奖信息",sb.toString());
        });
    }

}
