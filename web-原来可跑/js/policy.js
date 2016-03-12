//pdf饼图示意
window.P = {};
(function () {
    var P = window.P;//定义属于policy的命名空间
    var $window = $(window);

    $(document).ready(function () {
        $(".title1").click(function () {
            $(".sub-table1").slideToggle("fast");
        });
        $(".title2").click(function () {
            $(".sub-table2").slideToggle("fast");
        });
        $(".title3").click(function () {
            $(".sub-table3").slideToggle("fast");
        });
        $(".title4").click(function () {
            $(".sub-table4").slideToggle("fast");
        });
        $(".title5").click(function () {
            $(".sub-table5").slideToggle("fast");
        });
    });

})();