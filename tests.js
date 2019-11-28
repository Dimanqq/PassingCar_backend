//var host = 'http://localhost:8080';
var host = 'http://192.168.43.149:8080';
var auth = "d27402b6-11b7-11ea-9e1c-0242ac110002";

function regUser(cb){
	var r = new XMLHttpRequest();
	r.open('POST', host + '/create/user', true);
	r.send(JSON.stringify({
		email: "a@a",
		password: "12345",
		first_name: "nikita",
		last_name: "chernik",
		phone: "7999999"
	}));
	r.onload = function(){
		cb(JSON.parse(r.responseText));
	}
}

function uploadImg(auth, cb){
	var inp = document.createElement('input');
	inp.type = 'file';
	inp.click();
	inp.onchange = function(){
		var r = new XMLHttpRequest();
		r.open('POST', host + '/create/image', true);
		r.setRequestHeader('Content-Type', inp.files[0].type);
		r.setRequestHeader('Authorization', auth);
		r.send(inp.files[0]);
		r.onload = function(){
			var id = JSON.parse(r.responseText).image_id;
			window.open(host + "/images/" + id);
		}
	}
}

function createRide(auth, cb){
	var r = new XMLHttpRequest();
	r.open('POST', host + '/create/ride', true);
	r.setRequestHeader('Authorization', auth);
	r.send(JSON.stringify({
		lat_start: 10.567567,
		lon_start: 11.567567,
		lat_end: 10.567567,
		lon_end: 11.567567,
		time_start: new Date().toISOString(),
		places_count: 3
	}));
	r.onload = function(){
		cb(JSON.parse(r.responseText).ride_id);
	}
}

function invite(auth, cb, ride){
	if(ride === undefined){
		return createRide(auth, ride => invite(auth, cb, ride));
	}
	console.log(ride);
	var r = new XMLHttpRequest();
	r.open('POST', host + '/rides/' + ride + '/invite', true);
	r.setRequestHeader('Authorization', auth);
	r.send();
	r.onload = function(){
		cb(JSON.parse(r.responseText));
	}
}
