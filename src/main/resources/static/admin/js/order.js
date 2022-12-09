var icon = null;

function showDataOrder(status) {

    output = "";
    $.ajax({
        url: "/api/admin/order/find?status=" + status,
        method: "GET",
        dataType: "JSON",
        success: function (data) {
            if (data) {
                x = data;
            } else {
                x = "";
            }
            for (var i = 0; i < x.length; i++) {
                const date = new Date(x[i].createAt);

                var active = "";
                statusOrder(x[i].id, x[i].status, status);
                if (status == 6 || status == 7) {
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
                            active = "Người dùng hủy";
                            break;
                        case 5:
                            active = "Hệ thống hủy";
                            break;
                    }
                    output += "<tr><td>" + x[i].id +
                        "</td><td>" + x[i].username +
                        "</td><td>" + date.toLocaleString('en-GB') +
                        "</td><td>" + x[i].totalQuantity +
                        "</td><td>" + x[i].totaldiscount +
                        "</td><td>" + x[i].totalAmount +
                        "</td><td>" + active +
                        "</td><td>" + icon + "</td></tr>";
                } else {
                    output += "<tr><td>" + x[i].id +
                        "</td><td>" + x[i].username +
                        "</td><td>" + date.toLocaleString('en-GB') +
                        "</td><td>" + x[i].totalQuantity +
                        "</td><td>" + x[i].totaldiscount +
                        "</td><td>" + x[i].totalAmount +
                        "</td><td>" + icon + "</td></tr>";
                }



            }


            $("#tbody").html(output);
        },

    });
};

function statusOrder(id, status, page) {
    switch (status) {
        case 0:
            icon = "<a onclick='confirmOrder(" + id + ',' + page + ")' data-bs-toggle='modal' data-bs-tarpost='#staticBackdrop'><i class='fa fa-check icon_del'></i></a>" +
                "<a onclick='cancelOrder(" + id + ',' + page + ")'><i class='fa fa-times icon_del'></i></a>" +
                "<a data-toggle='modal' data-target='#myModal' onclick='showOrder(" + id + ',' + page + ")'><i class='fa fa-exclamation icon_del'></i></a>" +
                "<a onclick='delOrder(" + id + ',' + page + ")'><i class='fa fa-trash-o icon_del'></i></a>";
            break;
        case 1:
            icon = "<a onclick='confirmOrder(" + id + ',' + page + ")' data-bs-toggle='modal' data-bs-tarpost='#staticBackdrop'><i class='fa fa-check icon_del'></i></a>" +

                "<a data-toggle='modal' data-target='#myModal' onclick='showOrder(" + id + ',' + page + ")'><i class='fa fa-exclamation icon_del'></i></a>";
            break;
        case 2:
            icon = "<a onclick='confirmOrder(" + id + ',' + page + ")' data-bs-toggle='modal' data-bs-tarpost='#staticBackdrop'><i class='fa fa-check icon_del'></i></a>" +

                "<a data-toggle='modal' data-target='#myModal' onclick='showOrder(" + id + ',' + page + ")'><i class='fa fa-exclamation icon_del'></i></a>";
            break;
        case 3:
            icon = "<a data-toggle='modal' data-target='#myModal' onclick='showOrder(" + id + ',' + page + ")'><i class='fa fa-exclamation icon_del'></i></a>" +
                "<a onclick='delOrder(" + id + ',' + page + ")'><i class='fa fa-trash-o icon_del'></i></a>";
            break;
        default:
            icon = "<a data-toggle='modal' data-target='#myModal' onclick='showOrder(" + id + ',' + page + ")'><i class='fa fa-exclamation icon_del'></i></a>" +
                "<a onclick='delOrder(" + id + ',' + page + ")'><i class='fa fa-trash-o icon_del'></i></a>";
    }
};




function delOrder(id, page) {

    swal({
            title: "Bạn có chắc chắn muốn xóa?",
            text: "Sau khi xóa, bạn sẽ không thể khôi phục lại được!",
            icon: "warning",
            buttons: true,
            dangerMode: true,
        })
        .then(del => {
            if (!del) throw null;
            $.ajax({
                url: "/api/admin/order/delete?id=" + id,
                method: "POST",
                success: function (data) {
                    swal("", "Xóa thành công đơn hàng!", "success");
                    showDataOrder(page);
                }
            });
        });


};


function showOrder(id, page) {
    fetch('/api/admin/order/show?id=' + id).then(function (response) {
        return response.text();
    }).then(function (html) {
        document.getElementById("modal-body").innerHTML = html;
        const error = document.querySelectorAll(".error");   
        const actives = document.querySelectorAll(".actives");
        const orders = document.querySelectorAll(".order");
        document.getElementById('progress').style.width=((actives.length-1)/(orders.length-1))* 100+'%';
        if(error.length!=0){
            document.getElementById('progress').style.width='100%';
            document.getElementById('progress').style.background='linear-gradient(90deg, rgba(55,217,153,1) 0%, rgba(101,168,119,0.8858893899356618) 33%, rgba(129,138,98,0.9363095580028886) 55%, rgba(209,52,38,0.8578781854538691) 71%, rgba(255,3,3,1) 100%)';
        }
        var sum = 0;
		$(".total").each(function() {		
				sum += parseFloat(this.value);	
		});

       
const discount = document.getElementById('discounttotal');
if(discount){
    sum=sum-discount.value
}
document.getElementById('totaldetailorder').value=sum;
    }).catch(function (err) {
        console.warn('Something went wrong.', err);
    });
};



function cancelOrder(id, page) {
    $.ajax({
        url: "/api/admin/order/cancel?id=" + id,
        method: "GET",
        success: function (data) {
            swal("", "Đã hủy đơn hàng song!", "success");
            showDataOrder(page);
        }
    });

};


function confirmOrder(id, page) {
    $.ajax({
        url: "/api/admin/order/confirm?id=" + id,
        method: "GET",
        success: function (data) {
            swal("", "Đã thực hiện song!", "success");
            showDataOrder(page);
        }
    });

};


$(document).ready(function () {
    showDataOrder(7);
});