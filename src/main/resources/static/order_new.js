const body = document.getElementById("body");
const div = document.getElementById("resultDiv");
const addItemButton = document.getElementById("addItem");
const submitButton = document.getElementById("submitForm");
const currentLocation = window.location.href;
const url = currentLocation.substring(0, (currentLocation.lastIndexOf('/')-5))+"orders";



body.addEventListener("click", (event) => {


    const canselButtons = document.getElementsByClassName("cansel-button")
    if (addItemButton.contains(event.target)) {
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
               completed: false,
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
                   div.textContent = error.message;
               });
       }


    }

});


function createTableRow() {
    // Create a new table row element
    var newRow = document.createElement('tr');

    // Create cells with input elements
    var cell1 = document.createElement('td');
    cell1.className = 'align-middle h-100';
    var input1 = document.createElement('input');
    input1.type = 'text';
    input1.className = 'form-control itemNameInput';
    cell1.appendChild(input1);

    var cell2 = document.createElement('td');
    cell2.className = 'align-middle h-100';
    var input2 = document.createElement('input');
    input2.type = 'number';
    input2.className = 'form-control itemAmountInput';
    cell2.appendChild(input2);

    var cell3 = document.createElement('td');
    cell3.className = 'align-middle h-100';
    var input3 = document.createElement('input');
    input3.type = 'text';
    input3.className = 'form-control itemMeasurementInput';
    cell3.appendChild(input3);

    var cell4 = document.createElement('td');
    cell4.className = 'align-middle h-100';
    var input4 = document.createElement('input');
    input4.type = 'number';
    input4.className = 'form-control itemRackInput';
    cell4.appendChild(input4);

    var cell5 = document.createElement('td');
    cell5.className = 'align-middle';
    var removeButton = document.createElement('button');
    removeButton.type = 'button';
    removeButton.className = 'btn btn-sm btn-outline-danger my-2 cansel-button';
    removeButton.textContent = 'Remove';
    cell5.appendChild(removeButton);

    // Append cells to the row
    newRow.appendChild(cell1);
    newRow.appendChild(cell2);
    newRow.appendChild(cell3);
    newRow.appendChild(cell4);
    newRow.appendChild(cell5);

    return newRow;
}

function validateInputs() {
    var tableRows = document.querySelectorAll('#tableBody tr');
    var isValid = true;
    div.innerHTML = "";
    const dateTimeInput = document.getElementById('dateInput');
    if(!validateDateTime(dateTimeInput)){
        isValid = false;
    }
    tableRows.forEach(function(row) {
        const itemNameInput = row.querySelectorAll('.itemNameInput');
        if(!validateName(itemNameInput[0])){
            isValid = false;
        }
        const itemAmountInput = row.querySelectorAll('.itemAmountInput');
        if(!validateNumber(itemAmountInput[0])){
            isValid = false;
        }
        const itemMeasurementInput = row.querySelectorAll('.itemMeasurementInput');
        if(!validateMeasurement(itemMeasurementInput[0])){
            isValid = false;
        }
        const itemRackInput = row.querySelectorAll('.itemRackInput');
        if(!validateNumber(itemRackInput[0])){
            isValid = false;
        }

    });

    return isValid;
}


function validateName(stringElement) {
    const name = stringElement.value.trim();
    const regex = new RegExp(`^[a-zA-Z 0-9.,-]{4,30}$`);
    if (regex.test(name) && name !== "") {
        stringElement.classList.remove("border-danger");
        return true;
    } else {
        const label = document.createElement("p");
        label.textContent = "Name is invalid (4-30 characters, letters, numbers ',.-' only)";
        div.appendChild(label);
        stringElement.classList.add("border-danger")
        return false;
    }
}

function validateMeasurement(stringElement) {
    const name = stringElement.value.trim();
    const regex = new RegExp(`^[a-zA-Z 0-9.,-]{1,30}$`);
    if (regex.test(name) && name !== "") {
        stringElement.classList.remove("border-danger");
        return true;
    } else {
        const label = document.createElement("p");
        label.textContent = "Measurement is invalid (4-30 characters, letters, numbers ',.-' only)";
        div.appendChild(label);
        stringElement.classList.add("border-danger")
        return false;
    }
}

function validateNumber(element) {
    const number = element.value;
    if (number > 0 && number % 1 === 0) {
        element.classList.remove("border-danger");
        return true;
    } else {
        const label = document.createElement("p");
        label.textContent = "Amount or Rack is invalid (Must be 1 or more)";
        div.appendChild(label);
        element.classList.add("border-danger")
        return false;
    }
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
        const inputs = row.getElementsByTagName('input');

        const item = {
            name: inputs[0].value,
            amount: parseFloat(inputs[1].value),
            measurement: inputs[2].value,
            rackNumber: parseFloat(inputs[3].value)
        };

        items.push(item);
    }
    console.log(items)
    return items;
}
