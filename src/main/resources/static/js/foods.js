const URLs = {
    food: '/api/food',
    allFoods: '/api/foods-all',
    customFoods: '/api/foods-custom',
    favoriteFoods: '/api/foods-favorite',
    searchedFoods: '/api/foods',
    exercises: '/api/exercises',
    exercise: '/api/exercise',
};

const createRow = (f, i) => {
    if (i % 2 === 0) {
        return `<li class="unit-line even-row">${f.name}<input type="hidden" value="${f.id}"></li>`;
    } else {
        return `<li class="unit-line odd-row">${f.name}<input type="hidden" value="${f.id}"></li>`;
    }
};

const createOption = (v) => {
    return `<option value="${v.id}">${v.name}${v.name === 'g' ? '' : ` - ${v.equivalentInGrams} g`}</option>`;
};

const addFood = e => {
    e.preventDefault();
    const actionURL = e.target.getAttribute('action');

    const url = window.location.href.split("/");
    const date = url[url.length - 1].split('#')[0];

    const json = {
        quantity: document.getElementById('food-quantity').value,
        measure: document.getElementById('food-measure').value,
        date: date,
    };

    fetch(actionURL, {
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

const addExercise = e => {
    e.preventDefault();
    const actionURL = e.target.getAttribute('action');

    const nameInput = document.getElementById('exercise-name');
    const exerciseInfo = document.getElementById('exercise-info');
    const kcalBurnedInput = document.getElementById('exercise-kcal-burned');
    const kcalBurnedInfo = document.getElementById('exercise-kcal');

    const url = window.location.href.split("/");
    const date = url[url.length - 1];

    const json = {
        name: nameInput.style.display === 'none' ? exerciseInfo.innerText : nameInput.value,
        duration: document.getElementById('exercise-duration').value,
        kcalBurnedPerMin: kcalBurnedInput.style.display === 'none' ? kcalBurnedInfo.innerText : kcalBurnedInput.value,
        date: date,
    };

    fetch(actionURL, {
        method: 'post',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(json)
    })
        .then(() => {
            window.location = '/diary/' + date;
        });
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
                document.querySelector(`a[href$="${currentTab}"]`).click();
            }
        });

    return false;
};

const setWholeValToEmptyString = () => {
    document.getElementById('exercise-kcal').setAttribute('wholeVal', '');
};

const toggleSelected = (e, element) => {
    const li = e.target;

    let blockElement = null;

    if (element) {
        blockElement = document.getElementById(element);
    }

    if (li.classList.contains('selected')) {
        li.classList.remove('selected');
        if (blockElement) {
            hideElement(blockElement);
        } else {
            setWholeValToEmptyString();
        }
    } else {
        [...li.parentNode.children].forEach(li => {
            if (li.classList.contains('selected')) {
                li.classList.remove('selected');
            }
        });
        if (blockElement) {
            displayElement(blockElement);
        } else {
            setWholeValToEmptyString();
        }
        li.classList.add('selected');
    }
};

const displayFoodBlock = (e) => {
    const li = e.target;
    const foodAddBlock = document.getElementById('food-add-block');

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
                        <input type="number" name="quantity" id="food-quantity" placeholder="Amount"/>
                        <select id="food-measure"></select>
                        <button class="btn btn-info">Add</button>
                    </form>    
                </div>`;

            food.measures.forEach(m => {
                document.getElementById('food-measure').innerHTML += createOption(m);
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

const checkIfListItemIsSelected = list => {
    for (const li of list) {
        if (li.classList.contains('selected')) {
            return true;
        }
    }

    return false;
};

const displayElement = el => {
    el.style.display = 'inline-block';
};

const hideElement = el => {
    el.style.display = 'none';
};

const toggleDisplay = (el) => {
    if (el.style.display === 'none') {
        displayElement(el);
    } else {
        hideElement(el);
    }
};

const revealCustomExerciseBlock = ({nameInput, exerciseInfo, kcalBurnedInput, kcalBurnedInfo}) => {
    displayElement(nameInput);
    hideElement(exerciseInfo);
    displayElement(kcalBurnedInput);
    hideElement(kcalBurnedInfo);
};

const hideCustomExerciseBlock = ({nameInput, exerciseInfo, kcalBurnedInput, kcalBurnedInfo}) => {
    hideElement(nameInput);
    displayElement(exerciseInfo);
    hideElement(kcalBurnedInput);
    displayElement(kcalBurnedInfo);
};

const displayExerciseBlock = (e) => {
    const li = e.target;

    const elementsAsJson = {
        nameInput: document.getElementById('exercise-name'),
        exerciseInfo: document.getElementById('exercise-info'),
        kcalBurnedInput: document.getElementById('exercise-kcal-burned'),
        kcalBurnedInfo: document.getElementById('exercise-kcal'),
        duration: document.getElementById('exercise-duration'),
    };

    const isListItemSelected = checkIfListItemIsSelected([...li.parentNode.children]);

    if (isListItemSelected) {
        const exerciseId = li.children[0].value;
        hideCustomExerciseBlock(elementsAsJson);

        fetch(URLs.exercise + `/${exerciseId}`)
            .then(res => res.json())
            .then(data => {
                elementsAsJson.exerciseInfo.innerHTML = data.name;
                elementsAsJson.kcalBurnedInfo.innerHTML = `${data.kcalBurnedPerMin}`;
                elementsAsJson.duration.value = 60;
            });
    } else {
        revealCustomExerciseBlock(elementsAsJson);
        elementsAsJson.duration.value = '';
    }
};

const fillContainer = (data, table) => {
    let list = '';
    data.forEach((f, i) => {
        list += createRow(f, i);
    });

    const foodContainer = document.getElementById(table);
    foodContainer.innerHTML = list;
};

const getTableLine = () => {
    return document.querySelectorAll('.unit-line');
};

const getFoods = (URL, table) => {
    // Get the div with the post form
    const foodAddBlock = document.getElementById('food-add-block');
    foodAddBlock.style.display = 'none';

    fetch(URL)
        .then(res => res.json())
        .then(data => {

            fillContainer(data, table);
            const foods = getTableLine();
            foods.forEach(f => f.addEventListener('click', (e) => {
                toggleSelected(e, 'food-add-block');
                displayFoodBlock(e);
            }));
        });
};

const getExercises = (URL, table) => {
    fetch(URL)
        .then(res => res.json())
        .then(data => {

            fillContainer(data, table);
            const exercises = getTableLine();
            exercises.forEach(f => f.addEventListener('click', (e) => {
                toggleSelected(e, null);
                displayExerciseBlock(e);
            }));
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
    if (index) {
        window.location.href = window.location.href.substring(0, index);
    }
};

const handleDurationInput = () => {
    const durationInput = document.getElementById('exercise-duration');
    const kcalPerMinDiv = document.getElementById('exercise-kcal');


    durationInput.onkeydown = e => durationInput.setAttribute('previous', e.target.value);

    durationInput.onkeyup = e => {
        const kcalPerMin = document.getElementById('exercise-kcal').innerText;
        const previousValue = durationInput.getAttribute('previous');

        if (previousValue) {
            let currentValue = e.target.value;

            if (currentValue === '0' || currentValue === '') {
                currentValue = 1;
                durationInput.value = 1;
            }

            const ratio = +currentValue / previousValue;
            const wholeVal = kcalPerMinDiv.getAttribute('wholeVal');

            const res = wholeVal !== '' ? wholeVal * ratio : kcalPerMin * ratio;
            kcalPerMinDiv.setAttribute('wholeVal', res.toString());
            kcalPerMinDiv.innerText = (Math.round((res + Number.EPSILON) * 100) / 100).toString();
        }
    };
};

const getElementId = el => {
    return el.getAttribute('id');
};

const setUpModal = (openBtn, modal) => {
    // Get the <span> element that closes the foods
    const modalId = getElementId(modal);
    const closeBtn = document.querySelector(`#${modalId} .close-modal`);

    const idFoodModal = modalId.includes('food');

    // When the user clicks the button, open all foods
    openBtn.onclick = () => {
        modal.style.display = 'block';
        if (idFoodModal && !window.location.href.includes('#foods-all')) {
            window.location.href += '#foods-all';
            getFoods(URLs.allFoods, 'table-foods-all');
        } else {
            getExercises(URLs.exercises, 'table-exercises-all');
            const form = document.querySelector('#exercise-add-block > form');
            form.addEventListener('submit', addExercise);
            handleDurationInput();
        }
    };

    // When the user clicks on <span> (x), close the foods
    closeBtn.onclick = () => {
        modal.style.display = "none";
        if (idFoodModal) {
            removeHref();
        }
    };

    // When the user clicks anywhere outside of the foods, close it
    window.addEventListener('click', (event) => {
        if (event.target === modal) {
            modal.style.display = "none";
            if (idFoodModal) {
                removeHref();
            }
        }
    });
};

const setUpModals = () => {
    const foodModal = document.getElementById('food-modal');
    const exerciseModal = document.getElementById('exercise-modal');

    const addFoodBtn = document.getElementById('add-food-btn');
    const addExerciseBtn = document.getElementById('add-exercise-btn');

    setUpModal(addFoodBtn, foodModal);
    setUpModal(addExerciseBtn, exerciseModal);
};

const getCategoryQuery = () => {
    const foodCategoriesBlock = document.getElementById('food-categories-block');
    let category;

    if (foodCategoriesBlock.style.display === 'block') {
        category = document.getElementById('food-categories').value;
    } else {
        category = 'all';
    }

    return `&category=${category}`;
};

const search = () => {
    const searchInput = document.getElementById('food-search-input');
    const searchValue = searchInput.value;
    const currentTabWithoutHashTag = getCurrentTab().substring(1);

    let query = `?name=${searchValue}&type=${currentTabWithoutHashTag}`;
    query += `${getCategoryQuery().toLowerCase()}`;

    fetch(URLs.searchedFoods + query)
        .then(res => res.json())
        .then(data => {
            searchInput.value = '';
            fillContainer(data, `table-${currentTabWithoutHashTag}`);
            const foods = getTableLine();
            foods.forEach(f => f.addEventListener('click', (e) => {
                toggleSelected(e, 'food-add-block');
                displayFoodBlock(e);
            }));
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

const tabSwitchConfig = () => {
    const tabLinks = document.querySelectorAll('.nav-tabs a');
    const tabs = document.querySelectorAll('#nav-tab-content .tab-pane');

    tabLinks.forEach(t => t.addEventListener('click', () => {
        tabLinks.forEach(c => c.classList.remove('active'));
        t.classList.add('active');
        const href = t.getAttribute('href').substr(1);
        tabs.forEach(y => {
            const elementId = getElementId(y);
            if (elementId === href) {
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

const setUpCategorySettings = () => {
    const categorySettingsIcon = document.getElementById('food-category-settings');

    categorySettingsIcon.onclick = () => {
        const foodCategoriesBlock = document.getElementById('food-categories-block');
        toggleDisplay(foodCategoriesBlock);
    };
};

window.onload = () => {
    setUpModals();
    tabSwitchConfig();
    setUpFoodRemoval();
    setUpSearch();
    setUpCategorySettings();
};
