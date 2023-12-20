const body = document.getElementById("body");
const div = document.getElementById("resultDiv");
const currentLocation = window.location.href;
const url = currentLocation.substring(0, currentLocation.lastIndexOf('/')) + "/orders";
const urlSupply = currentLocation.substring(0, currentLocation.lastIndexOf('/')) + "/supplies";
const deleteButtons = document.getElementsByClassName("delete-button");
const deleteButtonsSupply = document.getElementsByClassName("delete-button-supply");
const completeButtons = document.getElementsByClassName("complete-button");
const completeButtonsSupply = document.getElementsByClassName("complete-button-supply");


body.addEventListener("click", (event) => {


    for (let i = 0; i < deleteButtons.length; i++) {
        if (deleteButtons[i].contains(event.target)) {
            const task_id = parseInt(deleteButtons[i].id.replace(/\D/g, ''), 10)
            const userConfirmed = window.confirm("Are you sure you want to delete task?");
            if (userConfirmed) {
                fetch(url + '/' + task_id, {
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

    for (let i = 0; i < completeButtons.length; i++) {
        if (completeButtons[i].contains(event.target)) {
            const task_id = parseInt(completeButtons[i].id.replace(/\D/g, ''), 10)
            const userConfirmed = window.confirm("Are you sure you want to complete task?");
            if (userConfirmed) {
                fetch(url + '/' + task_id + '/complete', {
                    method: "POST" // You can change this to "GET" or other HTTP methods
                })
                    .then(async response => {
                        if (response.ok) {
                            return response.text(); // or response.json() if the server returns JSON
                        } else {
                            throw new Error(await response.text());
                        }
                    })
                    .then(() => {
                        completeButtons[i].parentNode.innerHTML = "<p class=\"text-success\">Completed</p>";
                    })
                    .catch(error => {
                        div.innerHTML = "Error: " + error;
                    });
            }

        }
    }

    for (let i = 0; i < deleteButtonsSupply.length; i++) {
        if (deleteButtonsSupply[i].contains(event.target)) {
            const task_id = parseInt(deleteButtonsSupply[i].id.replace(/\D/g, ''), 10)
            const userConfirmed = window.confirm("Are you sure you want to delete task?");
            if (userConfirmed) {
                fetch(urlSupply + '/' + task_id, {
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

    for (let i = 0; i < completeButtonsSupply.length; i++) {
        if (completeButtonsSupply[i].contains(event.target)) {
            const task_id = parseInt(completeButtonsSupply[i].id.replace(/\D/g, ''), 10)
            const userConfirmed = window.confirm("Are you sure you want to complete task?");
            if (userConfirmed) {
                fetch(urlSupply + '/' + task_id + '/complete', {
                    method: "POST" // You can change this to "GET" or other HTTP methods
                })
                    .then(async response => {
                        if (response.ok) {
                            return response.text(); // or response.json() if the server returns JSON
                        } else {
                            throw new Error(await response.text());
                        }
                    })
                    .then(() => {
                        completeButtonsSupply[i].parentNode.innerHTML = "<p class=\"text-success\">Completed</p>";
                    })
                    .catch(error => {
                        div.innerHTML = "Error: " + error;
                    });
            }

        }
    }

});


const paragraphs = document.getElementsByClassName("supplyItemP");
document.addEventListener('DOMContentLoaded', function () {

    for (let i = 0; i < paragraphs.length; i++) {
        const amount = paragraphs[i].textContent.split(" ")[1];
        fetch(currentLocation.substring(0, currentLocation.lastIndexOf('/')) + "/items/" + paragraphs[i].textContent.split(" ")[0], {
            method: "GET", // You can change this to "GET" or other HTTP methods
        })
            .then(async response => {
                if (response.ok) {
                    return response.json(); // or response.json() if the server returns JSON
                } else {
                    throw new Error(await response.json());
                }
            })
            .then((data) => {
                paragraphs[i].textContent = data.name + ", Rack№ " + data.rack.number + ", amount - " + amount;
            })
            .catch(error => {
                div.innerHTML = "Error: " + error;
            });

    }
});