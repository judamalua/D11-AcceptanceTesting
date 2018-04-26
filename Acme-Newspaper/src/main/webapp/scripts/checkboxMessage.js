$(document).ready(function() {

	if ($("#broadcast").is(":checked")) {
		$(".select-wrapper:eq(1)").hide();
	} else {
		$(".select-wrapper:eq(1)").show();
	}
	$("#broadcast").click(function() {
		$(".select-wrapper:eq(1)").toggle();

	});
});
