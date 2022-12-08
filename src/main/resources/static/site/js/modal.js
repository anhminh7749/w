var modal = document.getElementById("myModal");

var showraitting = document.getElementById("showraitting");

var modalnewaddress = document.getElementById("modal-new-address");

var btnnewaddress = document.getElementById("btn-new-address");

var btnshowadd = document.getElementById("showaddressbtn");

var span = document.getElementsByClassName("close")[0];

var closeaddres = document.getElementsByClassName("closeFormAddress")[0];


if (showraitting) {
  span.onclick = function () {
    showraitting.style.display = "none";
  };
}


function showraiting(id, name, img) {
  showraitting.style.display = "block";
  $.ajax({
    url: "/api/user/orders/check/raitting?id=" + id,
    method: "GET",
    success: function (data) {
      document.getElementById('img-order').src = img;
      document.getElementById('name-order').innerText = name;
      if (data.user) {

      }
    },
  });

}

const details = document.querySelectorAll(".tF2pJg");
if (details) {
  details.forEach(items => {
    items.querySelectorAll('.stardust-button--secondary').forEach(item => {
      const id = items.querySelector('#orderdetails__id').value;
      const img = items.querySelector('#orderdetails__img').src;
      const name = items.querySelector('#orderdetails__name').innerText;
      item.addEventListener("click", () => {
        showraiting(id, name, img)
      });
    });
  });
}

if (modal) {
  btnshowadd.onclick = function () {
    modal.style.display = "block";
  };
  span.onclick = function () {
    modal.style.display = "none";
  };
}

if (btnnewaddress) {
  btnnewaddress.onclick = function () {
    modalnewaddress.style.display = "block";
  };
}


if (closeaddres) {
  closeaddres.onclick = function () {
    modalnewaddress.style.display = "none";
  };
}


window.onclick = function (event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
  if (event.target == modalnewaddress) {
    modalnewaddress.style.display = "none";
  }
};

function setStatusAddress(id) {
  $.ajax({
    type: "GET",
    url: "/api/user/profile/address/status?id=" + id,
    success: function (response) {
      document.location.reload(true);
    },
    error: function (response) {
      alert(response);
    },
  });
}

function deleteAddress(id) {
  swal({
    title: "Bạn có chắc chắn muốn xóa?",
    text: "Sau khi xóa, bạn sẽ không thể khôi phục lại được!",
    icon: "warning",
    buttons: true,
    dangerMode: true,
  }).then((del) => {
    if (!del) throw null;
    $.ajax({
      url: "/api/user/profile/address/delete?id=" + id,
      method: "POST",
      success: function (data) {
        swal("", "Xóa thành công đơn hàng!", "success");
        showDataOrder(page);
      }
    });
    swal("", "Xóa thành công địa chỉ!", "success");
    setTimeout(function () {
      document.location.reload(true)
    }, 1000);
  });
}

function updateAddress(id) {
  $.ajax({
    url: "/api/user/profile/address?id=" + id,
    method: "GET",
    success: function (data) {
      console.log(data);
      console.log(data.name);
      $('#address_form').append('<input type="hidden" name="id" value="' + id + '">');
      document.getElementById('first-name-1').value = data.name;
      document.getElementById('phone').value = data.phone;
      document.getElementById('street-address').value = data.address;
      document.getElementById('city').value = data.specificAddress;
      if (data.status == 1) {
        document.getElementById('yes').checked = true;
      } else {
        document.getElementById('no').checked = true;
      }

      modalnewaddress.style.display = "block";

    }
  });
}




/* 1. Visualizing things on Hover - See next part for action on click */
$('#stars li').on('mouseover', function () {
  var onStar = parseInt($(this).data('value'), 10); // The star currently mouse on

  // Now highlight all the stars that's not after the current hovered star
  $(this).parent().children('li.star').each(function (e) {
    if (e < onStar) {
      $(this).addClass('hover');
    } else {
      $(this).removeClass('hover');
    }
  });

}).on('mouseout', function () {
  $(this).parent().children('li.star').each(function (e) {
    $(this).removeClass('hover');
  });
});


/* 2. Action to perform on click */
$('#stars li').on('click', function () {
  var onStar = parseInt($(this).data('value'), 10); // The star currently selected
  var stars = $(this).parent().children('li.star');

  for (i = 0; i < stars.length; i++) {
    $(stars[i]).removeClass('selected');
  }

  for (i = 0; i < onStar; i++) {
    $(stars[i]).addClass('selected');
  }

  var ratingValue = parseInt($('#stars li.selected').last().data('value'), 10);
  var msg = "";

  switch (ratingValue) {
    case 1:
      msg = "Tệ"
      break;
    case 2:
      msg = "Không hài lòng"
      break;
    case 3:
      msg = "Bình thường"
      break;
    case 4:
      msg = "Hài lòng"
      break;
    case 5:
      msg = "Tuyệt vời"
      break;
  }
  responseMessage(msg);

});





function responseMessage(msg) {
  console.log(msg);
  document.getElementById('totalRaiting').innerText = msg;
}