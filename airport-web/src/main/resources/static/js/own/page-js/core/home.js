$(function () {

    mapInitialization("page-home-map-container")

    $("#html-content").mCustomScrollbar({
        axis: "y",
        theme: "dark",
        autoHideScrollbar: true,
        autoExpandScrollbar: true,
        mouseWheel: {
            preventDefault: true
        }
    })

    $("#aew-one").mCustomScrollbar({
        axis: "y",
        theme: "dark",
        autoHideScrollbar: true,
        autoExpandScrollbar: true,
        mouseWheel: {
            preventDefault: true
        }
    })

    $("#aew-two").mCustomScrollbar({
        axis: "y",
        theme: "dark",
        autoHideScrollbar: true,
        autoExpandScrollbar: true,
        mouseWheel: {
            preventDefault: true
        }
    })

    $("#aew-three").mCustomScrollbar({
        axis: "y",
        theme: "dark",
        autoHideScrollbar: true,
        autoExpandScrollbar: true,
        mouseWheel: {
            preventDefault: true
        }
    })

    $("#page-home-map-container").hover(function () {
        $("#html-content").mCustomScrollbar("disable")
    }, function () {
        $("#html-content").mCustomScrollbar("update")
    })


      let chartTs = echarts.init(document.getElementById("page-home-pie-body"));
      let optionTsPie = {
          tooltip: {
              trigger: 'item',
              formatter: "{a} <br/>{b} : {c} ({d}%)"
          },
          series: [
              {
                  type: 'pie',
                  radius: '50%',
                  center: ['45%', '48%'],
                  selectedMode: 'single',
                  data: [
                      {value: 535, name: '已完成运行数量'},
                      {value: 510, name: '正在运行数量'},
                      {value: 634, name: '取消数量'},
                      {value: 735, name: '延误数量'},
                      {value: 735, name: '预警数量'}
                  ],
                  itemStyle: {
                      emphasis: {
                          shadowBlur: 10,
                          shadowOffsetX: 0,
                          shadowColor: 'rgba(0, 0, 0, 0.5)'
                      }
                  }
              }
          ]
      }
      chartTs.setOption(optionTsPie);
      let chartEw = echarts.init(document.getElementById("page-home-line-body"));
      let optionEwLine = {
          tooltip: {
              trigger: "none",
              axisPointer: {
                  type: "cross"
              }
          },
          grid: {
              show: true,
              top: 30,
              left: 20,
              right: 50,
              bottom: 30,
              containLabel: true,
              backgroundColor: "rgba(232,232,232,0.53)",
          },
          xAxis: {
              type: 'category',
              axisTick: {
                  alignWithLabel: true
              },
              nameTextStyle: {
                  color: "rgba(153,153,153,1)"
              },
              name: "时间",
              axisTick: {
                  alignWithLabel: true
              },
              axisPointer: {
                  axis: "y",
                  snap: true,
                  label: {
                      formatter: function (params) {
                          return params.value
                      }
                  },
                  lineStyle: {
                      type: "solid",
                      width: 2
                  }
              },
              axisLabel: {
                  color: "rgba(153,153,153,1)"
              },
              data: ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24'],

          },
          yAxis: {
              type: 'value',
              max: 50,
              name: "数量",
              nameTextStyle: {
                  color: "rgba(153,153,153,1)"
              },
              axisLabel: {
                  color: "rgba(153,153,153,1)"
              }
          },
          series: [{
              data: [11, 22, 33, 44, 23, 45, 22],
              type: 'line',
              lineStyle: {
                  color: "rgba(175,44,48,1)"
              }
          }, {
              data: [38, 25, 12, 16, 44, 5, 33],
              type: 'line',
              lineStyle: {
                  color: "rgba(250,209,78,1)"
              }
          }, {
              data: [0, 33, 22, 32, 23, 14, 26],
              type: 'line',
              lineStyle: {
                  color: "rgba(21,205,150,1)"
              }
          }]
      }
      chartEw.setOption(optionEwLine)
})