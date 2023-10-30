// <----------------------Information user in Header---------------------->
fetch('/userInfo')
    .then((response) => response.json())
    .then((userJSON) => {
        document.getElementById('userEmail').innerHTML = userJSON.email;
        let html = '';
        userJSON.roles.forEach(function (elem) {
            html += '<div>' + elem.name + '</div>';
        });
        document.getElementById('rolesInfo').innerHTML = html;
    });


// <----------------------Get All Roles for forms---------------------->

fetch('/getAllRoles')
    .then((response) => response.json())
    .then((userJSON) => {
        let html = '';
        userJSON.forEach(function (elem) {
            html += '<option value="' + elem.id + '">' + elem.name + '</option>';
        });
        document.getElementById('rolesNew').innerHTML = html;
        document.getElementById('editRoles').innerHTML = html;
        document.getElementById('deleteRoles').innerHTML = html;
    });

// <----------------------Add new User in Form---------------------->

document.getElementById('formElem').addEventListener('submit', (e) => {
    e.preventDefault()

    fetch('/save_user', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            firstName: document.getElementById('firstNameNew').value,
            lastName: document.getElementById('lastNameNew').value,
            age: document.getElementById('ageNew').value,
            email: document.getElementById('emailNew').value,
            password: document.getElementById('passwordNew').value,
            roles: [
                {
                    id: document.getElementById('rolesNew').value
                }
            ]
        })
    })
        .then((response) => {
            if (response.ok) {
                document.getElementById('firstNameNew').value = '';
                document.getElementById('lastNameNew').value = '';
                document.getElementById('ageNew').value = '';
                document.getElementById('emailNew').value = '';
                document.getElementById('passwordNew').value = '';
                document.getElementById('rolesNew').value = '';
                getAllUsers();
            } else {
                response.json().then(violation => {
                    const outputDiv = document.getElementById("outputNew");
                    outputDiv.innerHTML = '';
                    violation.violations.forEach((violation) => {
                        const p = document.createElement("p");
                        p.innerHTML = `${violation.fieldName}: ${violation.message}`;
                        outputDiv.appendChild(p);
                    });
                })
            }
        })
})

// <----------------------Get All Users---------------------->

function getAllUsers() {
    fetch('/allUsers')
        .then(res => res.json())
        .then(data => {
            loadTable(data)
        })
}

function loadTable(listAllUsers) {
    let res = ``;

    for (let user of listAllUsers) {
        res +=
            `<tr id="row" >
                <th scope="row">${user.id}</th>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>`

        for (let role of user.roles) {
            res += `<div style="float: left; margin-right: 10px">` + role.name + `</div>`
        }
        res +=  `<td>
                    <button id="button-edit" class="btn btn-sm btn-primary" type="button"
                    data-bs-toggle="modal" data-bs-target="#editModel"
                    onclick="editModal(${user.id})">Edit</button></td>
                <td>
                    <button class="btn btn-sm btn-danger" type="button"
                    data-bs-toggle="modal" data-bs-target="#deleteModal"
                    onclick="deleteModal(${user.id})">Delete</button></td>
            </tr>`
    }
    document.getElementById('tableBodyAdmin').innerHTML = res;

}

function roles(role) {
    return `<div>` + role.name + `</div>`
}

function closeModal() {
    document.querySelectorAll(".btn-close").forEach((btn) => btn.click())
}

function editModal(id) {
    const url = '/getUser';
    let editId = `${url}/${id}`;
    fetch(editId, {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    }).then(res => {
        res.json().then(user => {
            document.getElementById('editId').value = user.id;
            document.getElementById('editFirstName').value = user.firstName;
            document.getElementById('editLastName').value = user.lastName;
            document.getElementById('editAge').value = user.age;
            document.getElementById('editEmail').value = user.email;
            document.getElementById('editPassword').value = '';
        })
    });

}

async function editUser() {
    let idValue = document.getElementById('editId').value;
    let firstNameValue = document.getElementById('editFirstName').value;
    let lastNameValue = document.getElementById('editLastName').value;
    let ageValue = document.getElementById('editAge').value;
    let emailValue = document.getElementById('editEmail').value;
    let passwordValue = document.getElementById('editPassword').value;
    let user = {
        id: idValue,
        firstName: firstNameValue,
        lastName: lastNameValue,
        age: ageValue,
        email: emailValue,
        password: passwordValue,
        roles: [
            {
                id: document.getElementById('editRoles').value
            }
        ]
    }

    let url ='/edit_user';
    await fetch(`${url}/${idValue}`, {
        method: 'PATCH',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        },
        body: JSON.stringify(user)
    })
    closeModal()
    getAllUsers()
}

function deleteModal(id) {
    const url = '/getUser'
    let delId = `${url}/${id}`;
    fetch(delId, {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json;charset=UTF-8'
        }
    }).then(res => {
        res.json().then(user => {
            document.getElementById('deleteID').value = user.id;
            document.getElementById('deleteFirstName').value = user.firstName;
            document.getElementById('deleteLastName').value = user.lastName;
            document.getElementById('deleteAge').value = user.age;
            document.getElementById('deleteEmail').value = user.email;
        })
    });
}

async function deleteUser() {
    const id = document.getElementById('deleteID').value
    const url = '/delete_user'
    let urlDel = `${url}/${id}`;

    let method = {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    }
    fetch(urlDel, method).then(() => {
        closeModal()
        getAllUsers()
    })

}

getAllUsers();