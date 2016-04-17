function alertMessage(stat, text) {
	$("#placeholder").html(
			'<div class="alert alert-' + stat
					+ ' fade in" role="alert"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'
					+ text + '</div>').alert();
}

function modalMessage(titel, text) {
	$("#title").html(titel);
	$("#text").html(text);
	$("#modal").modal();
}

$(document).on('click.bs.delete.data-api', '[data-toggle="delete"]', function(event) {
	var $this = $(this);
	var href = $this.data("url");

	if ($this.is('a')) {
		event.preventDefault();
	}

	$.ajax({
		url : href,
		type : 'DELETE'
	}).fail(function(xhr, textStatus, errorThrown) {
		var r = $.parseJSON(xhr.responseText);
		alertMessage("danger", r.message);
	}).done(function(data, textStatus, xhr) {
		alertMessage("success", "Das Wertpapier wurde gelöscht.");
	});
});

$(document).on('click.bs.managementapi.data-api', '[data-toggle="managementapi"]', function(event) {
	var $this = $(this);
	var href = $this.attr("href");
	var title = $this.text();

	if ($this.is('a')) {
		event.preventDefault();
	}

	$.getJSON(href, function(data, textStatus, xhr) {
		if (textStatus == "success") {
			modalMessage(title, xhr.responseText);
			$("#placeholder").alert("close");
		} else {
			var r = $.parseJSON(xhr.responseText);
			alertMessage("danger", r.message);
		}
	});
});

$("#filter-btn").click(function() {
	var $this = $(this);
	var url = $this.data("url");

	var type = $("#type option:selected").attr("value");
	if (type != "") {
		url += "/" + type;
	}

	var inventory = $("#inventory:checked").val();

	if (typeof inventory === "undefined") {
		inventory = "off";
	}

	$(location).attr("href", url + "?page=" + $this.data("page") + "&size=" + $this.data("size") + "&inventory=" + inventory);
});