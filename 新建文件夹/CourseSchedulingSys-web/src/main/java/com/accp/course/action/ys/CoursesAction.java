package com.accp.course.action.ys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accp.course.biz.ys.CoursesBiz;
import com.accp.course.pojo.Course;

@RestController
@RequestMapping("/courses")
public class CoursesAction {

	@Autowired
	private CoursesBiz biz;
	
	@RequestMapping(value="getCourse/{period}",method=RequestMethod.GET)
	public List<Course> queryCourseByPeriod(@PathVariable Integer period){
		return biz.queryCourseByPeriod(period);
	}
}
