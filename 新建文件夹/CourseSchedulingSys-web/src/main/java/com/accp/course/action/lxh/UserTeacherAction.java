package com.accp.course.action.lxh;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.accp.course.biz.lxh.UserTeacherBiz;
import com.accp.course.biz.lxh.lxhCourseBiz;
import com.accp.course.pojo.TeacherAndCourse;
import com.accp.course.pojo.UserInfo;
import com.accp.course.util.GetNaturalPrimaryKeyUtil;
import com.accp.course.util.Md5Util;
import com.accp.course.vo.lxh.UserInfoVO_lxh;
import com.accp.course.vo.lxh.UserTeacherVOView;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/lxh_userinfos")
public class UserTeacherAction {

	
	
	@Autowired
	private UserTeacherBiz biz;
	
	@Autowired
	private lxhCourseBiz courseBiz;

	@RequestMapping(value = "userinfo", method = RequestMethod.POST)
	public Map<String, Object> addUserInfo(@RequestBody UserInfoVO_lxh userInfo) {
		Map<String, Object> message = new HashMap<String, Object>();
		String userKey=GetNaturalPrimaryKeyUtil.getKey("teacher");
		System.out.println(userKey);
		try {
			UserInfo user = biz.selectUserTeacher(userKey);
			System.out.println(user+"---------------------------lxh");
			if (user != null) {
				message.put("code", "500");
				message.put("msg", "no");
				return message;
			}else {
				System.out.println(2);
				userInfo.setId(userKey);
				userInfo.setInDate(new Date());
				userInfo.setPwd(Md5Util.getMd5(userInfo.getPwd()));
				biz.addUserTeacher(userInfo);
				Integer [] userId = userInfo.getTeacherandcourseId();
				for (Integer id : userId) {
					courseBiz.addTeacherAndCourse(new TeacherAndCourse(null,userInfo.getId(),id,0) );
				}
				message.put("code", "200");
				message.put("msg", "ok");
			}
		} catch (Exception e) {
			message.put("code", "500");
			message.put("msg", e.getMessage());
		}
		return message;
	}

	@RequestMapping(value = "del", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> rmoveUserInfo(@RequestParam("id[]") String[] id) {
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			for (String string : id) {
				biz.removeUserTeacher(string);
			}
			message.put("code", "200");
			message.put("msg", "ok");
		} catch (Exception e) {
			message.put("code", "500");
			message.put("msg", e.getMessage());
		}
		return message;
	}

	@RequestMapping(value = "userinfo", method = RequestMethod.PUT)
	public Map<String, Object> updateUserInfo(@RequestBody UserInfo userInfo) {
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			Integer rs = biz.modifyUserTeacher(userInfo);
			if (rs > 0) {
				message.put("code", "200");
				message.put("msg", "ok");
			} else {
				message.put("code", "500");
				message.put("msg", "no");
			}
		} catch (Exception e) {
			message.put("code", "500");
			message.put("msg", e.getMessage());
		}
		return message;

	}

	@RequestMapping(value = "userInfo/{id}", method = RequestMethod.GET)
	public UserInfo selectUserTeacher(@PathVariable String id) {
		return biz.selectUserTeacher(id);
	}

	@RequestMapping(value = "userInfo/{startPage}/{pageSize}", method = RequestMethod.GET)
	public PageInfo<UserInfo> queryByUser(@PathVariable Integer startPage, @PathVariable Integer pageSize) {
		PageInfo<UserInfo> page = biz.selectUserTeacherAll(startPage, pageSize);
		return page;
	}

	@RequestMapping(value = "userInfovo/{startPage}/{pageSize}", method = RequestMethod.GET)
	public PageInfo<UserTeacherVOView> queryUserVo(@PathVariable Integer startPage, @PathVariable Integer pageSize) {
		PageInfo<UserTeacherVOView> page = biz.pageUser(startPage, pageSize);
		return page;
	}

}
