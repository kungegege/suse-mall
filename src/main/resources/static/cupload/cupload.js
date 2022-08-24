/**
 * 待开发功能：
 * 1. 拖拽排序
 * 2. 拖拽上传
 * 3. 放大预览
 * 4. 图片校验（大小、宽度、高度）
 * 5. OSS直传
 * 6. 取消自动上传 （目前是自动上传）
 * 7. 文件的类型限制（jpg、png、gif... pdf）
 */
class Cupload{

    // 默认值
    opt={
        height: "148px",
        width: "148px",
        // 是否开启图片删除功能
        enableDelete: true,
        enableFlag: true,
        // 是否开启图片次序调整功能
        enableResetOrder: true,
        limit: 4,
        // 图标标签 取消则传入 null
        tags: ['封面图片'],
        // 超出limit文件个数的限制回调
        exceedLimit(count, limit){
            alert('超过上限')
        },
        // 超出文件大小限制回调函数
        exceedFileSizeLimit(file, limit){
            alert(`${file.name} 超过文件大小限制, 最大支持${limit}kb`)
        },
        /**
         * 自定义文件上传，注意返回值是一个Promise对象
         * @param {File} files
         * @returns Promise
         */
        upload(files){
            return new Promise(function(resolve,reject){
                setTimeout(()=>{
                    let res = {
                        code: 0,
                        msg: '操作成功',
                        data: ['https://img1.baidu.com/it/u=3715217316,2290628211&fm=253&fmt=auto&app=120&f=JPEG?w=690&h=1035']
                    }
                    // 返回具体的内容（图片地址）
                    resolve(res.data);
                },100)
            })
        },
        // 起始图片
        defaultImgs:[],
        // 文件上传后端接口 （默认post接口， 通过method修改）
        url:'',
        method: 'post',
        // 自定义数据返回格式, 完整数据*/
        parseData(res){
            return {
                "code": res.code,         //解析接口状态
                "msg": res.msg,             //解析提示文本
                "data": res.data,      //解析数据列表
            }
        },
        // 自定义响应状态
        response: {
            error(error){        // 错误回调函数, error: 错误msg
                alert(error)
            },
            success(res){},     // 成功回调函数 res: 数据 data
            statusCode: 0       //规定成功的状态码, 默认0
        },
        // 图片大小限制,单位:kb , 默认100kb
        maxFileSize: 100,

        /** ~~ 钩子函数 */
        // 选着文件后的钩子函数
        afterSelectFile(files){
            console.log("...afterSelectFile",files)
        },
        // 上传文件前的钩子函数
        beforeUpload(files){
            console.log("...beforeUpload",files)
        },
        // 上传后回调  res: 服务器返回的结果
        afterUpload(res){
            console.log("...afterUpload",res)
        },
        // 删除前回调 img: 图片地址
        beforeDeleteImg(img){
            console.log("...deleteImg",img)
        }
    }

    // 上传成功后的预览图片容器
    imgBox=undefined

    constructor(ele ,opt, upload){
        if(typeof ele === 'string'){
            this.ele = $(ele)
        }else {
            this.ele = ele
        }
        for(let key in opt){
            this.opt[key]=opt[key];
        }
        this._init();
    }

    // 重置
    reset(){
        $(".pre-img-box").remove();
        this._verifyLimit();
    }
    // 初始化
    _init(){
        console.log("init...")
        let uploadBox =  $(`<div class='upload-box'></div>`);

        uploadBox.css({
            'height': this.opt.height,
            'width': this.opt.width,
            'line-height': this.opt.height
        });

        this.ele.css('height',this.opt.height)

        // multiple 支持多文件上传
        let inputDom  = $(`<input class='cupload-input' type='file' multiple/>`);
        this.imgBox=$(`<div class='img-box'></div>`);

        uploadBox.append(`<span class='cupload-btn'>+</span>`);
        uploadBox.append(inputDom);

        inputDom.change((val)=>{
            this.opt.afterSelectFile(val);
            this._dealChange(val);
        })

        this.ele.append(this.imgBox);
        this.ele.append(uploadBox);

        if(this.opt.defaultImgs && this.opt.defaultImgs.length){
            let res = this._verifyLimit(this.opt.defaultImgs.length);
            if(res){
                this._genPreImgBox(this.opt.defaultImgs);
            }
        }
    }

    /**
     * limit 校验
     * @param {Number} expect 起始个数
     * @returns Boolean
     */
    _verifyLimit(expect=0){
        let count = this.getUrls().length + expect;
        console.log('count',count)
        if(count == this.opt.limit){
            // 隐藏上传框
            this.ele.find('.upload-box').hide();
            return true;
        }

        if(count > this.opt.limit){
            this.opt.exceedLimit(count,this.opt.limit)
            return false;
        }

        $('.upload-box').show();
        return true;
    }

    // 事件初始化
    dealOrderEvent(id, direction) {

        let target = $("#" + id);

        if(direction==='left'){
            let preNode = target.prev();
            if(preNode.length===0 || preNode.attr('id')===id) return;
            let clone=target.clone(true)
            target.remove();
            preNode.before(clone)
        }else{
            let afterNode = target.next();
            if(afterNode.length===0 || afterNode.attr('id')===id) {
                return;
            }
            let clone=target.clone(true)
            target.remove();
            afterNode.after(clone)
        }
        this._resetImgTags()
    }

    // 删除节点
    delImgBox(id){
        this.opt.beforeDeleteImg($("#" + id).attr('src'));
        $("#" + id).remove();
        this._verifyLimit(0);
        this._resetImgTags();
    }

    // 图片标签
    _resetImgTags(){
        if(this.opt.tags){
            let preImgBoxs = $(`.pre-img-box`);
            this._removeImgTag()
            for(let i=0;i<preImgBoxs.length && i<this.opt.tags.length;i++){
                let targetDom =  $(preImgBoxs[i]);
                let imgTag = $( `<div class='img-tag'></div>`)
                imgTag.append(`<div class='tag'>${this.opt.tags[i]}</div>`)
                targetDom.append(imgTag);
            }
        }
    }
    // 移出所有图片标签
    _removeImgTag(){
        $('.img-tag').remove()
    }
    // 文件上传input change监听处理函数
    _dealChange(val){
        let files = val.target.files
        if(this._verifyLimit(files.length)){
            if(this.opt.url){
                // 多文件上传
                let data = new FormData();

                $.each(files, (i, file)=>{
                    // 校验图片大小
                    if(this._verifyFileSize(file)){
                        data.append('files', file);
                    }else{
                        this.opt.exceedFileSizeLimit(file, this.opt.maxFileSize);
                    }
                });
                let parseData = this.opt.parseData;
                this.opt.beforeUpload(val);
                $.ajax({
                    url: this.opt.url,
                    type: this.opt.method,
                    data: data,
                    processData: false,
                    contentType: false,
                    dataType: 'json',
                    success: res=>{
                        // 上传回调
                        this.opt.afterUpload(res);
                        if(parseData){
                            let {msg,data,code} = this.opt.parseData(res);
                            let {error,success,statusCode} = this.opt.response;
                            if(code===statusCode){
                                success({msg,data,code});
                            }else{
                                error(msg)
                            }
                            this._genPreImgBox(data);
                        }else{
                            if(res.code==0){
                                let imgs = res.data;
                                this._genPreImgBox(imgs);
                            }else{
                                alert(res.msg);
                            }
                        }
                    }
                })

            }else{
                this.opt.upload(files).then(data=>{
                    this._genPreImgBox(data);
                })
            }
        }
    }

    /**
     * 生成预览框
     * @param {array} imgs 图片的地址 src
     */
    _genPreImgBox(imgs){
        let data = imgs;
        for(let i=0;i<data.length;i++){
            this._resetImgTags()

            let id = this._getUUid();
            let imgItemBox = $(`<div class='pre-img-box' id='${id}'></div>`);

            imgItemBox.append(`<img src='${data[i]}' class='preview-img' style='width:${this.opt.width}'/>`);
            this.imgBox.append(imgItemBox);

            if(this.opt.enableResetOrder){

                let cuploadOrderRight = $(`<span class='cupload-order-right'></span>`);
                let cuploadOrderLeft = $(`<span class='cupload-order-left'></span>`);

                // 板顶右移事件
                cuploadOrderRight.click(()=>{
                    this.dealOrderEvent(id,'right')
                })
                // 绑定左移事件
                cuploadOrderLeft.click(()=>{
                    this.dealOrderEvent(id,'left')
                })

                imgItemBox.append(cuploadOrderRight);
                imgItemBox.append(cuploadOrderLeft);
            }
            if(this.opt.enableDelete){
                let cuploadDel = $(`<span class='cupload-del'></span>`);
                imgItemBox.append(cuploadDel);
                // 删除
                cuploadDel.click(()=>{
                    this.delImgBox(id);
                })
            }
            // 蒙版
            imgItemBox.append(`<div class='cupload-masking'></div>`)
        }
        this._resetImgTags();
    }

    // 获取所有上传后的图片地址
    getUrls(){
        let previewImgs = this.ele.find('.pre-img-box img'), ans = [];
        for(let i=0;i<previewImgs.length;i++){
            ans[i]=$(previewImgs[i]).attr('src');
        }
        console.log('previewImgs', previewImgs, ans)
        return ans;
    }

    _getUUid(){
        function S4() {
            return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
        }
        return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
    }

    /**
     * 校验图片的大小
     * @param {File}} file
     * @returns    true-校验成功、 false-失败
     */
    _verifyFileSize(file){
        console.log(file.name,':',file.size/1024);
        if((file.size/1024) > this.opt.maxFileSize){
            return false;
        }
        return true;
    }

}