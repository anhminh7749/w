var imgF = document.querySelector('.img-feature')
var listImg = document.querySelectorAll('.list-image img')
var prevBtn = document.querySelector('.prev')
var nextBtn = document.querySelector('.next')

var currentIndex = 0;
function updateImageByIndex(index) {
    //remove active class
document.querySelectorAll('.list-image div').forEach(item=>{
    item.classList.remove('active')
})

	currentIndex = index
	imgF.src = listImg[index].getAttribute('src')
    listImg[index].parentElement.classList.add('active')
}

listImg.forEach((imgElenment, index) => {
	imgElenment.addEventListener('click', e => {
		updateImageByIndex(index)
	})
})

prevBtn.addEventListener('click', e => {
if(currentIndex==0){
    currentIndex=listImg.length-1
}else{
    currentIndex--
}
updateImageByIndex(currentIndex)
})

nextBtn.addEventListener('click', e => {
if(currentIndex==listImg.length-1){
    currentIndex=0
}else{
    currentIndex++
}
updateImageByIndex(currentIndex)
})
updateImageByIndex(0)