package com.accp.course.action.zzw;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accp.course.biz.zzw.NoticeBiz;
import com.accp.course.pojo.Notice;

@RestController
@RequestMapping("notice")
public class NoticeAction {
	@Autowired
	private NoticeBiz biz;
	@RequestMapping(value = "/notice",method=RequestMethod.GET)
	public List<Notice> queryAll() throws Exception{
		return biz.queryAll();
	}
	@RequestMapping(value = "/notice/{id}",method=RequestMethod.GET)
	public Notice queryid(@PathVariable int id) throws Exception{
		return biz.queryByid(id);
	}
	@RequestMapping(value = "/notices/{title}",method=RequestMethod.GET)
	public List<Notice> queryAlltitle(@PathVariable String title) throws Exception{
		return biz.queryByTitle(title);
	}
	@RequestMapping(value = "/notice",method=RequestMethod.DELETE)
	public Map<String, Object> delNotice(@RequestBody List<Integer> roleid) throws Exception{
		int pd=biz.delNotice(roleid);
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
	@RequestMapping(value = "/notice",method=RequestMethod.POST)
	public Map<String, Object> addNotice(@RequestBody Notice n) throws Exception{
		n.setStartTime(new Date());
		int pd=biz.addNotice(n);
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
	@RequestMapping(value = "/notice",method=RequestMethod.PUT)
	public Map<String, Object> uptNotice(@RequestBody Notice n) throws Exception{
		int pd=biz.uptNotice(n);
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
