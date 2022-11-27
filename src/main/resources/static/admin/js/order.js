var icon = null;

function showDataOrder(status) {

    output = "";
    $.ajax({
        url: "/api/order/find?status=" + status,
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
                "<a onclick='showOrder(" + id + ',' + page + ")'><i class='fa fa-exclamation icon_del'></i></a>" +
                "<a onclick='delOrder(" + id + ',' + page + ")'><i class='fa fa-trash-o icon_del'></i></a>";
            break;
        case 1:
            icon = "<a onclick='confirmOrder(" + id + ',' + page + ")' data-bs-toggle='modal' data-bs-tarpost='#staticBackdrop'><i class='fa fa-check icon_del'></i></a>" +

                "<a onclick='showOrder(" + id + ',' + page + ")'><i class='fa fa-exclamation icon_del'></i></a>";
            break;
        case 2:
            icon = "<a onclick='confirmOrder(" + id + ',' + page + ")' data-bs-toggle='modal' data-bs-tarpost='#staticBackdrop'><i class='fa fa-check icon_del'></i></a>" +

                "<a onclick='showOrder(" + id + ',' + page + ")'><i class='fa fa-exclamation icon_del'></i></a>";
            break;
        case 3:
            icon = "<a onclick='showOrder(" + id + ',' + page + ")'><i class='fa fa-exclamation icon_del'></i></a>" +
                "<a onclick='delOrder(" + id + ',' + page + ")'><i class='fa fa-trash-o icon_del'></i></a>";
            break;
        default:
            icon = "<a onclick='showOrder(" + id + ',' + page + ")'><i class='fa fa-exclamation icon_del'></i></a>" +
                "<a onclick='delOrder(" + id + ',' + page + ")'><i class='fa fa-trash-o icon_del'></i></a>";
    }
};




function delOrder(id, page) {
    $.ajax({
        url: "/api/order/delete?id=" + id,
        method: "POST",
        success: function (data) {
            showDataOrder(page);
        }
    });

};


function showOrder(id, page) {
    $.ajax({
        url: "/api/order/show?id=" + id,
        method: "GET",
        success: function (data) {
            showDataOrder(page);
        }
    });

};


function cancelOrder(id, page) {
    $.ajax({
        url: "/api/order/cancel?id=" + id,
        method: "GET",
        success: function (data) {
            showDataOrder(page);
        }
    });

};


function confirmOrder(id, page) {
    $.ajax({
        url: "/api/order/confirm?id=" + id,
        method: "GET",
        success: function (data) {
            showDataOrder(page);
        }
    });

};


$(document).ready(function () {


    showDataOrder(7);

});
