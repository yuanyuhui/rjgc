package com.accp.course.action.cl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accp.course.biz.cl.NoticesBiz;
import com.accp.course.pojo.Notice;
import com.accp.course.vo.cl.NoticeAndNoticetypeVo;

@RestController
@RequestMapping("/api")
public class NoticesAction {
	@Autowired
	private NoticesBiz biz;

	/**
	 * 
	 * 方法名: queryNoticeById </br>
	 * 功能:根据公告类型查询公告 </br>
	 * 时间: 2018年9月3日 上午9:21:36</br>
	 *
	 * 作者: 无心 </br>
	 *
	 * @param typeId
	 * @return </br>
	 * @since JDK 1.8
	 */
	@RequestMapping(value = "notices/notice/{typeId}", method = RequestMethod.GET)
	public List<Notice> queryNoticeById(@PathVariable Integer typeId) {
		return biz.queryNoticeById(typeId);
	}

	@RequestMapping(value = "notices/noticeById/{notices}", method = RequestMethod.GET)
	public NoticeAndNoticetypeVo queryById(@PathVariable Integer notices) {
		return biz.queryById(notices);
	}

}
