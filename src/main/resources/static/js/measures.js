const URLs = {
    addMeasure: '/api/measure/add'
};

let row = 0;

const createRow = (m) => {
    const columns =
        `<td>${++row}</td>
         <td>${m.name}</td>
         <td>${m.equivalentInGrams}</td>
         <td><button type="button" class="measure-delete-btn btn-danger">X</button></td>`;
    return `<tr>${columns}</tr>`;
};

const addHiddenInput = id => {
    return `<td><input type="hidden" name="measures" value="${id}"></td>`;
};

const deleteMeasure = (e) => {
    e.target.parentNode.parentElement.remove();
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

const handleFoodForm = () => {
    const foodForm = document.getElementById('food-add-form');
    foodForm.addEventListener('submit', e => {
        e.preventDefault();

        const measureRows = document.getElementById('measures-container').children;
        [...measureRows].forEach(mr => {
            const columns = mr.children;
            const json = {
                name: columns[1].innerHTML,
                equivalentInGrams: columns[2].innerHTML
            };

            fetch(URLs.addMeasure, {
                method: 'post',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(json)
            })
                .then(res => res.json())
                .then(m => {
                    const measuresContainer = document.getElementById('measures-container');
                    let index = columns[0].innerHTML;
                    const row = measuresContainer.children[--index];
                    row.innerHTML += addHiddenInput(m.id);
                    $(foodForm).submit();
                });
        });
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
    setUpModal();
    handleMeasureForm();
    handleFoodForm();
};