fetch('/userInfo')
    .then((response) => response.json())
    .then((userJSON) => {
        document.getElementById('userEmail').innerHTML = userJSON.email;
        let html = '';
        userJSON.roles.forEach(function (elem) {
            html += '<div>' + elem.name + '</div>';
        });
        document.getElementById('rolesInfo').innerHTML = html;
        document.getElementById('rolesInfo2').innerHTML = html;
        document.getElementById('userEmail2').innerHTML = userJSON.email;
        document.getElementById('userId').innerHTML = userJSON.id;
        document.getElementById('userAge').innerHTML = userJSON.age;
        document.getElementById('userLastName').innerHTML = userJSON.lastName;
        document.getElementById('userFirstName').innerHTML = userJSON.firstName;
    });

