package com.codetiler.lotterycrawler.utils;

import com.codetiler.lotterycrawler.domain.BettingInfo;
import com.codetiler.lotterycrawler.domain.Lottery;
import lombok.extern.slf4j.Slf4j;

/**
 * 检查是否中奖
 */
@Slf4j
public class CheckLotteryInfoUtils {

    public static String check(Lottery lottery, BettingInfo bettingInfo){
        var info="";
        boolean blueHit=checkBlue(lottery.getBlue(), bettingInfo.getBlue());
        int redNum=checkRed(lottery.getReds(), bettingInfo.getReds());
        if(blueHit&&redNum==6){
            log.info("恭喜您本次投注中得一等奖。");
            info="恭喜您本次投注中得一等奖。";
        }else if(!blueHit&&redNum==6){
            log.info("恭喜您本次投注中得二等奖。");
            info="恭喜您本次投注中得二等奖。";
        } else if (blueHit&&redNum==5) {
            log.info("恭喜您本次投注中得三等奖。");
            info="恭喜您本次投注中得三等奖。";
        } else if ((blueHit&&redNum==4)||(!blueHit&&redNum==5)) {
            log.info("恭喜您本次投注中得四等奖。");
            info="恭喜您本次投注中得四等奖。";
        } else if ((blueHit&&redNum==3)||(!blueHit&&redNum==4)) {
            log.info("恭喜您本次投注中得五等奖。");
            info="恭喜您本次投注中得五等奖。";
        }else if (blueHit){
            log.info("恭喜您本次投注中得六等奖。");
            info="恭喜您本次投注中得六等奖。";
        }else {
            log.info("很遗憾您本次投注未中奖。");
            info="很遗憾您本次投注未中奖。";
        }
        return info;
    }

    public static boolean checkBlue(String blue1,String blue2){
        return blue1.equals(blue2);
    }

    public static int checkRed(String red1,String red2){
        String[] reds2=red2.split(",");
        int num=0;
        for (int i=0;i<reds2.length;i++){
            if(red1.indexOf(reds2[i])!=-1){
                num++;
            }
        }
        return  num;
    }


}
