const modal = document.querySelector('.modal');
const modalMes = document.querySelector('.modal-mes');

const showModal = document.querySelector('.show-modal');
const showModalMes = document.querySelector('.show-mes');
const closeModal = document.querySelector('.close-modal');
const closeModalMes = document.querySelector('.close-modal-mes');
const rating = document.querySelector('.rating');

if(showModalMes){
  showModalMes.addEventListener('click', function (){
    modalMes.classList.remove('hidden')
    modalMes.classList.add('flex')
  });
}

if(showModal){
  showModal.addEventListener('click', function (){
    modal.classList.remove('hidden')
    modal.classList.add('flex')
  });
}
if(closeModal){
  closeModal.addEventListener('click', function (){
    window.location = showModal.getAttribute('href') + '&stars=' + rating.getAttribute('data-value'); 
    modal.classList.add('hidden')
  });
}

if(closeModalMes){
  closeModalMes.addEventListener('click', function (){
    window.location = showModalMes.getAttribute('href'); 
    modalMes.classList.add('hidden')
  });
}

class Rating {
  constructor(dom) {
    dom.innerHTML = '<svg width="110" height="20"></svg>';
    this.svg = dom.querySelector('svg');
    for(var i = 0; i < 5; i++)
      this.svg.innerHTML += `<polygon data-value="${i+1}"
           transform="translate(${i*22},0)" 
           points="10,1 4,19.8 19,7.8 1,7.8 16,19.8">`;
    this.svg.onclick = e => this.change(e);
    this.render();
  }
  
  change(e) {
    let value = e.target.dataset.value;
    value && (this.svg.parentNode.dataset.value = value); 
    this.render();
  }
  
  render() {
    this.svg.querySelectorAll('polygon').forEach(star => {
      let on = +this.svg.parentNode.dataset.value >= +star.dataset.value;
      star.classList.toggle('active', on);
    });
  }
}

document.querySelectorAll('.rating').forEach(dom => new Rating(dom));