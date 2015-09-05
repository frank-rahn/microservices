function alertMessage(stat, text) {
	$("#placeholder").html('<div class="alert alert-'+stat+' fade in" role="alert"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' + text + '</div>').alert();
}

function modalMessage(titel, text) {
	$("#title").html(titel);
	$("#text").html(text);
	$("#modal").modal();
}

$(document).on('click.bs.delete.data-api', '[data-toggle="delete"]', function (event) {
	var $this = $(this);
	var href = $this.data("url");

	if ($this.is('a')) {
		event.preventDefault();
	}

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