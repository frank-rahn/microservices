function alertMessage(stat, text) {
	$("#placeholder").html('<div class="alert alert-'+stat+' fade in"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' + text + '</div>').alert();
}

$("#delete").click(function (event) {
	event.preventDefault();

	var href = $(this).attr("href");

	$.ajax({
	    url: href,
	    type: 'DELETE'
	}).fail(function (xhr, textStatus, errorThrown) {
		var r = $.parseJSON(xhr.responseText);
		alertMessage("danger", r.message);
	}).done(function (data, textStatus, xhr) {
		alertMessage("success", "Das Wertpapier wurde gelöscht.");
	});
});