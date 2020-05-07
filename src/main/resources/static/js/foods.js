const URLs = {
    food: '/api/food',
    allFoods: '/api/foods-all',
    customFoods: '/api/foods-custom',
    favoriteFoods: '/api/foods-favorite',
};

const createRow = i => {
    let columns =
        `<td class="food-line">
            ${i.name}
            <input type="hidden" value="${i.id}">
         </td>`;

    return `<tr>${columns}</tr>`;
};

const addFood = e => {
    e.preventDefault();
    const URL = e.target.getAttribute('action');

    const inputs = e.target.querySelectorAll('input');

    const url = window.location.href.split("/");
    const date = url[url.length - 1];

    const json = {
        quantity: inputs[0].value,
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
    const foodId = e.target.children[0].value;
    const foodAddBlock = document.getElementById('food-add-block');

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
                        <input type="number" name="quantity" placeholder="Amount"/>
                        <button class="btn btn-info">Add</button>
                    </form>    
                </div>`;

            const favoriteCheck = document.querySelector('.favorite-check');
            const isFavorite = food.favorite;
            if (isFavorite) {
                favoriteCheck.classList.add('checked');
            }
            favoriteCheck.addEventListener('click', (e) => toggleAsFavorite(e, foodId));

            const form = document.getElementById('food-add-form');
            form.addEventListener('submit', addFood);
        });

    foodAddBlock.style.display = 'block';
};

const getFoods = (URL, table) => {
    // Get the div with the post form
    const foodAddBlock = document.getElementById('food-add-block');
    foodAddBlock.style.display = 'none';

    fetch(URL)
        .then(res => res.json())
        .then(data => {
            let list = '';
            data.forEach(f => {
                list += createRow(f);
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
    const modal = document.getElementById('myModal');

    // Get the button that opens the foods
    const btn = document.getElementById('myBtn');

    // Get the <span> element that closes the foods
    const span = document.getElementsByClassName('close')[0];

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
    span.onclick = function () {
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
