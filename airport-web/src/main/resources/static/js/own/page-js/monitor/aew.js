/*------------------------- 加载预警信息为表格 此处Ajax分页展示 后续会有条件查询 ----------------------------*/
function loadAewInfoTableData(currentPage, pageSize) {
    /*------------------------- 条件参数 ----------------------------*/
    let params = {
        currentPage: currentPage,
        pageSize: pageSize
    }
    /*------------------------- Ajax请求数据 ----------------------------*/
    call("get", "/monitor/aew/list", params).done(function (result) {
        /*------------------------- 响应数据 ----------------------------*/
        let pageInfo = JSON.parse(result.data).data.pageInfo
        /*------------------------- 构建表格 ----------------------------*/
        $("#page-aew-table").jsGrid({
            width: "98%",
            data: pageInfo.list,
            fields: [
                {title: "序号", name: "aewInfoSeq", type: "text", align: "center"},
                {
                    title: "预警等级", name: "aew.aewLevel", type: "text", align: "center", filtercss: "jsgrid-filter-row",
                    itemTemplate: function (value, item) {
                        if (value === 1) {
                            return "<div class='aew-level-one'>" + "一级预警" + "</div>"
                        }
                        if (value === 2) {
                            return "<div class='aew-level-two'>" + "二级预警" + "</div>"
                        }
                        if (value === 3) {
                            return "<div class='aew-level-three'>" + "三级预警" + "</div>"
                        }
                    }
                },
                {
                    title: "时间", name: "aewInfoTime", align: "center"
                },
                {
                    title: "车型", name: "car.carType", align: "center"
                },
                {
                    title: "司机", name: "driver.driverName", align: "center"
                },
                {
                    title: "场景说明", name: "aew.aewScene", align: "center"
                },
                {
                    title: "操作", name: "[]", align: "center",
                    itemTemplate: function (value, item) {
                        item = JSON.stringify(item).replace(/"/g, '&quot;')
                        return '<button class="btn-check" onclick="aewDetail(' + item + ')">' + '查看' + '</button>'
                    }
                }
            ]
        })
        /*------------------------- 分页栏 ----------------------------*/
        $("#paging-bar").html(null)
        if (pageInfo.hasPreviousPage) {
            $("#paging-bar").append('<div class="paging-prev" onclick="loadAewInfoTableData(' + pageInfo.prePage + ', ' + pageInfo.pageSize + ')"><</div>')
        }
        $.each(pageInfo.navigatepageNums, function (index, ele) {
            if (pageInfo.pageNum === ele) {
                $("#paging-bar").append('<div class="page active" onclick="loadAewInfoTableData(' + ele + ', ' + pageInfo.pageSize + ')">' + ele + '</div>')

            } else {
                $("#paging-bar").append('<div class="page" onclick="loadAewInfoTableData(' + ele + ', ' + pageInfo.pageSize + ')">' + ele + '</div>')
            }
        })
        if (pageInfo.hasNextPage) {
            $("#paging-bar").append('<div class="paging-next" onclick="loadAewInfoTableData(' + pageInfo.nextPage + ', ' + pageInfo.pageSize + ')">></div>')
        }
    }).fail(function (err) {
        console.log(err)
    })
}


$(function () {
    loadAewInfoTableData(1, 10)
    $(".form-date").datetimepicker(
        {
            language: "zh-CN",
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2,
            forceParse: 0,
            format: "yyyy-mm-dd"
        })
})


function aewDetail(aewInfo) {
    console.log(aewInfo)
    let aewLevelTd;
    if (aewInfo.aew.aewLevel == 1) {
        aewLevelTd = "<div class='pull-right aewDetail-box-body-aew-item-body'>一级预警</div>"
    }
    if (aewInfo.aew.aewLevel == 2) {
        aewLevelTd = "<div class='pull-right aewDetail-box-body-aew-item-body'>二级预警</div>"
    }
    if (aewInfo.aew.aewLevel == 3) {
        aewLevelTd = "<div class='pull-right aewDetail-box-body-aew-item-body'>三级预警</div>"
    }
    bootbox.confirm({
        size: "large",
        closeButton: false,
        message: "<div class='aewDetail-box'> " +
            "<div class='aewDetail-box-title'>预警信息详情页</div>" +
            "<div class='aewDetail-box-body-car'>" +
            "<div class='pull-left aewDetail-box-body-car-left'>" +
            "<div class='aewDetail-box-body-car-item'>" +
            "<div class='pull-left aewDetail-box-body-car-item-title'>部门：</div>" +
            "<div class='pull-right aewDetail-box-body-car-item-body'>运行保障部</div>" +
            "</div>" +
            "<div class='aewDetail-box-body-car-item'>" +
            "<div class='pull-left aewDetail-box-body-car-item-title'>车辆类型：</div>" +
            "<div class='pull-right aewDetail-box-body-car-item-body'>" + aewInfo.car.carType + "</div>" +
            "</div>" +
            "<div class='aewDetail-box-body-car-item'>" +
            "<div class='pull-left aewDetail-box-body-car-item-title'>司机：</div>" +
            "<div class='pull-right aewDetail-box-body-car-item-body'>" + aewInfo.driver.driverName + "</div>" +
            "</div>" +
            "</div>" +
            "<div class='pull-left aewDetail-box-body-car-right'>" +
            "<div class='aewDetail-box-body-car-item'>" +
            "<div class='pull-left aewDetail-box-body-car-item-title'>科室：</div>" +
            "<div class='pull-right aewDetail-box-body-car-item-body'>水电管理室</div>" +
            "<div class='aewDetail-box-body-car-item'>" +
            "<div class='pull-left aewDetail-box-body-car-item-title'>车牌号码：</div>" +
            "<div class='pull-right aewDetail-box-body-car-item-body'>" + aewInfo.car.carNo + "</div>" +
            "</div>" +
            "<div class='aewDetail-box-body-car-item'>" +
            "<div class='pull-left aewDetail-box-body-car-item-title'>服务的航班：</div>" +
            "<div class='pull-right aewDetail-box-body-car-item-body'>" + aewInfo.planeSeq + "</div>" +
            "</div>" +
            "</div>" +
            "</div>" +
            "</div>" +
            "<div class='aewDetail-box-body-aew'>" +
            "<div class='aewDetail-box-body-aew-item'>" +
            "<div class='pull-left aewDetail-box-body-aew-item-title'>预警时间：</div>" +
            "<div class='pull-right aewDetail-box-body-aew-item-body'>" + aewInfo.aewInfoTime + "</div>" +
            "</div>" +
            "<div class='aewDetail-box-body-aew-item'>" +
            "<div class='pull-left aewDetail-box-body-aew-item-title'>预警等级：</div>" +
            aewLevelTd +
            "</div>" +
            "<div class='aewDetail-box-body-aew-item'>" +
            "<div class='pull-left aewDetail-box-body-aew-item-title'>预警场景：</div>" +
            "<div class='pull-right aewDetail-box-body-aew-item-body'>" + aewInfo.aew.aewScene + "</div>" +
            "</div>" +
            "</div>" +
            "</div>",
        buttons: {
            cancel: {
                label: '<i class="fa fa-times"></i> 取消',
                className: "btn-cancel"
            },
            confirm: {
                label: '<i class="fa fa-check"></i> 导出',
                className: "btn-export"
            },

        }, callback: function (result) {
        }
    })
}