const URLs = {
    food: '/api/food',
    allFoods: '/api/foods-all',
    customFoods: '/api/foods-custom',
    favoriteFoods: '/api/foods-favorite',
    searchedFoods: '/api/foods',
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
    const date = url[url.length - 1].split('#')[0];

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

const getCurrentTab = () => {
    const currentURL = window.location.href;
    const index = currentURL.indexOf('#');
    return currentURL.substr(index);
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
        .then(() => {
            const currentTab = getCurrentTab();
            if (currentTab === '#foods-favorite') {
                document.querySelector(`a[href$="${currentTab}"]`).click()
            }
        });

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

const fillContainer = (data, table) => {
    let list = '';
    data.forEach((f, i) => {
        list += createRow(f, i);
    });

    const foodContainer = document.getElementById(table);
    foodContainer.innerHTML = list;
};

const getFoods = (URL, table) => {
    // Get the div with the post form
    const foodAddBlock = document.getElementById('food-add-block');
    foodAddBlock.style.display = 'none';

    fetch(URL)
        .then(res => res.json())
        .then(data => {

            fillContainer(data, table);
            const foods = document.querySelectorAll('.food-line');
            foods.forEach(f => f.addEventListener('click', displayBlock));
        });
};

const setUpFoodRemoval = () => {
    [...document.getElementsByClassName('delete-food')]
        .forEach(f => {
            f.onclick = () => {
                const associationId = f.getAttribute('association-id');

                fetch(URLs.food + `/${associationId}`, {
                    method: 'delete',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                    .then(() => window.location.reload());
            };
        });
};

const removeHref = () => {
    const currentURL = window.location.href;
    const index = currentURL.indexOf('#');
    window.location.href = window.location.href.substring(0, index);
};

const setUpModal = () => {
    // Get the foods
    const modal = document.getElementById('my-modal');

    // Get the button that opens the food tab
    const btn = document.getElementById('add-food-btn');

    // Get the <span> element that closes the foods
    const closeBtn = document.getElementsByClassName('close-modal')[0];

    // When the user clicks the button, open all foods
    btn.onclick = function () {
        modal.style.display = 'block';
        window.location.href += '#foods-all';
        getFoods(URLs.allFoods, 'table-foods-all');
    };

// When the user clicks on <span> (x), close the foods
    closeBtn.onclick = function () {
        modal.style.display = "none";
        removeHref();
    };

// When the user clicks anywhere outside of the foods, close it
    window.onclick = function (event) {
        if (event.target === modal) {
            modal.style.display = "none";
            removeHref();
        }
    }
};

const search = () => {
    const searchInput = document.getElementById('food-search-input');
    const searchValue = searchInput.value;

    fetch(URLs.searchedFoods + `?name=${searchValue}`)
        .then(res => res.json())
        .then(data => {

            searchInput.value = '';
            fillContainer(data, 'table-foods-all');
            const foods = document.querySelectorAll('.food-line');
            foods.forEach(f => f.addEventListener('click', displayBlock));
        });
};

const setUpSearch = () => {
    const searchBtn = document.getElementById('food-search-btn');
    const searchForm = document.getElementById('food-search-form');
    searchBtn.onclick = () => search();
    searchForm.onsubmit = e => {
        e.preventDefault();
        search();
    }
};

const tabSwitchConfig = (tabLinks, tabs) => {
    tabLinks.forEach(t => t.addEventListener('click', () => {
        tabLinks.forEach(c => c.classList.remove('active'));
        t.classList.add('active');
        const href = t.getAttribute('href').substr(1);
        tabs.forEach(y => {
            if (y.getAttribute('id') === href) {
                y.classList.add('active', 'show');
                switch (href) {
                    case 'foods-all':
                        return getFoods(URLs.allFoods, 'table-foods-all');
                    case 'foods-custom':
                        return getFoods(URLs.customFoods, 'table-foods-custom');
                    case 'foods-favorite':
                        return getFoods(URLs.favoriteFoods, 'table-foods-favorite');
                }
            } else {
                y.classList.remove('active', 'show');
            }
        });
    }));
};

window.onload = () => {
    setUpModal();
    const tabLinks = document.querySelectorAll('.nav-tabs a');
    const tabs = document.querySelectorAll('#nav-tab-content .tab-pane');
    tabSwitchConfig(tabLinks, tabs);
    setUpFoodRemoval();
    setUpSearch();
};
