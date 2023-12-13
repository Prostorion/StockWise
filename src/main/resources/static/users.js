const body = document.getElementById("body");
const div = document.getElementById("resultDiv");
const url =  window.location.href;
const deleteButtons = document.getElementsByClassName("delete-button");


body.addEventListener("click", (event) => {


    for (let i = 0; i < deleteButtons.length; i++) {
        if (deleteButtons[i].contains(event.target)) {
            const user_id = parseInt(deleteButtons[i].id.replace(/\D/g, ''), 10)
            const userConfirmed = window.confirm("Are you sure you want to delete "
                +deleteButtons[i].parentNode.parentNode.childNodes.item(1).textContent
                +" with his/her tasks?");
            if (userConfirmed) {
                fetch(url + '/' + user_id, {
                    method: "DELETE"
                })
                    .then(async response => {
                        if (response.ok) {
                            return response.text();
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


});