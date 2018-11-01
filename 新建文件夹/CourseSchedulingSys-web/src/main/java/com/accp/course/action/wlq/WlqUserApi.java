package com.accp.course.action.wlq;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accp.course.biz.wlq.WlqUserBiz;
import com.accp.course.pojo.ClassInfo;
import com.accp.course.pojo.Classroom;
import com.accp.course.pojo.Timetable;
import com.accp.course.pojo.UserInfo;
import com.accp.course.util.DateFormatUtil;
import com.accp.course.util.GetNaturalPrimaryKeyUtil;
import com.accp.course.vo.wlq.StudentVoWlq;
import com.accp.course.vo.wlq.StudentkebiaoVoWlq;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("wlq")
public class WlqUserApi {

	@Autowired
	private WlqUserBiz biz;

	@RequestMapping(value = "/queryAllUser/{n}/{s}", method = RequestMethod.GET)
	public PageInfo<UserInfo> queryAllUser(@PathVariable Integer n, @PathVariable Integer s) {
		PageInfo<UserInfo> page = biz.queryAllUser(n, s);
		return page;
	}

	@RequestMapping(value = "/queryNameUser/{name}/{n}/{s}", method = RequestMethod.GET)
	public PageInfo<UserInfo> queryNameUser(@PathVariable Integer n, @PathVariable Integer s,
			@PathVariable String name) {
		PageInfo<UserInfo> page = biz.queryNameUser(n, s, name);

		return page;
	}

	@RequestMapping(value = "/queryClassUser/{cla}/{n}/{s}", method = RequestMethod.GET)
	public PageInfo<UserInfo> queryClassUser(@PathVariable Integer n, @PathVariable Integer s,
			@PathVariable String cla) {
		PageInfo<UserInfo> page = biz.queryClassUser(n, s, cla);
		return page;
	}

	@RequestMapping(value = "/queryClassId", method = RequestMethod.GET)
	public List<ClassInfo> queryClassId() {
		List<ClassInfo> list = biz.queryClassId();
		return list;
	}

	@RequestMapping(value = "/removeStudentById/{id}", method = RequestMethod.DELETE)
	public Map<String, Object> removeStudentById(@PathVariable String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (biz.removeStudenById(id) > 0) {
			map.put("code", 200);
		} else {
			map.put("code", 500);
		}
		return map;
	}

	@RequestMapping(value = "/queryUserById/{id}", method = RequestMethod.GET)
	public StudentVoWlq queryUserById(@PathVariable String id) {
		StudentVoWlq page = biz.queryById(id);
		return page;
	}

	@RequestMapping(value = "/queryTheacherById/{id}", method = RequestMethod.GET)
	public UserInfo queryTeacherById(@PathVariable String id) {
		UserInfo user = biz.queryTeacherId(id);
		return user;
	}

	@RequestMapping(value = "/modifyUserWlq", method = RequestMethod.PUT)
	public Map<String, Object> modifyUserWlq(@RequestBody UserInfo user) {

		user.setName(user.getName());

		System.out.println(user.getName());
		user.setAddress(user.getAddress());
		System.out.println(user.getAddress());
		user.setIdentityCard(user.getIdentityCard());
		user.setImg(user.getImg());
		user.setPhone(user.getPhone());
		user.setOutDate(this.getNowDate());
		user.setClassId(user.getClassId());
		user.setSex(user.getSex());
		Map<String, Object> map = new HashMap<String, Object>();
		if (biz.modifyUserWlq(user) > 0) {
			map.put("code", 200);
		} else {
			map.put("code", 500);
		}
		return map;
	}

	@RequestMapping(value = "/savaUserWlq", method = RequestMethod.POST)
	public Map<String, Object> savaUserWlq(@RequestBody UserInfo user) {
		String key = GetNaturalPrimaryKeyUtil.getKey("student");
		user.setId(key);
		System.out.println(key);
		user.setName(user.getName());
		user.setAddress(user.getAddress());
		user.setIdentityCard(user.getIdentityCard());
		user.setImg(user.getImg());
		user.setPhone(user.getPhone());
		user.setInDate(this.getNowDate());
		System.out.println(this.getNowDate());
		user.setClassId(user.getClassId());
		user.setSex(user.getSex());
		Map<String, Object> map = new HashMap<String, Object>();
		if (biz.savaStudent(user) > 0) {
			map.put("code", 200);
		} else {
			map.put("code", 500);
		}
		return map;
	}

	/**
	 * 转日期格式
	 * 
	 * @return
	 */
	public Date getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		java.util.Date date = null;
		try {
			date = formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new java.sql.Date(date.getTime());
	}

	@RequestMapping(value = "/queryAllStudentKebiao/{classId}", method = RequestMethod.GET)
	public Map<String, Object> queryStudentKebiao(@PathVariable String classId) {

		Map<String, Object> map = new HashMap<String, Object>();
		Date date = new Date();
		// 设置要获取到什么样的时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 获取String类型的时间
		String createdate = sdf.format(date);
		List<StudentkebiaoVoWlq> list = new ArrayList<StudentkebiaoVoWlq>();
		list = biz.queryClassKebiao(classId,createdate );
		map.put("list", list);
		List<String>shang=new ArrayList<>();
		List<String>xia=new ArrayList<>();
		List<String>room=new ArrayList<>();
		List<String>room2=new ArrayList<>();
		UserInfo user = new UserInfo();
		UserInfo user2 = new UserInfo();
		Classroom coo=new Classroom();
		Classroom coo2=new Classroom();
		String id = "";
		String id2 = "";
		Integer cid=0;
		Integer cid2=0;
		System.out.println(list.size());
		for (int i=0;i<=list.size()-1;i++) {
			id = list.get(i).getTeacherIdAM();
			System.out.println(id);
			id2 = list.get(i).getTeacherIdPM();
			cid=list.get(i).getClassRoomIdAM();
			cid2=list.get(i).getClassRoomIdPM();
			if (id != null && id2 != null) {
				user = biz.queryTeacherId(id);
				user2 = biz.queryTeacherId(id2);
				coo=biz.queryClassRoom(cid);
				coo2=biz.queryClassRoom(cid2);
				shang.add(i,user.getName());
				xia.add(i, user2.getName());
				room.add(i,coo.getName());
				room2.add(i,coo2.getName());
				
			} else if (id != null && id2 == null) {
				user = biz.queryTeacherId(id);
				user2 = biz.queryTeacherId(id2);
				shang.add(i,user.getName());
				room.add(i,coo.getName());
				room2.add(i,coo2.getName());
				System.out.println(user.getName());
				xia.add(i, "无");
			
			} else if (id == null && id2 != null) {
				user = biz.queryTeacherId(id);
				user2 = biz.queryTeacherId(id2);
				shang.add(i,"无");
				room.add(i,coo.getName());
				room2.add(i,coo2.getName());
				xia.add(i,user2.getName());

			} else {
				shang.add(i,"无");
				xia.add(i,"无");
				room.add(i,coo.getName());
				room2.add(i,coo2.getName());
			}
			
		}
		map.put("shang", shang);
		map.put("xia", xia);
		map.put("room", room);
		map.put("room2", room2);
		System.out.println(shang);
		System.out.println(xia);
		return map;
	}

	@RequestMapping(value = "/queryAllStudentKebiao2/{classId}/{riqi}/{riqi2}", method = RequestMethod.GET)
	public Map<String, Object> queryStudentKebiao2(@PathVariable String classId, @PathVariable String riqi,@PathVariable String riqi2) {
		System.out.println(riqi);
		System.out.println(riqi2);
		Map<String, Object> map = new HashMap<String, Object>();
		Date date = new Date();
		List<StudentkebiaoVoWlq> list = new ArrayList<StudentkebiaoVoWlq>();
		list = biz.queryClassKebiao2(classId, riqi, riqi2);
		map.put("list", list);
		List<String>shang=new ArrayList<>();
		List<String>xia=new ArrayList<>();
		List<String>room=new ArrayList<>();
		List<String>room2=new ArrayList<>();
		UserInfo user = new UserInfo();
		UserInfo user2 = new UserInfo();
		Classroom coo=new Classroom();
		Classroom coo2=new Classroom();
		String id = "";
		String id2 = "";
		Integer cid=0;
		Integer cid2=0;
		System.out.println(list.size());
		for (int i=0;i<=list.size()-1;i++) {
			id = list.get(i).getTeacherIdAM();
			System.out.println(id);
			id2 = list.get(i).getTeacherIdPM();
			cid=list.get(i).getClassRoomIdAM();
			cid2=list.get(i).getClassRoomIdPM();
			if (id != null && id2 != null) {
				user = biz.queryTeacherId(id);
				user2 = biz.queryTeacherId(id2);
				coo=biz.queryClassRoom(cid);
				coo2=biz.queryClassRoom(cid2);
				shang.add(i,user.getName());
				xia.add(i, user2.getName());
				room.add(i,coo.getName());
				room2.add(i,coo2.getName());
				
			} else if (id != null && id2 == null) {
				user = biz.queryTeacherId(id);
				user2 = biz.queryTeacherId(id2);
				shang.add(i,user.getName());
				room.add(i,coo.getName());
				room2.add(i,coo2.getName());
				System.out.println(user.getName());
				xia.add(i, "无");
			
			} else if (id == null && id2 != null) {
				user = biz.queryTeacherId(id);
				user2 = biz.queryTeacherId(id2);
				shang.add(i,"无");
				room.add(i,coo.getName());
				room2.add(i,coo2.getName());
				xia.add(i,user2.getName());

			} else {
				shang.add(i,"无");
				xia.add(i,"无");
				room.add(i,coo.getName());
				room2.add(i,coo2.getName());
			}
			
		}
		map.put("shang", shang);
		map.put("xia", xia);
		map.put("room", room);
		map.put("room2", room2);
		System.out.println(shang);
		System.out.println(xia);
		return map;
	}

	@RequestMapping(value = "/queryAllTeacherKebiao/{tid}", method = RequestMethod.GET)
	public List<Timetable> queryTeacherKebiao(@PathVariable String tid) {
		Date date = new Date();
		// 设置要获取到什么样的时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 获取String类型的时间
		String createdate = sdf.format(date);
		List<Timetable> list = biz.queryKebiaoTeacher(tid, "2018-05-06");

		System.out.println(list.toString());
		return list;
	}

	@RequestMapping(value = "/queryTeacherId/{id}", method = RequestMethod.GET)
	public UserInfo queryTeacherId(@PathVariable String id) {
		UserInfo page = biz.queryTeacherId(id);
		return page;
	}

}
