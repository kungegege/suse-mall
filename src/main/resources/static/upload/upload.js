// 上传时调用的主方法
function imgsUpload(imgObj, obj,urls,imgNum) {

    //第一次指向默认的input
    var lastInput = $('.' + fileClass).last(),imgSrc;   

    // 上传
    $(".uploadImgs-add").click(function () {

        // 获取最后一个input
        lastInput = $('.' + fileClass).last();

        // 清空所有input 的Id
        $('.' + fileClass).each(function (k, v) {
            $(this).attr('id', '');
        })

        // 只给最后一个input正确的ID值和label联动
        lastInput.attr('id', 'fileUpIpt');


        // 重新绑定事件
        lastInput.unbind('change');
        lastInput.bind("change", function () {
                
            let file = lastInput[0].files[0], win = window.URL || window.webkitURL;

            imgSrc = win.createObjectURL(file);
            
            imgObj.push(imgSrc);
            filesArr.push(file);
            // 上传文件
            upload(file)

            // 图片预览
            var upImgs = "<div class='show-img-div2'><span><img src='../../upload/img/close.png' /></span><img src='" + imgSrc + "'/></div>";
            $("#labelId").before(upImgs);
            
            // 解决同图片删后 不能上传的 BUG
            lastInput.val('');

            if(filesArr.length>=imgNum){
                // 超过上传上限
                $("#labelId").hide()
            }

            // 删除图标绑定删除事件
            $(".show-img-div2").each(function (k, v) {

                $(this).find('span').unbind('click');

                $(this).find('span').bind("click", function () {
                    deleteImg(imgObj, k, obj)
                })

                // 和预览图片机制联动
                $(this).unbind('click');
                $(this).bind('click', function () {
                    gbOption = obj;
                });

            })

        })
    })

}

//点击关闭图标删除图片数组
function deleteImg(imgObj, k, obj) {
    console.log(k)
    imgObj.splice(k, 1);
    filesArr.splice(k, 1);
    urls.splice(k, 1);

    if(filesArr.length<imgMaxNum){
        $("#labelId").show()
    }

    //删除点击的自己
    $(".show-img-div2:eq(" + k + ")").remove();

    //删除后重新绑定事件
    $(".show-img-div2").each(function (k, v) {
        $(this).find('span').unbind('click');
        $(this).find('span').bind("click", function () {
            deleteImg(imgObj, k)
        })
        //和预览图片机制联动
        $(this).unbind('click');
        $(this).bind('click', function () {
            gbOption = obj;
        });
    })

    //点击图片删除对应的input type=file
    if (!$('.' + fileClass).first()[0].files) { 
         //兼容IE，谷歌等如果需要和IE方式一样的方式，取消判断，input就不会删除
        $('.' + fileClass).eq(k).remove();
    }

}


function upload(file) {
    var  xmlHttp = new XMLHttpRequest();
  
    var formData = new FormData();
    formData.append("file",file)
    xmlHttp.open("post","http://127.0.1:8080/upload/", true)
    console.log("文件上传....")
    xmlHttp.send(formData)
    console.log("文件上传....finish")
    
    xmlHttp.onreadystatechange=()=>{
        if (xmlHttp.readyState==4 && xmlHttp.status==200){
            let {code,data,msg} = JSON.parse(xmlHttp.responseText)
            console.log(data)
            urls.push(data)
        }
    }
}