function openPopup(target) {
    const element = document.getElementById('popup');


    fetch(window.location.href + '/racks/' + target.textContent, {
        method: "GET" // You can change this to "GET" or other HTTP methods
    })
        .then(async response => {
            if (response.ok) {
                return await response.json(); // or response.json() if the server returns JSON
            } else {
                throw new Error(await response.text());
            }
        })
        .then((data) => {
            for (let i = 0; i < data.length; i++) {
                const p = document.createElement("p");
                p.textContent = data[i].name + " - " + data[i].amount +" "+ data[i].measurement;
                p.classList.add("currentP");
                element.insertBefore(p, element.firstChild);
            }
        })
        .catch(error => {
            element.innerHTML = "Error: " + error;
        });


    element.style.display = 'block';
}

function closePopup() {
    document.getElementById('popup').style.display = 'none';
    const p = document.getElementsByClassName("currentP");
    const pLength = p.length;
    for (let i = 0; i < pLength; i++) {
        p.item(0).remove();
    }
}

const body = document.getElementById("body");
const racks = document.getElementsByClassName("rack");
body.addEventListener("click", (event) => {

    for (let i = 0; i < racks.length; i++) {
        if (racks[i].contains(event.target)) {
            openPopup(racks[i]);
        }
    }
});