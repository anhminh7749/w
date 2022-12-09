
function checklogin() {

  var user = document.getElementById('username').value;
  var password = document.getElementById('password').value;

  if(user.length==0){
	swal("", "Trường username không được rỗng!", "error");
}else
	  if(password.length==0){
	setTimeout( swal("", "Trường password không được rỗng!", "error"),1400);

}else
	if(user.length<3 && user.length>0){
		swal("", "Trường username không dưới 8 ký tự!", "error");
	
}else{
	Login();
	//document.getElementById('submit').click();
}
  
	
	
	
 
}

function Login() {
	console.log(window.location.hostname);
	$.ajax({
	  url: "/api/auth/signin?username=" + document.getElementById("username").value +
		"&password=" + document.getElementById("password").value,
	  method: "POST",
	  dataType: "JSON",
	  success: function (response) {
		
	  },
	  error: function (response) {
		if (response.status == 401) {
		  swal("Đăng nhập thất bại!", "Tài khoản hoặc mật khẩu sai", "error");
		}
		if (response.status == 200) {
		  UserName = document.getElementById("username").value;
		  sessionStorage.setItem("UserName", UserName);
		  swal("Đăng nhập thành công!", "", "success");
		  let url='';
		  if(window.location.hostname=='localhost'){
			 url = 'http://localhost:8080'+response.responseText;
		  }else{
			 url = 'http://'+window.location.hostname+response.responseText;
		  }

		  setTimeout(function(){ window.location = url}, 1500);
		}
  
	  }
	})
  }
   