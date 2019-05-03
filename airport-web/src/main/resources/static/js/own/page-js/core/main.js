$(function () {

   $(".left-border").css("background", "none")

    $("#main-menu-ul").mCustomScrollbar({
        axis: "y",
        theme: "dark",
        autoHideScrollbar: true,
        autoExpandScrollbar: true,
        setWidth: 200
    })

    NProgress.configure({
        parent: "#main-content",
        trickleSpeed: 100,
    })

    loadHTML("/home", null, null, null)

    webSocketInitialization()
})