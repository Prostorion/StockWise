const body = document.getElementById("body");
const div = document.getElementById("resultDiv");
const addItemButton = document.getElementById("addItem");
const submitButton = document.getElementById("submitForm");
const currentLocation = window.location.href;
const url = currentLocation.substring(0, (currentLocation.lastIndexOf('/')-5))+"supplies";



body.addEventListener("click", (event) => {


    const canselButtons = document.getElementsByClassName("cansel-button")
    if (addItemButton.contains(event.target)) {
        console.log("addButton")
        const table = document.getElementById("tableBody");
        table.appendChild(createTableRow());

    }

    for (let i = 0; i < canselButtons.length; i++) {
        if (canselButtons[i].contains(event.target)){
            canselButtons[i].parentNode.parentNode.remove();
        }
    }


    if (submitButton.contains(event.target)) {
        event.preventDefault();
        if (validateInputs()){
            const items = convertTableToObjects();
            const assignee = document.getElementById("userSelect").value.trim().split("(")[1].split(")")[0];
            const deadline = new Date(document.getElementById("dateInput").value);
            deadline.setHours(deadline.getHours()+2);
            const task= {
                items: items,
                assignee: {
                    username: assignee
                },
                deadline: deadline
            }

            fetch(url, {
                method: "POST", // You can change this to "GET" or other HTTP methods
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(task)
            })
                .then(async response => {
                    if (response.ok) {
                        return response.text(); // or response.json() if the server returns JSON
                    } else {
                        throw new Error(await response.text());
                    }
                })
                .then(() => {
                    location.replace(currentLocation.substring(0, (currentLocation.lastIndexOf('/'))));
                })
                .catch(error => {
                    div.innerHTML = error.message;
                });
        }


    }

});


function createTableRow() {
    // Create a new table row element
    const row = document.getElementById("firstItemRow").cloneNode(true);
    const removeButton = document.createElement('button');
    removeButton.type = 'button';
    removeButton.className = 'btn btn-sm btn-outline-danger my-2 cansel-button';
    removeButton.textContent = 'Remove';
    row.getElementsByClassName("last-cell")[0].appendChild(removeButton);
    return row;
}

function validateInputs() {
    let isValid = true;
    div.innerHTML = "";
    const dateTimeInput = document.getElementById('dateInput');
    if(!validateDateTime(dateTimeInput)){
        isValid = false;
    }

    return isValid;
}




function validateDateTime(element) {
    const datetimeRegex = /^(\d{4})-(\d{2})-(\d{2})T(\d{2}):(\d{2})$/;
    const match = element.value.match(datetimeRegex);

    if (match) {
        // Step 3: Check if the extracted date and time values are valid
        const year = parseInt(match[1]);
        const month = parseInt(match[2]);
        const day = parseInt(match[3]);
        const hour = parseInt(match[4]);
        const minute = parseInt(match[5]);

        const isValidDate = !isNaN(year) && !isNaN(month) && !isNaN(day);
        const isValidTime = !isNaN(hour) && !isNaN(minute) && hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59;

        if (isValidDate && isValidTime) {
            element.classList.remove("border-danger");
            return true;
        } else {
            const label = document.createElement("p");
            label.textContent = "Invalid Date or Time format";
            div.appendChild(label);
            element.classList.add("border-danger")
            return false;
        }
    } else {
        const label = document.createElement("p");
        label.textContent = "Invalid Date or Time format";
        div.appendChild(label);
        element.classList.add("border-danger")
        return false;
    }
}


function convertTableToObjects() {
    const tableBody = document.getElementById('tableBody');
    const rows = tableBody.getElementsByTagName('tr');
    const items = [];

    for (let i = 0; i < rows.length; i++) {
        const row = rows[i];
        const select = row.getElementsByTagName('select')[0];
        const input = row.getElementsByTagName('input')[0];
        const selected = select.options[select.selectedIndex];
        const item = {
            item_id: selected.id.split(" ")[1],
            amount: input.value
        };

        items.push(item);
    }
    console.log(items)
    return items;
}
