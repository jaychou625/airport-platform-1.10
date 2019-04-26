$(function () {
    $("#html-content").mCustomScrollbar({
        axis: "y",
        theme: "dark",
        autoHideScrollbar: true,
        autoExpandScrollbar: true,
        mouseWheel: {
            preventDefault: true
        }
    })
    $("#page-car-table").mCustomScrollbar({
        axis: "x",
        theme: "dark",
        autoExpandScrollbar: true,
        mouseWheel: {
            preventDefault: true
        }
    })
})