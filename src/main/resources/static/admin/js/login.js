
function checklogin() {

  var user = document.getElementById('username').value;
  var password = document.getElementById('password').value;

  if(user.length==0){
	swal("", "Trường username không được rỗng!", "error");
}else
	  if(password.length==0){
	setTimeout( swal("", "Trường password không được rỗng!", "error"),1400);

}else
// 	if(user.length<9 && user.length>0){
// 		swal("", "Trường username không dưới 8 ký tự!", "error");
	
// }else
{
	console.log("saiodhiuash")
	document.getElementById('submit').click();
}
  
	
	
	
 
}