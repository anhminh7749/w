var Days = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]; // index => month [0-11]
let valuePhone;
let valueEmail;
$(document).ready(function () {
    valuePhone = $('input[name=phone]').val();
    valueEmail = $('input[name=email]').val();
    var option = '<option value="day">day</option>';
    var selectedDay = "day";
    for (var i = 1; i <= Days[0]; i++) { //add option days
        if (i < 10) {
            option += '<option value="' + 0 + i + '">' + 0 + i + '</option>';
        } else {
            option += '<option value="' + i + '">' + i + '</option>';
        }
    }
    $('#day').append(option);
    // $('#day').val(selectedDay);

    var option = '<option value="month" >month</option>';
    var selectedMon = "month";
    for (var i = 1; i <= 12; i++) {
        if (i < 10) {
            option += '<option value="' + 0 + i + '">' + 0 + i + '</option>';
        } else {
            option += '<option value="' + i + '">' + i + '</option>';
        }
    }
    $('#month').append(option);
    // $('#month').val(selectedMon);

    var option = '<option value="month">month</option>';
    var selectedMon = "month";
    for (var i = 1; i <= 12; i++) {
        if (i < 10) {
            option += '<option value="' + 0 + i + '">' + 0 + i + '</option>';
        } else {
            option += '<option value="' + i + '">' + i + '</option>';
        }
    }
    $('#month2').append(option);
    // $('#month2').val(selectedMon);

    var d = new Date();
    var option = '<option value="year">year</option>';
    selectedYear = "year";
    for (var i = 1930; i <= d.getFullYear(); i++) { // years start i
        option += '<option value="' + i + '">' + i + '</option>';
    }
    $('#year').append(option);
    // $('#year').val(selectedYear);
});

function isLeapYear(year) {
    year = parseInt(year);
    if (year % 4 != 0) {
        return false;
    } else if (year % 400 == 0) {
        return true;
    } else if (year % 100 == 0) {
        return false;
    } else {
        return true;
    }
}


function cancelEmail() {
    var change = '<div class="Z1Wx1m defautEmail" id="defautEmail"> <input type="text" value="' + valueEmail + '"name="email" style="border: 0px solid;" disabled></div> <a class="+x-MFb defautEmail btn-change-info" onclick="changeEmail()">Thay đổi</a>'
    $('.emailModifile').remove();
    $('#formEmail').append(change);
    document.getElementById('error-email').style.display = 'none'
}

function cancelPhone() {
    var change = '<div class="Z1Wx1m defautPhone" id="defautPhone"><input type="text" value="' + valuePhone + '"name="phone" style="border: 0px solid;" disabled></div> <a class="+x-MFb defautPhone btn-change-info" onclick="changePhone()">Thay đổi</a>'
    $('.phoneModifile').remove();
    $('#formPhone').append(change);
    document.getElementById('error-phone').style.display = 'none'
}

function changePhone() {
    var change = '<div class="ovqcxY phoneModifile"><input type="text" id="checkPhone" oninput="checkduplicatedphone()" name="phone" value="' + valuePhone + '" placeholder="Phone" class=" y-NK4C"></div> <a class="+x-MFb btn-change-info phoneModifile" onclick="cancelPhone()">Hủy bỏ</a>'
    $('.defautPhone').remove();
    $('#formPhone').append(change);
}

function changeEmail() {
    var change = '<div class="ovqcxY emailModifile"><input type="email" id="checkEmail" oninput="checkduplicated()" name="email" value="' + valueEmail + '" placeholder="Email" class=" y-NK4C"></div> <a class="+x-MFb emailModifile btn-change-info" onclick="cancelEmail()">Hủy bỏ</a>'
    $('.defautEmail').remove();
    $('#formEmail').append(change);

}

function change_year(select) {
    if (isLeapYear($(select).val())) {
        Days[1] = 29;
    } else {
        Days[1] = 28;
    }
    if ($("#month").val() == 2) {
        var day = $('#day');
        var val = $(day).val();
        $(day).empty();
        var option = '<option value="day">day</option>';
        for (var i = 1; i <= Days[1]; i++) { //add option days
            if (i < 10) {
                option += '<option value="' + 0 + i + '">' + 0 + i + '</option>';
            } else {
                option += '<option value="' + i + '">' + i + '</option>';
            }
        }
        $(day).append(option);
        if (val > Days[month]) {
            val = 1;
        }
        $(day).val(val);
    }
}

function change_month(select) {
    var day = $('#day');
    var val = $(day).val();
    $(day).empty();
    var option = '<option value="day">day</option>';
    var month = parseInt($(select).val()) - 1;
    for (var i = 1; i <= Days[month]; i++) {
        if (i < 10) {
            option += '<option value="' + 0 + i + '">' + 0 + i + '</option>';
        } else {
            option += '<option value="' + i + '">' + i + '</option>';
        }

    }
    $(day).append(option);
    if (val > Days[month]) {
        val = 1;
    }
    $(day).val(val);
}

function getbirtday() {
    var year = document.getElementById('year').value;
    var month = document.getElementById('month').value;
    var day = document.getElementById('day').value;
    document.getElementById('birtday').value = (year + "-" + month + "-" + day)
}

function submitforminfo() {
    $("input").removeAttr('disabled');
}

function checkduplicated() {
    var checkEmail = $("#checkEmail").val();
    const erorr = document.getElementById('error-email');
  
    $.ajax({
        url: "/api/user/profile/info/check?phone=null&email=" + checkEmail,
        method: "GET",
        success: function (data) {
            if (data == true) {
                console.log(data);
                erorr.style.display = 'block';
                erorr.style.color = '#31ae2c';
                erorr.innerText='Email hợp lệ';
            }
            if (data == false) {
                erorr.style.display = 'block';
                erorr.style.color = 'red';
                erorr.innerText='Email đã được sử dụng';
            }
        }
    });

}


function checkduplicatedphone() {
   
    var checkPhone = $("#checkPhone").val();
    const erorr = document.getElementById('error-phone');

    $.ajax({
        url: "/api/user/profile/info/check?email=null&phone=" + checkPhone,
        method: "GET",
        success: function (data) {
            if (data == true ) {
               
                erorr.style.display = 'block';
                erorr.style.color = '#31ae2c';
                erorr.innerText='Số điện thoại hợp lệ';
            }
            if (data == false ) {
                erorr.style.display = 'block';
                erorr.style.color = 'red';
                erorr.innerText='Số điện thoại đã được sử dụng';
            }
            
        }
    });
   

}

