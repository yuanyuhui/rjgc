package com.accp.course.action.zxf;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.accp.course.biz.zxf.ZxfBiz;
import com.accp.course.pojo.Period; 
import com.github.pagehelper.PageInfo;
 
@RestController
@RequestMapping("/api")
public class ZxfApi {
		 
	@Autowired
	private ZxfBiz biz;

	@RequestMapping(value ="zxf/{num}/{size}", method = RequestMethod.GET)
	public PageInfo<Period> getPersonListByPage(@PathVariable Integer num, @PathVariable Integer size)
			throws Exception {
		return biz.QueryAll(num,size);
	}
		
	@RequestMapping(value ="/zxf", method = RequestMethod.POST)
	public Map<String, String> savePeriod(@RequestBody Period p){
		int r=biz.SavePeriod(p);
		Map<String, String> map=new HashMap<String, String>(0);
		if(r>0) {
			map.put("code", "200");
		}else {
			map.put("code", "500");
		}
		return map;
	}
	
	@RequestMapping(value ="/zxf", method = RequestMethod.PUT)
	public Map<String, Object> updatePeriod(@RequestBody Period p){
		int r=biz.modifyPeriod(p);
		Map<String, Object> map=new HashMap<String, Object>(0);
		if(r>0) {
			map.put("code", "200");
		}else {
			map.put("code", "500");
		}
		return map;
	}
	
	@RequestMapping(value ="/zxf/{id}", method = RequestMethod.GET)
	public Period FindPeriodByid(@PathVariable String id) {
		return biz.FindPeriodByid(id);
	}
	
	@RequestMapping(value="deletegrade",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, String> reomove(@RequestParam("id[]")Integer [] id){
		System.err.println(id);
		Map<String, String> map=new HashMap<String,String>(0);
		if(biz.removeByid(id)>0){
			map.put("code", "200");
		}else {
			map.put("code", "500");
		}
		return map;
	}
		
}
