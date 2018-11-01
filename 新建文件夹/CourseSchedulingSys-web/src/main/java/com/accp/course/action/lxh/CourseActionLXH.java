package com.accp.course.action.lxh;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accp.course.biz.lxh.lxhCourseBiz;
import com.accp.course.pojo.Course;

@RestController
@RequestMapping("/lxh_course")
public class CourseActionLXH {

	@Autowired
	private lxhCourseBiz biz;
	
	@RequestMapping(value = "course", method = RequestMethod.GET)
	public List<Course> queryByUser() {
		return biz.selectCourseAll();
	}
}
