const currentUrl = window.location.href;

const patternHistory = /\/api\/v1\/warehouses\/(\d+)\/history/;
const matchHistory = currentUrl.match(patternHistory);
const patternItems = /\/api\/v1\/warehouses\/(\d+)\/items/;
const matchItems = currentUrl.match(patternItems);
const patternUsers = /\/api\/v1\/warehouses\/(\d+)\/users/;
const matchUsers = currentUrl.match(patternUsers);
const patternTasks = /\/api\/v1\/warehouses\/(\d+)\/tasks/;
const matchTasks = currentUrl.match(patternTasks);

// If there is a match and the ID is a certain number (replace 'certainNumber' with your specific number)
const buttons = document.querySelectorAll('.btn-warning');
const main = document.getElementById("link-main");
const items = document.getElementById("link-items");
const history = document.getElementById("link-history");
const tasks = document.getElementById("link-tasks");
const users = document.getElementById("link-users");
const warehouseName = document.getElementById("warehouseName")


document.addEventListener('DOMContentLoaded', function () {
    const warehouseNumber = currentUrl.split("/warehouses")[1].split("/")[1];
    main.href = "/api/v1/warehouses/"+warehouseNumber;
    items.href = main.href+"/items";
    history.href = main.href+"/history";
    tasks.href = main.href+"/tasks";
    users.href = main.href+"/users";

    fetch("/api/rest/warehouses/" + warehouseNumber, {
        method: "GET", // You can change this to "GET" or other HTTP methods
    })
        .then((response) => {
            if (response.ok) {
                return response.json();
            } else {
                return new Error("not found");
            }
        })
        .then((data) => {

            warehouseName.textContent = data.name.toUpperCase() ;
        })
        .catch(e => {
            warehouseName.textContent = ":(";
        })
});
buttons.forEach(function (button) {
    if (matchHistory && button.textContent.trim() === 'History') {
        button.classList.remove('btn-warning');
        button.classList.add('btn-outline-secondary'); // You can change this class to any other class you want
    }
    if (matchItems && button.textContent.trim() === 'Items') {
        button.classList.remove('btn-warning');
        button.classList.add('btn-outline-secondary'); // You can change this class to any other class you want
    }
    if (matchUsers && button.textContent.trim() === 'Users') {
        button.classList.remove('btn-warning');
        button.classList.add('btn-outline-secondary'); // You can change this class to any other class you want
    }
    if (matchTasks && button.textContent.trim() === 'Tasks') {
        button.classList.remove('btn-warning');
        button.classList.add('btn-outline-secondary'); // You can change this class to any other class you want
    }
});