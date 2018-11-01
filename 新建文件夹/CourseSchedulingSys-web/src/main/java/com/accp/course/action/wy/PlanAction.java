package com.accp.course.action.wy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

import com.accp.course.biz.cl.ClassroomBiz;
import com.accp.course.biz.wy.CourseBiz;
import com.accp.course.biz.wy.PlanBiz;
import com.accp.course.biz.wy.TimetableBiz;
import com.accp.course.biz.wy.UserInfoBiz;
import com.accp.course.biz.ys.ClassInfoBiz;
import com.accp.course.pojo.ClassInfo;
import com.accp.course.pojo.Classroom;
import com.accp.course.pojo.Plan;
import com.accp.course.pojo.PlanDetails;
import com.accp.course.pojo.Timetable;
import com.accp.course.util.CompareClassUtil;
import com.accp.course.util.CompareUtil;
import com.accp.course.util.DateFormatUtil;
import com.accp.course.util.GetNaturalPrimaryKeyUtil;
import com.accp.course.util.ParseUtil;
import com.accp.course.util.StringUtil;
import com.accp.course.vo.wy.ClassSchedule;
import com.accp.course.vo.wy.CourseVo;
import com.accp.course.vo.wy.PlanVo;
import com.accp.course.vo.wy.ScheduleVo;
import com.accp.course.vo.wy.SearchVo;
import com.accp.course.vo.wy.TeacherSchedule;
import com.accp.course.vo.wy.TimetableVo;
import com.accp.course.vo.wy.UserInfo_wy;
import com.accp.course.vo.ys.ClassInfoVo;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;

@SuppressWarnings("all")
@RestController
@RequestMapping("api")
public class PlanAction {

	@Autowired
	private PlanBiz biz;
	@Autowired
	private UserInfoBiz userBiz;
	@Autowired
	private ClassInfoBiz classBiz;
	@Autowired
	private ClassroomBiz roomBiz;
	@Autowired
	private CourseBiz courseBiz;
	@Autowired
	private TimetableBiz timeBiz;

	private static Integer schoolNoTime = 0;
	private static Integer N = 6;
	private static Map<String,UserInfo_wy> userMap = null;
	private static HashMap<Integer, CourseVo> courseMap = null;
	private static Map<String, TeacherSchedule> teacherList = null;
	private static Map<String, ClassSchedule> scheList = null;
	private static ArrayList<List<ScheduleVo>> timeTable = null;
	private static List<ClassInfoVo> classList = null;
	
	
	// 分页获取排课计划
	@RequestMapping(value = "plans", method = RequestMethod.POST)
	public PageInfo<Plan> getPlanList(@RequestBody SearchVo searchVo) {
		PageInfo<Plan> page = biz.findAllPlan(searchVo.getStartDate(), searchVo.getEndDate(), searchVo.getId(),
				searchVo.getPageNum(), searchVo.getPageSize());
		return page;
	}

	// 获取排课计划流水号
	@RequestMapping(value = "getid", method = RequestMethod.GET)
	public String getPlanId() {
		return GetNaturalPrimaryKeyUtil.getKey("plan");
	}

	// 获取所有教师信息
	@RequestMapping(value = "getTeacher", method = RequestMethod.GET)
	public List<UserInfo_wy> getAllTeacher() {
		return userBiz.findAllTeacher();
	}

	// 获取排课计划及详情
	@RequestMapping(value = "plandetails/{id}", method = RequestMethod.GET)
	public Map<String, Object> getPlanAndDetails(@PathVariable String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("plan", biz.getPlanById(id));
		map.put("details", biz.findPlanDetailsByPlanId(id));
		return map;
	}

	// 删除排课计划及详情
	@RequestMapping(value = "deleteplan/{id}", method = RequestMethod.DELETE)
	public Map<String, Object> deletePlanAndDetails(@PathVariable String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Plan plan = biz.getPlanById(id);
		if (plan.getSelect() == 1) {
			map.put("code", 501);
		} else {
			if (biz.removePlan(id) > 0) {
				map.put("code", 200);
			} else {
				map.put("code", 500);
			}
		}
		return map;
	}

	// 删除排课计划详情
	@RequestMapping(value = "deleteDetails", method = RequestMethod.DELETE)
	public Map<String, Object> deletePlanDetails(@RequestBody Integer[] removeId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (biz.removePlanDetails(removeId) > 0) {
			map.put("code", 200);
		} else {
			map.put("code", 500);
		}
		return map;
	}

	// 新增排课计划
	@RequestMapping(value = "addplan", method = RequestMethod.POST)
	public Map<String, Object> addPlan(@RequestBody PlanVo planVo) {
		Map<String, Object> map = new HashMap<String, Object>();
		biz.modifyAllPlanSelect();
		if (biz.savePlan(planVo) > 0) {
			Plan plan = biz.getSelectedPlan();
			List<PlanDetails> planList = biz.findPlanDetailsByPlanId(plan.getId());
			List<PlanDetails> list = ParseUtil.parsePlanDetails(planList);
			List<ClassInfo> classList = new ArrayList<ClassInfo>();
			for (PlanDetails temp : list) {
				classList.add(new ClassInfo(temp.getClassOrTeacherId(), null, null, null, null, null, null,
						temp.getClassFrequency(), null, null, null, temp.getPrior()));
			}
			biz.modifyClassPriorAndCourseCount(classList);
			map.put("code", 200);
		} else {
			map.put("code", 500);
		}
		return map;
	}

	// 生成课表
	@RequestMapping(value = "createTimetable", method = RequestMethod.POST)
	public Map<String, Object> createTimetable() {
		Map<String, Object> map = new HashMap<String, Object>(); // 响应消息map
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int weekday1 = calendar.get(Calendar.DAY_OF_WEEK)-1;
		calendar.add(Calendar.DATE, 7-weekday1+1);
		Date sDate = calendar.getTime();
		calendar.add(Calendar.DATE, 6);
		Date eDate = calendar.getTime();
		if(timeBiz.getTimetableExists(sDate, eDate)>0) {
			map.put("code", 201);
			return map;
		}
		Plan plan = biz.getSelectedPlan(); // 获取当前主计划
		schoolNoTime = ParseUtil.parseIntegerArrayToWeek(plan.getNoTime()).get(0); 	// 获取全校排课天数
		N = 7 - ParseUtil.parseIntegerArrayToWeek(plan.getNoTime()).size(); 	  	//获取需要排课的天数
		List<PlanDetails> planList = biz.findPlanDetailsByPlanId(plan.getId()); 	// 获取排课计划详情(未处理)
		Collections.sort(planList, new CompareUtil(0)); // 将计划详情按优先级降序排序
		classList = classBiz.queryClassInfo("null", "null", 1, 200).getList(); // 获取全校所有班级详情
		List<Classroom> roomList = roomBiz.queryClassroomInfoAll(1, 200).getList(); // 获取所有上课教室
		List<PlanDetails> tempList = ParseUtil.parsePlanDetails(planList); // 临时集合，存放处理后的排课计划详情
		timeTable = new ArrayList<List<ScheduleVo>>();
		scheList = new HashMap<String, ClassSchedule>(); // 处理后的每个教室排课计划
		teacherList = new HashMap<String, TeacherSchedule>(); // 处理后的每个教师排课计划
		courseMap = new HashMap<Integer,CourseVo>();
		userMap = new HashMap<String,UserInfo_wy>();
		for(UserInfo_wy u:userBiz.findAllTeacher()){
			userMap.put(u.getId(), u);
		}
		for(CourseVo vo:courseBiz.findCourse(new CourseVo(), 1, 1000).getList()) {
			courseMap.put(vo.getZid(), vo);
		}

		for (PlanDetails temp : tempList) { // 循环班级，设定班级排课计划，包括优先级，排课次数，无课时间
			List<Integer> noTimeList = new ArrayList<Integer>(); // 存放无课时间的int数组
			for (ClassInfoVo vo : classList) { // 将班级详情与班级排课计划组合
				if (temp.getClassOrTeacherId().equals(vo.getId())) {
					noTimeList = ParseUtil.parseIntegerArrayToWeek(temp.getNoTime()); // 获取班级无课时间
					scheList.put(vo.getId(),
							new ClassSchedule(vo.getId(), vo.getName(), vo.getPeriodId(), vo.getClassTeacherId(),
									vo.getTeacherId(), vo.getCourseCount(), vo.getClassRoomId(), vo.getState(),
									vo.getCourseId(), vo.getPrior(), vo.getClassTeacher(), vo.getTeacherName(),
									new ArrayList<Integer>()));
					for (Integer no : noTimeList) { // ↑填充班级排课计划
						scheList.get(vo.getId()).getNoTime().add(no); // 设置班级无课日期集合
					}
				}
			}
		}
		for (PlanDetails temp : tempList) {
			List<Integer> noTimeList = new ArrayList<Integer>();
			if (temp.getType() == 1) {
				noTimeList = ParseUtil.parseIntegerArrayToWeek(temp.getNoTime()); // 获取教师无课时间
				teacherList.put(temp.getClassOrTeacherId(),
						new TeacherSchedule(temp.getId(), temp.getPlanId(), temp.getClassOrTeacherId(), temp.getName(),
								temp.getNoTime(), temp.getType(), new ArrayList<Integer>()));
				for (Integer no : noTimeList) { // ↑填充教师排课计划
					teacherList.get(temp.getClassOrTeacherId()).getNoTimeList().add(no); // 设置教师无课日期集合
				}
			}
		}

		for (int i = 0; i < 7; i++) { // 初始化总课表
			timeTable.add(new ArrayList<ScheduleVo>());
		}

		scheduleCourseMain(timeTable,classList, roomList, 0); // 排课递归算法
		//System.out.println("timeTable.size():" + timeTable.size() + "   " + JSON.toJSONString(timeTable));
		
		List<Timetable> timeList = new ArrayList<Timetable>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int currWeekDay = cal.get(Calendar.DAY_OF_WEEK)-1;
		cal.add(Calendar.DATE, 7-currWeekDay+7);
		for(List<ScheduleVo> voList:timeTable) {
			for(Classroom room:roomList) {
				Timetable t = new Timetable();
				t.setDate(cal.getTime());
				t.setWeek(ParseUtil.parseIntToWeekday(ParseUtil.parseIntegerArrayToWeek(plan.getNoTime()).get(0)));
				for(ScheduleVo vo:voList) {
					if(vo.getRoomId() == room.getId() && vo.getIsAM() == 1) {
						t.setClassNameAM(vo.getClassName());
						t.setClassIdAM(vo.getClassId());
						t.setTeacherIdAM(vo.getTeacherId());
						t.setCourseIdAM(vo.getCourseId());
						Integer courseId = vo.getCourseId();
						if(courseId != null) {
							t.setCourseCountAM(courseMap.get(courseId).getCourseCount());
						}
						t.setCourseNameAM(vo.getCourseName());
						t.setClassRoomIdAM(vo.getRoomId());
					}
					if(vo.getRoomId() == room.getId() && vo.getIsAM() == 0) {
						t.setClassNamePM(vo.getClassName());
						t.setClassIdPM(vo.getClassId());
						t.setTeacherIdPM(vo.getTeacherId());
						t.setCourseIdPM(vo.getCourseId());
						Integer courseId = vo.getCourseId();
						if(courseId != null) {
							t.setCourseCountPM(courseMap.get(courseId).getCourseCount());
						}
						t.setCourseNamePM(vo.getCourseName());
						t.setClassRoomIdPM(vo.getRoomId());
					}
					t.setDate(vo.getDate());
					t.setWeek(ParseUtil.parseIntToWeekday(vo.getWeekday()));
				}
				t.setState(0);
				t.setClassRoomIdAM(room.getId());
				t.setClassRoomIdPM(room.getId());
				timeList.add(t);
			}
			
		}
		if(timeBiz.saveTimetable(timeList)>0) {
			map.put("code", 200);
		}else {
			map.put("code", 500);
		}
		return map;
	}

	//排课主方法，递归算法
	private static void scheduleCourseMain(ArrayList<List<ScheduleVo>> timeTable,List<ClassInfoVo> classList, List<Classroom> roomList,Integer day) {
		if (day == N) {
			System.out.println("***************************恭喜你!!!**********************************");
			System.out.println("***************************排课完毕!**********************************");
			return;
		}
		List<Classroom> y2Room = new ArrayList<Classroom>(); // 有序，循环对象
		List<Classroom> S12Room = new ArrayList<Classroom>(); // 有序，循环对象
		List<ClassInfoVo> y2Class = new ArrayList<ClassInfoVo>(); // 有序，插入对象，按优先级排序
		List<ClassInfoVo> S12Class = new ArrayList<ClassInfoVo>(); // 有序，插入对象，按优先级排序
		for (Classroom room : roomList) {
			if (room.getFloor() == 4) {
				y2Room.add(room);
			} else {
				S12Room.add(room);
			}
		}
		for (ClassInfoVo cl : classList) {
			if (cl.getPeriodId() == 3) {
				y2Class.add(cl);
			} else {
				S12Class.add(cl);
			}
		}
		Collections.sort(y2Class, new CompareClassUtil(0));
		Collections.sort(S12Class, new CompareClassUtil(0));

		List<ScheduleVo> tempList = new ArrayList<ScheduleVo>();

		scheduleCourse(tempList, S12Room, S12Class, day + 1); // S1、S2排课
		scheduleCourse(tempList, y2Room, y2Class, day + 1); // Y2排课

		//System.out.println("******************周" + (day + 1) + "课表*******************");
		//StringUtil.printOutTimetable(tempList); // 输出课表
		
		timeTable.set(day,tempList);
		scheduleCourseMain(timeTable, classList, roomList, (day + 1));

	}

	// 生成每一天的课表
	private static void scheduleCourse(List<ScheduleVo> tempList,List<Classroom> y2Room,List<ClassInfoVo> y2Class,Integer dayOfWeek) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int currWeekDay = cal.get(Calendar.DAY_OF_WEEK)-1;
		cal.add(Calendar.DATE, 7-currWeekDay+dayOfWeek);
		for(int i = 0; i < y2Room.size(); i++) {				//y2上午排课
			for(int j=0;j < y2Class.size();j++) {
				if(y2Room.get(i).getIsClassRoom() == 1 && y2Class.get(j).getState() == 1 && checkClassState(tempList, y2Room.get(i),y2Class.get(j),1,dayOfWeek)) {
					if(y2Class.get(j).getStudentsCount() == -1 || y2Class.get(j).getCourseCount() == 0) {
						ScheduleVo sche = new ScheduleVo(y2Room.get(i).getId(), y2Class.get(j).getId(), y2Class.get(j).getName(), null, null ,"自习", y2Class.get(j).getCourseCount(), 1,cal.getTime(),dayOfWeek);
						tempList.add(sche);							//自习
					}else {
						ScheduleVo sche = new ScheduleVo(y2Room.get(i).getId(), y2Class.get(j).getId(), y2Class.get(j).getName(), y2Class.get(j).getTeacherId(), y2Class.get(j).getCourseId() ,y2Class.get(j).getCourseName(), y2Class.get(j).getCourseCount(), 1,cal.getTime(),dayOfWeek);
						y2Class.get(j).setCourseCount(y2Class.get(j).getCourseCount() - 1);
						tempList.add(sche);													//正课
						checkClassInfo(y2Class.get(j));
					}						
					break;
				}else if(y2Room.get(i).getIsClassRoom() == 0 && y2Class.get(j).getState() == 0 && checkClassState(tempList, y2Room.get(i),y2Class.get(j),1,dayOfWeek)) {
					ScheduleVo sche = new ScheduleVo(y2Room.get(i).getId(), y2Class.get(j).getId(), y2Class.get(j).getName(), null, null ,"自习", y2Class.get(j).getCourseCount(), 1,cal.getTime(),dayOfWeek);
					tempList.add(sche);							//自习
					break;
				}
			}
		} 
		for(int i = 0; i < y2Room.size(); i++) {				//y2下午排课
			for(int j=0;j < y2Class.size();j++) {
				if(y2Room.get(i).getIsClassRoom() == 1 && y2Class.get(j).getState() == 0 && checkClassState(tempList, y2Room.get(i),y2Class.get(j),0,dayOfWeek)) {
					if(y2Class.get(j).getStudentsCount() == -1 || y2Class.get(j).getCourseCount() == 0) {
						ScheduleVo sche = new ScheduleVo(y2Room.get(i).getId(), y2Class.get(j).getId(), y2Class.get(j).getName(), null, null ,"自习", y2Class.get(j).getCourseCount(), 0,cal.getTime(),dayOfWeek);
						tempList.add(sche);
					}else {
						ScheduleVo sche = new ScheduleVo(y2Room.get(i).getId(), y2Class.get(j).getId(), y2Class.get(j).getName(), y2Class.get(j).getTeacherId(), y2Class.get(j).getCourseId() ,y2Class.get(j).getCourseName(), y2Class.get(j).getCourseCount(), 0,cal.getTime(),dayOfWeek);
						y2Class.get(j).setCourseCount(y2Class.get(j).getCourseCount() - 1);
						tempList.add(sche);							//正课
						checkClassInfo(y2Class.get(j));
					}
					break;
				}else if(y2Room.get(i).getIsClassRoom() == 0 && y2Class.get(j).getState() == 1 && checkClassState(tempList, y2Room.get(i),y2Class.get(j),0,dayOfWeek)) {
					ScheduleVo sche = new ScheduleVo(y2Room.get(i).getId(), y2Class.get(j).getId(), y2Class.get(j).getName(), null, null ,"自习", y2Class.get(j).getCourseCount(), 0,cal.getTime(),dayOfWeek);
					tempList.add(sche);							//自习	
					break;
				}
			}
		}
		for(ClassInfoVo vo:y2Class) {					//如果当天未排课，第二天必定排课
			boolean hasClass = false;
			for(ScheduleVo sVo:tempList) {
				if(sVo.getClassId().equals(vo.getId())) {		//若本天已排课，重置为排课计划优先级
					vo.setPrior(scheList.get(vo.getId()).getPrior());
					hasClass = true;
				}
			}
			if(!hasClass) {
				vo.setPrior(vo.getPrior()+10);
			}
		}
	}
	
	

	//检查排课规则
	private static boolean checkClassState(List<ScheduleVo> tempList, Classroom y2Room, ClassInfoVo y2Class,Integer isAM, Integer dayOfWeek) {
		Integer classCount = 0; // 本周正课次数
		Integer startWeekDay = 1;
		Integer endWeekDay = 1;

		/**
		 * 确定是否能排课
		 */
		for (int i = 0; i < tempList.size(); i++) { // 判断是否可以排课
			TeacherSchedule teacher = teacherList.get(y2Class.getTeacherId());
			// TeacherSchedule classteacher = teacherList.get(y2Class.getClassTeacherId());
			if (tempList.get(i).getClassId().equals(y2Class.getId()) && tempList.get(i).getIsAM() == isAM) {
				return false; // 此时间段是否已排课
			}
			if (scheList.get(y2Class.getId()).getNoTime().indexOf(dayOfWeek) != -1) {
				return false; // 班级无课时间不排课
			}
			if (teacher != null && teacher.getNoTimeList().indexOf(dayOfWeek) != -1) {
				return false; // 教员无课时间不排课
			}
			/*
			 * if(classteacher != null && classteacher.getNoTimeList().indexOf(dayOfWeek) !=
			 * -1) { return false; //班主任无课时间不排课 }
			 */
		}

		/**
		 * 确定如何排课
		 */
		for (int i = 0; i < timeTable.size(); i++) {
			for (ScheduleVo vo : timeTable.get(i)) {
				if (vo.getClassId().equals(y2Class.getId()) && vo.getTeacherId() != null) { // 表示排了正课
					if (classCount == 0) {
						startWeekDay = i + 1;
					}
					classCount++;
				}
				endWeekDay = i + 1;
			}
		}
		if (classCount == 3 && endWeekDay - startWeekDay == 2) {
			y2Class.setStudentsCount(-1);
			return true;
		}else {
			y2Class.setStudentsCount(30);
		}
		if (y2Class.getCourseCount() == 0) {
			return true;
		}
		return true;
	}
	
	//修改班级课程信息
	public static void checkClassInfo(ClassInfoVo classInfo) {
		List<UserInfo_wy> teacherMap1 = new ArrayList<UserInfo_wy>();
		List<UserInfo_wy> teacherMap2 = new ArrayList<UserInfo_wy>();
		List<UserInfo_wy> teacherMap3 = new ArrayList<UserInfo_wy>();
		CourseVo currCourse = courseMap.get(classInfo.getCourseId());
		CourseVo nextCourse = courseMap.get(classInfo.getCourseId()+1);
		classInfo.setCourseId(classInfo.getCourseId()+1);
		classInfo.setCourseCount(nextCourse.getCourseCount());
		classInfo.setCourseName(nextCourse.getName());
		classInfo.setPeriodId(nextCourse.getPeriodId());
		classInfo.setPeriodName(nextCourse.getPeriodName());
		if(nextCourse.getParentId() > currCourse.getParentId()) {
			for(UserInfo_wy u:userMap.values()) {
				if(u.getCourses().indexOf(nextCourse.getParentId()) != -1) {
					teacherMap1.add(u);
				}
			}
			for(UserInfo_wy u:teacherMap1) {
				int count = 0;
				for(ClassInfoVo classVo:classList) {
					if(u.getId().equals(classVo.getTeacherId()) || u.getId().equals(classVo.getClassTeacherId())) {
						count++;
					}
				}
				if(count == 0) {
					teacherMap2.add(u);
				}
				if(count == 1) {
					teacherMap3.add(u);
				}
			}
			if(teacherMap3.size()>0) {
				int index = (int)(Math.random()*(teacherMap3.size()-1));
				classInfo.setTeacherId(teacherMap3.get(index).getId());
				classInfo.setTeacherName(teacherMap3.get(index).getName());
			}else if(teacherMap2.size()>0) {
				int index = (int)(Math.random()*(teacherMap2.size()-1));
				classInfo.setTeacherId(teacherMap2.get(index).getId());
				classInfo.setTeacherName(teacherMap2.get(index).getName());
			}else if(teacherMap1.size()>0){
				int index = (int)(Math.random()*(teacherMap1.size()-1));
				classInfo.setTeacherId(teacherMap1.get(index).getId());
				classInfo.setTeacherName(teacherMap1.get(index).getName());
			}
		}
	}

}
