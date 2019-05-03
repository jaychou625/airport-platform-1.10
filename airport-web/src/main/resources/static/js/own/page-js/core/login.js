function checkForm() {
    let username = $("#username").val()
    let password = $("#password").val()
    if (!/^[a-zA-Z0-9_-]{5,16}$/.test(username)) {
        $("#login-info-label").text("用户名不规范")
        return false;
    }
    if (!/^(\w){6,20}$/.test(password)) {
        $("#login-info-label").text("密码不规范")
        return false;
    }
    return true

}

function login() {
    if (checkForm()) {
        let formValue = $("#login-form").serialize()
        call("post", "/login", formValue).done(function (result) {
            result = JSON.parse(result.data)
            if (result.status === 703) {
                location.href = "/main"
            } else {
                $("#login-info-label").text(result.data.error)
            }
        }).catch(function (err) {
            console.log(err)
        })
    }
}
