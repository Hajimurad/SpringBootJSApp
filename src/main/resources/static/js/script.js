let modal = $('#defaultModal');
let modalTitle = $('.modal-title');
let modalBody = $('.modal-body');
let modalFooter = $('.modal-footer');

let primaryButton = $('<button type="button" class="btn btn-primary"></button>');
let dismissButton = $('<button type="button" class="btn btn-secondary" data-dismiss="modal"></button>');
let dangerButton = $('<button type="button" class="btn btn-danger"></button>');

$(document).ready(function(){
    defaultModal();
    viewAllUsers();
    viewUser();
    newUserForm();
    saveNewUser();
});

const http = {
    fetch: async function(url, options = {}) {
        const response = await fetch(url, {
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            ...options,
        });
        return response;
    }
};

const userService = {
    findAll: async () => {
        return await http.fetch('/api/admin/');
    },
    add: async (data) => {
        return await http.fetch('/api/admin/', {
            method: 'POST',
            body: JSON.stringify(data)
        });
    },
    findById: async (id) => {
        return await http.fetch('/api/admin/' + id);
    },
    findUser: async () => {
        return await http.fetch('/api/user/');
    },

    update: async (data) => {
        return await http.fetch('/api/admin/', {
            method: 'PUT',
            body: JSON.stringify(data)
        });
    },
    delete: async (id) => {
        return await http.fetch('/api/admin/' + id, {
            method: 'DELETE'
        });
    },
};

const roleService = {
    findAll: async () => {
        return await http.fetch('/api/roles/');
    },
    findById: async (id) => {
        return await http.fetch('/api/roles/' + id);
    },
};

function defaultModal() {
    modal.modal({
        keyboard: true,
        backdrop: "static",
        show: false,
    }).on("show.bs.modal", function(event){
        let button = $(event.relatedTarget);
        let id = button.data('id');
        let action = button.data('action');
        switch(action) {

            case 'editUser':
                editUser($(this), id);
                break;

            case 'deleteUser':
                deleteUser($(this), id);
                break;
        }
    }).on('hidden.bs.modal', function(event){
        $(this).find('.modal-title').html('');
        $(this).find('.modal-body').html('');
        $(this).find('.modal-footer').html('');
    });
}

async function viewAllUsers() {
    $('#userTable tbody').empty();
    const userResponse = await userService.findAll();
    const usersJson = userResponse.json();
    usersJson.then(users => {
        users.forEach(user => {
            let userRow = `$(<tr>
                        <td>${user.id}</td>
                        <td>${user.firstName}</td>
                        <td>${user.lastName}</td>
                        <td>${user.age}</td>
                        <td>${user.username}</td>
                        <td>${user.roles.map(role => role.role)}</td>
                        <td><a class="btn"
                        data-action="editUser" data-toggle="modal" data-target="#defaultModal" data-id="${user.id}" style="color: white; background-color: #17a2b8">Edit</a></td>
                        <td><a class="btn"
                        data-action="deleteUser" data-toggle="modal" data-target="#defaultModal" data-id="${user.id}" style="color: white; background-color: #dc3545">Delete</a></td>
                        </tr>)`;
            $('#userTable tbody').append(userRow);
        });
    });
}

async function viewUser() {
    $('#principalUser tbody').empty();
    const userResponse = await userService.findUser();
    const usersJson = userResponse.json();
    usersJson.then(user => {
            let userRow = `$(<tr>
                        <td>${user.id}</td>
                        <td>${user.firstName}</td>
                        <td>${user.lastName}</td>
                        <td>${user.age}</td>
                        <td>${user.username}</td>
                        <td>${user.roles.map(role => role.role)}</td>
                        </tr>)`;
            $('#principalUser tbody').append(userRow);
        });
}


const roleJson = []
fetch('api/roles')
    .then(res => res.json())
    .then(roles => roles.forEach(role => roleJson.push(role)))

async function editUser(modal, id) {
    const userResponse = await userService.findById(id);
    const userJson = userResponse.json();
    const rolesResponse = await roleService.findAll();
    const rolesJson = rolesResponse.json();

    modal.find(modalTitle).html('Edit User');
    let userFormHidden = $('.userForm:hidden')[0];
    modal.find(modalBody).html($(userFormHidden).clone());
    let userForm = modal.find('.userForm');
    userForm.prop('id', 'updateUserForm');
    modal.find(userForm).show();
    dismissButton.html('Cancel');
    modal.find(modalFooter).append(dismissButton);
    primaryButton.prop('id', 'updateUserButton');
    primaryButton.html('Update');
    modal.find(modalFooter).append(primaryButton);

    userJson.then(user => {
        modal.find('#id').val(user.id);
        modal.find('#firstName').val(user.firstName);
        modal.find('#lastName').val(user.lastName);
        modal.find('#age').val(user.age);
        modal.find('#username').val(user.username);
        modal.find('#password');
        rolesJson.then(roles => {
            roles.forEach(role => {
                modal.find('#roles').append(new Option(role.role, role.id, false,false));
            });
        });
    });

    $('#updateUserButton').click(async function(){
        let id = userForm.find('#id').val().trim();
        let firstName = userForm.find('#firstName').val().trim();
        let lastName = userForm.find('#lastName').val().trim();
        let age = userForm.find('#age').val().trim();
        let username = userForm.find('#username').val().trim();
        let password = userForm.find('#password').val().trim();
        let rolesArray = modal.find('#roles').val()
        let roles = []

        let data = {
            id: id,
            name: firstName,
            surname: lastName,
            age: age,
            email: username,
            password: password,
            roles: roles
        }

        for (let r of roleJson) {
            for (let i = 0; i < rolesArray.length; i++) {
                if (r.id == rolesArray[i]) {
                    roles.push(r)
                }
            }
        }

        const userResponse = await userService.update(data);

        if (userResponse.status === 200) {
            await viewAllUsers();
            modal.find('.modal-title').html('Success');
            modal.find('.modal-body').html('User updated!');
            dismissButton.html('Close');
            modal.find(modalFooter).html(dismissButton);
            $('#defaultModal').modal('show');
        } else if (userResponse.status === 400) {
            userResponse.json().then(response => {
                response.validationErrors.forEach(function(error){
                    modal.find('#' + error.field).addClass('is-invalid');
                    modal.find('#' + error.field).next('.invalid-feedback').text(error.message);
                });
            });
        } else {
            userResponse.json().then(response => {
                let alert = `<div class="alert alert-success alert-dismissible fade show col-12" role="alert">
                        ${response.error}
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>`;
                modal.find('.modal-body').prepend(alert);
            });
        }
    });
}

async function deleteUser(modal, id) {
    const userResponse = await userService.findById(id);
    const userJson = userResponse.json();

    modal.find(modalTitle).html('Delete User');
    let message = '<strong>Are you sure you want to delete the following user?</strong>';
    modal.find(modalBody).html(message);
    let viewUserTableHidden = $('.viewUserTable:hidden')[0];
    modal.find(modalBody).append($(viewUserTableHidden).clone());
    let viewUserTable = modal.find('.viewUserTable');
    modal.find(viewUserTable).show();
    dismissButton.html('Close');
    modal.find(modalFooter).append(dismissButton);
    dangerButton.prop('id', 'deleteUserButton');
    dangerButton.html('Delete');
    modal.find(modalFooter).append(dangerButton);

    userJson.then(user => {
        modal.find('#id').html(user.id);
        modal.find('#firstName').html(user.firstName);
        modal.find('#lastName').html(user.lastName);
        modal.find('#age').html(user.age);
        modal.find('#username').html(user.username);
        modal.find('#roles').html(user.roles.map(role => role.role));
    });

    $('#deleteUserButton').click(async function(){
        const userResponse = await userService.delete(id);

        if (userResponse.status === 200) {
            await viewAllUsers();
            modal.find('.modal-title').html('Success');
            modal.find('.modal-body').html('User deleted!');
            dismissButton.html('Close');
            modal.find(modalFooter).html(dismissButton);
            $('#defaultModal').modal('show');
        } else {
            userResponse.json().then(response => {
                let alert = `<div class="alert alert-success alert-dismissible fade show col-12" role="alert">
                            ${response.error}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
                modal.find('.modal-body').prepend(alert);
            });
        }
    });
}

async function newUserForm() {
    let form = $(`#newUserForm`)

    const rolesResponse = await roleService.findAll();
    const rolesJson = rolesResponse.json();

    rolesJson.then(roles => {
        roles.forEach(role => {
            form.find('#roles').append(new Option(role.role, role.id, false,false));
        });
    });
}

async function saveNewUser() {
    $('#saveUserButton').click(async function(){
        let firstName = newUserForm.find('#firstName').val().trim();
        let lastName = newUserForm.find('#lastName').val().trim();
        let age = newUserForm.find('#age').val().trim();
        let username = newUserForm.find('#username').val().trim();
        let password = newUserForm.find('#password').val().trim();
        let rolesArray = newUserForm.find('#roles').val()
        let roles = []

        for (let r of roleJson) {
            for (let i = 0; i < rolesArray.length; i++) {
                if (r.id == rolesArray[i]) {
                    roles.push(r)
                }
            }
        }

        let data = {
            name: firstName,
            surname: lastName,
            age: age,
            email: username,
            password: password,
            roles: roles
        }

        const userResponse = await userService.add(data);

        if (userResponse.status === 201) {
            await viewAllUsers();
        } else if (userResponse.status === 400) {
            userResponse.json().then(response => {
                response.validationErrors.forEach(function(error){
                    newUserForm.find('#' + error.field).addClass('is-invalid');
                    newUserForm.find('#' + error.field).next('.invalid-feedback').text(error.message);
                });
            });
        } else {
            userResponse.json().then(response => {
                let alert = `<div class="alert alert-success alert-dismissible fade show col-12" role="alert">
                        ${response.error}
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>`;
                newUserForm.prepend(alert);
            });
        }
    });
}
