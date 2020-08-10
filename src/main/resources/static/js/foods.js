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

    delayFetchBy1Sec(() => {
        fetch(actionURL, {
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(json)
        })
            .then(checkFetchResponse)
            .then(() => {
                window.location = '/diary/' + date;
            })
            .catch(err => {
                handleError(err, 'error-container-add-food');
            });
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
        kcalBurned: kcalBurnedInput.style.display === 'none' ? kcalBurnedInfo.innerText : kcalBurnedInput.value,
        date: date,
    };

    delayFetchBy1Sec(() => {
        fetch(actionURL, {
            method: 'post',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(json)
        })
            .then(checkFetchResponse)
            .then(() => window.location = '/diary/' + date)
            .catch(err => {
                handleError(err, 'error-container-add-exercise');
            });
    });
};

const getCurrentTab = () => {
    const currentURL = window.location.href;
    const index = currentURL.indexOf('#');
    return currentURL.substr(index);
};

const getTabNameAfterDash = () => {
    return getCurrentTab().substr(1).split('-')[1];
}

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

    delayFetchBy1Sec(() => {
        fetch(URLs.favoriteFoods, {
            method: 'post',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(json)
        })
            .then(checkFetchResponse)
            .then(() => {
                const currentTab = getCurrentTab();
                if (currentTab === '#foods-favorite') {
                    document.querySelector(`a[href$="${currentTab}"]`).click();
                }
            })
            .catch(err => {
                handleError(err, 'error-container-add-food');
            });
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

    delayFetchBy1Sec(() => {
        fetch(URLs.food + `/${foodId}`)
            .then(checkFetchResponse)
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
                    <small class="error" id="error-container-add-food"></small> 
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
            })
            .catch(err => {
                const partialString = getTabNameAfterDash();
                hideElement(foodAddBlock);
                handleError(err, `error-container-add-food-${partialString}`);
            });
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

        delayFetchBy1Sec(() => {
            fetch(URLs.exercise + `/${exerciseId}`)
                .then(checkFetchResponse)
                .then(res => res.json())
                .then(data => {
                    elementsAsJson.exerciseInfo.innerHTML = data.name;
                    elementsAsJson.kcalBurnedInfo.innerHTML = `${data.kcalBurnedPerHour}`;
                    elementsAsJson.duration.value = 60;
                })
                .catch(err => handleError(err, 'error-container-add-exercise'));
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
    const foodAddBlock = document.getElementById('food-add-block');
    hideElement(foodAddBlock);

    delayFetchBy1Sec(() => {
        fetch(URL)
            .then(checkFetchResponse)
            .then(res => res.json())
            .then(data => {

                fillContainer(data, table);
                const foods = getTableLine();
                foods.forEach(f => f.addEventListener('click', (e) => {
                    toggleSelected(e, 'food-add-block');
                    displayFoodBlock(e);
                }));
            })
            .catch(err => {
                const partialString = getTabNameAfterDash();
                handleError(err, `error-container-add-food-${partialString}`);
            });
    });
};

const getExercises = (URL, table) => {
    delayFetchBy1Sec(() => {
        fetch(URL)
            .then(checkFetchResponse)
            .then(res => res.json())
            .then(data => {

                fillContainer(data, table);
                const exercises = getTableLine();
                exercises.forEach(f => f.addEventListener('click', (e) => {
                    toggleSelected(e, null);
                    displayExerciseBlock(e);
                }));
            })
            .catch(err => handleError(err, 'error-container-add-exercise'));
    });
};

const removeElement = (URL, className) => {
    [...document.getElementsByClassName(className)]
        .forEach(f => {
            f.onclick = () => {
                const associationId = f.getAttribute('association-id');
                delayFetchBy1Sec(() => {
                    fetch(URL + `/${associationId}`, {
                        method: 'delete',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                        .then(checkFetchResponse)
                        .then(() => window.location.reload())
                        .catch(err => {
                            const item = f.classList[1].split('-')[1]; // food or exercise
                            handleError(err, `error-container-${item}`);
                        });
                });
            };
        });
};

const setUpElementRemoval = () => {
    removeElement(URLs.food, 'delete-food');
    removeElement(URLs.exercise, 'delete-exercise');
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

    if (foodCategoriesBlock.style.display === 'inline-block') {
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

    delayFetchBy1Sec(() => {
        fetch(URLs.searchedFoods + query)
            .then(checkFetchResponse)
            .then(res => res.json())
            .then(data => {
                searchInput.value = '';
                fillContainer(data, `table-${currentTabWithoutHashTag}`);
                const foods = getTableLine();
                foods.forEach(f => f.addEventListener('click', (e) => {
                    toggleSelected(e, 'food-add-block');
                    displayFoodBlock(e);
                }));
            })
            .catch(err => {
                const partialString = getTabNameAfterDash();
                handleError(err, `error-container-add-food-${partialString}`);
            });
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

const progressArc = (canvas, span, p) => {
    const can = document.getElementById(canvas);
    const ctx = can.getContext('2d');
    const spanPercent = document.getElementById(span);

    const posX = can.width / 2;
    const posY = can.height / 2;
    const fps = 1000 / 200;
    let percent = 0;
    const onePercent = 360 / 100;
    const result = onePercent * p;
    can.lineCap = 'round';

    arcMove();

    function arcMove() {

        let degrees = 0;
        const acrInterval = setInterval(function () {
            degrees += 1;
            ctx.clearRect(0, 0, can.width, can.height);
            percent = degrees / onePercent;

            spanPercent.innerHTML = percent.toFixed();

            ctx.beginPath();
            ctx.arc(posX, posY, 80, (Math.PI / 180) * 270, (Math.PI / 180) * (270 + 360));
            ctx.strokeStyle = '#b1b1b1';
            ctx.lineWidth = 20;
            ctx.stroke();

            ctx.beginPath();
            ctx.strokeStyle = '#3949AB';
            ctx.lineWidth = 20;
            ctx.arc(posX, posY, 80, (Math.PI / 180) * 270, (Math.PI / 180) * (270 + degrees));
            ctx.stroke();
            if (degrees >= result) {
                clearInterval(acrInterval);
            }
        }, fps);
    }
};

const progressBar = (canvas) => {
    return {
        ctx: document.getElementById(canvas).getContext('2d'),
        display: function (p, color, text) {
            this.ctx.fillStyle = '#444444';
            this.ctx.fillRect(0, 0, this.ctx.canvas.width, this.ctx.canvas.height);
            this.ctx.fillStyle = color;
            this.ctx.fillRect(0, 0, p * this.ctx.canvas.width / 100, this.ctx.canvas.height);
            this.ctx.font = "2.5rem Verdana";
            this.ctx.fillStyle = 'white';
            this.ctx.textAlign = 'center';
            this.ctx.fillText(text, this.ctx.canvas.width / 2, this.ctx.canvas.height / 1.75);
        },
    };
};

const distributedProgressBar = (canvas) => {
    return {
        ctx: document.getElementById(canvas).getContext('2d'),
        display: function ({bmrPercentage, activityLevelPercentage, exercisePercentage}, {c1, c2, c3}) {
            const rects = [
                {
                    x: 0, y: 0, w: bmrPercentage * this.ctx.canvas.width / 100, h: this.ctx.canvas.height
                },
                {
                    x: bmrPercentage * this.ctx.canvas.width / 100,
                    y: 0,
                    w: activityLevelPercentage * this.ctx.canvas.width / 100,
                    h: this.ctx.canvas.height
                },
                {
                    x: bmrPercentage * this.ctx.canvas.width / 100 + activityLevelPercentage * this.ctx.canvas.width / 100,
                    y: 0,
                    w: exercisePercentage * this.ctx.canvas.width / 100,
                    h: this.ctx.canvas.height
                },
            ];
            let i = 0;

            const colors = [c1, c2, c3];

            while (i < rects.length) {
                let r = rects[i];
                this.ctx.fillStyle = colors[i++];
                this.ctx.fillRect(r.x, r.y, r.w, r.h);
            }

            const can = document.getElementById(canvas);
            const c = can.getContext('2d');

            const spans = [...document.getElementsByClassName('absolute-block')];

            can.onmousemove = function (e) {
                // important: correct mouse position:
                const rect = this.getBoundingClientRect();
                const x = e.clientX - rect.left;
                const y = e.clientY - rect.top;
                let i = 0;

                while (i < rects.length) {
                    // add a single rect to path:
                    let r = rects[i++];
                    c.beginPath();
                    c.rect(r.x, r.y, r.w, r.h);

                    // check if we hover it, display element, if not hide it
                    if (c.isPointInPath(x, y)) {
                        displayElement(spans[i - 1]);
                    } else {
                        hideElement(spans[i - 1]);
                    }
                }
            };

            can.onmouseleave = function (e) {
                i = 0;
                while (i < rects.length) {
                    hideElement(spans[i++]);
                }
            }
        },
    };
};

const getKcalFromElement = el => {
    let innerText = el.innerText;
    let index = innerText.indexOf(' / ');
    if (index > -1) {
        innerText = innerText.substring(0, index);
    } else {
        index = innerText.indexOf(' kcal');
        if (index > -1) {
            innerText = innerText.substring(0, index);
        }
    }
    return innerText;
};

const setKcalConsumedProgressBar = () => {
    const kcalConsumed = document.getElementById('kcal-consumed').innerText;
    const kcalBudget = document.getElementById('kcal-burned').innerText;
    const percentage = kcalConsumed / kcalBudget * 100;
    const text = `${kcalConsumed} / ${kcalBudget}`;
    progressBar('kcal-consumed-progress-bar').display(percentage, '#30acf0', text);
    progressArc('kcal-consumed-progress-arc', 'percent', percentage);
};

const getDecimalPart = num => {
    return num % 1;
};

const increaseBiggestNum = nums => {
    const objClone = {...nums};

    let n = 0;
    let temp = 0;
    let index;

    for (let key in objClone) {
        temp = getDecimalPart(objClone[key]);
        if (n < temp) {
            n = temp;
            index = key;
        }
    }
    objClone[index] += 1;

    return objClone;
};

const setKcalBurnedProgressBar = () => {
    const kcalConsumed = document.getElementById('kcal-burned').innerText;
    const kcalFromActivityLevel = getKcalFromElement(document.getElementById('kcal-activity-level'));
    const kcalFromBmr = getKcalFromElement(document.getElementById('kcal-bmr'));
    const kcalFromExercise = getKcalFromElement(document.getElementById('kcal-exercise'));
    const bmrPercentage = kcalFromBmr / kcalConsumed * 100;
    const activityLevelPercentage = kcalFromActivityLevel / kcalConsumed * 100;
    const exercisePercentage = kcalFromExercise / kcalConsumed * 100;

    let percentages = {
        bmrPercentage,
        activityLevelPercentage,
        exercisePercentage
    };
    let adjustedPercentages;
    if (parseFloat(bmrPercentage.toFixed()) + parseFloat(activityLevelPercentage.toFixed()) + parseFloat(exercisePercentage.toFixed()) < 100) {
        adjustedPercentages = increaseBiggestNum(percentages);
    }
    document.getElementById('kcal-bmr-percents')
        .innerText = `${adjustedPercentages
        ? adjustedPercentages.bmrPercentage.toFixed()
        : percentages.bmrPercentage.toFixed()}%`;
    document.getElementById('kcal-activity-percents')
        .innerText = `${adjustedPercentages
        ? adjustedPercentages.activityLevelPercentage.toFixed()
        : percentages.activityLevelPercentage.toFixed()}%`;
    document.getElementById('kcal-exercise-percents')
        .innerText = `${adjustedPercentages
        ? adjustedPercentages.exercisePercentage.toFixed()
        : percentages.exercisePercentage.toFixed()}%`;
    const colorJson = {
        c1: '#f0a24d',
        c2: '#38c791',
        c3: '#f36381',
    };
    distributedProgressBar('kcal-burned-progress-bar').display(percentages, colorJson);
};

const setKcalBudgetProgressBar = () => {
    const kcalBudget = document.getElementById('kcal-budget').innerText;
    progressBar('kcal-budget-progress-bar').display(kcalBudget, '#c54ef0', kcalBudget);
};

const setCanvas = () => {
    setKcalConsumedProgressBar();
    setKcalBurnedProgressBar();
    setKcalBudgetProgressBar();
};

window.onload = () => {
    setUpModals();
    tabSwitchConfig();
    setUpElementRemoval();
    setUpSearch();
    setUpCategorySettings();
    setCanvas();
};
