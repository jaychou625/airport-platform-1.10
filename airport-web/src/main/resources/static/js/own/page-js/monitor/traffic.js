$(function () {

    mapInitialization("page-traffic-map")

    $("#map-info-body").mCustomScrollbar({
        theme: "dark",
        autoHideScrollbar: true,
        autoExpandScrollbar: true,
        mouseWheel: {
            preventDefault: true
        }
    })
})