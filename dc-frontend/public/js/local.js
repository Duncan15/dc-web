//var baseURL="http://localhost:8082"
//var baseURL="http://10.24.11.220:8082"
var baseURL = "http://10.13.56.36:8082"
$(function() {
  var $requestBtn = $("#request-btn");
  var $crawlingBtn = $("#crawling-btn");
  var $monitorBtn = $("#monitor-btn");
  var $deliveryBtn = $("#delivery-btn");
  var $configBtn = $("#config-btn");
  var $sensingBtn = $("#sensing-btn");
  var $estimateBtn = $("#estimate-btn");
  var $showTime = $("#showTime");
  //Bellow is code for estimate.
  $showTime.on('click',function (event) {

      $.ajax({
          url:baseURL+"/api/ClickBtn",
          data:{},
          type:"get",
          dataType:"text"

      }).done(function (data) {
          alert(data)
      })

  })
  $estimateBtn.on('click', function(event) {

    var $estiOperateBtn = $("#esti-operate-btn");

    $estiOperateBtn.off('click');
    $estiOperateBtn.on('click', function() {
      var tmpl = $.templates("#esti-control-list");
      $.ajax({
        url: baseURL + '/api/datacrawling/estimate/show/all',
        type: 'GET',
        dataType: 'json',
      })
        .done(function(data) {
          if (data['errno'] != 0) {
            alert("服务器错误");
          } else {
            var html = tmpl.render(data['data']);
            $("#esti-control-list-content").html(html);

            //点击修改按钮之后就进入了另外的一个GET请求。
            $(".change-estimate").off('click');
            $(".change-estimate").on('click', function(event) {
              //获取到按钮父标签的id才能GET到这一行的信息
              var $a = $(event.target);
              var $th = $a.parent('th');
              var $id = $th.siblings('.esti-id');
              var id = $id.text();
              // console.log("id is "+id);
              $.ajax({
                url: baseURL + '/api/datacrawling/estimate/change/' + id,
                type: 'GET',
                dataType: 'json'
              })
                .done(function(data) {
                  console.log("success");
                  if (data['errno'] != 0) {
                    alert("服务器错误");
                  } else {
                    //下面用获取到的这一行的数据对相对应的html表格进行赋值。
                    $("#conf-estiId").val(id);
                    $("#conf-linksXpath").val(data['data']['linksXpath']);
                    $("#conf-contentXpath").val(data['data']['contentXpath']);
                    $("#conf-walkTimes").val(data['data']['walkTimes']);
                    $("#conf-startWord").val(data['data']['startWord']);
                    $("#startPageNum").val(data['data']['startPageNum']);
                    $("#paramValueList").val(data['data']['paramValueList']);
                    $("#paramQuery").val(data['data']['paramQuery']);
                    $("#prefix").val(data['data']['prefix']);
                    $("#paramPage").val(data['data']['paramPage']);
                    $("#paramList").val(data['data']['paramList']);
                    $("#conf-contentLocation").val(data['data']['contentLocation'] || 'text');
                    $("#conf-querySend").val(data['data']['querySend'] || 'get');
                  }

                  $("#edit-estiamte .return-btn").off('click');
                  $("#edit-estimate .return-btn").on('click', function(event) {
                    event.preventDefault();
                    /* Act on the event */
                    //移除当前的div，激活原来的div。
                    $("#edit-estimate").removeClass("active");
                    $("#esti-task-monitor").addClass('active');
                    //confirmBtn的作用是返回到列表页面。
                    $estiOperateBtn.click();
                  });
                  var validator = $("#confirm-estimate-form").validate({
                    submitHandler: function() {
                      var id = $("#conf-estiId").val();
                      var linksXpath = $("#conf-linksXpath").val().trim();
                      // var pagesInfoId = $("#conf-pagesInfoId").val().trim();
                      var contentXpath = $("#conf-contentXpath").val().trim();
                      var walkTimes = $("#conf-walkTimes").val().trim();
                      var startWord = $("#conf-startWord").val().trim();
                      var contentLocation = $("#conf-contentLocation").val().trim();
                      var querySend = $("#conf-querySend").val().trim();

                      if (linksXpath == "") {
                        alert("输入不能为空");
                      }

                      $.ajax({
                        url: baseURL + '/api/datacrawling/estimate/update/' + id,
                        type: 'POST',
                        dataType: 'json',
                        data: {
                          linksXpath: linksXpath,
                          // pagesInfoId: pagesInfoId,
                          contentXpath: contentXpath,
                          walkTimes: walkTimes,
                          startWord: startWord,
                          contentLocation: contentLocation,
                          querySend: querySend
                        }
                      })
                        .done(function(data) {
                          console.log("success");
                          if (data['errno'] != 0) {
                            alert("服务器错误");
                          } else {
                            alert("修改成功");
                            $("#edit-estimate .return-btn").click();
                          }
                        })
                        .fail(function() {
                          console.log("error");
                        })
                        .always(function() {
                          console.log("complete");
                        });
                    }
                  });
                  validator.resetForm();
                })
                .fail(function() {
                  console.log("error");
                })
                .always(function() {
                  console.log("complete");
                });
            });//change js end

            //表格数据传输正常。
            //下面总体逻辑运转正常。
            //接下来解决后端接受GET请求，启动线程的问题。

            $(".esti-control-btn").off('click');
            $(".esti-control-btn").on('click', function() {
              var $tr = $(this).parents("tr");
              var estiId = $tr.find("th.esti-id").text().trim();
              var action = $(this).attr("name");
              $.LoadingOverlay("show");
              $.ajax({
                url: baseURL + '/api/datacrawling/estimoni?action=option&option=' + action + '&estiId=' + estiId,
                type: 'GET',
                dataType: 'json'
              })
                .done(function(data) {
                  // console.log(data['data']);
                  $.LoadingOverlay("hide", true);
                  alert(data['data']['msg']);
                })
                .fail(function() {
                  console.log("error");
                })
                .always(function() {
                  console.log("complete");
                });
            });


          }
        })
        .fail(function() {
          console.log("error");
        })
        .always(function() {
          console.log("complete");
        });


      // setInterval每隔一定时间就调用一次函数。3000毫秒=3秒。
      var handle = setInterval(function() {
        var tmpl = $.templates("#esti-control-list");
        $.ajax({
          url: baseURL + '/api/datacrawling/estimate/show/all',
          type: 'GET',
          dataType: 'json',

        })
          .done(function(data) {
            if (data['errno'] != 0) {
              alert("服务器错误");
            } else {
              var html = tmpl.render(data['data']);
              $("#esti-control-list-content").html(html);

              //点击修改按钮之后就进入了另外的一个GET请求。
              $(".change-estimate").off('click');
              $(".change-estimate").on('click', function(event) {
                //获取到按钮父标签的id才能GET到这一行的信息
                var $a = $(event.target);
                var $th = $a.parent('th');
                var $id = $th.siblings('.esti-id');
                var id = $id.text();
                // console.log("id is "+id);
                $.ajax({
                  url: baseURL + '/api/datacrawling/estimate/change/' + id,
                  type: 'GET',
                  dataType: 'json'
                })
                  .done(function(data) {
                    console.log("success");
                    if (data['errno'] != 0) {
                      alert("服务器错误");
                    } else {
                      //下面用获取到的这一行的数据对相对应的html表格进行赋值。
                      $("#conf-estiId").val(id);
                      $("#conf-linksXpath").val(data['data']['linksXpath']);
                      // $("#conf-pagesInfoId").val(data['data']['pagesInfoId']);
                      $("#conf-contentXpath").val(data['data']['contentXpath']);
                      $("#conf-walkTimes").val(data['data']['walkTimes']);
                      $("#conf-startWord").val(data['data']['startWord']);
                      $("#startPageNum").val(data['data']['startPageNum']);
                      $("#paramValueList").val(data['data']['paramValueList']);
                      $("#paramQuery").val(data['data']['paramQuery']);
                      $("#prefix").val(data['data']['prefix']);
                      $("#paramPage").val(data['data']['paramPage']);
                      $("#paramList").val(data['data']['paramList']);
                      $("#conf-contentLocation").val(data['data']['contentLocation']);
                      $("#conf-querySend").val(data['data']['querySend']);
                    }

                    $("#edit-estiamte .return-btn").off('click');
                    $("#edit-estimate .return-btn").on('click', function(event) {
                      event.preventDefault();
                      /* Act on the event */
                      //移除当前的div，激活原来的div。
                      $("#edit-estimate").removeClass("active");
                      $("#esti-task-monitor").addClass('active');
                      //confirmBtn的作用是返回到列表页面。
                      $estiOperateBtn.click();
                    });
                    var validator = $("#confirm-estimate-form").validate({
                      submitHandler: function() {
                        var id = $("#conf-estiId").val();
                        var linksXpath = $("#conf-linksXpath").val().trim();
                        var contentXpath = $("#conf-contentXpath").val().trim();
                        var walkTimes = $("#conf-walkTimes").val().trim();
                        var startWord = $("#conf-startWord").val().trim();
                        var contentLocation = $("#conf-contentLocation").val().trim();
                        var querySend = $("#conf-querySend").val().trim();

                        if (linksXpath == "") {
                          alert("输入不能为空");
                        }

                        $.ajax({
                          url: baseURL + '/api/datacrawling/estimate/update/' + id,
                          type: 'POST',
                          dataType: 'json',
                          data: {
                            linksXpath: linksXpath,
                            // pagesInfoId: pagesInfoId,
                            contentXpath: contentXpath,
                            walkTimes: walkTimes,
                            startWord: startWord,
                            contentLocation: contentLocation,
                            querySend: querySend
                          }
                        })
                          .done(function(data) {
                            console.log("success");
                            if (data['errno'] != 0) {
                              alert("服务器错误");
                            } else {
                              alert("修改成功");
                              $("#edit-estimate .return-btn").click();
                            }
                          })
                          .fail(function() {
                            console.log("error");
                          })
                          .always(function() {
                            console.log("complete");
                          });
                      }
                    });
                    validator.resetForm();
                  })
                  .fail(function() {
                    console.log("error");
                  })
                  .always(function() {
                    console.log("complete");
                  });
              });//change js end

              $(".esti-control-btn").off('click');
              $(".esti-control-btn").on('click', function() {
                var $tr = $(this).parents("tr");
                var estiId = $tr.find("th.esti-id").text().trim();
                var action = $(this).attr("name");
                $.LoadingOverlay("show");
                $.ajax({
                  url: baseURL + '/api/datacrawling/estimoni?action=option&option=' + action + '&estiId=' + estiId,
                  type: 'GET',
                  dataType: 'json'
                })
                  .done(function(data) {
                    $.LoadingOverlay("hide", true);
                    alert(data['data']['msg']);
                  })
                  .fail(function() {
                    console.log("error");
                  })
                  .always(function() {
                    console.log("complete");
                  });
              });
            }
          })
          .fail(function() {
            console.log("error");
          })
          .always(function() {
            console.log("complete");
          });
        if (handle == undefined) {
          console.log("undefined");
        } else {
          if (!$("#estimate").hasClass('active')) {
            clearInterval(handle);
          }
        }
      }, 3000);


    });

    $estiOperateBtn.click();
  })



  $sensingBtn.on('click', function(event) {

    var $urlsensingBtn = $("#url-sensing-btn");
    var $resultsensingBtn = $("#result-sensing-btn");
    $urlsensingBtn.off('click');
    $urlsensingBtn.on('click', function() {
      var tmpl = $.templates("#sensingAll-list");
      $.ajax({
        url: baseURL + '/api/datacrawling/sense/all',
        type: 'POST',
        dataType: 'json',

      })
        .done(function(data) {
          if (data['errno'] != 0) {
            alert("服务器错误");
          } else {
            var html = tmpl.render(data['data']);
            $("#url-sensingAll-list-content").html(html);

            $(".sense-control-btn").off('click');
            $(".sense-control-btn").on('click', function() {
              var $tr = $(this).parents("tr");
              var senseId = $tr.find("th.sense-id").text().trim();
              var action = $(this).attr("name");
              // $.LoadingOverlay("show");
              $.ajax({
                url: baseURL + '/api/datacrawling/sensemoni/option',
                type: 'POST',
                dataType: 'json',
                data: {
                  action: action,
                  senseId: senseId,
                }
              })
                .done(function(data) {
                  // console.log(data['data']);
                  $.LoadingOverlay("hide", true);
                  alert(data['data']['msg']);
                })
                .fail(function() {
                  console.log("error");
                })
                .always(function() {
                  console.log("complete");
                });
            });

            $(".show-sense").off('click');
            $(".show-sense").on('click', function() {
              var $tr = $(this).parents("tr");
              var senseId = $tr.find("th.sense-id").text().trim();
              var tmpl = $.templates("#sensing-list");
              $.ajax({
                url: baseURL + '/api/datacrawling/sense/show',
                type: 'POST',
                dataType: 'json',
                data: {
                  getId: senseId,
                }
              })
                .done(function(data) {
                  console.log("success");
                  if (data['errno'] != 0) {
                    alert("服务器错误");
                  } else {
                    var html = tmpl.render(data['data']);
                    $("#url-sensing-list-content").html(html);
                    // $.LoadingOverlay("show");
                    $resultsensingBtn.click();
                  }

                })
                .fail(function() {
                  console.log("error");
                })
                .always(function() {
                  console.log("complete");
                });


            })
          }

        })
        .fail(function() {
          console.log("error");
        })
        .always(function() {
          console.log("complete");
        });


      // setInterval每隔一定时间就调用一次函数。3000毫秒=3秒。
      var handle = setInterval(function() {
        var tmpl = $.templates("#sensingAll-list");
        $.ajax({
          url: baseURL + '/api/datacrawling/sense/all',
          type: 'POST',
          dataType: 'json',

        })
          .done(function(data) {
            if (data['errno'] != 0) {
              alert("服务器错误");
            } else {
              var html = tmpl.render(data['data']);
              $("#url-sensingAll-list-content").html(html);

              $(".sense-control-btn").off('click');
              $(".sense-control-btn").on('click', function() {
                var $tr = $(this).parents("tr");
                var senseId = $tr.find("th.sense-id").text().trim();
                var action = $(this).attr("name");
                // $.LoadingOverlay("show");
                $.ajax({
                  url: baseURL + '/api/datacrawling/sensemoni/option',
                  type: 'POST',
                  dataType: 'json',
                  data: {
                    action: action,
                    senseId: senseId,
                  }
                })
                  .done(function(data) {
                    // console.log(data['data']);
                    $.LoadingOverlay("hide", true);
                    alert(data['data']['msg']);
                  })
                  .fail(function() {
                    console.log("error");
                  })
                  .always(function() {
                    console.log("complete");
                  });
              });

              $(".show-sense").off('click');
              $(".show-sense").on('click', function() {
                var $tr = $(this).parents("tr");
                var senseId = $tr.find("th.sense-id").text().trim();
                var tmpl = $.templates("#sensing-list");
                $.ajax({
                  url: baseURL + '/api/datacrawling/sense/show',
                  type: 'POST',
                  dataType: 'json',
                  data: {
                    getId: senseId,
                  }
                })
                  .done(function(data) {
                    console.log("success");
                    if (data['errno'] != 0) {
                      alert("服务器错误");
                    } else {
                      var html = tmpl.render(data['data']);
                      $("#url-sensing-list-content").html(html);
                      // $.LoadingOverlay("show");
                      $resultsensingBtn.click();
                    }

                  })
                  .fail(function() {
                    console.log("error");
                  })
                  .always(function() {
                    console.log("complete");
                  });


              })

            }

          })
          .fail(function() {
            console.log("error");
          })
          .always(function() {
            console.log("complete");
          });
      }, 3000);


    });


    // $("#page-url").val("");
    // var validator=$("#url-sensing-form").validate({
    //     submitHandler:function () {
    //       var  pageUrl=$("#page-url").val.trim();
    //       if(pageUrl==""){
    //         alert("输入不能为空");
    //       }
    //       sessionStorage.setItem("senseUrl",pageUrl)
    //       $.ajax({
    //           url:baseURL+'/api/datacrawling/sense/new',
    //           type:'POST',
    //           dataType: 'json',
    //           data: {
    //               pageUrl: pageUrl,
    //           }
    //       })
    //           .done(function(data) {
    //               console.log("success");
    //               if(data['errno']!=0){
    //                   alert("服务器错误");
    //               }else{
    //                   alert("新建成功");
    //                   $resultsensingBtn.click();
    //               }
    //           })
    //           .fail(function() {
    //               console.log("error");
    //           })
    //           .always(function() {
    //               console.log("complete");
    //           });
    //     }
    // })
    //   validator.resetForm();
    //


    // $resultsensingBtn.off('click');
    // $resultsensingBtn.on('click',function () {
    //     var tmpl=$.templates("#sensing-list");
    //     var getUrl="all";
    //     var senenUrl=sessionStorage.getItem("senseUrl");
    //     if(senenUrl!=null){
    //         getUrl=senenUrl;
    //     }
    //     $.ajax({
    //         url: baseURL+'/api/datacrawling/sense/show',
    //         type: 'POST',
    //         dataType: 'json',
    //         data: {
    //             getUrl: getUrl,
    //         }
    //     })
    //     .done(function(data) {
    //         console.log("success");
    //         if(data['errno']!=0){
    //             alert("服务器错误");
    //         }else{
    //             var html=tmpl.render(data['data']);
    //             $("#url-sensing-list-content").html(html);
    //         }
    //     })
    //     .fail(function() {
    //         console.log("error");
    //     })
    //     .always(function() {
    //         console.log("complete");
    //     });
    //
    //     var handle=setInterval(function(){
    //         var getUrl="all";
    //         var senenUrl=sessionStorage.getItem("senseUrl");
    //         if(senenUrl!=null){
    //             getUrl=senenUrl;
    //         }
    //         $.ajax({
    //             url: baseURL+'/api/datacrawling/sense/show',
    //             type: 'POST',
    //             dataType: 'json',
    //             data: {
    //                 getUrl: getUrl,
    //             }
    //         })
    //             .done(function(data) {
    //                 console.log("success");
    //                 if(data['errno']!=0){
    //                     alert("服务器错误");
    //                 }else{
    //                     var html=tmpl.render(data['data']);
    //                     $("#url-sensing-list-content").html(html);
    //                 }
    //             })
    //             .fail(function() {
    //                 console.log("error");
    //             })
    //             .always(function() {
    //                 console.log("complete");
    //             });
    //     },3000);
    //
    //
    // })


    $urlsensingBtn.click();
  })
  $requestBtn.on('click', function(event) {
    var $newRequestBtn = $("#new-request-btn");
    var $requestConfirmBtn = $("#request-confirm-btn");
    var $siteResourceBtn = $("#site-resource-btn");
    $newRequestBtn.off('click');
    $newRequestBtn.on('click', function() {
      $("#request-name").val("");
      $("#request-desc").val("");
      var validator = $("#request-form").validate({
        submitHandler: function() {
          var requestName = $("#request-name").val().trim();
          var requestDesc = $("#request-desc").val().trim();
          if (requestName == "") {
            alert("输入不能为空");
          }
          $.ajax({
            url: baseURL + '/api/datacrawling/request/new',
            type: 'POST',
            dataType: 'json',
            data: {
              requestName: requestName,
              requestDesc: requestDesc
            }
          })
            .done(function(data) {
              console.log("success");
              if (data['errno'] != 0) {
                alert("服务器错误");
              } else {
                alert("新建成功");
                $requestConfirmBtn.click();
              }
            })
            .fail(function() {
              console.log("error");
            })
            .always(function() {
              console.log("complete");
            });
        }
      });
      validator.resetForm();
    });
    $requestConfirmBtn.off('click');
    $requestConfirmBtn.on('click', function() {
      var tmpl = $.templates("#request-list");
      $.ajax({
        url: baseURL + '/api/datacrawling/request/all',
        type: 'GET',
        dataType: 'json'
      })
        .done(function(data) {
          console.log("success");
          if (data['errno'] != 0) {
            alert("服务器错误");
          } else {
            var content = data['data'];
            var html = tmpl.render(content);
            $("#request-list-content").html(html);
            $(".change-request").off('click');
            $(".change-request").on('click', function(event) {
              var $a = $(event.target);
              var $th = $a.parent('th');
              var $id = $th.siblings('.request-id');
              var id = $id.text();
              $.ajax({
                url: baseURL + '/api/datacrawling/request/' + id,
                type: 'GET',
                dataType: 'json'
              })
                .done(function(data) {
                  console.log("success");
                  if (data['errno'] != 0) {
                    alert("服务器错误");
                  } else {
                    $("#confirm-request-id").val(id);
                    $("#confirm-request-name").val(data['data']['requestName']);
                    $("#confirm-request-desc").val(data['data']['requestDesc']);
                  }
                  $("#edit-request .return-btn").off('click');
                  $("#edit-request .return-btn").on('click', function(event) {
                    event.preventDefault();
                    /* Act on the event */
                    $("#edit-request").removeClass("active");
                    $("#request-confirm").addClass('active');
                    $requestConfirmBtn.click();
                  });
                  var validator = $("#confirm-request-form").validate({
                    submitHandler: function() {
                      var id = $("#confirm-request-id").val();
                      var requestName = $("#confirm-request-name").val().trim();
                      var requestDesc = $("#confirm-request-desc").val().trim();
                      if (requestName == "") {
                        alert("输入不能为空");
                      }
                      $.ajax({
                        url: baseURL + '/api/datacrawling/request/' + id,
                        type: 'POST',
                        dataType: 'json',
                        data: {
                          requestName: requestName,
                          requestDesc: requestDesc
                        }
                      })
                        .done(function(data) {
                          console.log("success");
                          if (data['errno'] != 0) {
                            alert("服务器错误");
                          } else {
                            alert("修改成功");
                            $("#edit-request .return-btn").click();
                          }
                        })
                        .fail(function() {
                          console.log("error");
                        })
                        .always(function() {
                          console.log("complete");
                        });
                    }
                  });
                  validator.resetForm();
                })
                .fail(function() {
                  console.log("error");
                })
                .always(function() {
                  console.log("complete");
                });
            });
          }
        })
        .fail(function() {
          console.log("error");
        })
        .always(function() {
          console.log("complete");
        });

    });
    $siteResourceBtn.off('click');
    $siteResourceBtn.on('click', function() {
      var tmpl = $.templates("#site-list");
      $.ajax({
        url: baseURL + '/api/datacrawling/task/all',
        type: 'GET',
        dataType: 'json'
      })
        .done(function(data) {
          console.log("success");
          if (data['errno'] != 0) {
            alert("服务器错误");
          } else {
            var content = data['data'];
            var html = tmpl.render(content);
            $("#site-list-content").html(html);
          }
        })
        .fail(function() {
          console.log("error");
        })
        .always(function() {
          console.log("complete");
        });

    });
    $newRequestBtn.click();
  });
  $crawlingBtn.on('click', function(event) {
    var $newTaskBtn = $("#new-task-btn");
    var $paserRuleBtn = $("#paser-rule-btn");
    var $templateExtractBtn = $("#content-extract-btn");
    var $taskMonitorBtn = $("#task-monitor-btn");
    $newTaskBtn.off('click');
    $newTaskBtn.on('click', function() {
      $("input[name='running-mode'][value='unstructed']").on("click", function() {
        $("#driver-select-group").hide("slow");
        $("#base-select-group").show('slow');
      });
      $("input[name='running-mode'][value='structed']").on("click", function() {
        $("#driver-select-group").show("slow");
        $("#base-select-group").hide('slow');
      })
      $("input[name='running-mode'][value='unstructed']").click();
      var $taskName = $("#task-name");
      var $workPath = $("#work-path");
      var $siteLink = $("#site-link");
      $taskName.val("");
      $workPath.val("");
      $siteLink.val("");
      var validator = $("#task-form").validate({
        submitHandler: function() {
          var taskName = $taskName.val();
          var workPath = $workPath.val();
          var runningMode = $("input[name='running-mode']:checked").val();
          var driver = $("input[name='driver-select']:checked").val();
          var base = $("input[name='base-select']:checked").val();
          var siteURL = $siteLink.val();
          if (taskName == "" || workPath == "" || siteURL == "") {
            alert("输入不能为空");
            return;
          }
          if (runningMode == 'unstructed') {
            driver = 'none';
          } else if (runningMode == 'structed') {
            base = "apiBased";
          }
          $.ajax({
            url: baseURL + '/api/datacrawling/task/new',
            type: 'POST',
            dataType: 'JSON',
            data: {
              taskName: taskName,
              workPath: workPath,
              runningMode: runningMode,
              driver: driver,
              base: base,
              siteURL: siteURL,
            }
          })
            .done(function(data) {
              console.log("success");
              if (data['errno'] != 0) {
                alert(data['data']['msg']);
              } else {
                alert("新建成功");
                $paserRuleBtn.click();
              }
            })
            .fail(function() {
              console.log("error");
            })
            .always(function() {
              console.log("complete");
            });

        }
      });
      validator.resetForm();
    });
    $paserRuleBtn.off('click');
    $paserRuleBtn.on('click', function() {
      var tmpl = $.templates("#rule-list");
      $.ajax({
        url: baseURL + '/api/datacrawling/task/all',
        type: 'GET',
        dataType: 'json'
      })
        .done(function(data) {
          console.log("success");
          if (data['errno'] != 0) {
            alert("服务器错误");
          } else {
            var content = data['data'];
            var html = tmpl.render(content);
            $("#rule-list-content").html(html);
            $(".delete-rule").off('click');
            $(".delete-rule").on('click', function(event) {
              var $tr = $(this).parents("tr");
              var taskID = $tr.find("th.rule-id").text();
              $.ajax({
                url: baseURL + '/api/datacrawling/task/' + taskID,
                type: 'DELETE',
                dataType: 'json'
              }).done(function(data){
                console.log(data);
              })
            });
            $(".change-rule").off('click');
            $(".change-rule").on('click', function(event) {
              var $tr = $(this).parents("tr");
              var runningMode = $tr.find("th[name='running-mode']").text().trim();
              var subMode = $tr.find("th[name='sub-mode']").text().trim();
              var $unstructedUrlbased = $("#unstructed-urlbased");
              var $unstructedApibased = $("#unstructed-apibased");
              var $unstructedJsonBased = $("#unstructed-jsonbased");

              var $structedUndriver = $("#structed-undriver");
              var $structedDriver = $("#structed-driver");
              var $structedApi = $("#structed-api")

              if (runningMode == "文本型") {
                if (subMode == "基于页面刷新") {
                  $unstructedJsonBased.hide();
                  $unstructedUrlbased.show();
                  $unstructedApibased.hide();
                } else if (subMode == '基于接口刷新') {
                  $unstructedUrlbased.hide();
                  $unstructedApibased.show();
                  $unstructedJsonBased.hide();
                } else {
                  $unstructedUrlbased.hide();
                  $unstructedApibased.hide();
                  $unstructedJsonBased.show();
                }
                $structedDriver.hide();
                $structedUndriver.hide();
                $structedApi.hide();
              } else if (runningMode == "结构型") {
                if (subMode == "基于浏览器驱动") {
                  $structedDriver.show();
                  $structedApi.hide();
                  $structedUndriver.hide();
                } else if (subMode == "基于链接") {
                  $structedDriver.hide();
                  $structedApi.hide();
                  $structedUndriver.show();
                } else {
                  $structedApi.show();
                  $structedDriver.hide();
                  $structedUndriver.hide();
                }
                $unstructedApibased.hide();
                $unstructedUrlbased.hide();
                $unstructedJsonBased.hide();
              }
              var taskID = $tr.find("th.rule-id").text();
              $(".rule-config-form .rule-id").val(taskID);

              var $urlParamConfigBtn = $("#url-param-config-btn");
              var $loginParamConfigBtn = $("#login-param-config-btn");
              var $downloadParamConfigBtn = $("#download-param-config-btn");
              $urlParamConfigBtn.off('click');
              $urlParamConfigBtn.on('click', { taskID: taskID, runningMode: runningMode, subMode: subMode}, function(event) {
                var taskID = event.data.taskID;
                var form = null;
                if (runningMode == '文本型') {
                  if (subMode == "基于页面刷新") {
                    form = $("#unstructed-urlbased");
                    $.ajax({
                      url: baseURL + '/api/datacrawling/task/' + taskID,
                      type: 'GET',
                      dataType: 'json'
                    })
                      .done(function(data) {
                        console.log("success");
                        if (data['errno'] != 0) {
                          alert("服务器错误");
                        } else {
                          form.find("p[name='site-link']").text(data['data']['siteURL']);
                          form.find("input[name='search-link']").val(data['data']['searchURL']);
                          form.find("input[name='keyword-name']").val(data['data']['keywordName']);
                          form.find("input[name='page-name']").val(data['data']['pageParamName']);
                          form.find("input[name='page-value']").val(data['data']['pageParamValue']);
                          form.find("input[name='infoLinkXpath']").val(data['data']['infoLinkXpath']);
                          form.find("input[name='other-param-name']").val(data['data']['otherParamName']);
                          form.find("input[name='other-param-value']").val(data['data']['otherParamValue']);
                        }
                      })
                      .fail(function() {
                        console.log("error");
                      })
                      .always(function() {
                        console.log("complete");
                      });
                    var validator = form.validate({
                      submitHandler: function() {
                        var id = form.find("input.rule-id").val();
                        var pageParamValue = form.find("input[name='page-value']").val().trim();
                        if (pageParamValue.split(",").length != 2) {
                          alert("开始页面号输入错误");
                          return;
                        }
                        $.ajax({
                          url: baseURL + '/api/datacrawling/task/urlparam/' + id,
                          type: 'POST',
                          dataType: 'json',
                          data: {
                            searchURL: form.find("input[name='search-link']").val().trim(),
                            keywordName: form.find("input[name='keyword-name']").val().trim(),
                            pageParamName: form.find("input[name='page-name']").val().trim(),
                            infoLinkXpath: form.find("input[name='infoLinkXpath']").val().trim(),
                            pageParamValue: form.find("input[name='page-value']").val().trim(),
                            otherParamName: form.find("input[name='other-param-name']").val().trim(),
                            otherParamValue: form.find("input[name='other-param-value']").val().trim()
                          }
                        })
                          .done(function(data) {
                            console.log("success");
                            if (data['errno'] != 0) {
                              alert(data['data']['msg']);
                            } else {
                              alert('修改成功');
                              form.find("button[type='submit']").blur();
                              $urlParamConfigBtn.click();
                            }
                          })
                          .fail(function() {
                            console.log("error");
                          })
                          .always(function() {
                            console.log("complete");
                          });
                      }
                    });
                    validator.resetForm();
                  } else if (subMode == "基于接口刷新") {
                    form = $("#unstructed-apibased");
                    $.ajax({
                      url: baseURL + '/api/datacrawling/task/' + taskID,
                      type: 'GET',
                      dataType: 'json'
                    })
                      .done(function(data) {
                        console.log("success");
                        if (data['errno'] != 0) {
                          alert("服务器错误");
                        } else {
                          form.find("p[name='site-link']").text(data['data']['siteURL']);
                          form.find("input[name='search-link']").val(data['data']['searchURL']);
                          form.find("input[name='inputXpath']").val(data['data']['inputXpath']);
                          form.find("input[name='submitXpath']").val(data['data']['inputSubmitXpath']);
                          form.find("input[name='infoLinkXpath']").val(data['data']['infoLinkXpath']);
                          form.find("input[name='payloadXpath']").val(data['data']['payloadXpath']);
                        }
                      })
                      .fail(function() {
                        console.log("error");
                      })
                      .always(function() {
                        console.log("complete");
                      });
                    var validator = form.validate({
                      submitHandler: function() {
                        var id = form.find("input.rule-id").val();
                        var payloadXpath = form.find("input[name='payloadXpath']").val().trim();
                        if (payloadXpath.split(",").length != 2) {
                          alert("数据Xpath输入格式错误");
                          return;
                        }
                        $.ajax({
                          url: baseURL + '/api/datacrawling/task/urlparam/' + id,
                          type: 'POST',
                          dataType: 'json',
                          data: {
                            searchURL: form.find("input[name='search-link']").val().trim(),
                            inputXpath: form.find("input[name='inputXpath']").val().trim(),
                            inputSubmitXpath: form.find("input[name='submitXpath']").val().trim(),
                            infoLinkXpath: form.find("input[name='infoLinkXpath']").val().trim(),
                            payloadXpath: payloadXpath,
                          }
                        })
                          .done(function(data) {
                            console.log("success");
                            if (data['errno'] != 0) {
                              alert(data['data']['msg']);
                            } else {
                              alert('修改成功');
                              form.find("button[type='submit']").blur();
                              $urlParamConfigBtn.click();
                            }
                          })
                          .fail(function() {
                            console.log("error");
                          })
                          .always(function() {
                            console.log("complete");
                          });
                      }
                    });
                    validator.resetForm();
                  } else {
                    // 基于json刷新
                    form = $("#unstructed-jsonbased");
                    $.ajax({
                      url: baseURL + '/api/datacrawling/task/' + taskID,
                      type: 'GET',
                      dataType: 'json'
                    })
                      .done(function(data) {
                        console.log("success");
                        if (data['errno'] != 0) {
                          alert("服务器错误");
                        } else {
                          form.find("p[name='site-link']").text(data['data']['siteURL']);
                          form.find("input[name='prefix']").val(data['data']['prefix']);
                          form.find("input[name='paramQuery']").val(data['data']['paramQuery']);
                          form.find("input[name='paramPage']").val(data['data']['paramPage']);
                          form.find("input[name='pageStrategy']").val(data['data']['pageStrategy']);
                          form.find("input[name='constString']").val(data['data']['constString']);
                          form.find("input[name='totalAddress']").val(data['data']['totalAddress']);
                          form.find("input[name='contentAddress']").val(data['data']['contentAddress']);
                          form.find("input[name='linkRule']").val(data['data']['linkRule']);
                          form.find("input[name='payloadRule']").val(data['data']['payloadRule'])
                        }
                      })
                      .fail(function() {
                        console.log("error");
                      })
                      .always(function() {
                        console.log("complete");
                      });
                    var validator = form.validate({
                      submitHandler: function() {
                        var id = form.find("input.rule-id").val();
                        $.ajax({
                          url: baseURL + '/api/datacrawling/task/urlparam/' + id,
                          type: 'POST',
                          dataType: 'json',
                          data: {
                            prefix: form.find("input[name='prefix']").val().trim(),
                            paramQuery: form.find("input[name='paramQuery']").val().trim(),
                            paramPage: form.find("input[name='paramPage']").val().trim(),
                            pageStrategy: form.find("input[name='pageStrategy']").val().trim(),
                            constString: form.find("input[name='constString']").val().trim(),
                            totalAddress: form.find("input[name='totalAddress']").val().trim(),
                            contentAddress: form.find("input[name='contentAddress']").val().trim(),
                            linkRule: form.find("input[name='linkRule']").val().trim(),
                            payloadRule: form.find("input[name='payloadRule']").val().trim(),
                          }
                        })
                          .done(function(data) {
                            console.log("success");
                            if (data['errno'] != 0) {
                              alert(data['data']['msg']);
                            } else {
                              alert('修改成功');
                              form.find("button[type='submit']").blur();
                              $urlParamConfigBtn.click();
                            }
                          })
                          .fail(function() {
                            console.log("error");
                          })
                          .always(function() {
                            console.log("complete");
                          });
                      }
                    });
                    validator.resetForm();
                  }
                } else {
                  if (subMode == '基于浏览器驱动') {
                    form = $("#structed-driver");
                    $.ajax({
                      url: baseURL + '/api/datacrawling/task/' + taskID,
                      type: 'GET',
                      dataType: 'json'
                    })
                      .done(function(data) {
                        console.log("success");
                        if (data['errno'] != 0) {
                          alert("服务器错误");
                        } else {
                          form.find("p[name='site-link']").text(data['data']['siteURL']);
                          form.find("input[name='nav-frame']").val(data['data']['iframeNav']);
                          form.find("input[name='nav-value']").val(data['data']['navValue']);
                          form.find("input[name='search-iframe']").val(data['data']['iframeCon']);
                          form.find("input[name='search-btn']").val(data['data']['searchButton']);
                          form.find("input[name='res-row-name']").val(data['data']['resultRow']);
                          form.find("input[name='next-pg-xpath']").val(data['data']['nextPageXPath']);
                          form.find("input[name='cur-pg-xpath']").val(data['data']['pageNumXPath']);
                          form.find("input[name='sub-pg-iframe']").val(data['data']['iframeSubParam']);
                          form.find("input[name='dropdown-list-class-name']").val(data['data']['arrow']);
                          form.find("input[name='other-param-name']").val(data['data']['otherParamName']);
                          form.find("input[name='other-param-value']").val(data['data']['otherParamValue']);
                        }
                      })
                      .fail(function() {
                        console.log("error");
                      })
                      .always(function() {
                        console.log("complete");
                      });
                    var validator = form.validate({
                      submitHandler: function() {
                        var id = form.find("input.rule-id").val();
                        $.ajax({
                          url: baseURL + '/api/datacrawling/task/urlparam/' + id,
                          type: 'POST',
                          dataType: 'json',
                          data: {
                            iframeNav: form.find("input[name='nav-frame']").val().trim(),
                            navValue: form.find("input[name='nav-value']").val().trim(),
                            iframeCon: form.find("input[name='search-iframe']").val().trim(),
                            searchButton: form.find("input[name='search-btn']").val().trim(),
                            resultRow: form.find("input[name='res-row-name']").val().trim(),
                            nextPageXPath: form.find("input[name='next-pg-xpath']").val().trim(),
                            pageNumXPath: form.find("input[name='cur-pg-xpath']").val().trim(),
                            iframeSubParam: form.find("input[name='sub-pg-iframe']").val().trim(),
                            arrow: form.find("input[name='dropdown-list-class-name']").val().trim(),
                            otherParamName: form.find("input[name='other-param-name']").val().trim(),
                            otherParamValue: form.find("input[name='other-param-value']").val().trim()
                          }
                        })
                          .done(function(data) {
                            console.log("success");
                            if (data['errno'] != 0) {
                              alert(data['data']['msg']);
                            } else {
                              alert('修改成功');
                              form.find("button[type='submit']").blur();
                              $urlParamConfigBtn.click();
                            }
                          })
                          .fail(function() {
                            console.log("error");
                          })
                          .always(function() {
                            console.log("complete");
                          });
                      }
                    });
                    validator.resetForm();
                  } else if (subMode == "基于链接") {
                    form = $("#structed-undriver");
                    $.ajax({
                      url: baseURL + '/api/datacrawling/task/' + taskID,
                      type: 'GET',
                      dataType: 'json'
                    })
                      .done(function(data) {
                        console.log("success");
                        if (data['errno'] != 0) {
                          alert("服务器错误");
                        } else {
                          form.find("p[name='site-link']").text(data['data']['siteURL']);
                          form.find("input[name='search-link']").val(data['data']['searchURL']);
                          form.find("input[name='keyword-name']").val(data['data']['keywordName']);
                          form.find("input[name='page-name']").val(data['data']['pageParamName']);
                          form.find("input[name='page-value']").val(data['data']['pageParamValue']);
                          form.find("input[name='other-param-name']").val(data['data']['otherParamName']);
                          form.find("input[name='other-param-value']").val(data['data']['otherParamValue']);
                          form.find("input[name='attr-value']").val(data['data']['paramQueryValueList']);
                        }
                      })
                      .fail(function() {
                        console.log("error");
                      })
                      .always(function() {
                        console.log("complete");
                      });
                    var validator = form.validate({
                      submitHandler: function() {
                        var id = form.find("input.rule-id").val();
                        $.ajax({
                          url: baseURL + '/api/datacrawling/task/urlparam/' + id,
                          type: 'POST',
                          dataType: 'json',
                          data: {
                            searchURL: form.find("input[name='search-link']").val().trim(),
                            keywordName: form.find("input[name='keyword-name']").val().trim(),
                            pageParamName: form.find("input[name='page-name']").val().trim(),
                            pageParamValue: form.find("input[name='page-value']").val().trim(),
                            otherParamName: form.find("input[name='other-param-name']").val().trim(),
                            otherParamValue: form.find("input[name='other-param-value']").val().trim(),
                            paramQueryValueList: form.find("input[name='attr-value']").val().trim()
                          }
                        })
                          .done(function(data) {
                            console.log("success");
                            if (data['errno'] != 0) {
                              alert(data['data']['msg']);
                            } else {
                              alert('修改成功');
                              form.find("button[type='submit']").blur();
                              $urlParamConfigBtn.click();
                            }
                          })
                          .fail(function() {
                            console.log("error");
                          })
                          .always(function() {
                            console.log("complete");
                          });
                      }
                    });
                    validator.resetForm();
                  } else {
                    // 基于接口
                    form = $("#structed-api");
                    $.ajax({
                      url: baseURL + '/api/datacrawling/task/' + taskID,
                      type: 'GET',
                      dataType: 'json'
                    })
                      .done(function(data) {
                        console.log("success");
                        if (data['errno'] != 0) {
                          alert("服务器错误");
                        } else {
                          form.find("p[name='site-link']").text(data['data']['siteURL']);
                          form.find("input[name='searchURL']").val(data['data']['searchURL']);
                          form.find("input[name='keywordName']").val(data['data']['keywordName']);
                          form.find("input[name='pageParamName']").val(data['data']['pageParamName']);
                          form.find("input[name='pageParamValue']").val(data['data']['pageParamValue']);
                          form.find("input[name='otherParamName']").val(data['data']['otherParamName']);
                          form.find("input[name='otherParamValue']").val(data['data']['otherParamValue']);
                          form.find("input[name='pageSize']").val(data['data']['pageSize']);
                          form.find("input[name='totalAddress']").val(data['data']['totalAddress']);
                          form.find("input[name='contentAddress']").val(data['data']['contentAddress']);
                          form.find("input[name='paramQueryValueList']").val(data['data']['paramQueryValueList']);
                        }
                      })
                      .fail(function() {
                        console.log("error");
                      })
                      .always(function() {
                        console.log("complete");
                      });
                    var validator = form.validate({
                      submitHandler: function() {
                        var id = form.find("input.rule-id").val();
                        $.ajax({
                          url: baseURL + '/api/datacrawling/task/urlparam/' + id,
                          type: 'POST',
                          dataType: 'json',
                          data: {
                            searchURL: form.find("input[name='searchURL']").val().trim(),
                            keywordName: form.find("input[name='keywordName']").val().trim(),
                            pageParamName: form.find("input[name='pageParamName']").val().trim(),
                            pageParamValue: form.find("input[name='pageParamValue']").val().trim(),
                            otherParamName: form.find("input[name='otherParamName']").val().trim(),
                            otherParamValue: form.find("input[name='otherParamValue']").val().trim(),
                            contentAddress: form.find("input[name='contentAddress']").val().trim(),
                            totalAddress: form.find("input[name='totalAddress']").val().trim(),
                            pageSize: form.find("input[name='pageSize']").val().trim(),
                            paramQueryValueList: form.find("input[name='paramQueryValueList']").val().trim()
                          }
                        })
                          .done(function(data) {
                            console.log("success");
                            if (data['errno'] != 0) {
                              alert(data['data']['msg']);
                            } else {
                              alert('修改成功');
                              form.find("button[type='submit']").blur();
                              $urlParamConfigBtn.click();
                            }
                          })
                          .fail(function() {
                            console.log("error");
                          })
                          .always(function() {
                            console.log("complete");
                          });
                      }
                    });
                    validator.resetForm();
                  }
                }
              });
              $loginParamConfigBtn.off('click');
              $loginParamConfigBtn.on('click', { taskID: taskID }, function(event) {
                var taskID = event.data.taskID;
                var form = $("#login-param-form");
                $.ajax({
                  url: baseURL + '/api/datacrawling/task/' + taskID,
                  type: 'GET',
                  dataType: 'json'
                })
                  .done(function(data) {
                    console.log("success");
                    if (data['errno'] != 0) {
                      alert("服务器错误");
                    } else {
                      form.find("input[name='login-link']").val(data['data']['loginURL']);
                      form.find("input[name='username-id']").val(data['data']['userNameID']);
                      form.find("input[name='password-id']").val(data['data']['passwordID']);
                      form.find("input[name='submitXpath']").val(data['data']['submitXpath']);
                      form.find("input[name='username']").val(data['data']['username']);
                      form.find("input[name='password']").val(data['data']['password']);
                    }
                  })
                  .fail(function() {
                    console.log("error");
                  })
                  .always(function() {
                    console.log("complete");
                  });
                var validator = form.validate({
                  submitHandler: function() {
                    var id = form.find("input.rule-id").val();
                    $.ajax({
                      url: baseURL + '/api/datacrawling/task/loginparam/' + id,
                      type: 'POST',
                      dataType: 'json',
                      data: {
                        loginURL: form.find("input[name='login-link']").val().trim(),
                        userNameID: form.find("input[name='username-id']").val().trim(),
                        passwordID: form.find("input[name='password-id']").val().trim(),
                        submitXpath: form.find("input[name='submitXpath']").val().trim(),
                        username: form.find("input[name='username']").val().trim(),
                        password: form.find("input[name='password']").val().trim()
                      }
                    })
                      .done(function(data) {
                        console.log("success");
                        if (data['errno'] != 0) {
                          alert("服务器错误");
                        } else {
                          alert("修改成功");
                          form.find("button[type='submit']").blur();
                          $loginParamConfigBtn.click();
                        }
                      })
                      .fail(function() {
                        console.log("error");
                      })
                      .always(function() {
                        console.log("complete");
                      });
                  }
                });
                validator.resetForm();
              });
              $downloadParamConfigBtn.off('click');
              $downloadParamConfigBtn.on('click', { taskID: taskID }, function(event) {
                var taskID = event.data.taskID;
                var form = $("#download-param-form");
                $.ajax({
                  url: baseURL + '/api/datacrawling/task/' + taskID,
                  type: 'GET',
                  dataType: 'json'
                })
                  .done(function(data) {
                    console.log("success");
                    if (data['errno'] != 0) {
                      alert("服务器错误");
                    } else {
                      form.find("input[name='thread-num']").val(data['data']['threadNum']);
                      form.find("input[name='timeout-num']").val(data['data']['timeout']);
                      form.find("input[name='charset']").val(data['data']['charset']);
                      form.find("input[name='data-gross']").val(data['data']['datagross']);
                    }
                  })
                  .fail(function() {
                    console.log("error");
                  })
                  .always(function() {
                    console.log("complete");
                  });
                var validator = form.validate({
                  submitHandler: function() {
                    var id = form.find("input.rule-id").val();
                    $.ajax({
                      url: baseURL + '/api/datacrawling/task/downloadparam/' + id,
                      type: 'POST',
                      dataType: 'json',
                      data: {
                        threadNum: form.find("input[name='thread-num']").val(),
                        timeout: form.find("input[name='timeout-num']").val(),
                        charset: form.find("input[name='charset']").val(),
                        datagross: form.find("input[name='data-gross']").val() || 0
                      }
                    })
                      .done(function(data) {
                        console.log("success");
                        if (data['errno'] != 0) {
                          alert(data['data']['msg']);
                        } else {
                          alert("修改成功");
                          form.find("button[type='submit']").blur();
                          $downloadParamConfigBtn.click();
                        }
                      })
                      .fail(function() {
                        console.log("error");
                      })
                      .always(function() {
                        console.log("complete");
                      });
                  }
                });
                validator.resetForm();

              });
              $("#edit-rule .return-btn").off('click');
              $("#edit-rule .return-btn").on('click', function(event) {
                event.preventDefault();
                /* Act on the event */
                $("#edit-rule").removeClass("active");
                $("#paser-rule").addClass('active');
                $paserRuleBtn.click();
              });

              $urlParamConfigBtn.click();
            });
          }
        })
        .fail(function() {
          console.log("error");
        })
        .always(function() {
          console.log("complete");
        });

    });
    $templateExtractBtn.off('click');
    $templateExtractBtn.on('click', function() {
      var $templateListBtn = $("#template-list-btn");
      var $newTemplateBtn = $("#new-template-btn");
      $templateListBtn.off('click');
      $templateListBtn.on('click', function() {
        var tmpl = $.templates("#template-list");
        $.ajax({
          url: baseURL + '/api/datacrawling/task/template/all',
          type: 'GET',
          dataType: 'json'
        })
          .done(function(data) {
            console.log("success");
            if (data['errno'] != 0) {
              alert("服务器错误");
            } else {
              var content = data['data'];
              var html = tmpl.render(content);
              $("#template-list-content").html(html);
              $(".delete-template").off('click');
              $(".delete-template").on('click', function(event){
                event.preventDefault();
                var $tr = $(this).parents("tr");
                var templateID = $tr.find("th[name='template-id']").text().trim();
                var taskID = $tr.find("th[name='rule-id']").text().trim();
                $.ajax({
                  url: baseURL + '/api/datacrawling/task/template?ruleId=' + taskID + '&templateId=' + templateID,
                  type: 'DELETE',
                  dataType: 'json'
                }).done(function(data){
                  console.log(data);
                })
              });

              $(".change-template").off('click');
              $(".change-template").on('click', function(event) {
                $("#edit-template .return-btn").off();
                $("#edit-template .return-btn").on('click', function(event) {
                  event.preventDefault();
                  /* Act on the event */
                  $("#edit-template").removeClass("active");
                  $("#template-list-table").addClass('active');
                  $templateExtractBtn.click();
                });
                var $tr = $(this).parents("tr");
                var templateID = $tr.find("th[name='template-id']").text().trim();
                var taskID = $tr.find("th[name='rule-id']").text().trim();
                var form = $("#change-template-form");
                form.find("input[name='template-id']").val(templateID);
                $.ajax({
                  url: baseURL + '/api/datacrawling/task/template/' + templateID + '?taskID=' + taskID,
                  type: 'GET',
                  dataType: 'json'
                })
                  .done(function(data) {
                    console.log("success");
                    if (data['errno'] != 0) {
                      alert("服务器错误");
                    } else {
                      form.find("input[name='task-id']").val(data['data']['taskID']);
                      form.find("p[name='pattern-name']").text(data['data']['templateName']);
                      form.find("input[name='pattern-xpath']").val(data['data']['templateXpath']);
                      if (data['data']['runningMode'] == 'structed') {
                        form.find("input[name='pattern-type']").show();
                        form.find("input[name='pattern-formula']").show();
                        form.find("input[name='pattern-header-xpath']").show();
                        form.find("input[name='pattern-type']").val(data['data']['templateType']);
                        form.find("input[name='pattern-formula']").val(data['data']['templateFormula']);
                        form.find("input[name='pattern-header-xpath']").val(data['data']['templateHeaderXpath']);
                      } else {
                        form.find("input[name='pattern-type']").val("");
                        form.find("input[name='pattern-formula']").val("");
                        form.find("input[name='pattern-header-xpath']").val("");
                        form.find("input[name='pattern-type']").hide();
                        form.find("input[name='pattern-formula']").hide();
                        form.find("input[name='pattern-header-xpath']").hide();

                      }
                    }
                  })
                  .fail(function() {
                    console.log("error");
                  })
                  .always(function() {
                    console.log("complete");
                  });
                var vm = $(this);
                var validator = form.validate({
                  submitHandler: function() {
                    var id = form.find("input[name='template-id']").val();
                    $.ajax({
                      url: baseURL + '/api/datacrawling/task/template/' + id,
                      type: 'POST',
                      dataType: 'json',
                      data: {
                        taskID: form.find("input[name='task-id']").val().trim(),
                        templateName: form.find("p[name='pattern-name']").text().trim(),
                        templateXpath: form.find("input[name='pattern-xpath']").val().trim(),
                        templateType: form.find("input[name='pattern-type']").val().trim(),
                        templateFormula: form.find("input[name='pattern-formula']").val().trim(),
                        templateHeaderXpath: form.find("input[name='pattern-header-xpath']").val().trim(),
                      }
                    })
                      .done(function(data) {
                        console.log("success");
                        if (data['errno'] != 0) {
                          alert(data['data']['msg']);
                        } else {
                          alert('修改成功');
                          form.find("button[type='submit']").blur();
                          vm.click();
                        }
                      })
                      .fail(function() {
                        console.log("error");
                      })
                      .always(function() {
                        console.log("complete");
                      });

                  }
                });
                validator.resetForm();
              });
            }
          })
          .fail(function() {
            console.log("error");
          })
          .always(function() {
            console.log("complete");
          });

      });
      $newTemplateBtn.off('click');
      $newTemplateBtn.on('click', function() {
        $("input[name='pattern-mode'][value='unstructed']").off();
        $("input[name='pattern-mode'][value='unstructed']").on('click', function() {
          var form = $("#new-template-form");
          var tmpl = $.templates("#task-id-list");
          $.ajax({
            url: baseURL + '/api/datacrawling/task/all',
            type: 'GET',
            dataType: 'json',
          })
            .done(function(data) {
              console.log("success");
              if (data['errno'] != 0) {
                alert("服务器错误");
              } else {
                var content = data['data']['content'];
                var newContent = [];
                for (item in content) {
                  if (content[item]['runningMode'] == 'unstructed') {
                    newContent.push(content[item]);
                  }
                }
                var html = tmpl.render(newContent);
                $("#task-id-list-content").html(html);
              }
            })
            .fail(function() {
              console.log("error");
            })
            .always(function() {
              console.log("complete");
            });

          form.find("input[name='pattern-type']").hide();
          form.find("input[name='pattern-formula']").hide();
          form.find("input[name='pattern-header-xpath']").hide();
        });
        $("input[name='pattern-mode'][value='structed']").off();
        $("input[name='pattern-mode'][value='structed']").on('click', function() {
          var tmpl = $.templates("#task-id-list");
          $.ajax({
            url: baseURL + '/api/datacrawling/task/all',
            type: 'GET',
            dataType: 'json',
          })
            .done(function(data) {
              console.log("success");
              if (data['errno'] != 0) {
                alert("服务器错误");
              } else {
                var content = data['data']['content'];
                var newContent = [];
                for (item in content) {
                  if (content[item]['runningMode'] == 'structed') {
                    newContent.push(content[item]);
                  }
                }

                var html = tmpl.render(newContent);
                $("#task-id-list-content").html(html);
              }
            })
            .fail(function() {
              console.log("error");
            })
            .always(function() {
              console.log("complete");
            });


          form.find("input[name='pattern-type']").show();
          form.find("input[name='pattern-formula']").show();
          form.find("input[name='pattern-header-xpath']").show();
        });
        $("input[name='pattern-mode'][value='unstructed']").click();
        var form = $("#new-template-form");
        form.find("input[name='pattern-name']").val("");
        form.find("input[name='pattern-xpath']").val("");
        var validator = form.validate({
          submitHandler: function() {
            $.ajax({
              url: baseURL + '/api/datacrawling/task/template/new',
              type: 'POST',
              dataType: 'json',
              data: {
                taskID: form.find("select[name='task-id']").val().trim(),
                templateName: form.find("input[name='pattern-name']").val().trim(),
                templateXpath: form.find("input[name='pattern-xpath']").val().trim(),
                templateType: form.find("input[name='pattern-type']").val().trim(),
                templateFormula: form.find("input[name='pattern-formula']").val().trim(),
                templateHeaderXpath: form.find("input[name='pattern-header-xpath']").val().trim(),
              }
            })
              .done(function(data) {
                console.log("success");
                if (data['errno'] != 0) {
                  alert(data['data']['msg']);
                } else {
                  alert('修改成功');
                  $templateExtractBtn.click();
                }
              })
              .fail(function() {
                console.log("error");
              })
              .always(function() {
                console.log("complete");
              });

          }
        });
        validator.resetForm();
      });
      $templateListBtn.click();
    });
    $taskMonitorBtn.off('click');
    $taskMonitorBtn.on('click', function() {
      var tmpl = $.templates("#task-control-list");
      $.ajax({
        url: baseURL + '/api/datacrawling/task/all',
        type: 'GET',
        dataType: 'json'
      })
        .done(function(data) {
          console.log("success");
          if (data['errno'] != 0) {
            alert("服务器错误");
          } else {
            var content = data['data'];
            var html = tmpl.render(content);
            $("#task-control-list-content").html(html);
            $(".crawler-control-btn").off('change');
            $(".crawler-control-btn").on('change', function(e) {
              var $tr = $(this).parents("tr");
              var taskID = $tr.find("th.task-id").text().trim();
              var action = ''
              var value = e.target.value
              if (value === '停止') action = 'stop'
              else if (value === '启动') action = 'start'
              else if (value === '删除') action = 'delete'
              console.log(action);
              if (action === 'delete') {
                $.LoadingOverlay("show");
                $.ajax({
                  url: baseURL + '/api/datacrawling/task/' + taskID,
                  type: 'DELETE',
                  dataType: 'json'
                })
                  .done(function(data) {
                    $.LoadingOverlay("hide", true);
                    alert(data['data']);
                  })
                  .fail(function() {
                    $.LoadingOverlay("hide", true);
                  });
              } else if(action){
                $.LoadingOverlay("show");
                $.ajax({
                  url: baseURL + '/api/datacrawling/task/monitor?action=option&option=' + action + '&taskID=' + taskID,
                  type: 'GET',
                  dataType: 'json'
                })
                  .done(function(data) {
                    console.log("success");
                    $.LoadingOverlay("hide", true);
                    alert(data['data']['msg']);
                  })
                  .fail(function() {
                    console.log("error");
                  })
                  .always(function() {
                    console.log("complete");
                  });
              }
            });
          }
        })
        .fail(function() {
          console.log("error");
        })
        .always(function() {
          console.log("complete");
        });

    });
    $newTaskBtn.click();
  });
  $monitorBtn.on('click', function(event) {
    var $taskCrawlingMonitorBtn = $("#task-crawling-monitor-btn");
    $taskCrawlingMonitorBtn.off('click');
    $taskCrawlingMonitorBtn.on('click', function() {
      var tmpl = $.templates("#task-monitor-list");
      $.ajax({
        url: baseURL + '/api/datacrawling/task/monitor?action=status',
        type: 'GET',
        dataType: 'json'
      })
        .done(function(data) {
          console.log("success");
          if (data['errno'] != 0) {
            alert("服务器错误");
          } else {
            var html = tmpl.render(data['data']);
            $("#task-monitor-list-content").html(html);
          }
        })
        .fail(function() {
          console.log("error");
        })
        .always(function() {
          console.log("complete");
        });

      var handle = setInterval(function() {
        $.ajax({
          url: baseURL + '/api/datacrawling/task/monitor?action=status',
          type: 'GET',
          dataType: 'json'
        })
          .done(function(data) {
            console.log("success");
            if (data['errno'] != 0) {
              alert("服务器错误");
            } else {
              var html = tmpl.render(data['data']);
              $("#task-monitor-list-content").html(html);
            }
          })
          .fail(function() {
            console.log("error");
          })
          .always(function() {
            console.log("complete");
          });
        if (handle == undefined) {
          console.log("undefined");
        } else {
          if (!$("#monitor").hasClass('active')) {
            clearInterval(handle);
          }
        }
      }, 3000);
      // setTimeout(function(){
      //   var $menuBar = $("#menu-bar");
      //   $menuBar.on("click", function(event) {
      //     //$("#task-crawling-monitor").removeClass('active');
      //     $menuBar.off();
      //   });
      // }, 1000);

    });
    $taskCrawlingMonitorBtn.click();
  });
  $deliveryBtn.on('click', function(event) {
    var tmpl = $.templates("#delivery-task-list");
    $.ajax({
      url: baseURL + "/api/datacrawling/task/all",
      type: 'GET',
      dataType: 'json'
    }).done(function(data) {
      if (data['errno'] != 0) {
        alert('服务器错误');
      } else {
        content = data['data'];
        var html = tmpl.render(content);
        $("#delivery-task-list-content").html(html);
        $(".see-delivery-task-btn").off('click');
        $(".see-delivery-task-btn").on('click', function(event) {
          var $tr = $(this).parents("tr");
          var taskID = $tr.find("th.task-id").text();
          var runningMode = $tr.find("th.running-mode").text();
          if (runningMode.trim() == "文本型") {
               // window.location.href=baseURL + '/api/datacrawling/download?id='+taskID;
            // alert("文本型数据交付请直接到工作路径下查看");
            // return;
            //   $.ajax({
            //       url: baseURL + '/api/datacrawling/download?id='+taskID,
            //       type: 'GET',
            //       // dataType: 'json'
            //
            //   })
            //       .done(function (data) {
            //         // $.download(baseURL+ '/api/datacrawling/download?id='+taskID, 'get', data.value)
            //           console.log(data)
            //       })
            //       // .done(function(data) {
            //       //     // console.log(data['data']);
            //       //     $.LoadingOverlay("hide", true);
            //       //     alert(data['data']['msg']);
            //       // })
            //       .fail(function() {
            //           console.log("error");
            //       })
            //       .always(function() {
            //           console.log("complete");
            //       });
            var url = baseURL + '/api/datacrawling/download?id='+ taskID;;
            let xhr = new XMLHttpRequest()
            let fileName = `${taskID}.zip` // 文件名称
            xhr.open('GET', url, true)
            xhr.responseType = 'arraybuffer'
            xhr.onload = function() {
              if (this.status === 200) {
                let type = xhr.getResponseHeader('Content-Type')
                console.log(type);
                if (type.includes('json')) { // 当返回结果类型为application/json时，打印json信息
                  const tempBlob = new Blob( [this.response], {type: type} )
                  // 通过 FileReader 读取这个 blob
                  const reader = new FileReader()
                  reader.onload = e => {
                    const res = e.currentTarget.result
                    const message = JSON.parse(res)
                    console.dir(message);
                    alert(message.data)
                    // 此处对fileReader读出的结果进行JSON解析
                    // 可能会出现错误，需要进行捕获
                    try {
                      const json = JSON.parse(res)
                      if (json) {
                        // 解析成功说明后端导出出错，进行导出失败的操作，并直接返回
                        return
                      }
                    } catch (err) {
                      // 该异常为无法将字符串转为json
                      // 说明返回的数据是一个流文件
                      // 不需要处理该异常，只需要捕获即刻
                    }
                    // 如果代码能够执行到这里，说明后端给的是一个流文件，再执行上面导出的代码
                    // do export code
                  }
                  // 将blob对象以文本的方式读出，读出完成后将会执行 onload 方法
                  reader.readAsText(tempBlob)
                } else {
                  let blob = new Blob([this.response], {type: type})
                  alert('下载开始，稍后即可选择路径保存')
                  if (typeof window.navigator.msSaveBlob !== 'undefined') {
                    window.navigator.msSaveBlob(blob, fileName)
                  } else {
                    let URL = window.URL || window.webkitURL
                    let objectUrl = URL.createObjectURL(blob)
                    console.log(objectUrl);
                    if (fileName) {
                      var a = document.createElement('a')
                      if (typeof a.download === 'undefined') {
                        window.location = objectUrl
                      } else {
                        a.href = objectUrl
                        a.download = fileName
                        document.body.appendChild(a)
                        a.click()
                        a.remove()
                      }
                    } else {
                      window.location = objectUrl
                    }
                  }
                }
              } else {
                alert(`请求错误：${this.status}`)
              }
            }
            xhr.send();
            // 发送ajax请求
          }
          $("#delivery-modal").off('shown.bs.modal');
          $('#delivery-modal').on('shown.bs.modal', function() {
            var selectTmpl = $.templates("#table-name-list");
            $.ajax({
              url: baseURL + "/api/datacrawling/data/" + taskID,
              type: "GET",
              dataType: 'json'
            }).done(function(data) {
              if (data['errno'] != 0) {
                alert('服务器错误');
              } else {
                //temporary
                var content = data['data'];
                var html = selectTmpl.render(content);
                $("#table-name-list-content").html(html);
              }
            });
            $("#table-data-search-btn").off('click');
            var getTableData = function(id, tbName, pageNum) {
              var tmpl = $.templates("#table-data-list");
              $.ajax({
                url: baseURL + "/api/datacrawling/data/all?id=" + id + "&tbName=" + tbName + "&pageNum=" + pageNum + "&pageSize=" + 10,
                type: "GET",
                dataType: "json"
              }).done(function(data) {
                if (data['errno'] != 0) {
                  alert(data['data']);
                } else {
                  var content = data['data']['content'];
                  var headContent = [];
                  var bodyContent = [];
                  if (content.length >= 1) {
                    for (key in content[0]) {
                      headContent.push(key);
                    }
                    for (var i = 0; i < content.length; i++) {
                      var row = [];
                      for (key in content[i]) {
                        row.push(content[i][key]);
                      }
                      bodyContent.push(row);
                    }
                  }
                  var rdata = data['data'];
                  rdata['headContent'] = headContent;
                  rdata['bodyContent'] = bodyContent;
                  var html = tmpl.render(rdata);
                  $("#table-data-list-content").html(html);
                }
              })
            };
            $("#table-data-search-btn").on('click', function() {
              var tbName = $("#table-name-list-content").val().trim();
              var pg = 1;
              getTableData(taskID, tbName, pg);
              $("#pre-pg-btn").off('click');
              $("#next-pg-btn").off('click');
              $("#pre-pg-btn").on('click', function() {
                if (pg == 1) {
                  alert("已是最前页");
                } else {
                  pg--;
                  getTableData(taskID, tbName, pg);
                }
              });
              $("#next-pg-btn").on('click', function() {
                pg++;
                getTableData(taskID, tbName, pg);
              })
            });
          });
        })


      }
    })
  })
  $configBtn.on('click', function(event) {
    event.preventDefault();
    /* Act on the event */
    var $dbConfigBtn = $("#db-config-btn");
    var $plaConfigBtn = $("#pla-config-btn");
    $dbConfigBtn.off();
    $dbConfigBtn.on('click', function(event) {
      var render = function() {
        $.ajax({
          url: baseURL + '/api/datacrawling/config/mysql',
          type: 'GET',
          dataType: 'json'
        })
          .done(function(data) {
            if (data['errno'] != 0) {
              alert('服务器错误');
            } else {
              $("#mysql-url").val(data['data']['mysqlURL']);
              $("#mysql-username").val(data['data']['mysqlUserName']);
              $("#mysql-password").val(data['data']['mysqlPassword']);
            }
          })
          .fail(function() {
            console.log("error");
          })
          .always(function() {
            console.log("complete");
          });

      };
      var validator = $("#db-form").validate({
        submitHandler: function() {
          var mysqlURL = $("#mysql-url").val().trim();
          var mysqlUserName = $("#mysql-username").val().trim();
          var mysqlPassword = $("#mysql-password").val().trim();
          if (mysqlURL == "" || mysqlUserName == "" || mysqlPassword == "") {
            alert("输入不能为空");
            return;
          }
          $.ajax({
            url: baseURL + '/api/datacrawling/config/mysql',
            type: 'POST',
            dataType: 'json',
            data: {
              mysqlURL: mysqlURL,
              mysqlUserName: mysqlUserName,
              mysqlPassword: mysqlPassword
            }
          })
            .done(function(data) {
              if (data['errno'] != 0) {
                alert('服务器错误');
              } else {
                alert("修改成功");
                render();
              }
            })
            .fail(function() {
              console.log("error");
            })
            .always(function() {
              console.log("complete");
            });
        }
      });
      validator.resetForm();
      render();
    });
    $plaConfigBtn.off();
    $plaConfigBtn.on('click', function(event) {
      var render = function() {
        $.ajax({
          url: baseURL + '/api/datacrawling/config/platform',
          type: 'GET',
          dataType: 'json'
        })
          .done(function(data) {
            if (data['errno'] != 0) {
              alert('服务器错误');
            } else {
              $("#base-work-dir").val(data['data']['baseWorkDir']);
            }
          })
          .fail(function() {
            console.log("error");
          })
          .always(function() {
            console.log("complete");
          });
      };
      var validator = $("#pla-form").validate({
        submitHandler: function() {
          var baseWorkDir = $("#base-work-dir").val().trim();
          if (baseWorkDir == "") {
            alert("输入不能为空");
            return;
          }
          $.ajax({
            url: baseURL + '/api/datacrawling/config/platform',
            type: 'POST',
            dataType: 'json',
            data: {
              baseWorkDir: baseWorkDir
            }
          })
            .done(function(data) {
              if (data['errno'] != 0) {
                alert(data['data']);
              } else {
                alert("修改成功");
                render();
              }
            })
            .fail(function() {
              console.log("error");
            })
            .always(function() {
              console.log("complete");
            });
        }
      });
      validator.resetForm();
      render();
    });
    $dbConfigBtn.click();
  });
  $crawlingBtn.click();

});
