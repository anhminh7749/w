var modal = document.getElementById("myModal");

var modalnewaddress = document.getElementById("modal-new-address");

var btnnewaddress = document.getElementById("btn-new-address");

var btnshowadd = document.getElementById("showaddressbtn");

var span = document.getElementsByClassName("close")[0];

var closeaddres = document.getElementsByClassName("closeFormAddress")[0];

btnshowadd.onclick = function() {
  modal.style.display = "block";
}
btnnewaddress.onclick=function() {
    modalnewaddress.style.display = "block";
  }

span.onclick = function() {
  modal.style.display = "none";
}
closeaddres.onclick = function() {
    modalnewaddress.style.display = "none";
  }

window.onclick = function(event) {
  if (event.target == modal ) {
    modal.style.display = "none";
  }
  if ( event.target == modalnewaddress) {
    modalnewaddress.style.display = "none";
  }
  
}