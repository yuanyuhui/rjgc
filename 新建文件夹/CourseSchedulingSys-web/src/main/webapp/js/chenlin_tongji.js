$(function() {
	/*查询日期*/
	layui.use('laydate', function() {
		var laydate = layui.laydate;

		//执行一个laydate实例
		laydate.render({
			elem: '#time' //指定元素
				,
			format: 'yyyy年MM月'
		});
	});

	/*f分页*/
	layui.use('laypage', function() {
		var laypage = layui.laypage;

		//执行一个laypage实例
		laypage.render({
			elem: 'Paging' //注意，这里的 test1 是 ID，不用加 # 号
				,
			count: 50 //数据总数，从服务端得到
		});
	});

	/*树状图*/
	jQuery("#tree").click(function() {
		jQuery("#p41").css("display", "none");
		jQuery("#p42").css("display", "block");
		// 基于准备好的dom，初始化echarts实例
		var myChart = echarts.init(document.getElementById('p42'));

		// 指定图表的配置项和数据
		var option = {
			title: {
				text: '树状图统计课时'
			},
			tooltip: {},
			legend: {
				data: ['S1', 'S2', 'Y2', '总课时']
			},
			xAxis: {
				data: ["胡杰", "张三", "李四", "王五", "赵六", "王良一", "阁道三", "王良四"]
			},
			yAxis: {},
			series: [{
				name: 'S1',
				type: 'bar',
				data: [22, 30, 22, 20, 20, 23, 22, 25]
			}, {
				name: 'S2',
				type: 'bar',
				data: [25, 20, 30, 25, 24, 26, 27, 20]
			}, {
				name: 'Y2',
				type: 'bar',
				data: [20, 18, 22, 15, 0, 0, 0, 0]
			}, {
				name: '总课时',
				type: 'bar',
				data: [67, 68, 74, 70, 44, 49, 49, 45]
			}]
		};

		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option);

	});
	/*表格*/
	jQuery("#tab").click(function() {
		jQuery("#p42").css("display", "none");
		jQuery("#p41").css("display", "block");
	});
});

/*获取时间*/
jQuery("#cha").click(function() {
	var time = jQuery("#time").val();
	if(time == null || time == "") {
		layer.open({
			title: '提示',
			content: '请选择日期'
		});
		return;
	}
	jQuery("#data").text(time);
});