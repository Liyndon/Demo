package com.demo.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class ExtController {
	
	@RequestMapping("/toExtPage")
	public String toExtPage(){
		return "ext/grid1";
	}
	
	@RequestMapping("/loadData")
	@ResponseBody
	public Map<String, Object> loadData(){
		Map<String, Object> map = new HashMap<>();
		map.put("totalProperty", 10);
		List<Object> list = new ArrayList<>();
		for(int i=0;i<100;i++){
			Map<String, Object> item = new HashMap<>();
			item.put("id", i+1);
			item.put("name", "name"+i);
			item.put("date", new Date());
			list.add(item);
		}
		map.put("root", list);
		return map;
	}
}
