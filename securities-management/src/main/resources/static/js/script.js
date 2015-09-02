$("#delete").click(function (event) {
	event.preventDefault();

	var href = $(this).attr("href");

	$.ajax({
	    url: href,
	    type: 'DELETE'
	}).fail(function (xhr, textStatus, errorThrown) {
		var r = $.parseJSON(xhr.responseText);
		$("#alert-placeholder").html('<div class="alert alert-danger fade in"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a> ' + r.message + '</div>').alert();
	}).done(function (data, textStatus, xhr) {
		$("#alert-placeholder").html('<div class="alert alert-success fade in"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a> Das Wertpapier wurde gelöscht.</div>').alert();
	});
});