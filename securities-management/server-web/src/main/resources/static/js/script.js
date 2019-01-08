// Default Einstellungen für den jQuery UI Datepicker
if ($.datepicker) {
    $.datepicker.setDefaults({
        beforeShow: function(input) {
            $(input).css({
                'position': 'relative',
                'z-index': 1051
            });
        },
        showButtonPanel: true,
        showWeek: true,
        closeText: 'Schließen',
        prevText: '&#x3C; Zurück',
        nextText: 'Vor &#x3E;',
        currentText: 'Heute',
        monthNames: ['Januar','Februar','März','April','Mai','Juni','Juli','August','September','Oktober','November','Dezember'],
        monthNamesShort: ['Jan','Feb','Mär','Apr','Mai','Jun','Jul','Aug','Sep','Okt','Nov','Dez'],
        dayNames: ['Sonntag','Montag','Dienstag','Mittwoch','Donnerstag','Freitag','Samstag'],
        dayNamesShort: ['So','Mo','Di','Mi','Do','Fr','Sa'],
        dayNamesMin: ['So','Mo','Di','Mi','Do','Fr','Sa'],
        dateFormat: 'dd.mm.yy',
        weekHeader: 'KW',
        firstDay: 1,
        isRTL: false,
        showMonthAfterYear: false,
        yearSuffix: ''
    });
}

// Default Einstellungen für die DataTable
if ($.fn.dataTable) {
    $.extend(true, $.fn.dataTable.defaults, {
        language: {
            emptyTable: 'Keine Daten in der Tabelle vorhanden',
            info: '_START_ bis _END_ von _TOTAL_ Einträgen',
            infoEmpty: '0 bis 0 von 0 Einträgen',
            infoFiltered: '(gefiltert von _MAX_ Einträgen)',
            infoPostFix: '',
            infoThousands: '.',
            decimal: ',',
            lengthMenu: '_MENU_ Einträge anzeigen',
            loadingRecords: 'Wird geladen...',
            processing: 'Bitte warten...',
            search: 'Suchen',
            zeroRecords: 'Keine Einträge vorhanden.',
            paginate: {
                first: '<span class="glyphicon glyphicon-chevron-left"></span><span class="glyphicon glyphicon-chevron-left"></span>',
                previous: '<span class="glyphicon glyphicon-chevron-left"></span>',
                next: '<span class="glyphicon glyphicon-chevron-right"></span>',
                last: '<span class="glyphicon glyphicon-chevron-right"></span><span class="glyphicon glyphicon-chevron-right"></span>'
            },
            aria: {
                sortAscending: ': aktivieren, um Spalte aufsteigend zu sortieren',
                sortDescending: ': aktivieren, um Spalte absteigend zu sortieren'
            },
            select: {
                rows: {
                    _: '%d Zeilen ausgewählt',
                    0: 'Zum Auswählen auf eine Zeile klicken',
                    1: '1 Zeile ausgewählt'
                }
            }
        },
        lengthMenu: [5, 10, 25, 50, 100],
        pagingType: 'full_numbers'
    });
}

// Meldungen setzen
function alertMessage(stat, text) {
    $("#placeholder").html(
        '<div class="alert alert-' + stat
        + ' fade in" role="alert"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>'
        + text + '</div>').alert();
}

// Meldungen schließen
function resetMessage() {
    $("#placeholder").alert("close");
}

// Modalen Dialog anzeigen
function modalMessage(titel, text, error) {
    if (error) {
        $("#title").html("<strong><i>Fehler:</i></strong> " + titel);
        $("#text").html(text).css('background-color', 'red');
    } else {
        $("#title").html(titel);
        $("#text").html(text).removeAttr('style');
    }

    $("#modal").modal();
}

// Definition: data-toggle="delete"
$(document).on('click.bs.delete.data-api', '[data-toggle="delete"]', function(event) {
    var $this = $(this);
    var href = $this.data("url");
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    if ($this.is('a')) {
        event.preventDefault();
    }

    $.ajax({
        type : 'DELETE',
        url : href,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(header, token);
        },
        success: function () {
            alertMessage("success", "Das Wertpapier wurde gelöscht.");
        },
        error: function (jqXHR) {
            alertMessage("danger", jqXHR.responseText.message);
        }
    });
});

// Definition: data-toggle="managementapi"
$(document).on('click.bs.managementapi.data-api', '[data-toggle="managementapi"]', function(event) {
    var $this = $(this);
    var href = $this.attr("href");
    var title = $this.text();

    if ($this.is('a')) {
        event.preventDefault();
    }

    $.ajax({
        type: "GET",
        url: href,
        success: function (data, textStatus, jqXHR) {
            modalMessage(title, jqXHR.responseText);
            resetMessage();
        },
        error: function (jqXHR) {
            modalMessage(title, jqXHR.responseText, true);
            resetMessage();
        }
    });
});

$("#filter-btn").on('click',function() {
    var $this = $(this);
    var url = $this.data("url");

    var type = $("#type option:selected").attr("value");
    if (type !== "") {
        url += "/" + type;
    }

    var inventory = $("#inventory:checked").val();

    if (typeof inventory === "undefined") {
        inventory = "off";
    }

    $(window.location).attr("href", url + "?page=" + $this.data("page") + "&size=" + $this.data("size") + "&inventory=" + inventory);
});