function alertMessage(stat, text) {
	$("#placeholder").html('<div class="alert alert-'+stat+' fade in" role="alert"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>' + text + '</div>').alert();
}

function modalMessage(titel, text) {
	$("#placeholder").html(
`<div class="modal fade" id="modal" role="dialog">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">Titel</h4>
			</div>
			<div class="modal-body">
				<p>Text</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Abbrechen</button>
			</div>
		</div>
	</div>
</div>`
	);
	$("#modal").modal();
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