layui.define(['layer','base64','jqCookie'], function (exports) {

    let $ = layui.$,
        layer = layui.layer,
        base64 = layui.base64,
        jqCookie = layui.jqCookie;

    let userOpt = {
        user: undefined,
        token: undefined,
        parse: function (token) {
            if (!this.verify(token)){
                return null;
            }
            let payload = token.split(".")[1];
            let user = base64.decode(payload);
            return JSON.parse(user);
        },
        verify: token=>{
            // TODO 引入JWT-js
            return true;
        },
        getUser: function(){
            if (!this.user) {
                let token = jqCookie.cookie('get',{
                    name: 'user-token'
                });
                this.user = this.parse(token);
            }
            return this.user;
        },
        // 重新 加载数据
        reload: function(token) {
            console.log("this", this)
            let user = this.parse(token);
            this.token=token;
            this.user = user;
            return user;
        },
        // 持久化
        persistent: token => {},
        clear: function (){
            jqCookie.cookie('delete', {
                name: 'user-token'
            })
        }
    };

    //暴露接口
    exports('userOpt', userOpt);
})