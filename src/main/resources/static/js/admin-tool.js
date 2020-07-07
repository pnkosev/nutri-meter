const URLs = {
    users: "/api/users",
    categories: '/api/categories',
    tags: '/api/tags',
};

const createUserRow = (u, i) => {
    const roles = u.authorities.map(u => u.authority);

    const role = roles.includes("ROLE_ADMIN")
        ? "Admin"
        : "User";

    const isAdmin = role === "Admin";

    const form =
        `<form action="/api/users/${isAdmin ? 'demote' : 'promote'}/${u.id}" method="post">
            <button class="btn btn-secondary">${isAdmin ? "Demote" : "Promote"}</button>
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
    const editForm =
        `<form action='/api/${subRoute}/edit/${x.id}' class="edit" method="post">
            <button class="btn btn-secondary">edit</button>
        </form>`;

    const deleteForm =
        `<form action='/api/${subRoute}/delete/${x.id}' data-confirm="Are you sure to delete this item?" class="delete" method="post">
            <button class="btn btn-secondary">delete</button>
        </form>`;

    const columns =
        `<td>${x.name}</td>
        <td>${editForm}</td>
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

const edit = e => {
    e.preventDefault();

    alert('edit');
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

            const editForms = document.querySelectorAll(`#${containerName} form.edit`);
            const deleteForms = document.querySelectorAll(`#${containerName} form.delete`);
            editForms.forEach(f => f.addEventListener('submit', edit));
            deleteForms.forEach(f => f.addEventListener('submit', remove));
        });
};

window.onload = () => {
    fillUsersContainer();

    const links = [...document.getElementsByTagName('a')];
    links.forEach(l => {
        if (l.href.endsWith('categories')) {
            l.addEventListener('click', () => fillCategoriesTagsContainer(URLs.categories,  'categories'));
        }

        if (l.href.endsWith('tags')) {
            l.addEventListener('click', () => fillCategoriesTagsContainer(URLs.tags,  'tags'));
        }
    });
};

