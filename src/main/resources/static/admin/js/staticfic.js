
    if(document.getElementById('chartContainer')){
        let chardata=[];
        $.ajax({
            url: "/api/admin/staticyear" ,
            method: "GET",
            success: function (data) {
                
                for (let index = 0; index < data.length; index++) {
                    
                    const x ={
                        label:data[index].startyear,
                        x:index,    
                        y:parseInt(data[index].value)
                    }
                    chardata.push(x);
                }
            }
        });
       
        var options = {
          
          data: [              
          {
            type: "column",
            dataPoints: chardata
          }
          ]
        };
        $("#chartContainer").CanvasJSChart(options);
    }
   
    