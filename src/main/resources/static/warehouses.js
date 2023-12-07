// Get the form data from an HTML form with id "myForm"
const body = document.getElementById("body");
const getFormButton = document.getElementById("getForm");

const div = document.getElementById("resultDiv");
const content = document.getElementById("content");
const deleteButtons = document.getElementsByClassName("delete-button");
const settingsButtons = document.getElementsByClassName("update-button");
let isFormShows = false;
const updateButtons = [];

// URL where you want to send the request
const url = "/api/v1/warehouses";

body.addEventListener("click", (event) => {

    if (getFormButton.contains(event.target)) {
        const table = document.getElementById("tableBody");
        table.innerHTML += "<tr>\n" +
            "                <td class=\"align-middle h-100\">\n" +
            "                    <input type=\"text\" placeholder=\"Name\" id=\"warehouseNameInput\" class=\"form-control\">\n" +
            "                </td>\n" +
            "                <td class=\"align-middle h-100\">\n" +
            "                    <input type=\"text\" placeholder=\"Address\" id=\"warehouseAddressInput\" class=\"form-control\">\n" +
            "                </td>\n" +
            "                <td class=\"align-middle\">\n" +
            "                    <div class=\"text-center d-flex justify-content-around align-middle h-100\">\n" +
            "                    <button class=\"btn btn-sm btn-outline-danger mx-1\" id=\"cansel\">Cansel</button>\n" +
            "                    <button type=\"submit\" class=\"btn btn-sm btn-outline-warning mx-1\" id=\"submitButton\">Add</button>\n" +
            "                    </div>\n" +
            "                </td>\n" +
            "            </tr>";
        getFormButton.remove();
        isFormShows = true;
    }
    if (isFormShows) {
        const canselButton = document.getElementById("cansel");
        const submitButton = document.getElementById("submitButton");
        if (canselButton.contains(event.target)) {
            canselButton.parentNode.parentNode.parentNode.remove();
            isFormShows = false;
            content.appendChild(getFormButton);
        }

        if (submitButton.contains(event.target)) {
            const warehouse = {
                name: document.getElementById("warehouseNameInput").value.trim(),
                address: document.getElementById("warehouseAddressInput").value.trim(),
            }
            event.preventDefault()
            if (validateInput(document.getElementById("warehouseNameInput"), document.getElementById("warehouseAddressInput"))) {
                // Create a fetch request
                canselButton.parentNode.parentNode.parentNode.remove();
                content.appendChild(getFormButton);
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
                        location.reload();
                    })
                    .catch(error => {
                        div.innerHTML = error.message;
                    });
            }

        }

    }

    for (i = 0; i < deleteButtons.length; i++) {
        if (deleteButtons[i].contains(event.target)) {
            const warehouse_id = parseInt(deleteButtons[i].id.replace(/\D/g, ''), 10)
            const userConfirmed = window.confirm("Are you sure you want to delete " + deleteButtons[i].parentNode.parentNode.parentNode.childNodes.item(1).textContent + "?");
            if (userConfirmed) {
                fetch(url + '/' + warehouse_id, {
                    method: "DELETE" // You can change this to "GET" or other HTTP methods
                })
                    .then(async response => {
                        if (response.ok) {
                            return response.text(); // or response.json() if the server returns JSON
                        } else {
                            throw new Error(await response.text());
                        }
                    })
                    .then(() => {
                        location.reload()
                    })
                    .catch(error => {
                        div.innerHTML = "Error: " + error;
                    });
            }

        }
    }

    for (i = 0; i < settingsButtons.length; i++) {
        if (settingsButtons[i].contains(event.target)) {
            event.preventDefault();
            const warehouse_id = parseInt(settingsButtons[i].id.replace(/\D/g, ''), 10);
            const settingsButton = settingsButtons[i];
            const row = settingsButton.parentNode.parentNode.parentNode;
            const name = row.childNodes.item(1).textContent;
            const address = row.childNodes.item(3).textContent;
            row.innerHTML = "<tr>\n" +
                "                <td class=\"align-middle h-100\">\n" +
                "                    <input type=\"text\" id=\"warehouseNameInput" + warehouse_id + "\" class=\"form-control\" value=\"" + name + "\">\n" +
                "                </td>\n" +
                "                <td class=\"align-middle h-100\">\n" +
                "                    <input type=\"text\" id=\"warehouseAddressInput" + warehouse_id + "\" class=\"form-control\" value=\"" + address + "\">\n" +
                "                </td>\n" +
                "                <td class=\"align-middle\">\n" +
                "                    <div class=\"text-center d-flex justify-content-around align-middle h-100\">\n" +
                "                    <button type=\"submit\" class=\"btn btn-sm btn-outline-warning mx-1\" id=\"updateButton" + warehouse_id + "\">Update</button>\n" +
                "                    </div>\n" +
                "                </td>\n" +
                "            </tr>";
            updateButtons.push(document.getElementById("updateButton" + warehouse_id));
        }
    }
    for (i = 0; i < updateButtons.length; i++) {
        if (updateButtons[i].contains(event.target)) {
            const warehouse_id = parseInt(updateButtons[i].id.replace(/\D/g, ''), 10)

            const warehouse = {
                id: warehouse_id,
                name: document.getElementById("warehouseNameInput" + warehouse_id).value.trim(),
                address: document.getElementById("warehouseAddressInput" + warehouse_id).value.trim(),
            }
            event.preventDefault();
            if (validateInput(document.getElementById("warehouseNameInput" + warehouse_id), document.getElementById("warehouseAddressInput" + warehouse_id))) {
                // Create a fetch request
                fetch(url + "/" + warehouse_id, {
                    method: "PUT", // You can change this to "GET" or other HTTP methods
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
                        location.reload();
                    })
                    .catch(error => {
                        div.innerHTML = error.message;
                    });
            }
        }

    }


})
;


function validateInput(nameInput, addressInput) {
    div.innerHTML = "";
    return validateName(nameInput) & validateAddress(addressInput);
}

function validateName(nameElement) {
    const name = nameElement.value.trim();
    const regex = /^[a-zA-Z 0-9.,-]{4,30}$/;
    if (regex.test(name) && name !== "") {
        nameElement.classList.remove("border-danger");
        return true;
    } else {
        const label = document.createElement("p");
        label.textContent = "Name is invalid (4-30 characters, letters, numbers ',.-' only)";
        div.appendChild(label);
        nameElement.classList.add("border-danger")
        return false;
    }
}

function validateAddress(addressElement) {
    const address = addressElement.value.trim();
    const regex = /^[a-zA-Z0-9 .,-]{5,30}$/;
    if (regex.test(address) && address !== "") {
        addressElement.classList.remove("border-danger");
        return true;
    } else {
        const label = document.createElement("p");
        label.textContent = "Address is invalid (5-30 characters, letters, numbers ',.-' only)";
        div.appendChild(label);
        addressElement.classList.add("border-danger")
        return false;
    }
}
