package com.accp.course.action.ys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accp.course.biz.ys.ClassInfoBiz;
import com.accp.course.biz.ys.CoursesBiz;
import com.accp.course.biz.ys.TeacherBiz;
import com.accp.course.pojo.ClassInfo;
import com.accp.course.pojo.Course;
import com.accp.course.pojo.UserInfo;
import com.accp.course.util.GetNaturalPrimaryKeyUtil;
import com.accp.course.vo.ys.ClassInfoVo;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/courses")
public class ClassInfoAction {
	
	@Autowired
	private ClassInfoBiz biz;
	
	@Autowired
	private TeacherBiz teacherBiz;
	
	@Autowired
	private CoursesBiz coursesBiz;
	
	
	@RequestMapping(value="getClassInfo/{period}/{major}/{pageNum}/{pageSize}",method=RequestMethod.GET)
	public PageInfo<ClassInfoVo> queryAllClassInfo(Model model,@PathVariable String period,@PathVariable String major,
			@PathVariable Integer pageNum,@PathVariable Integer pageSize){
		PageInfo<ClassInfoVo> page = biz.queryClassInfo(period,major,pageNum, pageSize);
		model.addAttribute("AllClassInfo", page);
		return page;
	}
	
	@RequestMapping(value="classInfo/add",method=RequestMethod.POST)
	public Map<String,String> addClassInfo(@RequestBody ClassInfo classInfo){
		Map<String, String> rs = new HashMap<String, String>();
		GetNaturalPrimaryKeyUtil util=new GetNaturalPrimaryKeyUtil();
		String id= util.getKey("class");
		classInfo.setId(id);
		Integer count = biz.addClassInfo(classInfo);
		System.out.println(id);
		if(count>0) {
			rs.put("code", "200");
			rs.put("msg", "ok");
		}
		return rs;
	}
	
	@RequestMapping(value = "classInfo/modify", method = RequestMethod.PUT)
	public Map<String, String> updateClassInfo(@RequestBody ClassInfo classInfo) throws Exception {
		Map<String, String> message = new HashMap<String, String>();
		try {
			biz.modifyClassInfo(classInfo);
			System.out.println(11);
			message.put("code", "200");
			message.put("msg", "ok");
		} catch (Exception ex) {
			message.put("code", "500");
			message.put("msg", ex.getMessage());
		}
		return message;
	}

	@RequestMapping(value = "classInfo/remove/{cId}", method = RequestMethod.DELETE)
	public Map<String, String> removeClassInfo(@PathVariable String[] cId) throws Exception {
		Map<String, String> message = new HashMap<String, String>();
		try {
			biz.removeClassInfo(cId);
			message.put("code", "200");
			message.put("msg", "ok");
		} catch (Exception ex) {
			message.put("code", "500");
			message.put("msg", ex.getMessage());
		}
		return message;
	}
	
	@RequestMapping(value="getClassInfo/{id}",method=RequestMethod.GET)
	public List<ClassInfoVo> queryClassInfoById(@PathVariable String id){
		return biz.queryClassInfoById(id);
	}
	
	@RequestMapping(value="getUpdateInfo/{periodId}",method=RequestMethod.GET)
	public Map<String,Object> queryUpdateInfo(@PathVariable Integer periodId){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Course> course = coursesBiz.queryCourseByPeriod(periodId);
		List<UserInfo> classTeacher = teacherBiz.queryClassTeacher();
		List<UserInfo> teacher = teacherBiz.queryTeacher();
		map.put("course", course);
		map.put("classTeacher", classTeacher);
		map.put("teacher", teacher);
		return map;
	}
}
