/*------------------------- 加载页面方法 ----------------------------*/
function loadHTML(url, params) {
    let content = $("#main-content");
    NProgress.start()
    call("get", url, params).done(function (result) {
        $(content).html(result.data)
        NProgress.done()
    }).catch(function (err) {
        throw err
    })
}