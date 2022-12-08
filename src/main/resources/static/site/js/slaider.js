var imgF = document.querySelector('.img-feature')
var listImg = document.querySelectorAll('.list-image img')
var prevBtn = document.querySelector('prev')
var nextBtn = document.querySelector('next')

var currentIndex = 0;
function updateImageByIndex(index) {
	currentIndex = index
	imgF.src = listImg[index].getAttribute('src')
}

listImg.forEach((imgF, index) => {
	imgF.addEventListener('click', e => {
		updateImageByIndex(index)
	})
})

prevBtn.addEventListener('click', e => {

})