package com.accp.course.action.lxh;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accp.course.biz.lxh.CountBiz;
import com.accp.course.vo.lxh.CountVO;
import com.github.pagehelper.PageInfo;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

@RestController
@RequestMapping("/lxh_counts")
public class CountAction {

	@Autowired
	private CountBiz biz;
	
	@RequestMapping(value = "count/{startPage}/{pageSize}", method = RequestMethod.GET)
	public PageInfo<CountVO> queryByUser(@PathVariable Integer startPage, @PathVariable Integer pageSize) {
		PageInfo<CountVO> page = biz.queryOnMoneyCountTime(startPage, pageSize);
		return page;
	}
	
	@RequestMapping(value="getData",method=RequestMethod.GET)
	public List<CountVO> getDate(){
		/*Map<String, Integer> count=new HashMap<String ,Integer>();
		for(CountVO vo:biz.getData()) {
			count.put(vo.getTeachername(), vo.getCourseCountSum());
		}*/
		return biz.getData();
		/*return count;*/
	}
	@RequestMapping(value="getExcle" ,method=RequestMethod.GET)
	public Map<String ,String> getExcle(HttpServletResponse response,HttpSession session){
		Map<String ,String> msg=new HashMap<String ,String>();
		
		List<CountVO> vo=biz.getData();
		List<String[]> list = new ArrayList<String[]>();
		
		for (CountVO co : vo) {
			String[] str = new String[] { co.getTeacherid().toString(),co.getTeachername().toString(),co.getYear().toString(),co.getMonth().toString(),co.getCourseCountS1().toString(),co.getCourseCountS2().toString(),co.getCourseCountY2().toString(),co.getCourseCountSum().toString()};
			
			list.add(str);
		}
		
		int count = this.reprotExcel(list,session);
		if(count>0) {
			msg.put("code", "200");
			System.out.println(200);
			return msg;
		}else {
			msg.put("code","500");
			return msg;
		}
		
	}
	
	
	public Integer reprotExcel(List<String[]> pageDataList,HttpSession session) {
		//String realPath = session.getServletContext().getRealPath("/uploads");
		String realPath="D:\\";
		Random random=new Random();
		String fileName = "课时统计-"+random.nextInt();
		try {
			WritableWorkbook wbook = Workbook
					.createWorkbook(new FileOutputStream(realPath+"/"+fileName + ".xls")); // 建立excel文件
			WritableSheet wsheet = wbook.createSheet("导出数据", 0); // sheet名称
			WritableCellFormat cellFormatNumber = new WritableCellFormat();
			cellFormatNumber.setAlignment(Alignment.RIGHT);

			WritableFont wf = new WritableFont(WritableFont.ARIAL, 12,
					WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
					jxl.format.Colour.BLACK); // 定义格式、字体、粗体、斜体、下划线、颜色
			WritableCellFormat wcf = new WritableCellFormat(wf); // title单元格定义
			WritableCellFormat wcfc = new WritableCellFormat(); // 一般单元格定义
			WritableCellFormat wcfe = new WritableCellFormat(); // 一般单元格定义
			wcf.setAlignment(jxl.format.Alignment.CENTRE); // 设置对齐方式
			wcfc.setAlignment(jxl.format.Alignment.CENTRE); // 设置对齐方式

			wcf.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			wcfc.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);
			wcfe.setBorder(jxl.format.Border.ALL,
					jxl.format.BorderLineStyle.THIN);

			wsheet.setColumnView(0, 20);// 设置列宽
			wsheet.setColumnView(1, 10);
			wsheet.setColumnView(2, 20);

			int rowIndex = 0;
			int columnIndex = 0;
			if (null != pageDataList) {
				// rowIndex++;
				columnIndex = 0;
				wsheet.setRowView(rowIndex, 500);// 设置标题行高
				wsheet.addCell(new Label(columnIndex++, rowIndex, fileName, wcf));
				wsheet.mergeCells(0, rowIndex,  5, rowIndex);// 合并标题所占单元格
				rowIndex++;
				columnIndex = 0;
				wsheet.setRowView(rowIndex, 380);// 设置项目名行高
				wsheet.addCell(new Label(columnIndex++, rowIndex, "教员编号", wcf));
				wsheet.addCell(new Label(columnIndex++, rowIndex, "教员名字", wcf));
				wsheet.addCell(new Label(columnIndex++, rowIndex, "年份", wcf));
				wsheet.addCell(new Label(columnIndex++, rowIndex, "月份", wcf));
				wsheet.addCell(new Label(columnIndex++, rowIndex, "S1课时", wcf));
				wsheet.addCell(new Label(columnIndex++, rowIndex, "S2课时", wcf));
				wsheet.addCell(new Label(columnIndex++, rowIndex, "Y2课时", wcf));
				wsheet.addCell(new Label(columnIndex++, rowIndex, "总课时", wcf));
				// 开始行循环
				for (String[] array : pageDataList) { // 循环列
					rowIndex++;
					columnIndex = 0;
					wsheet.addCell(new Label(columnIndex++, rowIndex, array[0],
							wcfe));
					wsheet.addCell(new Label(columnIndex++, rowIndex, array[1],
							wcfe));
					wsheet.addCell(new Label(columnIndex++, rowIndex, array[2],
							wcfe));
					wsheet.addCell(new Label(columnIndex++, rowIndex, array[3],
							wcfe));
					wsheet.addCell(new Label(columnIndex++, rowIndex, array[4],
							wcfe));
					wsheet.addCell(new Label(columnIndex++, rowIndex, array[5], 
							wcfe));
					wsheet.addCell(new Label(columnIndex++, rowIndex, array[6],
							wcfe));
					wsheet.addCell(new Label(columnIndex++, rowIndex, array[7], 
							wcfe));
					
				}

				rowIndex++;
				columnIndex = 0;
			}

			wbook.write();
			if (wbook != null) {
				wbook.close();
			}
			return 1;
		} catch (Exception e) {
			return 0;
		}

	}
	
}
