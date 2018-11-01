package com.accp.course.action.zzw;

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

import com.accp.course.biz.zzw.AuthorityBiz;
import com.accp.course.pojo.Function;
import com.accp.course.pojo.Role;
import com.accp.course.pojo.RoleAndFunction;
import com.accp.course.vo.zzw.RaF;
import com.accp.course.vo.zzw.RoleAndUserinfo;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("authorit")
public class AuthoritAction {
	@Autowired
	private AuthorityBiz biz;
	@RequestMapping(value = "/authorit/role",method=RequestMethod.POST)
	public Map<String, Object> addAuthorit(@RequestBody Role r) throws Exception{
		r.setCreateTime(new Date());
		int pd=biz.addRole(r);
		Map<String, Object> message = new HashMap<String, Object>();
		if(pd>0) {
			message.put("code", "200");
			message.put("msg", "ok");
		}else {
			message.put("code", "500");
			message.put("msg", "no");
		}
		return message;
	}
	@RequestMapping(value = "/authorit/role",method=RequestMethod.PUT)
	public Map<String, Object> updateAuthorit(@RequestBody Role r) throws Exception{
		int pd=biz.updateRole(r);
		Map<String, Object> message = new HashMap<String, Object>();
		if(pd>0) {
			message.put("code", "200");
			message.put("msg", "ok");
		}else {
			message.put("code", "500");
			message.put("msg", "no");
		}
		return message;
	}
	@RequestMapping(value = "/authorit/{roleid}",method=RequestMethod.DELETE)
	public Map<String, Object> delAuthorit(@PathVariable int roleid) throws Exception{
		int pd=biz.delRole(roleid);
		Map<String, Object> message = new HashMap<String, Object>();
		if(pd>0) {
			message.put("code", "200");
			message.put("msg", "ok");
		}else {
			message.put("code", "500");
			message.put("msg", "no");
		}
		return message;
	}
	@RequestMapping(value = "/authorit/role/{roleid}",method=RequestMethod.GET)
	public Role queryAuthorit(@PathVariable int roleid) throws Exception{
		return biz.queryRole(roleid);
	}
	@RequestMapping(value = "/authorit/role",method=RequestMethod.GET)
	public List<Role> queryAllAuthorit() throws Exception{
		return biz.queryAllRole();
	}
	@RequestMapping(value = "/authorit/function/{roleid}",method=RequestMethod.GET)
	public List<Function> queryRoleAndFunctionByRoleid(@PathVariable int roleid) throws Exception{
		return biz.queryRoleAndFunctionByRoleid(roleid);
	}
	@RequestMapping(value = "/authorit/function",method=RequestMethod.GET)
	public List<Function> queryallFunction() throws Exception{
		return biz.queryAllFunction();
	}
	@RequestMapping(value = "/authorit/raf/{roleid}",method=RequestMethod.GET)
	public List<RoleAndFunction> queryFuctionByroleid(@PathVariable int roleid) throws Exception{
		return biz.queryFuctionByroleid(roleid);
	}
	@RequestMapping(value = "/authorit/function/{fid}",method=RequestMethod.DELETE)
	public Map<String, Object> delRoleAndFunction(@PathVariable int id) {
		int pd=biz.delRoleAndFunction(id);
		Map<String, Object> message = new HashMap<String, Object>();
		if(pd>0) {
			message.put("code", "200");
			message.put("msg", "ok");
		}else {
			message.put("code", "500");
			message.put("msg", "no");
		}
		return message;
	}
	@RequestMapping(value = "/authorit/function",method=RequestMethod.DELETE)
	public Map<String, Object> usRoleAndFunctionByRoleid(@RequestBody List<String> rf) {
		int pd=biz.usRoleAndFunctionByRoleid(rf);
		Map<String, Object> message = new HashMap<String, Object>();
		if(pd>0) {
			message.put("code", "200");
			message.put("msg", "ok");
		}else {
			message.put("code", "500");
			message.put("msg", "no");
		}
		return message;
	}
	@RequestMapping(value = "/authorit/roleanduser",method=RequestMethod.PUT)
	public Map<String, Object> updateUserinfoRoleid(@RequestBody RoleAndUserinfo ru) {
		int pd = biz.updateUserinfoRoleid(ru);
		Map<String, Object> message = new HashMap<String, Object>();
		if(pd>0) {
			message.put("code", "200");
			message.put("msg", "ok");
		}else {
			message.put("code", "500");
			message.put("msg", "no");
		}
		return message;
	}
	@RequestMapping(value = "/authorit/ru/{name}/{pageNum}/{pageSize}",method=RequestMethod.GET)
	public PageInfo<RaF> queryAllUr(@PathVariable String name,@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
		return biz.queryAllUr(name, pageNum, pageSize);
	}
	@RequestMapping(value = "/authorit/ur/{roleid}/{pageNum}/{pageSize}",method=RequestMethod.GET)
	public PageInfo<RaF> queryUrByroleid(@PathVariable int roleid,@PathVariable Integer pageNum, @PathVariable Integer pageSize) {
		return biz.queryAllUrByroleid(roleid, pageNum, pageSize);
	}
}
