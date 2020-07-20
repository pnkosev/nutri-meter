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

const createRow = (m) => {
    const columns =
        `<td>${++row}</td>
         <td>${m.name}</td>
         <td>${m.equivalentInGrams}</td>
         <td class="text-right"><button type="button" class="measure-delete-btn btn-danger">X</button></td>`;
    return `<tr>${columns}</tr>`;
};

const addHiddenInput = id => {
    return `<td class="hidden"><input type="hidden" name="measures" value="${id}"></td>`;
};

const deleteMeasure = (e) => {
    const id = e.target.parentElement.parentElement.children[4]?.children[0].value;
    if (id) {
        fetch(URLs.deleteMeasure, {
            method: 'post',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({id})
        })
            .then();
    }
    e.target.parentNode.parentElement.remove();
    row--;
};

const handleMeasureForm = () => {
    const form = document.getElementById('measure-add-form');
    form.addEventListener('submit', e => {
        e.preventDefault();

        const name = document.getElementById('measure').value;
        const equivalentInGrams = document.getElementById('grams').value;

        const json = {
            name,
            equivalentInGrams
        };

        const measuresContainer = document.getElementById('measures-container');
        measuresContainer.innerHTML += createRow(json);
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
                if (input.getAttribute('name') !== 'measures'){
                    input.setAttribute('name', 'measures');
                }
            }
        });
};


const handleFoodForm = () => {
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

    fetch(URLs.addMeasure, {
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(jsonArray)
    })
        .then(res => res.json())
        .then(arr => {
            let index = 0;
            measureRows.forEach(r => {
                r.innerHTML += addHiddenInput(arr[index++].id);
            });
        })
        .finally(() => {
            foodForm.submit();
        });
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

window.onload = () => {
    getInitialRowCount();
    setUpModal();
    handleMeasureForm();
    const btn = document.querySelector('#food-add-form > div.button-holder > button');
    btn.addEventListener('click', handleFoodForm);
    [...document.getElementsByClassName('measure-delete-btn')].forEach(e => e.addEventListener('click', deleteMeasure));
    handleHiddenInputs();
};