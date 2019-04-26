$(function () {
    $("#main-menu-ul li").hover(function () {
        $(this).children(".left-border").css("background", "rgba(175, 45, 45, 1)");
    }, function () {
        $(this).children(".left-border").css("background", "none")
    })
})