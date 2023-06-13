package com.me.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.me.vo.ReturnMessage;
import com.me.po.Surveys;
import com.me.service.SurveysService;
import com.me.service.TableService;
import com.me.utils.JSONResultFormatterUtils;
import com.me.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author noname
 * @since
 */
@RestController
@RequestMapping("/surveys")
public class SurveysController {

    @Autowired
    private SurveysService surveysService;
    @Autowired
    private TableService tableService;
    @Autowired
    private RedisUtil redisUtil;

/**
 * returnPage 方法用于处理 GET 请求 "/surveys/page"，返回指定 pageId 的问卷调查内容。
 * @param pageId 要查询的问卷调查的 pageId。
 * @return 如果查询结果为空，则返回一个 ReturnMessage 对象，否则将查询结果中的 content 字段作为字符串返回。
 */
@GetMapping("/page")
@ResponseBody
public ReturnMessage returnPage(@RequestParam("pageId")String pageId){

    // 使用 SurveysService 类的 selectOne 方法查询数据库中 pageid 字段等于 pageId 的记录
    Surveys surveys = surveysService.selectOne(new EntityWrapper<Surveys>().eq("pageid",pageId));

    // 如果查询结果为空，则返回一个 ReturnMessage 对象
    if(surveys == null){
        return new ReturnMessage(true);
    }

    // 否则将查询结果中的 content 字段作为字符串返回
    String data = surveys.getContent();
    return new ReturnMessage(true,data);
}

    @PostMapping("/commit")
    @ResponseBody
    public ReturnMessage commitResult(@RequestParam("params")String result,@RequestParam("tableId") String tableId){
        HashMap rMap = JSONResultFormatterUtils.forResult(result,redisUtil,tableId);
        tableService.insertData(rMap,tableId);
        return new ReturnMessage(true);
    }
}

