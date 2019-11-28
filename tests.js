var host = 'http://localhost:8080';

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
			cb(JSON.parse(r.responseText));
		}
	}
}
