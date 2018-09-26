var baseURL="http://localhost:8082"
$(function(){
  var $requestBtn=$("#request-btn");
  var $crawlingBtn=$("#crawling-btn");
  var $monitorBtn=$("#monitor-btn");
  var $deliveryBtn=$("#delivery-btn");
  var $configBtn=$("#config-btn");

  $requestBtn.on('click',function(event) {
    var $newRequestBtn=$("#new-request-btn");
    var $requestConfirmBtn=$("#request-confirm-btn");
    var $siteResourceBtn=$("#site-resource-btn");
    $newRequestBtn.off('click');
    $newRequestBtn.on('click',function(){
      $("#request-name").val("");
      $("#request-desc").val("");
      var validator=$("#request-form").validate({
        submitHandler:function(){
          var requestName=$("#request-name").val().trim();
          var requestDesc=$("#request-desc").val().trim();
          if(requestName==""){
            alert("输入不能为空");
          }
          $.ajax({
            url: baseURL+'/api/datacrawling/request/new',
            type: 'POST',
            dataType: 'json',
            data: {
              requestName: requestName,
              requestDesc:requestDesc
            }
          })
          .done(function(data) {
            console.log("success");
            if(data['errno']!=0){
              alert("服务器错误");
            }else{
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
    $requestConfirmBtn.on('click',function(){
      var tmpl=$.templates("#request-list");
      $.ajax({
        url: baseURL+'/api/datacrawling/request/all',
        type: 'GET',
        dataType: 'json'
      })
      .done(function(data) {
        console.log("success");
        if(data['errno']!=0){
          alert("服务器错误");
        }else{
          var content=data['data'];
          var html=tmpl.render(content);
          $("#request-list-content").html(html);
          $(".change-request").off('click');
          $(".change-request").on('click', function(event) {
            var $a=$(event.target);
            var $th=$a.parent('th');
            var $id=$th.siblings('.request-id');
            var id=$id.text();
            $.ajax({
              url: baseURL+'/api/datacrawling/request/'+id,
              type: 'GET',
              dataType: 'json'
            })
            .done(function(data) {
              console.log("success");
              if(data['errno']!=0){
                alert("服务器错误");
              }else{
                $("#confirm-request-id").val(id);
                $("#confirm-request-name").val(data['data']['requestName']);
                $("#confirm-request-desc").val(data['data']['requestDesc']);
              }
              $("#edit-request .return-btn").off('click');
              $("#edit-request .return-btn").on('click',function(event) {
                event.preventDefault();
                /* Act on the event */
                $("#edit-request").removeClass("active");
                $("#request-confirm").addClass('active');
                $requestConfirmBtn.click();
              });
              var validator=$("#confirm-request-form").validate({
                submitHandler:function(){
                  var id=$("#confirm-request-id").val();
                  var requestName=$("#confirm-request-name").val().trim();
                  var requestDesc=$("#confirm-request-desc").val().trim();
                  if(requestName==""){
                    alert("输入不能为空");
                  }
                  $.ajax({
                    url: baseURL+'/api/datacrawling/request/'+id,
                    type: 'POST',
                    dataType: 'json',
                    data: {
                      requestName: requestName,
                      requestDesc:requestDesc
                    }
                  })
                  .done(function(data) {
                    console.log("success");
                    if(data['errno']!=0){
                      alert("服务器错误");
                    }else{
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
    $siteResourceBtn.on('click',function(){
      var tmpl=$.templates("#site-list");
      $.ajax({
        url: baseURL+'/api/datacrawling/task/all',
        type: 'GET',
        dataType: 'json'
      })
      .done(function(data) {
        console.log("success");
        if(data['errno']!=0){
          alert("服务器错误");
        }else{
          var content=data['data'];
          var html=tmpl.render(content);
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
  $crawlingBtn.on('click',function(event) {
    var $newTaskBtn=$("#new-task-btn");
    var $paserRuleBtn=$("#paser-rule-btn");
    var $templateExtractBtn=$("#content-extract-btn");
    var $taskMonitorBtn=$("#task-monitor-btn");
    $newTaskBtn.off('click');
    $newTaskBtn.on('click',function(){
      $("input[name='running-mode'][value='unstructed']").on("click",function(){
        $("#driver-select-group").hide("slow");
      });
      $("input[name='running-mode'][value='structed']").on("click",function(){
        $("#driver-select-group").show("slow");
      })
      $("input[name='running-mode'][value='unstructed']").click();
      var $taskName=$("#task-name");
      var $workPath=$("#work-path");
      $taskName.val("");
      $workPath.val("");
      var validator=$("#task-form").validate({
        submitHandler:function(){
          var taskName=$taskName.val();
          var workPath=$workPath.val();
          var runningMode=$("input[name='running-mode']:checked").val();
          var driver=$("input[name='driver-select']:checked").val();
          if(taskName==""||workPath==""){
            alert("输入不能为空");
            return;
          }
          if(workPath[workPath.length-1]!='/'){
            workPath=workPath+'/';
          }
          if(runningMode=='unstructed'){
            driver='false';
          }
          $.ajax({
            url: baseURL+'/api/datacrawling/task/new',
            type: 'POST',
            dataType: 'JSON',
            data: {
              taskName: taskName,
              workPath: workPath,
              runningMode:runningMode,
              driver:driver
            }
          })
          .done(function(data) {
            console.log("success");
            if(data['errno']!=0){
              alert(data['data']['msg']);
            }else{
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
    $paserRuleBtn.on('click',function(){
      var tmpl=$.templates("#rule-list");
      $.ajax({
        url: baseURL+'/api/datacrawling/task/all',
        type: 'GET',
        dataType: 'json'
      })
      .done(function(data) {
        console.log("success");
        if(data['errno']!=0){
          alert("服务器错误");
        }else{
          var content=data['data'];
          var html=tmpl.render(content);
          $("#rule-list-content").html(html);
          $(".change-rule").off('click');
          $(".change-rule").on('click',function(event) {
            var $tr=$(this).parents("tr");
            var runningMode=$tr.find("th[name='running-mode']").text().trim();
            var driver=$tr.find("th[name='driver']").text().trim();
            var $unstructedUndriver=$("#unstructed-undriver");
            var $structedUndriver=$("#structed-undriver");
            var $structedDriver=$("#structed-driver");
            if(runningMode=="文本型"){
              $unstructedUndriver.show();
              $structedDriver.hide();
              $structedUndriver.hide();
            }else{
              if(driver=="是"){
                $unstructedUndriver.hide();
                $structedDriver.show();
                $structedUndriver.hide();
              }else{
                $unstructedUndriver.hide();
                $structedDriver.hide();
                $structedUndriver.show();
              }
            }
            var taskID=$tr.find("th.rule-id").text();
            $(".rule-config-form .rule-id").val(taskID);
            var $urlParamConfigBtn=$("#url-param-config-btn");
            var $loginParamConfigBtn=$("#login-param-config-btn");
            var $downloadParamConfigBtn=$("#download-param-config-btn");
            $urlParamConfigBtn.off('click');
            $urlParamConfigBtn.on('click',{taskID:taskID,runningMode:runningMode,driver:driver},function(event){
              var taskID=event.data.taskID;
              var form=null;
              if(runningMode=='文本型'){
                form=$("#unstructed-undriver");
                $.ajax({
                  url: baseURL+'/api/datacrawling/task/'+taskID,
                  type: 'GET',
                  dataType: 'json'
                })
                .done(function(data) {
                  console.log("success");
                  if(data['errno']!=0){
                    alert("服务器错误");
                  }else{
                    form.find("input[name='site-link']").val(data['data']['siteURL']);
                    form.find("input[name='search-link']").val(data['data']['searchURL']);
                    form.find("input[name='keyword-name']").val(data['data']['keywordName']);
                    form.find("input[name='page-name']").val(data['data']['pageParamName']);
                    form.find("input[name='page-value']").val(data['data']['pageParamValue']);
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
                var validator=$("#unstructed-undriver").validate({
                  submitHandler:function(){
                    var id=form.find("input.rule-id").val();
                    $.ajax({
                      url: baseURL+'/api/datacrawling/task/urlparam/'+id,
                      type: 'POST',
                      dataType: 'json',
                      data: {
                        siteURL:form.find("input[name='site-link']").val().trim(),
                        searchURL:form.find("input[name='search-link']").val().trim(),
                        keywordName:form.find("input[name='keyword-name']").val().trim(),
                        pageParamName:form.find("input[name='page-name']").val().trim(),
                        pageParamValue:form.find("input[name='page-value']").val().trim(),
                        otherParamName:form.find("input[name='other-param-name']").val().trim(),
                        otherParamValue:form.find("input[name='other-param-value']").val().trim()
                      }
                    })
                    .done(function(data) {
                      console.log("success");
                      if(data['errno']!=0){
                        alert(data['data']['msg']);
                      }else{
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
              }else if(driver=='是'){
                form=$("#structed-driver");
                $.ajax({
                  url: baseURL+'/api/datacrawling/task/'+taskID,
                  type: 'GET',
                  dataType: 'json'
                })
                .done(function(data) {
                  console.log("success");
                  if(data['errno']!=0){
                    alert("服务器错误");
                  }else{
                    form.find("input[name='site-link']").val(data['data']['siteURL']);
                    form.find("input[name='nav-frame']").val(data['data']['iframeNav']);
                    form.find("input[name='nav-value']").val(data['data']['navValue']);
                    form.find("input[name='search-iframe']").val(data['data']['iframeCon']);
                    form.find("input[name='search-btn']").val(data['data']['searchButton']);
                    form.find("input[name='res-row-name']").val(data['data']['resultRow']);
                    form.find("input[name='next-pg-xpath']").val(data['data']['nextPageXPath']);
                    form.find("input[name='cur-pg-xpath']").val(data['data']['pageNumXPath']);
                    form.find("input[name='sub-pg-iframe']").val(data['data']['iframeSubParam']);
                    form.find("input[name='dropdown-list-class-name']").val(data['data']['arrow']);
                    form.find("input[name='login-button']").val(data['data']['loginButton']);
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
                var validator=$("#structed-driver").validate({
                  submitHandler:function(){
                    var id=form.find("input.rule-id").val();
                    $.ajax({
                      url: baseURL+'/api/datacrawling/task/urlparam/'+id,
                      type: 'POST',
                      dataType: 'json',
                      data: {
                        siteURL:form.find("input[name='site-link']").val().trim(),
                        iframeNav:form.find("input[name='nav-frame']").val().trim(),
                        navValue:form.find("input[name='nav-value']").val().trim(),
                        iframeCon:form.find("input[name='search-iframe']").val().trim(),
                        searchButton:form.find("input[name='search-btn']").val().trim(),
                        resultRow:form.find("input[name='res-row-name']").val().trim(),
                        nextPageXPath:form.find("input[name='next-pg-xpath']").val().trim(),
                        pageNumXPath:form.find("input[name='cur-pg-xpath']").val().trim(),
                        iframeSubParam:form.find("input[name='sub-pg-iframe']").val().trim(),
                        arrow:form.find("input[name='dropdown-list-class-name']").val().trim(),
                          loginButton:form.find("input[name='login-button']").val().trim(),
                        otherParamName:form.find("input[name='other-param-name']").val().trim(),
                        otherParamValue:form.find("input[name='other-param-value']").val().trim()
                      }
                    })
                    .done(function(data) {
                      console.log("success");
                      if(data['errno']!=0){
                        alert(data['data']['msg']);
                      }else{
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
              }else{
                form=$("#structed-undriver");
                $.ajax({
                  url: baseURL+'/api/datacrawling/task/'+taskID,
                  type: 'GET',
                  dataType: 'json'
                })
                .done(function(data) {
                  console.log("success");
                  if(data['errno']!=0){
                    alert("服务器错误");
                  }else{
                    form.find("input[name='site-link']").val(data['data']['siteURL']);
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
                var validator=$("#structed-undriver").validate({
                  submitHandler:function(){
                    var id=form.find("input.rule-id").val();
                    $.ajax({
                      url: baseURL+'/api/datacrawling/task/urlparam/'+id,
                      type: 'POST',
                      dataType: 'json',
                      data: {
                        siteURL:form.find("input[name='site-link']").val().trim(),
                        searchURL:form.find("input[name='search-link']").val().trim(),
                        keywordName:form.find("input[name='keyword-name']").val().trim(),
                        pageParamName:form.find("input[name='page-name']").val().trim(),
                        pageParamValue:form.find("input[name='page-value']").val().trim(),
                        otherParamName:form.find("input[name='other-param-name']").val().trim(),
                        otherParamValue:form.find("input[name='other-param-value']").val().trim(),
                        paramQueryValueList:form.find("input[name='attr-value']").val().trim()
                      }
                    })
                    .done(function(data) {
                      console.log("success");
                      if(data['errno']!=0){
                        alert(data['data']['msg']);
                      }else{
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
            });
            $loginParamConfigBtn.off('click');
            $loginParamConfigBtn.on('click',{taskID:taskID},function(event){
              var taskID=event.data.taskID;
              var form=$("#login-param-form");
              $.ajax({
                url: baseURL+'/api/datacrawling/task/'+taskID,
                type: 'GET',
                dataType: 'json'
              })
              .done(function(data) {
                console.log("success");
                if(data['errno']!=0){
                  alert("服务器错误");
                }else{
                  form.find("input[name='login-link']").val(data['data']['loginURL']);
                  form.find("input[name='username-id']").val(data['data']['userNameID']);
                  form.find("input[name='password-id']").val(data['data']['passwordID']);
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
              var validator=form.validate({
                submitHandler:function(){
                  var id=form.find("input.rule-id").val();
                  $.ajax({
                    url: baseURL+'/api/datacrawling/task/loginparam/'+id,
                    type: 'POST',
                    dataType: 'json',
                    data: {
                      loginURL:form.find("input[name='login-link']").val().trim(),
                      userNameID:form.find("input[name='username-id']").val().trim(),
                      passwordID:form.find("input[name='password-id']").val().trim(),
                      username:form.find("input[name='username']").val().trim(),
                      password:form.find("input[name='password']").val().trim()
                    }
                  })
                  .done(function(data) {
                    console.log("success");
                    if(data['errno']!=0){
                      alert("服务器错误");
                    }else{
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
            $downloadParamConfigBtn.on('click',{taskID:taskID},function(event){
              var taskID=event.data.taskID;
              var form=$("#download-param-form");
              $.ajax({
                url: baseURL+'/api/datacrawling/task/'+taskID,
                type: 'GET',
                dataType: 'json'
              })
              .done(function(data) {
                console.log("success");
                if(data['errno']!=0){
                  alert("服务器错误");
                }else{
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
              var validator=form.validate({
                submitHandler:function(){
                  var id=form.find("input.rule-id").val();
                  $.ajax({
                    url: baseURL+'/api/datacrawling/task/downloadparam/'+id,
                    type: 'POST',
                    dataType: 'json',
                    data: {
                      threadNum: form.find("input[name='thread-num']").val(),
                      timeout: form.find("input[name='timeout-num']").val(),
                      charset: form.find("input[name='charset']").val(),
                      datagross: form.find("input[name='data-gross']").val()||0
                    }
                  })
                  .done(function(data) {
                    console.log("success");
                    if(data['errno']!=0){
                      alert(data['data']['msg']);
                    }else{
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
    $templateExtractBtn.on('click',function(){
      var $templateListBtn=$("#template-list-btn");
      var $newTemplateBtn=$("#new-template-btn");
      $templateListBtn.off('click');
      $templateListBtn.on('click',function(){
        var tmpl=$.templates("#template-list");
        $.ajax({
          url: baseURL+'/api/datacrawling/task/template/all',
          type: 'GET',
          dataType: 'json'
        })
        .done(function(data) {
          console.log("success");
          if(data['errno']!=0){
            alert("服务器错误");
          }else{
            var content=data['data'];
            var html=tmpl.render(content);
            $("#template-list-content").html(html);
            $(".change-template").off('click');
            $(".change-template").on('click',function(event){
              $("#edit-template .return-btn").off();
              $("#edit-template .return-btn").on('click', function(event) {
                event.preventDefault();
                /* Act on the event */
                $("#edit-template").removeClass("active");
                $("#template-list-table").addClass('active');
                $templateExtractBtn.click();
              });
              var $tr=$(this).parents("tr");
              var templateID=$tr.find("th[name='template-id']").text().trim();
              var form=$("#change-template-form");
              form.find("input[name='template-id']").val(templateID);
              $.ajax({
                url: baseURL+'/api/datacrawling/task/template/'+templateID,
                type: 'GET',
                dataType: 'json'
              })
              .done(function(data) {
                console.log("success");
                if(data['errno']!=0){
                  alert("服务器错误");
                }else{
                  form.find("input[name='task-id']").val(data['data']['taskID']);
                  form.find("p[name='pattern-name']").text(data['data']['templateName']);
                  form.find("input[name='pattern-type']").val(data['data']['templateType']);
                  form.find("input[name='pattern-xpath']").val(data['data']['templateXpath']);
                  form.find("input[name='pattern-title-xpath']").val(data['data']['templateHeaderXpath']);
                  form.find("input[name='pattern-formula']").val(data['data']['templateFormula']);
                }
              })
              .fail(function() {
                console.log("error");
              })
              .always(function() {
                console.log("complete");
              });
              var vm=$(this);
              var validator=form.validate({
                submitHandler:function(){
                  var id=form.find("input[name='template-id']").val();
                  $.ajax({
                    url: baseURL+'/api/datacrawling/task/template/'+id,
                    type: 'POST',
                    dataType: 'json',
                    data: {
                      taskID:form.find("input[name='task-id']").val().trim(),
                      templateName:form.find("p[name='pattern-name']").text().trim(),
                      templateType:form.find("input[name='pattern-type']").val().trim(),
                      templateXpath:form.find("input[name='pattern-xpath']").val().trim(),
                      templateFormula:form.find("input[name='pattern-formula']").val().trim(),
                      templateHeaderXpath:form.find("input[name='pattern-title-xpath']").val().trim()
                    }
                  })
                  .done(function(data) {
                    console.log("success");
                    if(data['errno']!=0){
                      alert(data['data']['msg']);
                    }else{
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
      $newTemplateBtn.on('click',function(){
        var tmpl=$.templates("#task-id-list");
        $.ajax({
          url: baseURL+'/api/datacrawling/task/all',
          type: 'GET',
          dataType: 'json',
        })
        .done(function(data) {
          console.log("success");
          if(data['errno']!=0){
            alert("服务器错误");
          }else{
            var content=data['data'];
            var html=tmpl.render(content);
            $("#task-id-list-content").html(html);
          }
        })
        .fail(function() {
          console.log("error");
        })
        .always(function() {
          console.log("complete");
        });
        var form=$("#new-template-form");
        form.find("input[name='pattern-name']").val("");
        form.find("input[name='pattern-type']").val("");
        form.find("input[name='pattern-xpath']").val("");
        form.find("input[name='pattern-formula']").val("");
        form.find("input[name='pattern-title-xpath']").val("");
        var validator=form.validate({
          submitHandler:function(){
            $.ajax({
              url: baseURL+'/api/datacrawling/task/template/new',
              type: 'POST',
              dataType: 'json',
              data:{
                taskID:form.find("select[name='task-id']").val().trim(),
                templateName:form.find("input[name='pattern-name']").val().trim(),
                templateType:form.find("input[name='pattern-type']").val().trim(),
                templateXpath:form.find("input[name='pattern-xpath']").val().trim(),
                templateFormula:form.find("input[name='pattern-formula']").val().trim(),
                templateHeaderXpath:form.find("input[name='pattern-title-xpath']").val().trim()
              }
            })
            .done(function(data) {
              console.log("success");
              if(data['errno']!=0){
                alert(data['data']['msg']);
              }else{
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
    $taskMonitorBtn.on('click',function(){
      var tmpl=$.templates("#task-control-list");
      $.ajax({
        url: baseURL+'/api/datacrawling/task/all',
        type: 'GET',
        dataType: 'json'
      })
      .done(function(data) {
        console.log("success");
        if(data['errno']!=0){
          alert("服务器错误");
        }else{
          var content=data['data'];
          var html=tmpl.render(content);
          $("#task-control-list-content").html(html);
          $(".crawler-control-btn").off('click');
          $(".crawler-control-btn").on('click',function(){
            var $tr=$(this).parents("tr");
            var taskID=$tr.find("th.task-id").text().trim();
            var action=$(this).attr("name");
            $.LoadingOverlay("show");
            $.ajax({
              url: baseURL+'/api/datacrawling/task/monitor?action=option&option='+action+'&taskID='+taskID,
              type: 'GET',
              dataType: 'json'
            })
            .done(function(data) {
              console.log("success");
              $.LoadingOverlay("hide",true);
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

    });
    $newTaskBtn.click();
  });
  $monitorBtn.on('click',function(event) {
    var $taskCrawlingMonitorBtn=$("#task-crawling-monitor-btn");
    $taskCrawlingMonitorBtn.off('click');
    $taskCrawlingMonitorBtn.on('click',function(){
      var tmpl=$.templates("#task-monitor-list");
      $.ajax({
        url: baseURL+'/api/datacrawling/task/monitor?action=status',
        type: 'GET',
        dataType: 'json'
      })
      .done(function(data) {
        console.log("success");
        if(data['errno']!=0){
          alert("服务器错误");
        }else{
          var html=tmpl.render(data['data']);
          $("#task-monitor-list-content").html(html);
        }
      })
      .fail(function() {
        console.log("error");
      })
      .always(function() {
        console.log("complete");
      });

      var handle=setInterval(function(){
        $.ajax({
          url: baseURL+'/api/datacrawling/task/monitor?action=status',
          type: 'GET',
          dataType: 'json'
        })
        .done(function(data) {
          console.log("success");
          if(data['errno']!=0){
            alert("服务器错误");
          }else{
            var html=tmpl.render(data['data']);
            $("#task-monitor-list-content").html(html);
          }
        })
        .fail(function() {
          console.log("error");
        })
        .always(function() {
          console.log("complete");
        });
        if(handle==undefined){
          console.log("undefined");
        }else{
          if(!$("#task-crawling-monitor").hasClass('active')){
            clearInterval(handle);
          }
        }
      },3000);
    });
    $taskCrawlingMonitorBtn.click();
  });
  $deliveryBtn.on('click',function (event) {
      var tmpl=$.templates("#delivery-task-list");
      $.ajax({
          url:baseURL+"/api/datacrawling/task/all",
          type: 'GET',
          dataType: 'json'
      }).done(function (data) {
          if(data['errno']!=0){
            alert('服务器错误');
          }else {
            content=data['data'];
            var html=tmpl.render(content);
            $("#delivery-task-list-content").html(html);
            $(".see-delivery-task-btn").off('click');
            $(".see-delivery-task-btn").on('click',function (event) {
                var $tr=$(this).parents("tr");
                var taskID=$tr.find("th.task-id").text();
                var runningMode=$tr.find("th.running-mode").text();
                if(runningMode.trim()=="文本型"){
                  alert("文本型数据交付请直接到工作路径下查看");
                  return;
                }
                $("#delivery-modal").off('shown.bs.modal');
                $('#delivery-modal').on('shown.bs.modal', function () {
                  var selectTmpl=$.templates("#table-name-list");
                  $.ajax({
                      url:baseURL+"/api/datacrawling/data/"+taskID,
                      type:"GET",
                      dataType:'json'
                  }).done(function (data) {
                      if(data['errno']!=0){
                        alert('服务器错误');
                      }else {
                        //temporary
                          var content=data['data'];
                          var html=selectTmpl.render(content);
                          $("#table-name-list-content").html(html);
                      }
                  });
                  $("#table-data-search-btn").off('click');
                  var getTableData=function (id,tbName,pageNum) {
                      var tmpl=$.templates("#table-data-list");
                      $.ajax({
                          url:baseURL+"/api/datacrawling/data/all?id="+id+"&tbName="+tbName+"&pageNum="+pageNum+"&pageSize="+10,
                          type:"GET",
                          dataType:"json"
                      }).done(function (data) {
                          if(data['errno']!=0){
                            alert(data['data']);
                          }else {
                            var content=data['data']['content'];
                            var headContent=[];
                            var bodyContent=[];
                            if(content.length>=1){
                              for(key in content[0]){
                                headContent.push(key);
                              }
                              for(var i=0;i<content.length;i++){
                                var row=[];
                                for(key in content[i]){
                                  row.push(content[i][key]);
                                }
                                bodyContent.push(row);
                              }
                            }
                            content['headContent']=headContent;
                            content['bodyContent']=bodyContent;
                            var html=tmpl.render(content);
                            $("#table-data-list-content").html(html);
                          }
                      })
                  };
                  $("#table-data-search-btn").on('click',function () {
                    var tbName=$("#table-name-list-content").val().trim();
                    var pg=1;
                    getTableData(taskID,tbName,pg);
                    $("#pre-pg-btn").off('click');
                    $("#next-pg-btn").off('click');
                    $("#pre-pg-btn").on('click',function () {
                        if(pg==1){
                          alert("已是最前页");
                        }else {
                          pg--;
                          getTableData(taskID,tbName,pg);
                        }
                    });
                    $("#next-pg-btn").on('click',function () {
                        pg++;
                        getTableData(taskID,tbName,pg);
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
    var $dbConfigBtn=$("#db-config-btn");
    $dbConfigBtn.on('click',function(event) {
      var render=function(){
        $.ajax({
          url: baseURL+'/api/datacrawling/config/mysql',
          type: 'GET',
          dataType: 'json'
        })
        .done(function(data) {
          if(data['errno']!=0){
            alert('服务器错误');
          }else{
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
      var validator=$("#db-form").validate({
        submitHandler:function(){
          var mysqlURL=$("#mysql-url").val().trim();
          var mysqlUserName=$("#mysql-username").val().trim();
          var mysqlPassword=$("#mysql-password").val().trim();
          if(mysqlURL==""||mysqlUserName==""||mysqlPassword==""){
            alert("输入不能为空");
            return;
          }
          $.ajax({
            url: baseURL+'/api/datacrawling/config/mysql',
            type: 'POST',
            dataType: 'json',
            data: {
              mysqlURL: mysqlURL,
              mysqlUserName:mysqlUserName,
              mysqlPassword:mysqlPassword
            }
          })
          .done(function(data) {
            if(data['errno']!=0){
              alert('服务器错误');
            }else{
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
  $requestBtn.click();

});
