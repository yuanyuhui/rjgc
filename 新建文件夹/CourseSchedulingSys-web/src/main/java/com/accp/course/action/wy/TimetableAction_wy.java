package com.accp.course.action.wy;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accp.course.biz.wy.TimetableBiz;
import com.accp.course.vo.wy.TimetableVo;

@RestController
@RequestMapping("timetable")
public class TimetableAction_wy {

	@Autowired
	private TimetableBiz biz;
	
	
	@RequestMapping(value="nextWeekTimetable",method=RequestMethod.GET)
	public List<TimetableVo> getTimetable(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MILLISECOND, 0);
		int weekday1 = cal.get(Calendar.DAY_OF_WEEK)-1;
		cal.add(Calendar.DATE, 7-weekday1+1);
		Date sDate = cal.getTime();
		cal.add(Calendar.DATE, 6);
		Date eDate = cal.getTime();	
		return biz.findTimetableInDateArea(sDate, eDate);
	}
	
}
