/*------------------------- ajax请求 ----------------------------*/
function call(method, url, params) {
    // 结果对象
    let result =  {
        status: "",
        data: "",
        error: ""
    }
    let def = $.Deferred()
    $.ajax(url, {
        type: method,
        timeout: 6000,
        cache: false,
        data: params,
        contentType: "application/x-www-form-urlencoded",
        ifModified: true,
        beforeSend: function (xmr) {
            console.log("ajax prepare send ... ")
            return true
        },
        dataFilter: function (data, type) {
            return data
        },
        success: function (data, status) {
            result.status = status
            result.data = data
            result.error = ""
            def.resolve(result)
        },
        error: function (xmr, status, error) {
            result.status = status
            result.data = ""
            result.error = error
            def.reject(result)
        },
        complete: function (xmr, textStatus) {
            let sessionStatus = xmr.getResponseHeader("session-status")
            if (sessionStatus == "timeout") {
                window.location.href = "/login"
            }
        }
    })
    return def.promise()
}

