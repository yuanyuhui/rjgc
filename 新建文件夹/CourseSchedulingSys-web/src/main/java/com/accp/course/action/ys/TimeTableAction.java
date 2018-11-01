package com.accp.course.action.ys;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accp.course.biz.ys.TimeTableBiz;
import com.accp.course.pojo.Timetable;
import com.accp.course.vo.wy.TimetableVo;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/courses")
public class TimeTableAction {

	@Autowired
	private TimeTableBiz biz;
	
	@RequestMapping(value="/getClassTable/{classTeacherId}/{cName}/{startDate}/{endDate}/{pageNum}/{pageSize}",method=RequestMethod.GET)
	public PageInfo<TimetableVo> queryClassTeacherInfo(Model model,@PathVariable String classTeacherId,@PathVariable String cName,@PathVariable String startDate,
			@PathVariable String endDate,@PathVariable Integer pageNum,@PathVariable Integer pageSize){
		
		PageInfo<TimetableVo> page = biz.queryTable(classTeacherId,cName, startDate, endDate, pageNum, pageSize);
		model.addAttribute("classTeacherTable", page);
		return page;
	}
	
	@RequestMapping(value="/getTableNextWeek",method=RequestMethod.GET)
	public List<TimetableVo> querytimeTableNextWeek(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// getTimeInterval(sdf);
		Calendar calendar1 = Calendar.getInstance();
		// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		calendar1.setFirstDayOfWeek(Calendar.MONDAY);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayOfWeek) {
			calendar1.add(Calendar.DAY_OF_MONTH, -1);
		}
		// 获得当前日期是一个星期的第几天
		int day = calendar1.get(Calendar.DAY_OF_WEEK);
 
		//获取当前日期前（下）x周同星几的日期
		calendar1.add(Calendar.DATE, 7*1);
		calendar1.add(Calendar.DATE, calendar1.getFirstDayOfWeek() - day);
			String beginDate = sdf.format(calendar1.getTime());
			calendar1.add(Calendar.DATE, 6);
			String endDate = sdf.format(calendar1.getTime());
			System.out.println(beginDate+" 到 "+endDate);
			return biz.querytimeTableNextWeek(beginDate, endDate);
	}
	
	@RequestMapping(value="/getCountByDate/{date}",method=RequestMethod.GET)
	public Integer queryCountByDate(@PathVariable String date) {
		return biz.queryCountByDate(date);
	}
}
