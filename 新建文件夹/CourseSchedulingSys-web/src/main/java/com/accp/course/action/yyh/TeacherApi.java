package com.accp.course.action.yyh;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accp.course.biz.yyh.Md5Password;
import com.accp.course.biz.yyh.UserBiz;
import com.accp.course.pojo.ClassInfo;
import com.accp.course.pojo.UserInfo;
import com.accp.course.util.Base64ConvertImageUtil;
import com.accp.course.vo.yyh.TeacherVo;
import com.accp.course.vo.yyh.TeacherVo2;
import com.github.pagehelper.PageInfo;

/**
 * 班主任信息管理
 * @author yuanyuhui
 *
 */
@RestController
@RequestMapping("/yyh")
public class TeacherApi {
	@Autowired
	private UserBiz biz;
	
	/**
	 * 查看所有班主任
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "Teacher/{pageNum}/{pageSize}/{type}/{condition}", method = RequestMethod.GET)
	public PageInfo<TeacherVo> getPersonListByPage(@PathVariable Integer pageNum,@PathVariable Integer pageSize, @PathVariable String type,@PathVariable String condition)
			throws Exception {
		if(type.equals("null")) {
			type=null;
		}else if(type=="null") {
			type=null;
		}
		
		if(condition.equals("null")) {
			condition=null;
		}else if(condition=="null") {
			condition=null;
		}
		return biz.queryTeachervoList(pageNum, pageSize,type,condition);
	}
	
	/**
	 * 修改班级表所属班主任
	 * @param id
	 * @param classTeacherId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "class/{id}/{classTeacherId}", method = RequestMethod.PUT)
	public Map<String, Object> modifyclass(@PathVariable String id, @PathVariable String classTeacherId)
			throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		int r=biz.modifyclass(id, classTeacherId);
		if(r>0) {
			map.put("code", 200);
			map.put("msg", "修改班级表所属班主任成功");
		}else {
			map.put("code", 500);
			map.put("msg", "修改班级表所属班主任失败");
		}
		return map;
	}
	
	/**
	 * 查询没有班主任的班级	
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "classInfo", method = RequestMethod.GET)
	public List<ClassInfo> queryClassinfoList()
			throws Exception {
		return biz.queryClassinfoList();
	}
	

	/**
	 * 查询没有班主任的班级	
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "userinfoByid/{id}", method = RequestMethod.GET)
	public UserInfo queryuserinfobyid(@PathVariable String id)
			throws Exception {
		return biz.queryuserinfo(id);
	}
	
	/**
	 * 新增班主任
	 */
	@RequestMapping(value = "TeacherVo", method = RequestMethod.POST)
	public Map<String, Object> addTeacher(@RequestBody TeacherVo2 json)
			throws Exception {
		Map<String, Object> message = new HashMap<String, Object>();
		System.out.println("进来了");
//			String dirRealPath = "C:\\Users\\yuanyuhui\\Documents\\WeChat Files\\yyh_y666\\Files\\layuiDemo\\img";
//			// 服务器真实地址
//			String fName = teacherVo.getImg();
				
			// 数据库操作
			json.setPwd(Md5Password.md5Password(json.getPwd()));
			json.setRoleId(2);
			json.setState(0);
			biz.addTeacherVo(json);
			// 把base64字符串反序列化成图片存储
//			Base64ConvertImageUtil.generateImageFromBase64(teacherVo.getImg(), dirRealPath + File.separator + fName);
			message.put("code", "200");
			message.put("msg", "新增班主任成功");
			return message;
	}
	
	/**
	 * 修改班主任
	 */
	@RequestMapping(value = "Teacher", method = RequestMethod.PUT)
	public Map<String, Object> modifyTeacher(@RequestBody UserInfo house)
			throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		int r=biz.modifyUserInfo(house);
		if(r>0) {
			map.put("code", 200);
			map.put("msg", "修改班主任成功");
		}else {
			map.put("code", 500);
			map.put("msg", "修改失败");
		}
		return map;
	}
	
	/**
	 * 删除班主任
	 */
	@RequestMapping(value = "Teacher/{id}", method = RequestMethod.DELETE)
	public Map<String, Object> removeUserInfo(@PathVariable String [] id)
			throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		int r=biz.removeUserInfo(id);
		if(r>0) {
			map.put("code", 200);
			map.put("msg", "删除班主任成功");
		}else {
			map.put("code", 500);
			map.put("msg", "删除班主任失败");
		}
		return map;
	}
}
