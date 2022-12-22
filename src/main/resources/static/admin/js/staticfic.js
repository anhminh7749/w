// if (document.getElementById('chartContainer')) {

// }

// let chardata = [];
// $.ajax({
//     url: "/api/admin/staticyear",
//     method: "GET",
//     success: function (data) {

//         for (let index = 0; index < data.length; index++) {

//             const x = {
//                 label: data[index].startyear,
//                 x: index,
//                 y: parseInt(data[index].value)
//             }
//             chardata.push(x);
//         }
//     }
// });
// console.log(chardata);
// var options = {

//     data: [{
//         type: "column",
//         dataPoints: chardata
//     }]
// };
// $("#chartContainer").CanvasJSChart(options);


$(document).ready(function () {
    var sumrai = 0;
    document.querySelectorAll('#raiforstar').forEach((item) => {
        sumrai += Number(item.innerHTML);
    })
    let index = 5;
    $('.bar span').hide();
    document.querySelectorAll('#raiforstar').forEach((item) => {
        let x = (item.innerHTML / sumrai) * 100;
        $('#bar-' + index).animate({
            width: x + '%'
        }, 1000);
        index--
    })

    setTimeout(function () {
        $('.bar span').fadeIn('slow');
    }, 1000);

});