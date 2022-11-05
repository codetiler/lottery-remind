package com.codetiler.lotterycrawler.domain;

import com.google.gson.JsonObject;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data

public class GameInfo {

    private String title;
    private Map<String,String> price;
    private Map<String, List<Map<String,List<Map<String,String>>>>> promotions;
}
