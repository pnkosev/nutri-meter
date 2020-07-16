const URLs = {
    food: '/api/food',
    allFoods: '/api/foods-all',
    customFoods: '/api/foods-custom',
    favoriteFoods: '/api/foods-favorite',
};

const createRow = (f, i) => {
    if (i % 2 === 0) {
        return `<li class="food-line even-row">${f.name}<input type="hidden" value="${f.id}"></li>`;
    } else {
        return `<li class="food-line odd-row">${f.name}<input type="hidden" value="${f.id}"></li>`;
    }
};

const createOption = (v) => {
    return `<option value="${v.id}">${v.name}${v.name === 'g' ? '' : ` - ${v.equivalentInGrams} g`}</option>`;
};

const addFood = e => {
    e.preventDefault();
    const URL = e.target.getAttribute('action');

    const url = window.location.href.split("/");
    const date = url[url.length - 1];

    const json = {
        quantity: document.getElementById('quantity').value,
        measure: document.getElementById('measure').value,
        date: date,
    };

    fetch(URL, {
        method: 'post',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(json)
    })
        .then(() => {
            window.location = '/diary/' + date;
        });

    return false;
};

const toggleAsFavorite = (e, foodId) => {
    const classList = e.target.classList;

    let isFavorite;

    if (classList.contains('checked')) {
        classList.remove('checked');
        isFavorite = false;
    } else {
        classList.add('checked');
        isFavorite = true;
    }

    const json = {
        foodId,
        isFavorite
    };

    fetch(URLs.favoriteFoods, {
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(json)
    })
        .then();

    return false;
};

const displayBlock = (e) => {
    const li = e.target;
    const foodAddBlock = document.getElementById('food-add-block');

    if (li.classList.contains('selected')) {
        li.classList.remove('selected');
        foodAddBlock.style.display = 'none';
        return false;
    } else {
        [...li.parentNode.children].forEach(li => {
            if (li.classList.contains('selected')) {
                li.classList.remove('selected');
            }
        });
        foodAddBlock.style.display = 'block';
        li.classList.add('selected');
    }

    const foodId = li.children[0].value;

    fetch(URLs.food + `/${foodId}`)
        .then(res => res.json())
        .then(food => {
            foodAddBlock.innerHTML =
                `<div class="text-center">
                    <div>
                        <span>${food.name}</span>
                        <span class="fa fa-star favorite-check"></span>
                    </div>
                    <form id="food-add-form" action=/api/food/${food.id} method="post">
                        <label for="quantity">Amount</label>
                        <input type="number" name="quantity" id="quantity" placeholder="Amount"/>
                        <select id="measure"></select>
                        <button class="btn btn-info">Add</button>
                    </form>    
                </div>`;

            food.measures.forEach(m => {
                document.getElementById('measure').innerHTML += createOption(m);
            });

            const favoriteCheck = document.querySelector('.favorite-check');
            const isFavorite = food.favorite;
            if (isFavorite) {
                favoriteCheck.classList.add('checked');
            }
            favoriteCheck.addEventListener('click', (e) => toggleAsFavorite(e, foodId));

            const form = document.getElementById('food-add-form');
            form.addEventListener('submit', addFood);
        });
};

const getFoods = (URL, table) => {
    // Get the div with the post form
    const foodAddBlock = document.getElementById('food-add-block');
    foodAddBlock.style.display = 'none';

    fetch(URL)
        .then(res => res.json())
        .then(data => {
            let list = '';
            data.forEach((f, i) => {
                list += createRow(f, i);
            });

            const foodContainer = document.getElementById(table);
            foodContainer.innerHTML = list;

            const foods = document.querySelectorAll('.food-line');
            foods.forEach(f => f.addEventListener('click', displayBlock));

            // const forms = document.querySelectorAll('form');
            // forms.forEach(f => f.addEventListener('submit', addFood));
        });
};

const setUpModal = () => {
    // Get the foods
    const modal = document.getElementById('my-modal');

    // Get the button that opens the food tab
    const btn = document.getElementById('add-food-btn');

    // Get the <span> element that closes the foods
    const closeBtn = document.getElementsByClassName('close')[0];

    // When the user clicks the button, open the foods
    btn.onclick = function () {
        modal.style.display = 'block';

        const navAllFoods = document.getElementById("nav-foods-all-tab");
        const navCustomFoods = document.getElementById("nav-foods-custom-tab");
        const navFavoriteFoods = document.getElementById("nav-foods-favorite-tab");

        navAllFoods.addEventListener('click', () => getFoods(URLs.allFoods, 'table-foods-all'));
        navCustomFoods.addEventListener('click', () => getFoods(URLs.customFoods, 'table-foods-custom'));
        navFavoriteFoods.addEventListener('click', () => getFoods(URLs.favoriteFoods, 'table-foods-favorite'));

        getFoods(URLs.allFoods, 'table-foods-all');
    };

// When the user clicks on <span> (x), close the foods
    closeBtn.onclick = function () {
        modal.style.display = "none";
    };

// When the user clicks anywhere outside of the foods, close it
    window.onclick = function (event) {
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }
};

window.onload = () => {
    setUpModal();
};
