const div = document.getElementById("resultDiv");
const body = document.getElementById("body");
const submitButton = document.getElementById("submitButton");
const url = window.location.href;
body.addEventListener("click", (event) => {

    if (submitButton.contains(event.target)) {


        const warehouse = {
            height: document.getElementById("height").value,
            width: document.getElementById("width").value,
            rackHeight: document.getElementById("rackHeight").value,
            rackWidth: document.getElementById("rackWidth").value
        }
        event.preventDefault()
        if (validateForm()) {
            fetch(url, {
                method: "POST", // You can change this to "GET" or other HTTP methods
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(warehouse)
            })
                .then(async response => {
                    if (response.ok) {
                        return response.text(); // or response.json() if the server returns JSON
                    } else {
                        throw new Error(await response.text());
                    }
                })
                .then(() => {
                    location.replace(url.substring(0, (url.lastIndexOf('/'))));
                })
                .catch(error => {
                    div.innerHTML = JSON.parse(error.message).message;
                });
        }

    }

});


function validateForm() {


    const height = parseInt(document.getElementById('height').value, 10);
    const width = parseInt(document.getElementById('width').value, 10);
    const rackHeight = parseInt(document.getElementById('rackHeight').value, 10);
    const rackWidth = parseInt(document.getElementById('rackWidth').value, 10);

    let isGood = true;

    if (!isPositiveInteger(height)) {
        div.textContent = 'Please enter positive integer for height.';
        document.getElementById('height').classList.add("border-danger")
        isGood = false;
    } else {
        document.getElementById('height').classList.remove("border-danger");
    }

    if (!isPositiveInteger(width)) {
        div.textContent = 'Please enter positive integer for width.';
        document.getElementById('width').classList.add("border-danger")
        isGood = false;
    } else {
        document.getElementById('width').classList.remove("border-danger");
    }

    if (!isPositiveInteger(rackHeight)) {
        div.textContent = 'Please enter positive integer for rack height.';
        document.getElementById('rackHeight').classList.add("border-danger")
        isGood = false;
    } else {
        document.getElementById('rackHeight').classList.remove("border-danger");
    }

    if (!isPositiveInteger(rackWidth)) {
        div.textContent = 'Please enter positive integer for rack width.';
        document.getElementById('rackWidth').classList.add("border-danger")
        isGood = false;
    } else {
        document.getElementById('rackWidth').classList.remove("border-danger");
    }

    if (rackHeight > height * 0.5) {
        div.textContent = 'Rack height cannot exceed 50% of warehouse height.';
        document.getElementById('rackHeight').classList.add("border-danger")
        isGood = false;
    } else {
        document.getElementById('rackHeight').classList.remove("border-danger");
    }

    if (rackWidth > width * 0.5) {
        div.textContent = 'Rack width cannot exceed 50% of warehouse width.';
        document.getElementById('rackWidth').classList.add("border-danger")
        isGood = false;
    } else {
        document.getElementById('rackWidth').classList.remove("border-danger");
    }

    return isGood;
}

function isPositiveInteger(value) {
    return /^\d+$/.test(value) && parseInt(value, 10) > 0;
}