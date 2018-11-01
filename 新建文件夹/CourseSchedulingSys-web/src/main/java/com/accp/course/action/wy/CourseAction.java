package com.accp.course.action.wy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accp.course.biz.wy.CourseBiz;
import com.accp.course.pojo.Course;
import com.accp.course.vo.wy.CourseVo;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/courses")
public class CourseAction {

	@Autowired
	private CourseBiz biz;

	@RequestMapping(value = "getCourse/{type}/{condition}/{pageNum}/{pageSize}", method = RequestMethod.GET)
	public PageInfo<CourseVo> queryCourse(@PathVariable String type, @PathVariable String condition,
			@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
		CourseVo course = new CourseVo();
		if ("courseName".equals(type) && !"null".equals(condition)) {
			course.setName(condition);
		} else if ("period".equals(type) && Integer.parseInt(condition) > 0) {
			course.setPeriodId(Integer.parseInt(condition));
		}
		PageInfo<CourseVo> page = biz.findCourse(course, pageNum, pageSize);
		return page;
	}
	
	@RequestMapping(value="course",method=RequestMethod.POST)
	public Map<String, Object> addCourse(@RequestBody Course course){
		Map<String, Object> map = new HashMap<String, Object>();
		if(biz.saveCourse(course)>0) {
			map.put("code", 200);
		}else {
			map.put("code", 500);
		}
		return map;
	}
	
	@RequestMapping(value="course",method=RequestMethod.DELETE)
	public Map<String, Object> deleteCourse(@RequestBody Integer[] ids){
		Map<String, Object> map = new HashMap<String, Object>();
		int count = biz.getCourseBeforeDelete(ids);
		if(count == 1) {
			map.put("code", 201);
		}else if(count == 2) {
			map.put("code", 202);
		}else if(count == 3) {
			map.put("code", 203);
		}else {
			if(biz.removeCourse(ids)>0) {
				map.put("code", 200);
			}else {
				map.put("code", 500);
			}
		}
		return map;
	}
	
	@RequestMapping(value="course",method=RequestMethod.PUT)
	public Map<String, Object> updateCourse(@RequestBody Course course){
		Map<String, Object> map = new HashMap<String, Object>();
		if(biz.modifyCourse(course)>0) {
			map.put("code", 200);
		}else {
			map.put("code", 500);
		}
		return map;
	}
	
	@RequestMapping(value="course/{id}",method=RequestMethod.GET)
	public CourseVo getCourse(@PathVariable Integer id){
		return biz.getCourseById(id);
	}
	
	@RequestMapping(value="pCourse",method=RequestMethod.GET)
	public List<Course> findParentCourse(){
		return biz.findParentCourse();
	}

}
