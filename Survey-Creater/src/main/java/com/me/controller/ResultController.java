package com.me.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.me.vo.AnalyzeResult;
import com.me.dto.Page;
import com.me.vo.ReturnMessage;
import com.me.po.Surveys;
import com.me.service.SurveysService;
import com.me.service.TableService;
import com.me.utils.AnalyzeUtils;
import com.me.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @ClassName ResultController
 * @Description TODO
 * @Author noname
 * @Data
 * @Version 1.0
 **/
@Slf4j
@RestController
public class ResultController {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private TableService tableService;
    @Autowired
    private SurveysService surveysService;

    @GetMapping("/resultGet")
    @ResponseBody
    public ReturnMessage resultGet(@RequestParam("pageId") String pageId ,@RequestParam("current") Integer current, @RequestHeader("Authorization")String token){

        Map<Object, Object> myMap = redisUtil.hmget(pageId);
        if(myMap == null|| "d41d8cd98f00b204e9800998ecf8427e".equals(pageId)){
            return new ReturnMessage(true,true);
        }

        Page page = tableService.selectPage(pageId, (HashMap) myMap,current);
        if(page == null){
            return new ReturnMessage(true,true);
        }
        return new ReturnMessage(true, page);
    }

    @DeleteMapping("/deleteSurvey")
    @ResponseBody
    public ReturnMessage deleteSurvey(@RequestParam("pageId") String pageId,@RequestHeader("Authorization")String token){
        surveysService.delete(new EntityWrapper<Surveys>().eq("pageid",pageId));
        tableService.dropTable(pageId);
        redisUtil.del(pageId);
        return new ReturnMessage(true,"删除成功");
    }

    @GetMapping("/analyze")
    @ResponseBody
    public ReturnMessage analyze(@RequestParam("pageId") String pageId,@RequestHeader("Authorization")String token){
        Map<Object, Object> columns = redisUtil.hmget(pageId);
        List<String> alist = new ArrayList<>(64);
        Iterator itor = columns.entrySet().iterator();
        while (itor.hasNext()){
            Map.Entry entry = (Map.Entry) itor.next();
            if (AnalyzeUtils.filterQuestions(entry.getValue().toString())){
                if (entry.getValue().toString().equals("text")&&!entry.getKey().toString().startsWith("map")){
                    continue;
                }
                alist.add(entry.getKey().toString());
            }
        }
        return new ReturnMessage(true,alist);

    }


 }
