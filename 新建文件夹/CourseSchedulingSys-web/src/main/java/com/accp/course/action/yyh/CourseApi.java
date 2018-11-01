package com.accp.course.action.yyh;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.accp.course.biz.yyh.CourseBiz_yyh;
import com.accp.course.vo.yyh.CourseVo1;
import com.accp.course.vo.yyh.CourseVo2;
import com.github.pagehelper.PageInfo;

/**
 * 课程API
 * @author yuanyuhui
 *
 */
@RestController
@RequestMapping("/yyhCourse")
public class CourseApi {
	@Autowired
	private CourseBiz_yyh biz;
	
	@RequestMapping(value = "CourseVo/{pageNum}/{pageSize}/{type}/{condition}", method = RequestMethod.GET)
	public PageInfo<CourseVo1> getPersonListByPage(@PathVariable Integer pageNum,@PathVariable Integer pageSize, @PathVariable String type,@PathVariable String condition)
			throws Exception {
		if(type.equals("null")) {
			type=null;
		}else if(type=="null") {
			type=null;
		}
		
		if(condition.equals("null")) {
			condition=null;
		}else if(condition=="null") {
			condition=null;
		}
		return biz.querylist(pageNum, pageSize,type,condition);
	}
	
	@RequestMapping(value = "CourseVo2", method = RequestMethod.POST)
	public Map<String, String> addCourseVo2(@RequestBody CourseVo2 c) throws Exception{
		Map<String, String> map=new HashMap<String,String>();
		int count=biz.addCourseVo2(c);
		if(count>0) {
			map.put("code", "200");
			map.put("msg", "新增课程信息成功");
		}
		else if(count==-1) {
			map.put("code", "400");
			map.put("msg", "已有重复名称失败");
		}
		else {
			map.put("code", "500");
			map.put("msg", "新增课程信息失败");
		}
		return map;
	}
	
	@RequestMapping(value = "CourseVo2/{zid}", method = RequestMethod.GET)
	public CourseVo2 querycousevo2(@PathVariable String zid) {
		return biz.querycousevo2(zid);
	}
	
	@RequestMapping(value = "CourseVo2", method = RequestMethod.PUT)
	public Map<String, String> querycousevo2(@RequestBody CourseVo2 c) {
		Map<String, String> map=new HashMap<String,String>();
		int count=biz.updatecousevo2(c);
		if(count>0) {
			map.put("code", "200");
			map.put("msg", "修改课程信息成功");
		}else {
			map.put("code", "500");
			map.put("msg", "修改课程信息失败");
		}
		return map;
	}
	
	/**
	 * 刪除课程信息
	 */
	@RequestMapping(value = "CourseVo2/{id}", method = RequestMethod.DELETE)
	public Map<String, Object> removeUserInfo(@PathVariable String [] id)
			throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		int r=biz.removeUserInfo(id);
		if(r>0) {
			map.put("code", 200);
			map.put("msg", "删除课程成功");
		}else {
			map.put("code", 500);
			map.put("msg", "删除课程失败");
		}
		return map;
	}
	
	@RequestMapping(value = "CourseVo", method = RequestMethod.POST)
	public Map<String, String> addCourseVo(@RequestBody CourseVo1 c) throws Exception{
		Map<String, String> map=new HashMap<String,String>();
		int count=biz.addcousevo(c);
		if(count>0) {
			map.put("code", "200");
			map.put("msg", "新增书本信息成功");
		}
		else if(count==-1) {
			map.put("code", "400");
			map.put("msg", "已有重复名称失败");
		}
		else {
			map.put("code", "500");
			map.put("msg", "新增书本信息失败");
		}
		return map;
	}
}
