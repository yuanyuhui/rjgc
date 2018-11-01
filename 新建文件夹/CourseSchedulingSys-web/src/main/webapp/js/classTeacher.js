//显示窗体
function showWindowDialog(url, title) {
	$("#windowDialog").find(".modal-title").text(title);
	$("#windowDialog").find(".modal-body").load(url); //ajax加载页面
	$("#windowDialog").modal();
}

//显示提示框
function showAlertDialog(msg) {
	$("#alertDialog").find(".modal-body h3").text(msg);
	$("#alertDialog").modal(); //显示
}