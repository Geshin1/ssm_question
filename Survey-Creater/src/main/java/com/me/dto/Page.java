package com.me.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @ClassName Page
 * @Description TODO
 * @Author noname
 * @Data
 * @Version 1.0
 **/

@Data
public class Page {
    private int total;
    private ArrayList<HashMap<String,Object>> list;
    private int current;
    private int pages;
    private int size;
    private boolean hasPrevious;
    private boolean hasNext;

}
