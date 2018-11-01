package com.accp.course.action.cl;

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

import com.accp.course.biz.cl.ClassroomBiz;
import com.accp.course.pojo.Classroom;
import com.github.pagehelper.PageInfo;

@RestController
@RequestMapping("/api")
public class ClassroomAction {

	@Autowired
	private ClassroomBiz biz;

	/**
	 * 
	 * 方法名: queryAllClassroomInfo </br>
	 * 功能: </br>
	 * 时间: 2018年8月24日 上午11:36:47</br>
	 *
	 * 作者: 无心 </br>
	 *
	 * @param page
	 * @param size
	 * @return </br>
	 * @since JDK 1.8
	 */
	@RequestMapping(value = "rooms/{page}/{size}", method = RequestMethod.GET)
	public PageInfo<Classroom> queryAllClassroomInfo(@PathVariable Integer page, @PathVariable Integer size) {
		return biz.queryClassroomInfoAll(page, size);
	}

	/**
	 * 
	 * 方法名: deleteClassroomInfoById </br>
	 * 功能:根据ID删除教室信息 </br>
	 * 时间: 2018年8月27日 上午11:26:54</br>
	 *
	 * 作者: 无心 </br>
	 *
	 * @param id
	 * @return </br>
	 * @since JDK 1.8
	 */
	@RequestMapping(value = "del", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> deleteClassroomInfoById(@RequestParam("id[]") Integer[] id) throws Exception {
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			biz.removeClassroomInfo(id);
			message.put("code", "200");
			message.put("msg", "ok");
		} catch (Exception ex) {
			message.put("code", "500");
			message.put("msg", ex.getMessage());
		}
		return message;
	}

	/**
	 * 
	 * 方法名: updateClassroomInfoByid </br>
	 * 功能:修改教室信息 </br>
	 * 时间: 2018年8月31日 上午8:54:41</br>
	 *
	 * 作者: 无心 </br>
	 *
	 * @param room
	 * @return </br>
	 * @since JDK 1.8
	 */
	@RequestMapping(value = "rooms/room", method = RequestMethod.PUT)
	public Map<String, String> updateClassroomInfoByid(@RequestBody Classroom room) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			biz.modifyClassInfo(room);
			message.put("code", "200");
			message.put("msg", "ok");
		} catch (Exception ex) {
			message.put("code", "500");
			message.put("msg", ex.getMessage());
		}
		return message;
	}

	/**
	 * 
	 * 方法名: addClassroomInfo </br>
	 * 功能:新增教室信息 </br>
	 * 时间: 2018年8月31日 上午8:55:12</br>
	 *
	 * 作者: 无心 </br>
	 *
	 * @param room
	 * @return </br>
	 * @since JDK 1.8
	 */
	@RequestMapping(value = "rooms/add", method = RequestMethod.PUT)
	public Map<String, String> addClassroomInfo(@RequestBody Classroom room) {
		Map<String, String> message = new HashMap<String, String>();
		try {
			biz.addClassroomInfo(room);
			message.put("code", "200");
			message.put("msg", "ok");
		} catch (Exception ex) {
			message.put("code", "500");
			message.put("msg", ex.getMessage());
		}
		return message;
	}

	/**
	 * 
	 * 方法名: queryAllClassroomInfoById </br>
	 * 功能：根据ID查询单个详情信息 </br>
	 * 时间: 2018年8月29日 上午10:11:03</br>
	 *
	 * 作者: 无心 </br>
	 *
	 * @param id
	 * @return </br>
	 * @since JDK 1.8
	 */
	@RequestMapping(value = "rooms/room/{ClassroomId}", method = RequestMethod.GET)
	public Classroom queryAllClassroomInfoById(@PathVariable Integer ClassroomId) {
		return biz.queryAllClassroomInfoById(ClassroomId);
	}

	/**
	 * 
	 * 方法名: queryAllClassroomInfoById </br>
	 * 功能：根据name查询单个详情信息 辅助查询、新增 </br>
	 * 时间: 2018年8月29日 上午10:11:03</br>
	 *
	 * 作者: 无心 </br>
	 *
	 * @param id
	 * @return </br>
	 * @since JDK 1.8
	 */
	@RequestMapping(value = "rooms/names/{name}", method = RequestMethod.GET)
	public Classroom queryAllClassrommInfoByName(@PathVariable String name) {
		return biz.queryAllClassrommInfoByName(name);
	}
}
