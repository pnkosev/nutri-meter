const loader = {
    loaderElement: document.getElementById('loader'),
    show: function() {
        this.loaderElement.style.display = 'block';
    },
    hide: function() {
        this.loaderElement.style.display = 'none';
    }
};