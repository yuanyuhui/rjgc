package com.accp.course.action.hyh;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accp.course.biz.hyh.UserInfoBiz_hyh;
import com.accp.course.pojo.Period;
import com.accp.course.pojo.UserInfo;
import com.accp.course.vo.hyh.RoleAndUserVo;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/hyh")
public class HyhApi {
	@Autowired
	private UserInfoBiz_hyh biz;

	@RequestMapping(value = "queryById/{name}", method = RequestMethod.GET)
	public RoleAndUserVo getByName(@PathVariable String name) throws Exception {
		return biz.queryInfo(name);
	}
	@RequestMapping(value = "findPwd/{name}/{pwd}", method = RequestMethod.GET)
	public  Map<String, Object> findPwd(@PathVariable String name,@PathVariable String pwd) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		int i=biz.findPwd(name,pwd);
		if(i>0) {
			map.put("code", "200");
			map.put("msg", "密码正确");
		}else {
			map.put("code", "500");
			map.put("msg", "密码输入错误");
		}
		return map;
	}
	
	@RequestMapping(value = "updateById", method = RequestMethod.PUT)
	public Map<String, Object> udpateUserInfo(@RequestBody UserInfo us) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		int i = biz.UpdateUserVo(us);
		if (i > 0) {
			map.put("code", "200");
			map.put("msg", "修改成功");
		} else {
			map.put("code", "500");
			map.put("msg", "修改失败");
		}
		return map;
	}
	@RequestMapping(value = "updatePwd", method = RequestMethod.PUT)
	public Map<String, Object> updatePwd(@RequestBody UserInfo us) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		int i = biz.UpdatePwd(us);
		if (i > 0) {
			map.put("code", "200");
			map.put("msg", "修改成功");
		} else {
			map.put("code", "500");
			map.put("msg", "修改失败");
		}
		return map;
	}
}
