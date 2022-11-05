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
import org.apache.tomcat.util.buf.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LotteryInfoServiceImpl implements LotteryInfoService {

    private final String uri = System.getenv("LOTTERY_URI");

    private final CloseableHttpClient httpClient= HttpClients.createDefault();

    @Override
    public Lottery getLotteryInfo() {
        HttpGet httpGet = getHttpGet(uri);
        CloseableHttpResponse response;
        Lottery lottery=new Lottery();
        try {
            response =httpClient.execute(httpGet);
            String result= EntityUtils.toString(response.getEntity(),"UTF-8");
            Gson gson=new Gson();
            @SuppressWarnings("unchecked")
            Map<String,List<Map<String,String>>> map=gson.fromJson(result, Map.class);
            List<Map<String,String>> list=map.get("result");
            log.info("彩票信息：{}",list.get(0).toString());
            lottery.setIssue(list.get(0).get("code"));
            lottery.setBlue(list.get(0).get("blue"));
            lottery.setReds(list.get(0).get("red"));
        } catch (IOException e) {
            log.error("获取彩票信息失败。错误详情：{}",e.getMessage(),e);
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

    /**
     * 从彩票官网获取彩票信息
     * @param uri 彩票链接
     * @return 返回彩票中奖信息
     */
    public List<Lottery> getLotteryInfo(String uri){
        List<Lottery> lotteryList=new ArrayList<>();
        HttpGet httpGet = getHttpGet(uri);
        CloseableHttpResponse response;
        try {
            response =httpClient.execute(httpGet);
            String result= EntityUtils.toString(response.getEntity(),"UTF-8");
            Gson gson=new Gson();
            @SuppressWarnings("unchecked")
            Map<String,List<Map<String,String>>> map=gson.fromJson(result, Map.class);
            List<Map<String,String>> list=map.get("result");
            for (Map<String,String> item:list) {
                Lottery lottery=new Lottery();
                lottery.setIssue(item.get("code"));
                lottery.setBlue(item.get("blue"));
                lottery.setReds(item.get("red"));
                lotteryList.add(lottery);
            }
        } catch (IOException e) {
            log.error("获取彩票信息失败。{}",e.getMessage(),e);
        }
        return lotteryList;
    }

    /**
     * 从500彩票网获取彩票信息
     * @param uri 彩票链接
     * @return 返回彩票信息
     */
    public Lottery getLotteryInfoFrom500(String uri){
        Document doc;
        Lottery lottery=new Lottery();
        try {
            doc = Jsoup.connect(uri).get();
            Element body=doc.body();
            Elements ballElements=body.select(".ball_box01").select("li");
            List<String> ballList= ballElements.stream().map(Element::text).collect(Collectors.toList());

            String reds=StringUtils.join(ballList.subList(0,6), ',');
            String blue=ballList.get(6);
            lottery.setReds(reds);
            lottery.setBlue(blue);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  lottery;
    }

    public List<Map<String,String>> getAllLink(String url){
        Document doc;
        List<Map<String,String>> hrefList=new ArrayList<>();
        try {
            doc = Jsoup.connect(url).get();
            Element body=doc.body();
            Elements selectListElements=body.select(".iSelectList").select("a");
            for (Element element:selectListElements) {
                String href=element.attr("href");
                String text=element.text();
                Map<String,String> map=new HashMap<>();
                map.put(text,href);
                hrefList.add(map);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return hrefList;
    }



}
