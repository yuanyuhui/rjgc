
//转换无课时间字符串
function createWeekToString(WeekStr){
	var intList = [0,0,0,0,0,0,0];
	var weekList = WeekStr.split("、");
	for(var i=0;i<weekList.length;i++){
		if(weekList[i] == "星期一"){
			intList[0] = 1;
			continue;
		}else if(weekList[i] == "星期二"){
			intList[1] = 1;
			continue;
		}else if(weekList[i] == "星期三"){
			intList[2] = 1;
			continue;
		}else if(weekList[i] == "星期四"){
			intList[3] = 1;
			continue;
		}else if(weekList[i] == "星期五"){
			intList[4] = 1;
			continue;
		}else if(weekList[i] == "星期六"){
			intList[5] = 1;
			continue;
		}else if(weekList[i] == "星期天"){
			intList[6] = 1;
			continue;
		}
	}
	return parseListToString(intList);
}

//将int转换为星期
function appendIntToWeek(int){
	switch(int){
		case 1:
			return "星期一、";
		case 2:
			return "星期二、";
		case 3:
			return "星期三、";
		case 4:
			return "星期四、";
		case 5:
			return "星期五、";
		case 6:
			return "星期六、";
		case 7:
			return "星期天、";
	}
}

//将数组按“、”拼接为字符串
function parseListToString(list){
	var str = "";
	for(var i in list){
		str += list[i]+"、";
	}
	return str.substring(0,str.length-1);
}

//根据数组规则确定无课时间
function parseIntStrToWeekStr(intStr){
	var list = intStr.split("、");
	var weekStr = "";
	for(var i=0;i<list.length;i++){
		if(list[i] == 1){
			switch(i){
				case 0:
					weekStr += "星期一、"
					break;
				case 1:
					weekStr += "星期二、"
					break;
				case 2:
					weekStr += "星期三、"
					break;
				case 3:
					weekStr += "星期四、"
					break;
				case 4:
					weekStr += "星期五、"
					break;
				case 5:
					weekStr += "星期六、"
					break;
				case 6:
					weekStr += "星期天、"
					break;
			}
		}
	}
	return weekStr.substring(0,weekStr.length-1);
}

//将数字转换为字符串优先级
function parseIntToPrior(num){
	switch (num) {
		case 1:
			return "最低";
		case 2:
			return"低";
		case 3:
			return"中";
		case 4:
			return"高";
		case 5:
			return"最高";
	}
}

//将字符串优先级转换为数字
function parsePriorToInt(prior){
	if(prior == "最低"){
		return 1;
	}else if(prior == "低"){
		return 2;
	}else if(prior == "中"){
		return 3;
	}else if(prior == "高"){
		return 4;
	}else if(prior == "最高"){
		return 5;
	}
	
}




