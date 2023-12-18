const body = document.getElementById("body");
const div = document.getElementById("resultDiv");
const currentLocation = window.location.href;
const url =  currentLocation.substring(0, currentLocation.lastIndexOf('/'))+"/orders";
const deleteButtons = document.getElementsByClassName("delete-button");
const completeButtons = document.getElementsByClassName("complete-button");


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

});