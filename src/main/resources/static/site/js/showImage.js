let uploadButton = document.getElementById("imageUpload");
let chosenImage = document.getElementById("imagePreview");
let imageName = document.getElementById("imagePreview");

if (uploadButton) {
  uploadButton.onchange = () => {
    let reader = new FileReader();
    reader.readAsDataURL(uploadButton.files[0]);
    reader.onload = () => {
      chosenImage.setAttribute("src", reader.result);
    };
    imageName.innerText = uploadButton.files[0].name;
  };
}

if (document.getElementById("getImg")) {
  $.ajax({
    url: "/api/user/get/img",
    method: "POST",
    success: function (data) {
      if(data.avatar){
      const img = '/api/images/' + data.avatar
      document.getElementById("avatar__img").setAttribute("src", img);
      document.getElementById("username__slide").innerText = data.name;
    }
    },
  });
}