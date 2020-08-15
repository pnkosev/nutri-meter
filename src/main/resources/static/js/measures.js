const URLs = {
    addMeasure: '/api/measure/add',
    deleteMeasure: '/api/measure/delete'
};

let row = 0;

const getInitialRowCount = () => {
    const numberOfRows = document.getElementById('measures-container').children.length;

    if (numberOfRows > 0) {
        row = numberOfRows;
    }
};

const createRow = ({name, equivalentInGrams}) => {
    const columns =
        `<td>${++row}</td>
         <td>${name}</td>
         <td>${equivalentInGrams}</td>
         <td class="text-right"><button type="button" class="measure-delete-btn btn-danger">X</button></td>`;
    return `<tr>${columns}</tr>`;
};

const addHiddenInput = id => {
    return `<td class="hidden"><input type="hidden" name="measures" value="${id}"></td>`;
};

const deleteMeasure = (e) => {
    const id = e.target.parentElement.parentElement.children[4]?.children[0].value;

    if (id) {
        delayFetchBy1Sec(() => {
            fetch(URLs.deleteMeasure, {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({id})
            })
                .then(checkFetchResponse)
                .catch(handleError);
        });
    }
    e.target.parentNode.parentElement.remove();
    row--;
};

const addOptionToSelect = ({name, equivalentInGrams}) => {
    const select = document.getElementById('measures');
    const option = document.createElement('option');
    option.value = `${name} - ${equivalentInGrams}`;
    option.innerHTML = `${name} - ${equivalentInGrams}g`;
    select.append(option);
};

const handleMeasureForm = () => {
    const form = document.getElementById('measure-add-form');
    form.addEventListener('submit', e => {
        e.preventDefault();

        const measure = {
            name: document.getElementById('measure').value,
            equivalentInGrams: document.getElementById('grams').value,
        };

        if (!validator.isDigit(measure.equivalentInGrams, 4)) {
            showErrorMessage(
                'Equivalent in grams must be between 1 and 4 digits!',
                'error-container-measure'
            );
            return;
        }

        const measuresContainer = document.getElementById('measures-container');
        measuresContainer.innerHTML += createRow(measure);

        addOptionToSelect(measure);

        form.reset();
        document.getElementById('my-modal').style.display = "none";
        [...document.getElementsByClassName('measure-delete-btn')].forEach(e => e.addEventListener('click', deleteMeasure));
    });
};


const handleHiddenInputs = () => {
    [...document.getElementById('measures-container').children]
        .forEach((r, i) => {
            const cols = r.children;
            if (cols.length === 5) {
                const input = cols[4].children[0];
                if (input.getAttribute('name') !== 'measures') {
                    input.setAttribute('name', 'measures');
                }
            }
        });
};


const handleFoodForm = e => {
    e.preventDefault();
    const foodForm = document.getElementById('food-add-form');
    const measureRows = [...document.getElementById('measures-container').children];
    let jsonArray = [];
    measureRows.forEach(mr => {
        const columns = mr.children;

        if (columns.length > 4) {
            return;
        }

        const json = {
            name: columns[1].innerHTML,
            equivalentInGrams: columns[2].innerHTML
        };

        jsonArray.push(json);
    });

    if (jsonArray.length > 0) {
        delayFetchBy1Sec(() => {
            fetch(URLs.addMeasure, {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(jsonArray)
            })
                .then(checkFetchResponse)
                .then(res => res.json())
                .then(arr => {
                    let index = 0;
                    measureRows
                        .slice(measureRows.length - jsonArray.length) // slice from the first row without hidden input
                        .forEach(r => {
                            r.innerHTML += addHiddenInput(arr[index++].id);
                        });
                    foodForm.submit();
                })
                .catch(handleError);
        });
    } else {
        foodForm.submit();
    }
};

const setUpSelect = () => {
    const select = document.getElementById('measures');
    select.setAttribute('prev', select.value);
    select.onchange = e => {
        const quantity = document.getElementById('nutrients-in');
        const nextValue = e.target.value;
        recalculateNutrients(quantity.value, select.getAttribute('prev'), nextValue);
        select.setAttribute('prev', nextValue);
        quantity.value = 1;
    };
};

const setUpNutrientInput = () => {
    const nutrientInput = document.getElementById('nutrients-in');

    nutrientInput.onkeydown = e => {
        let previousValue = e.target.value;

        const indexOfFirstNonDigit = previousValue.match('[^\\d]+')?.index;
        if (indexOfFirstNonDigit) {
            previousValue = previousValue.substring(0, index);
        }

        nutrientInput.setAttribute('previous', previousValue);
    };

    nutrientInput.onkeyup = e => {
        const previousValue = nutrientInput.getAttribute('previous');
        if (previousValue) {
            let currentValue = e.target.value;
            if (currentValue === '0' || currentValue === '') {
                currentValue = 1;
                nutrientInput.value = 1;
            }

            // DOES NOT WORK IF TYPING TOO FAST
            // if (isNaN(currentValue.substring(currentValue.length - 1))) {
            //     currentValue = currentValue.substring(0, currentValue.length - 1);
            //     nutrientInput.value = currentValue;
            // }

            const indexOfFirstNonDigit = currentValue.match('[^\\d]+')?.index;
            if (indexOfFirstNonDigit) {
                currentValue = currentValue.substring(0, index);
                nutrientInput.value = currentValue;
            }

            const ratio = previousValue / +currentValue;

            const inputList = [...document.querySelectorAll('.all-nutrients input')];

            inputList.forEach(i => {
                if (i.value) {
                    const res = i.getAttribute('wholeVal') ? i.getAttribute('wholeVal') / ratio : i.value / ratio;
                    i.setAttribute('wholeVal', res);
                    i.value = Math.round((res + Number.EPSILON) * 100) / 100;
                }
            });
        }
    };
};

const setUpModal = () => {
    const modal = document.getElementById('my-modal');

    const btn = document.getElementById('measure-add-btn');

    // Get the <span> element that closes the foods
    const closeBtn = document.getElementsByClassName('close')[0];

    // When the user clicks the button, open the foods
    btn.onclick = function () {
        modal.style.display = 'block';
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

const recalculateNutrients = (quantity, previousMeasure, currentMeasure) => {
    let previousMeasureEquivalentInGrams = 1;
    if (previousMeasure.includes(' - ')) {
        const previousMeasureData = previousMeasure.split(' - ');
        previousMeasureEquivalentInGrams = previousMeasureData[1];
    }
    const previousAmount = +quantity * +previousMeasureEquivalentInGrams;

    let currentMeasureEquivalentInGrams = 1;
    if (currentMeasure.includes(' - ')) {
        const currentMeasureData = currentMeasure.split(' - ');
        currentMeasureEquivalentInGrams = currentMeasureData[1];
    }

    const currentAmount = +currentMeasureEquivalentInGrams;

    const ratio = currentAmount / previousAmount;

    const inputList = [...document.querySelectorAll('.all-nutrients input')];

    inputList.forEach(i => {
        if (i.value) {
            const res = i.getAttribute('wholeVal') ? i.getAttribute('wholeVal') * ratio : i.value * ratio;
            i.setAttribute('wholeVal', res);
            i.value = Math.round((res + Number.EPSILON) * 100) / 100;
        }
    });
};

window.onload = () => {
    getInitialRowCount();
    setUpModal();
    setUpSelect();
    setUpNutrientInput();
    handleMeasureForm();
    const foodForm = document.getElementById('food-add-form');
    foodForm.onsubmit = e => handleFoodForm(e);
    [...document.getElementsByClassName('measure-delete-btn')].forEach(e => e.addEventListener('click', deleteMeasure));
    handleHiddenInputs();
};