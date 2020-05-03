const URLs = {
    users: "/api/all-users",
};

const createRow = (u, i) => {
    const roles = u.authorities.map(u => u.authority);

    const role = roles.includes("ROLE_ADMIN")
        ? "Admin"
        : "User";

    const isAdmin = role === "Admin";

    const form =
        `<form action="/api/${isAdmin ? 'demote' : 'promote'}/${u.id}" method="post">
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

const mote = e => {
    e.preventDefault();

    const URL = e.target.getAttribute('action');

    fetch(URL, {method: 'post'})
        .then(() => {
            window.location = '/user/all';
        });

    return false;
};

const fillUsersContainer = () => {
    fetch(URLs.users)
        .then(res => res.json())
        .then(data => {
            let list = '';
            data.forEach((u, i) => {
                list += createRow(u, i);
            });

            const container = document.getElementById("users-container");
            container.innerHTML = `${list}`;

            const forms = document.querySelectorAll('form');
            forms.forEach(f => f.addEventListener('submit', mote));
        });
};

window.onload = () => {
    fillUsersContainer();
};

