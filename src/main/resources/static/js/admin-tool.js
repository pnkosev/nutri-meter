const URLs = {
    users: "/api/user/all",
    categories: '/api/category/all',
    tags: '/api/tag/all',
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
    if (str.endsWith('ies')) {
        str = str.substr(0, str.length - 3) + 'y';
    } else {
        str = str.substr(0, str.length - 1);
    }

    return str;
};

const fillUsersContainer = () => {
    fetch(URLs.users)
        .then(res => res.json())
        .then(data => {
            let list = '';
            data.forEach((u, i) => {
                list += createUserRow(u, i);
            });

            const container = document.getElementById("users-container");
            container.innerHTML = `${list}`;

            const forms = document.querySelectorAll('#all-users form');
            forms.forEach(f => f.addEventListener('submit', mote));
        });
};

const fillCategoriesTagsContainer = (url, containerName) => {
    fetch(url)
        .then(res => res.json())
        .then(data => {
            let list = '';
            data.forEach(x => {
                list += createCategoryTagRow(x, containerName);
            });

            const container = document.querySelector(`#${containerName}-container`);
            container.innerHTML = list !== '' ? `${list}` : `<tr><td>Empty</td><td></td><td></td></tr>`;

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
                        return fillUsersContainer();
                    case 'categories':
                        return fillCategoriesTagsContainer(URLs.categories, 'categories');
                    case 'tags':
                        return fillCategoriesTagsContainer(URLs.tags, 'tags');
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
            return fillUsersContainer();
        }
        case categories: {
            let div = document.getElementById(categories);
            div.classList.add('active', 'show');
            return fillCategoriesTagsContainer(URLs.categories, categories);
        }
        case tags: {
            let div = document.getElementById(tags);
            div.classList.add('active', 'show');
            return fillCategoriesTagsContainer(URLs.tags, tags);
        }
    }
};

window.onload = () => {
    const tabLinks = document.querySelectorAll('.nav-tabs a');
    const tabs = document.querySelectorAll('.container .tab-pane');
    refreshSetup(tabLinks, tabs);
    tabSwitchConfig(tabLinks, tabs);
};

