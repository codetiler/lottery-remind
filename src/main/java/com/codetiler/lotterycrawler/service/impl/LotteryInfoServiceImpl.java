package com.codetiler.lotterycrawler.service.impl;

import com.codetiler.lotterycrawler.domain.Lottery;
import com.codetiler.lotterycrawler.service.LotteryInfoService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class LotteryInfoServiceImpl implements LotteryInfoService {

    private final String uri = System.getenv("LOTTERY_URI");

    private CloseableHttpClient httpClient= HttpClients.createDefault();

    @Override
    public Lottery getLotteryInfo() {
        HttpGet httpGet = getHttpGet(uri);
        CloseableHttpResponse response= null;
        Lottery lottery=new Lottery();
        try {
            response =httpClient.execute(httpGet);
            String result= EntityUtils.toString(response.getEntity(),"UTF-8");
            Gson gson=new Gson();
            Map map=gson.fromJson(result, Map.class);
            List<Map> list= (List) map.get("result");
            log.info("彩票信息：",list.get(0).toString());
            lottery.setIssue(list.get(0).get("code").toString());
            lottery.setBlue(list.get(0).get("blue").toString());
            lottery.setReds(list.get(0).get("red").toString());
        } catch (IOException e) {
            log.error("获取彩票信息失败。",e.getMessage(),e);
        }
        return lottery;
    }

    private HttpGet getHttpGet(String uri){
        HttpGet httpGet=new HttpGet(uri);
        httpGet.addHeader("Connection", "keep-alive");
        httpGet.addHeader("Cache-Control", "max-age=0");
        httpGet.addHeader("Host","www.cwl.gov.cn");
        httpGet.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
        httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/;q=0.8");
        return  httpGet;
    }
}
