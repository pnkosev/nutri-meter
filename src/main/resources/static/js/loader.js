// THIS VERSION CAN BE USED IF THE SCRIPT IS LOADED AT THE END OF THE BODY,
// OTHERWISE THE LOADER ELEMENT IS NULL SINCE IT TRIES TO GET IT BEFORE THE PAGE IS LOADED
// const loader = {
//     loaderElement: document.getElementById('loader'),
//     show: function() {
//         this.loaderElement.style.display = 'block';
//     },
//     hide: function() {
//         this.loaderElement.style.display = 'none';
//     }
// };


// THIS VERSION WILL CREATE THE OBJECT AT EACH CALL WHICH IS NOT OPTIMAL
// const loader = () => {
//     return {
//         loaderElement: document.getElementById('loader'),
//         show: function () {
//             this.loaderElement.style.display = 'block';
//         },
//         hide: function () {
//             this.loaderElement.style.display = 'none';
//         }
//     }
// };

const loader = {
    loaderElement: () => {
        return document.getElementById('loader');
    },
    show: function () {
        this.loaderElement().style.display = 'block';
    },
    hide: function () {
        this.loaderElement().style.display = 'none';
    }
};

const delayFetchBy1Sec = fetch => {
    loader.show();
    setTimeout(() => {
        fetch();
        loader.hide();
    }, 1000);
};