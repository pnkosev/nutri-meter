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

const isJsonString = (str) => {
    try {
        JSON.parse(str);
    } catch (e) {
        return false;
    }
    return true;
};

const extractValuesFromJson = (json) => {
    return Object.values(json).join('\n');
    // return Object.keys(json)
    //     .map(k => `${json[k]}`)
    //     .join('\n');
};

const showErrorMessage = (msg, containerId) => {
    isJsonString(msg) ? msg = extractValuesFromJson(JSON.parse(msg)) : msg;
    const container = document.getElementById(containerId);
    container.innerText = msg;
    setTimeout(() => {
        container.innerText = '';
    }, 3000);

    // library for pop-ups. If interested- add in head the following links:
    // <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
    // <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
    // Toastify({
    //     text: msg,
    //     duration: 3000,
    //     close: true,
    //     gravity: "top", // `top` or `bottom`
    //     position: 'right', // `left`, `center` or `right`
    //     backgroundColor: "linear-gradient(to right, #00b09b, #96c93d)",
    // }).showToast();

    // library for pop-ups. If interested- add in head the following link:
    // <script src="https://cdnjs.cloudflare.com/ajax/libs/notify/0.4.2/notify.min.js"></script>
    // $.notify(msg, "error");
};

