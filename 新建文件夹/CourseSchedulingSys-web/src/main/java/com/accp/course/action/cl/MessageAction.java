package com.accp.course.action.cl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accp.course.biz.cl.MessageBiz;
import com.accp.course.pojo.Message;

@RestController
@RequestMapping("/api")
public class MessageAction {

	@Autowired
	private MessageBiz biz;

	@RequestMapping(value = "messages/message/{userId}", method = RequestMethod.GET)
	public Message queryAllClassroomInfoById(@PathVariable String userId) {
		return biz.queryRecentlyMessage(userId);
	}

	@RequestMapping(value = "messages/re", method = RequestMethod.PUT)
	public Map<String, Object> updateMessageBystate(@RequestBody Message re) {
		Map<String, Object> message = new HashMap<String, Object>();
		try {
			biz.updateMessageBystate(re);
			message.put("code", "200");
			message.put("msg", "ok");
		} catch (Exception ex) {
			message.put("code", "500");
			message.put("msg", ex.getMessage());
		}
		return message;

	}
}
