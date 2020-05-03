const URLs = {
    allFoods: '/api/foods-all',
    customFoods: '/api/foods-custom'
};

const createRow = i => {
    let columns =
        `<td>${i.name}</td>
        <td>
            <form action=/api/food/${i.id} method="post">
                <input type="number" name="quantity" placeholder="Amount"/>
                <button class="btn btn-info">Add</button>
            </form>
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

const getFoods = (URL, table) => {
    fetch(URL)
        .then(res => res.json())
        .then(data => {
            let list = '';
            data.forEach(f => {
                list += createRow(f);
            });

            const foodContainer = document.getElementById(table);
            foodContainer.innerHTML = list;

            const forms = document.querySelectorAll('form');
            forms.forEach(f => f.addEventListener('submit', addFood));
        });
};

const setUpModal = () => {
    // Get the foods
    const modal = document.getElementById("myModal");

// Get the button that opens the foods
    const btn = document.getElementById("myBtn");

// Get the <span> element that closes the foods
    const span = document.getElementsByClassName("close")[0];

// When the user clicks the button, open the foods
    btn.onclick = function () {
        modal.style.display = "block";

        const navAllFoods = document.getElementById("nav-foods-all-tab");
        const navCustomFoods = document.getElementById("nav-foods-custom-tab");

        navAllFoods.addEventListener('click', () => getFoods(URLs.allFoods, 'table-foods-all'));
        navCustomFoods.addEventListener('click', () => getFoods(URLs.customFoods, 'table-foods-custom'));

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

const foods = document.getElementById("myModal");

window.onload = () => {
    setUpModal();
};
