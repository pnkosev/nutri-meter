const checkFetchResponse = (res) => {
    if (!res.ok) {
        throw res; // originally 'throw new Error(res.statusText)', but statusText is always empty string
    }
    return res;
};

const handleError = (err, containerId = 'error-container', callBackFunc = showErrorMessage) => {
    if (typeof err.text === 'function') {
        err.text()
            .then(msg => callBackFunc(msg, containerId));
    } else {
        // in this case the server is down
        // so the msg should really be 'Server is temporary unavailable' or something
        return callBackFunc(err, containerId);
    }
};

const showErrorMessage = (msg, containerId) => {
    const container = document.getElementById(containerId);
    container.innerText = msg;
    setTimeout(() => {
        container.innerText = '';
    }, 3000);
    // $.notify(msg, "error");
    // library for pop-ups. If interested- add in head the following link:
    // <script src="https://cdnjs.cloudflare.com/ajax/libs/notify/0.4.2/notify.min.js"></script>
};