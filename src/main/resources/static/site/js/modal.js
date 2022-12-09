var modal = document.getElementById("myModal");

var showraitting = document.getElementById("showraitting");

var modalnewaddress = document.getElementById("modal-new-address");

var btnnewaddress = document.getElementById("btn-new-address");

var btnshowadd = document.getElementById("showaddressbtn");

var span = document.getElementsByClassName("close")[0];

var closeaddres = document.getElementsByClassName("closeFormAddress")[0];

const sussecraiting = document.getElementById('sussecraiting');

if (showraitting) {
  span.onclick = function () {
    showraitting.style.display = "none";
    const list = document.querySelectorAll('.star');
    document.getElementById('textarearaiting').value = '';
    for (i = 0; i < 5; i++) {
      list[i].classList.remove('selected');
    }
  };
}


function showraiting(id, name, img) {
  showraitting.style.display = "block";
  $.ajax({
    url: "/api/user/orders/check/raitting?id=" + id,
    method: "GET",
    success: function (data) {
      document.getElementById('orderdetailsid').value = id;
      document.getElementById('img-order').src = img;
      document.getElementById('name-order').innerText = name;
      
      if (data.point) {
        document.getElementById('textarearaiting').value = data.comment;
        const list = document.querySelectorAll('.star');
        for (i = 0; i < data.point; i++) {
          list[i].classList.add('selected');
        }
        document.getElementById('setDefault').style.pointerEvents = 'none';
        document.getElementById("sussecraiting").style.display='none';
      }else{
        document.getElementById('setDefault').style.pointerEvents = 'unset';
        document.getElementById("sussecraiting").style.display='block';
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

if (sussecraiting) {
  sussecraiting.addEventListener('click', () => {
    var raiting = {
      "id": null,
      "comment": document.getElementById('textarearaiting').value,
      "point": document.querySelectorAll('.selected').length,
      "active": null,
      "createAt": null,
      "users": null
    }
    console.log(document.getElementById('orderdetailsid').value);
    $.ajax({
      url: "/api/user/orders/create/raitting?id=" + document.getElementById('orderdetailsid').value,
      method: "POST",
      data: JSON.stringify(raiting),
      dataType: "text",
      contentType: "application/json",
      success: function (data) {

        swal({
          title: 'Đã đánh giá sản phẩm thành công!',
          type: 'success',
          confirmButtonText: 'okay'
        })
        setTimeout(function(){ location.reload(); }, 1500);
      },
    });
  })
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





$('#stars li').on('mouseover', function () {
  var onStar = parseInt($(this).data('value'), 10);

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
  document.getElementById('totalRaiting').innerText = msg;
}