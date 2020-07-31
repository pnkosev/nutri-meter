const URLs = {
    users: '/api/user/all',
    categories: '/api/category/all',
    tags: '/api/tag/all',
    search: '/api/users',
};

const createUserRow = (u, i) => {
    const roles = u.authorities.map(u => u.authority);

    const role = roles.includes("ROLE_ADMIN")
        ? "Admin"
        : "User";

    const isAdmin = role === "Admin";

    const form =
        `<form action="/api/user/${isAdmin ? 'demote' : 'promote'}/${u.id}" method="post">
            <button class="btn ${u.authorities.length !== 1 ? 'btn-warning' : 'btn-success'}">${isAdmin ? "Demote" : "Promote"}</button>
        </form>`;

    const columns =
        `<td>${i + 1}</td>
        <td>${u.username}</td>
        <td>${u.email}</td>
        <td>${role}</td>
        <td>${form}</td>`;
    return `<tr>${columns}</tr>`;
};

const createCategoryTagRow = (x, subRoute) => {
    subRoute = getSingularFromPlural(subRoute);
    const editBtn = `<a href='/food/${subRoute}/edit/${x.id}' type="button" class="btn btn-info">edit</a>`;

    const deleteForm =
        `<form action='/api/${subRoute}/delete/${x.id}' data-confirm="Are you sure to delete this item?" class="delete" method="post">
            <button class="btn btn-danger">delete</button>
        </form>`;

    const columns =
        `<td>${x.name}</td>
        <td>${editBtn}</td>
        <td>${deleteForm}</td>`;
    return `<tr>${columns}</tr>`;
};

const mote = e => {
    e.preventDefault();

    const URL = e.target.getAttribute('action');

    fetch(URL, {method: 'post'})
        .then(() => {
            window.location.reload();
        });

    return false;
};

const remove = e => {
    e.preventDefault();

    if (confirm(e.target.getAttribute('data-confirm'))) {
        const URL = e.target.getAttribute('action');

        fetch(URL, {method: 'post'})
            .then(() => {
                document.querySelector('a.active').click();
            });
    }

    return false;
};

const getSingularFromPlural = str => {
    const indexOfDash = str.indexOf('-');
    if (indexOfDash) {
        str = str.substring(0, indexOfDash);
    }

    if (str.endsWith('ies')) {
        str = str.substr(0, str.length - 3) + 'y';
    } else {
        str = str.substr(0, str.length - 1);
    }

    return str;
};

const getUserList = data => {
    let list = '';
    data.forEach((u, i) => {
        list += createUserRow(u, i);
    });
    return list;
};

const getCategoryTagList = (data, subRoute) => {
    let list = '';
    data.forEach(x => {
        list += createCategoryTagRow(x, subRoute);
    });
    return list;
};

const fillContainer = (list, containerName) => {
    const container = document.getElementById(containerName);
    container.innerHTML = list !== '' ? `${list}` : `<tr><td>Empty</td><td></td><td></td></tr>`;
};

const fillUserContainer = (data, containerName) => {
    fillContainer(getUserList(data), containerName);
};

const fillCategoryTagContainer = (data, containerName) => {
    fillContainer(getCategoryTagList(data, containerName), containerName);
};

const fetchUsers = () => {
    fetch(URLs.users)
        .then(res => res.json())
        .then(data => {

            fillUserContainer(data, 'users-container');
            const forms = document.querySelectorAll('#users-container form');
            forms.forEach(f => f.addEventListener('submit', mote));
        });
};

const fetchCategoriesTags = (url, containerName) => {
    fetch(url)
        .then(res => res.json())
        .then(data => {

            fillCategoryTagContainer(data, `${containerName}-container`);
            const deleteForms = document.querySelectorAll(`#${containerName} form.delete`);
            deleteForms.forEach(f => f.addEventListener('submit', remove));
        });
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
                    case 'all-users':
                        return fetchUsers();
                    case 'categories':
                        return fetchCategoriesTags(URLs.categories, 'categories');
                    case 'tags':
                        return fetchCategoriesTags(URLs.tags, 'tags');
                }
            } else {
                y.classList.remove('active', 'show');
            }
        });
    }));

    // SAME AS THE ABOVE EXCEPT FOR THE CONTAINER FILLING
    // $('.nav-tabs a').click(function (e) {
    //     // No e.preventDefault() here  // <<<<<< THIS IS BY DEFAULT FROM $
    //     $(this).tab('show');
    // });
};

const refreshSetup = (tabLinks, tabs) => {
    const currentURL = window.location.href;
    const index = currentURL.indexOf('#');
    const currentTab = currentURL.substr(index + 1);
    const allUsers = 'all-users';
    const categories = 'categories';
    const tags = 'tags';

    tabLinks.forEach(l => {
        l.classList.remove('active');
        if (l.getAttribute('href').endsWith(currentTab)) {
            l.classList.add('active');
        }
    });

    tabs.forEach(c => c.classList.remove('active', 'show'));

    switch (currentTab) {
        case allUsers: {
            let div = document.getElementById(allUsers);
            div.classList.add('active', 'show');
            return fetchUsers();
        }
        case categories: {
            let div = document.getElementById(categories);
            div.classList.add('active', 'show');
            return fetchCategoriesTags(URLs.categories, categories);
        }
        case tags: {
            let div = document.getElementById(tags);
            div.classList.add('active', 'show');
            return fetchCategoriesTags(URLs.tags, tags);
        }
    }
};

const search = () => {
    const searchInput = document.getElementById('user-search-input');
    const searchValue = searchInput.value;

    if (searchValue) {
        fetch(URLs.search + `?username=${searchValue}`)
            .then(res => res.json())
            .then(data => {

                fillUserContainer(data,"users-container" );
                searchInput.value = '';
                const forms = document.querySelectorAll('#users-container form');
                forms.forEach(f => f.addEventListener('submit', mote));
            });
    }
};

const searchSetUp = () => {
    const searchBtn = document.getElementById('user-search-btn');
    const searchForm = document.getElementById('user-search-form');

    searchBtn.onclick = () => search();

    searchForm.onsubmit = e => {
        e.preventDefault();
        search();
    };
};

window.onload = () => {
    const adminToolLink = document.querySelector('#navbarNav [href$="#all-users"]');
    adminToolLink.addEventListener('click', () => window.location.reload());
    const tabLinks = document.querySelectorAll('.nav-tabs a');
    const tabs = document.querySelectorAll('.container .tab-pane');
    refreshSetup(tabLinks, tabs);
    tabSwitchConfig(tabLinks, tabs);
    searchSetUp();
};

