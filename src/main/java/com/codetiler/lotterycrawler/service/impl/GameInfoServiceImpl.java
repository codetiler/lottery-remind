package com.codetiler.lotterycrawler.service.impl;

import com.codetiler.lotterycrawler.domain.GameInfo;
import com.codetiler.lotterycrawler.domain.Lottery;
import com.codetiler.lotterycrawler.service.GameInfoService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.description.method.MethodDescription;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GameInfoServiceImpl implements GameInfoService {

    private final String epicUri = System.getenv("EPIC_URI");

    private final CloseableHttpClient httpClient= HttpClients.createDefault();

    @Override
    public List<GameInfo> getGameInfoList() {
        List<GameInfo> list=new ArrayList<>();
        HttpGet httpGet=new HttpGet(epicUri);
        CloseableHttpResponse response;

        try {
            response =httpClient.execute(httpGet);
            String result= EntityUtils.toString(response.getEntity(),"UTF-8");
            Gson gson=new Gson();
            JsonObject jo=gson.fromJson(result, JsonObject.class);

            JsonArray array =jo.getAsJsonObject("data")
                    .getAsJsonObject("Catalog")
                    .getAsJsonObject("searchStore")
                    .getAsJsonArray("elements");
            Type listType = new TypeToken<List<GameInfo>>(){}.getType();
            list = gson.fromJson(array,listType);


            List<GameInfo> newList=filterNew(list);
            List<GameInfo> oldList=filter(list);

            log.info("游戏信息：list size:{}",list.size());

        } catch (IOException e) {
            log.error("获取游戏信息失败。错误详情：{}",e.getMessage(),e);
        }
        return filterNew(list);
    }

    private List<GameInfo> filterNew(List<GameInfo> list){
        return list.stream().filter(item->item.getPromotions()!=null).collect(Collectors.toList());
    }

    private List<GameInfo> filter(List<GameInfo> list){
        return list.stream().filter(item->item.getPromotions()==null).collect(Collectors.toList());
    }
}
