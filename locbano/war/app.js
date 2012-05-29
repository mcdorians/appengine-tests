/**
 * Location Based Notes by Dorian Knoblauch
 * Copyright 2012
 */

var token = "";

var noteTemplate = '<div class="note">' + '<div class="title"></div>'
		+ '<div class="expand">' + '<div class="text"></div><hr />'
		+ '<div class="edit"></div>' + '<div class="meta"></div>' + '</div>'
		+ '</div>';

var gmapTemplate = '<img src="http://maps.google.com/maps/api/staticmap'
		+ '?center={lat},{lng}&zoom=14&markers=color:blue|{lat},{lng}&size=200x200&maptype=roadmap&sensor=false"/>'

var editOnTemplate = '<span class="saveButton button">SAVE</span>';
var editOffTemplate = '<span class="editButton button">EDIT</span>';
var deleteTemplate = '<span class="deleteButton button">DELETE</span>';

var main, add, notebooks, settings;
var gl;

$(window).load(function() {
	if (document.location.href.indexOf("?t=") != -1) {
		try {
			if (typeof (navigator.geolocation) == 'undefined') {
				gl = google.gears.factory.create('beta.geolocation');
			} else {
				gl = navigator.geolocation;
			}
		} catch (e) {
		}
		if (gl) {
			token = document.location.href.split("?t=")[1];
			main = $('#main');
			add = $('#add');
			notebooks = $('#notebooks');
			notebooks.hide();
			settings = $('#settings');
			settings.hide();

			add.click(createNote);
			loadNotes();

			setTimeout(function() {
				window.scrollTo(0, 1);
			}, 100);

		} else {
			alert("No GPS");
		}
	} else {
		newNotebook();
	}

});

function newNotebook() {
	$.ajax({
		url : 'locbano',
		data : {
			n : ''
		},
		success : function(data) {
			document.location.href = "?t=" + JSON.parse(data);
		}
	});
}

function loadNotes() {
	gl.getCurrentPosition(function(position) {
		send({
			g : (position.coords.latitude + "I" + position.coords.longitude)
		});
	}, gpsError);
}
function updateNote(id, text) {
	send({
		id : id,
		txt : text
	})
}

function createNote() {
	gl.getCurrentPosition(function(position) {
		send({
			g : (position.coords.latitude + "I" + position.coords.longitude),
			txt : 'Add text here'
		});
	}, gpsError);
}

function deleteNote(id) {
	if (confirm("Delete note?")) {
		send({
			id : id
		});
		$('#' + id).remove();
	} else {

	}
}

function gpsError() {
	alert("Please Allow Gps Accsess");
}

function send(para) {
	var param = $.extend({
		t : token
	}, para);

	$.ajax({
		url : 'locbano',
		data : param,
		success : function(data) {
			processResult(JSON.parse(data));
		}
	});
}

function processResult(data) {
	if (data && data[0]) {
		for ( var i = 0; i < data.length; i++) {
			buildNote(data[i], false);
		}
	}
	if (data && data.id) {
		buildNote(data, true);
	}

}

function buildNote(note, edit) {
	var e = $($(noteTemplate));
	var title = note.text;
	if (title.length > 30) {
		title = title.substr(0, 30) + "...";
	}
	var distance = note.lastDistance + "";
	if (distance.length > 4) {
		distance = distance.substr(0, 4) + "";
	}
	distance += 'm';
	title = distance + " - " + note.date + " - " + title;
	$(e).attr('id', note.id);
	$('.title', e).html(title);
	$('.text', e).text(note.text);
	$('.meta', e).html(
			gmapTemplate.replace(/{lat}/g, note.lat)
					.replace(/{lng}/g, note.lng));
	var saveButton = $($(editOnTemplate));
	var editButton = $($(editOffTemplate));
	var deleteButton = $($(deleteTemplate));
	bindId(saveButton, editButton, deleteButton, $('.title', e), note.id);
	$('.edit', e).append(saveButton);
	$('.edit', e).append(editButton);
	$('.edit', e).append(deleteButton);
	saveButton.hide();
	$('.expand', e).hide();

	if (edit) {
		main.prepend(e);
		editButton.click();
		$('.expand', e).show('slow');
	} else {
		main.append(e);
	}
}

function bindId(saveButton, editButton, deleteButton, title, id) {
	saveButton.click(function() {
		var text = $('#' + id + ' .text textarea').val();
		updateNote(id, text);
		$('#' + id + ' .text').text(text);
		$('#' + id + ' .editButton').show();
		$('#' + id + ' .saveButton').hide();
	});
	editButton.click(function() {
		var text = $('#' + id + ' .text').text();
		$('#' + id + ' .text').html('<textarea></textarea>');
		$('#' + id + ' .text textarea').val(text);
		$('#' + id + ' .saveButton').show();
		$('#' + id + ' .editButton').hide();
	});
	deleteButton.click(function() {
		deleteNote(id);
	});
	title.click(function() {
		if ($('#' + id + ' .expand').css('display') == 'none') {
			$('#' + id + ' .expand').show('slow');
		} else {
			$('#' + id + ' .expand').hide('slow');
		}
	});
}
