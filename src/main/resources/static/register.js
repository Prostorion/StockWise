// Get the form data from an HTML form with id "myForm"
const body = document.getElementById("body");
const submitButton = document.getElementById("submitButton");
const form = document.getElementById("form");
const div = document.getElementById("resultDiv");



// URL where you want to send the request
const url = "/api/v1/users";

body.addEventListener("click", (event) => {
    if (submitButton.contains(event.target)) {


        const user = {
            username: document.getElementById("usernameInput").value.trim(),
            firstname: document.getElementById("firstnameInput").value.trim(),
            lastname: document.getElementById("lastnameInput").value.trim(),
            password: document.getElementById("passwordInput").value,
        }
        event.preventDefault()
        if(validateInput()){
            // Create a fetch request
            fetch(url, {
                method: "POST", // You can change this to "GET" or other HTTP methods
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(user)
            })
                .then(async response => {
                    if (response.ok) {
                        return response.text(); // or response.json() if the server returns JSON
                    } else {
                        throw new Error(await response.text());
                    }
                })
                .then(() => {
                    // Handle the response data here
                    form.remove();
                    const label = document.createElement("h1");
                    label.textContent = "Done!";
                    const a = document.createElement("a");
                    a.textContent = "Return to main page";
                    a.href = "/api/v1";
                    a.className = "link-secondary link-offset-2 link-underline-opacity-25 link-underline-opacity-100-hover mb-15";
                    div.innerHTML = "";
                    div.appendChild(label);
                    div.appendChild(a);
                })
                .catch(error => {
                    div.innerHTML = error.message;
                });
        }


    }

})




function validateInput() {

    div.innerHTML = "";
    return validateUsername() & validateName(document.getElementById("firstnameInput")) & validateName(document.getElementById("lastnameInput")) & validatePassword()
}

function validateUsername() {
    const usernameElement = document.getElementById("usernameInput");
    const username = usernameElement.value.trim();
    const regex = /^[a-zA-Z0-9]{4,30}$/;
    if(regex.test(username) && username !==""){
        usernameElement.classList.remove("border-danger");
        return true;
    }
    else {
        const label = document.createElement("p");
        label.textContent = "Username is invalid (4-30 characters, letters and numbers only)";
        div.appendChild(label);
        usernameElement.classList.add("border-danger")
        return false;
    }
}

function validateName(nameElement) {
    const name = nameElement.value.trim();
    const regex = /^[A-Z][a-zA-Z]{3,29}$/;
    if(regex.test(name) && name !==""){
        nameElement.classList.remove("border-danger");
        return true;
    }
    else {
        const label1 = document.createElement("p");
        if (nameElement.id.includes("first")){
            label1.textContent = "First name";
        }else{
            label1.textContent = "Last name";
        }
        label1.textContent +=" is invalid (4-30 characters, letters only, starts with capital)";
        div.appendChild(label1);
        nameElement.classList.add("border-danger")
        return false;
    }
}

function validatePassword() {
    const passwordElement = document.getElementById("passwordInput");
    const password = passwordElement.value;
    const confirmPasswordElement = document.getElementById("confirmPasswordInput");
    const confirmPassword = confirmPasswordElement.value;
    const regex = /^[a-zA-Z0-9$_#@%*?!.,]{4,30}$/;
    if(regex.test(password) && password !=="" && password === confirmPassword){
        passwordElement.classList.remove("border-danger");
        confirmPasswordElement.classList.remove("border-danger");
        return true;
    }
    else {
        const label = document.createElement("p");
        if (password !== confirmPassword){
                label.textContent = "Passwords are different";
        } else {
                label.textContent = "Password is invalid (4-30 characters: letters, numbers or $_#@%*?!.,)";
        }
        div.appendChild(label);
        passwordElement.classList.add("border-danger")
        confirmPasswordElement.classList.add("border-danger")
        return false;
    }
}