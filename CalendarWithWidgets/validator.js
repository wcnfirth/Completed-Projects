// validator.js
// Functions used for checking that input to the form are valid

function checkNames() {
    var name = document.getElementById("eventname");
    var location = document.getElementById("location");

    var name_format = name.value.search(/^[A-Za-z0-9]+$/);
    var location_format = location.value.search(/^[A-Za-z0-9]+$/);

    if (name_format != 0 && location_format != 0) {
        alert("Event Name and Location must be alphanumeric");
        return false;
    } else if (name_format != 0) {
        alert("Event Name must be alphanumeric");
        return false;
    } else if (laction_format != 0) {
        alert("Location must be alphanumeric");
        return false;
    } else return true;
}

document.getElementById("eventname").onchange = checkNames;
document.getElementById("location").onchange = checkNames;