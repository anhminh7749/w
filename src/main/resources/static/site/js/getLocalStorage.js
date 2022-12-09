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

let iddel = null;

function logout() {
  swal({
    title: "Bạn có chắc chắn muốn đăng xuất?",
    text: "",
    icon: "info",
    buttons: true,
    dangerMode: true,
  })
  .then(del => {
    if (!del) throw null;
    $.ajax({
      url: "/api/auth/signout",
      method: "POST",
      dataType: "JSON",
    })
    swal("Đã đăng xuất!", "Bạn sẽ được chuyển hướng đến trang chủ", "success");
    sessionStorage.removeItem("UserName");
    
    setTimeout(function(){ window.location = 'http://localhost:8080/api/site' }, 1500);    
  });
  
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
        const url =window.location.hostname+response.responseText;
        console.log(url);
        setTimeout(function(){ window.location = url }, 1500);
      }

    }
  })
}

function showDataUserOrder(status) {

  output = "";
  $.ajax({
    url: "/api/user/orders/find?status=" + status,
    method: "GET",
    dataType: "JSON",
    success: function (data) {
      var x;
      if (data) {
        x = data;
      } else {
        x = "";
      }
      for (var i = 0; i < x.length; i++) {
        const date = new Date(x[i].createAt);

        var active = "";


        switch (x[i].status) {
          case 0:
            active = "Chờ xác nhận";
            break;
          case 1:
            active = "Chờ lấy hàng";
            break;
          case 2:
            active = "Đang giao";
            break;
          case 3:
            active = "Đã giao";
            break;
          case 4:
            active = "Hủy bởi bạn";
            break;
          case 5:
            active = "Hệ thống hủy";
            break;
        }
        const link = ' href="/api/user/orders/detail/' + x[i].id + '"';
        if (x[i].status == 0) {
          output += "<tr><td>" + x[i].id +
            "</td><td>" + x[i].totalQuantity +
            "</td><td>" + x[i].totalAmount +
            "</td><td>" + date.toLocaleString('en-GB') +
            "</td><td>" + active +
            "</td><td>" +
            "<a onclick='cancelorder(" + x[i].id + ")'> <i class='icon-remove'></i></a>" +
            "<a " + link + "><i class='icon-exclamation'></i> </a>" +
            "</td></tr>";
        } else {
          output += "<tr><td>" + x[i].id +
            "</td><td>" + x[i].totalQuantity +
            "</td><td>" + x[i].totalAmount +
            "</td><td>" + date.toLocaleString('en-GB') +
            "</td><td>" + active +
            "</td><td>" +
            "<a" + link + "><i class='icon-exclamation'></i> </a>" +
            "</td></tr>";
        }
      }
      $("#tbody").empty();
      $("#tbody").html(output);
    },

  });
};
//


function cancelorder(id) {
  swal({
      title: "Bạn có chắc chắn muốn hủy đơn hàng này?",
      text: "Sau khi hủy bạn sẽ không thể khôi phục lại được!",
      icon: "warning",
      buttons: true,
      dangerMode: true,
    })
    .then(del => {
      if (!del) throw null;
      $.ajax({
        url: "/api/user/orders/cancel?id=" + id,
        method: "POST",
        success: function (data) {
          swal("", "Hủy thành công đơn hàng!", "success");
          showDataOrder(document.querySelector('input[name="mode"]:checked').value);
        }
      });
    });
}

if (checkdiscountcode) {
  checkdiscountcode.addEventListener("click", () => {
    $.ajax({
      type: "POST",
      contentType: "application/json; charset=utf-8",
      url: "/code/check?discountcode=" +
        discountcode.value +
        "&total=" +
        shoptotalCost.innerText,
      success: function (response) {
        if (response == "null") {
          discountcode.value = "";
          swal("", "Mã giảm giá không tồn tại!", "error");

        } else if (response == "notexist") {
          discountcode.value = "";
          swal("", "Mã giảm giá không tồn tại!", "error");
        } else if (response == "mismatched") {
          discountcode.value = "";
          swal("", "Mã giảm giá không khớp!", "error");

        } else if (response == "oof") {
          discountcode.value = "";
          swal("", "Mã giảm giá đã hết lượt sử dụng!", "error");

        } else if (response == "Insufficientfunds") {
          discountcode.value = "";
          swal("", "Đơn hàng chưa đủ tiền để áp dụng mã giảm giá!", "error");

        } else if (response == "date") {
          discountcode.value = "";
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

}
if (checkout) {
  checkout.addEventListener("click", () => {
    if (addressId.value) {
      if (shopCartItems.length != 0) {
        $.ajax({
          type: "POST",
          contentType: "application/json; charset=utf-8",
          url: "http://localhost:8080/cart/save?addressId=" +
            addressId.value +
            "&discountcode=" +
            discountcode.value,
          data: {
            shopcart: JSON.stringify(shopCartItems),
          },
          success: function (response) {
            if (response == "success") {
              clearCartItems();
              clearCartShopItems();
              swal("", "Thanh toán thành công!", "success");

            }
          },
          error: function (response) {
            alert(response);
          },
        });
      } else {
        swal("Vui lòng thêm sản phẩm vào giỏ hàng", "Chưa có sản phẩm nào được chọn!", "warning");
      }

    } else {
      swal("", "Vui lòng chọn địa chỉ!", "warning");

    }
  });

}
if (document.getElementById("redirect")) {
  document.getElementById("redirect").value = window.location.pathname;
  btnCreateAddress.addEventListener("click", () => {
    setTimeout(formaddress.submit(), 1000);
  });
}



function likeProduct(id) {
  $.ajax({
    type: "GET",
    contentType: "application/json; charset=utf-8",
    url: "/api/site/wishlist/like?id=" + id + "&username=" + sessionStorage.getItem("UserName"),
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



const error = document.querySelectorAll(".error");
const actives = document.querySelectorAll(".actives");
const orders = document.querySelectorAll(".order");
if (orders) {
  document.getElementById('progress').style.width = ((actives.length - 1) / (orders.length - 1)) * 100 + '%';
  if (error.length != 0) {
    document.getElementById('progress').style.width = '100%';
    document.getElementById('progress').style.background = 'linear-gradient(90deg, rgba(55,217,153,1) 0%, rgba(101,168,119,0.8858893899356618) 33%, rgba(129,138,98,0.9363095580028886) 55%, rgba(209,52,38,0.8578781854538691) 71%, rgba(255,3,3,1) 100%)';
  }


}