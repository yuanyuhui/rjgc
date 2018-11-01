package com.accp.course.action.zzw;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.accp.course.biz.zzw.EducationalBiz;
import com.accp.course.pojo.UserInfo;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("educational")
public class EducationalAction {
	@Autowired
	private EducationalBiz biz;
	@RequestMapping(value = "/educational/{p}/{s}",method=RequestMethod.GET)
	public PageInfo<UserInfo> queryAll(@PathVariable Integer p, @PathVariable Integer s)throws Exception {
		PageInfo<UserInfo> data = biz.queryAllEducational(p, s);
		return data;
	}
	@RequestMapping(value = "/educationala/{id}",method=RequestMethod.GET)
	public UserInfo queryOne(@PathVariable String id)throws Exception {
		UserInfo data = biz.queryOneEducational(id);
		return data;
	}
	@RequestMapping(value = "/educational",method=RequestMethod.PUT)
	public Map<String, Object> Update(@RequestBody UserInfo user)throws Exception {
		int pd = biz.updateEducational(user);
		Map<String, Object> message = new HashMap<String, Object>();
		if(pd>0) {
			message.put("code", "200");
			message.put("msg", "ok");
		}else {
			message.put("code", "500");
			message.put("msg", "no");
		}
		return message;
	}
	@RequestMapping(value = "/educational",method=RequestMethod.DELETE)
	public Map<String, Object> delete(@RequestBody List<String> arr)throws Exception {
		int pd = biz.removeEducational(arr);
		Map<String, Object> message = new HashMap<String, Object>();
		if(pd>0) {
			message.put("code", "200");
			message.put("msg", "ok");
		}else {
			message.put("code", "500");
			message.put("msg", "no");
		}
		return message;
	}
}
