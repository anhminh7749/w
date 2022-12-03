const checkout = document.querySelector("#checkout");
const checkdiscountcode = document.querySelector("#checkdiscountcode");
const discountcode = document.querySelector("#discountcode");
const countcode = document.querySelector("#discount");
const addressId = document.querySelector("#addressId");
const btnCreateAddress = document.querySelector("#createAddress");
const closeFormAddress = document.querySelector("#closeFormAddress");
const AgreeDel = document.querySelector('#AgreeDel');
var formaddress = document.getElementById("address_form");

let message;

let iddel=null;

function cancelorder(id){
  iddel = id;
}


checkdiscountcode.addEventListener("click", () => {
  $.ajax({
    type: "POST",
    contentType: "application/json; charset=utf-8",
    url:
      "/code/check?discountcode=" +
      discountcode.value +
      "&total=" +
      shoptotalCost.innerText,
    success: function (response) {
      if (response == "null") {
        discountcode.value="";
        swal("", "Mã giảm giá không tồn tại!", "error");
       
      } else if (response == "notexist") {
        discountcode.value="";
        swal("", "Mã giảm giá không tồn tại!", "error");
      } else if (response == "mismatched") {
        discountcode.value="";
        swal("", "Mã giảm giá không khớp!", "error");
        
      } else if (response == "oof") {
        discountcode.value="";
        swal("", "Mã giảm giá đã hết lượt sử dụng!", "error");
      
      } else if (response == "Insufficientfunds") {
        discountcode.value="";
        swal("", "Đơn hàng chưa đủ tiền để áp dụng mã giảm giá!", "error");
        
      } else if (response == "date") {
        discountcode.value="";
        swal("", "Mã giảm giá đã hết hạn hoặc chưa đến hạn sử dụng!", "error");
      } else {
        swal("", "Áp dụng mã Thành công!", "success");
      
        countcode.innerHTML = response;
      }
    },
    error: function (response) {
      alert(response);
    },
  });
});

checkout.addEventListener("click", () => {
  if (addressId.value) {
    if(shopCartItems.length != 0){
      $.ajax({
        type: "POST",
        contentType: "application/json; charset=utf-8",
        url:
          "http://localhost:8080/cart/save?addressId=" +
          addressId.value +
          "&discountcode=" +
          discountcode.value,
        data: {
          shopcart: JSON.stringify(shopCartItems),
        },
        success: function (response) {
          if (response == "success") {
            clearCartItems();
            clearCartShopItems() ;
            swal("", "Thanh toán thành công!", "success");
           
          }
        },
        error: function (response) {
          alert(response);
        },
      });
    }else{
      swal("Vui lòng thêm sản phẩm vào giỏ hàng", "Chưa có sản phẩm nào được chọn!", "warning");
    }
   
  } else {
    swal("", "Vui lòng chọn địa chỉ!", "warning");
    
  }
});


document.getElementById("redirect").value = window.location.pathname;
btnCreateAddress.addEventListener("click", () => {
  
  
  setTimeout(formaddress.submit(),1000);
});


function likeProduct(id){
  $.ajax({
    type: "GET",
    contentType: "application/json; charset=utf-8",
    url:
      "/api/site/wishlist/like?id=" + id+"&username="+sessionStorage.getItem("UserName"),
      success: function (response) {
        switch (response) {
          case 0:
          
            document.getElementById("iconLikePro").classList.remove("icon-heart");
            document.getElementById("iconLikePro").classList.add("icon-heart-empty");
            break;
            case 1:
              
              document.getElementById("iconLikePro").classList.remove("icon-heart-empty");
              document.getElementById("iconLikePro").classList.add("icon-heart");
            break;
        
          default:
            swal("", "Bạn phải đăng nhập mới có thể thêm vào danh sách yêu thích!", "error");
          
            break;
        }
       
      },
      error: function (response) {
        alert(response);
      }, 
  });
}
