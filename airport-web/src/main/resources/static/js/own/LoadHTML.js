/*------------------------- 加载页面 ----------------------------*/
function loadHTML(url, params) {
    if (params == null) {
        params = {}
    }
    let content = $("#main-content");
    NProgress.start()
    call("get", url, params).done(function (result) {
        $(content).html(result.data)
        NProgress.done()
    }).catch(function (err) {
        throw err
    })
}