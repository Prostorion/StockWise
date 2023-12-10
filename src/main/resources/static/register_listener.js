

function validateInput() {

    div.innerHTML = "";
    return validateUsername() & validateName(document.getElementById("firstnameInput")) & validateName(document.getElementById("lastnameInput")) & validatePassword()
}

function validateUsername() {
    const usernameElement = document.getElementById("usernameInput");
    const username = usernameElement.value.trim();
    const regex = /^[a-zA-Z0-9]{4,30}$/;
    if (regex.test(username) && username !== "") {
        usernameElement.classList.remove("border-danger");
        return true;
    } else {
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
    if (regex.test(name) && name !== "") {
        nameElement.classList.remove("border-danger");
        return true;
    } else {
        const label1 = document.createElement("p");
        if (nameElement.id.includes("first")) {
            label1.textContent = "First name";
        } else {
            label1.textContent = "Last name";
        }
        label1.textContent += " is invalid (4-30 characters, letters only, starts with capital)";
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
    if (regex.test(password) && password !== "" && password === confirmPassword) {
        passwordElement.classList.remove("border-danger");
        confirmPasswordElement.classList.remove("border-danger");
        return true;
    } else {
        const label = document.createElement("p");
        if (password !== confirmPassword) {
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