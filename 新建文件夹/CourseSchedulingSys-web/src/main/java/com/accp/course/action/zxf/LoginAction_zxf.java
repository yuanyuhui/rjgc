package com.accp.course.action.zxf;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accp.course.biz.zxf.IUserBiz;
import com.accp.course.vo.zxf.UserInfoVo;

@RestController
@RequestMapping("/login")
public class LoginAction_zxf {

	@Autowired
	private IUserBiz biz;

	@RequestMapping(value = "/zxf", method = RequestMethod.POST)
	public Map<String, Object> Login(@RequestBody UserInfoVo user, HttpSession session) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String pwd1 = user.getPwd();
		System.out.println(pwd1);
		MessageDigest md5 = null;
		 md5 = MessageDigest.getInstance("MD5");  
		char[] charArray = pwd1.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}

		String pwd = hexValue.toString();
		System.out.println(pwd);
		System.out.println(user.getId());
		user = biz.findUser(user.getId(), pwd);
		if (user != null) {
			session.setAttribute("user", user);
			map.put("user", user);
			map.put("code", "200");
			map.put("msg", "登录成功!");
		} else {
			map.put("code", "500");
			map.put("msg", "登录失败!");
		}
		return map;
	}
	
	@RequestMapping(value="getUser",method=RequestMethod.GET)
	public UserInfoVo getUser(HttpSession session) {
		UserInfoVo vo = (UserInfoVo)session.getAttribute("user");
		return vo;
	}

}
