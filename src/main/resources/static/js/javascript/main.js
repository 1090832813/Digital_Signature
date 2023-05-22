new Vue({
    el: '#app',
    data() {
        return {
            rname:'',
            par:'',
            AES:'',
            dig:'',
            filelist: [],
            digList:[],
            verify:{},
            verifyList:[],
            formData: {},
            search:'',
            dialogFormVisible:false,
            dialogFormVisible2:false,
            dialogFormVisible4Edit: false,
            dialogImageUrl:'',
            dialogVisible:false,
            disabled:false,
            // 默认显示第几页
            currentPage:1,
            // 总条数，根据接口获取数据长度(注意：这里不能为空)
            totalCount:1,
            // 个数选择器（可修改）
            pageSizes:[3,5,10],
            // 默认每页显示的条数（可修改）
            PageSize:5,
            rules: {
                picture_name: [{ required: true, message: '请输入图片名称', trigger: 'blur' }],
                AES: [{ required: true, message: '请输入AES密钥', trigger: 'blur' }],
                dig: [{ required: true, message: '请上传签名文件', trigger: 'blur' }],
                picture:[{ required: true, message: '请上传图片', trigger: 'blur' }]
            },

        }
    },
    methods: {
        sea(){
            let that = this
            if(this.search=='')
                this.init()
            else {
                axios.post("/file/find",this.search,{headers: { 'Content-Type': 'text/plain'}}).then(res=>{
                that.filelist = res.data
                that.totalCount=res.data.length
                })
            }

        },
        // 每页显示的条数
        handleSizeChange(val) {
            // 改变每页显示的条数
            this.PageSize=val
            // 注意：在改变每页显示的条数时，要将页码显示到第一页
            this.currentPage=1
        },
        // 显示第几页
        handleCurrentChange(val) {
            // 改变默认的页数
            this.currentPage=val
        },

        handleOpen(key, keyPath) {
            console.log(key, keyPath);

        },
        handleClose(key, keyPath) {
            console.log(key, keyPath);
        },

        handlePreview(file) {
            console.log(file);
        },




        handlePictureCardPreviewTable(file) {

            this.dialogImageUrl =window.document.location.href+'./img/'+file.picture_realname+'.'+file.picture_type;
            console.log(this.dialogImageUrl)
            this.dialogVisible = true;
        },
        handleDownload(file) {
            let f =file.picture_realname+"."+file.picture_type
            axios.post("/file/download",f,).then((result)=>{
                var url = ("http://localhost:8081/img/"+result.config.data)
                var that = this
                var fileName = file.picture_name
                console.log(fileName)
                this.convertUrlToBase64(url).then(function (base64) {
                    var blob = that.convertBase64UrlToBlob(base64); // 转为blob对象
                    // 下载
                    if (that.myBrowser() == "IE") {
                        window.navigator.msSaveBlob(blob, fileName + ".png");
                        console.log(blob)
                    } else if (that.myBrowser() == "FF") {
                        window.location.href = url;
                    } else {
                        var a = document.createElement("a");
                        a.download = fileName;
                        a.href = URL.createObjectURL(blob);
                        console.log(a.href)
                        document.body.appendChild(a)
                        a.click();
                        URL.revokeObjectURL(a.href) // 释放URL 对象
                        document.body.removeChild(a)
                    }
                });
            })
            f=f+";"+Cookies.get('email')
            axios.post("/file/downloadMsg",f,{headers: { 'Content-Type': 'application/json','data': 'JSON.stringify(Data)'}}).then((res)=>{
                console.log(res)

                this.downloadFile(res.data,file.picture_name)
            })
        },
        downloadFile(data,fileName){
            // data为blob格式
            var blob = new Blob([data]);
            var downloadElement = document.createElement('a');
            var href = window.URL.createObjectURL(blob);

            downloadElement.href = href;
            downloadElement.download = fileName;
            document.body.appendChild(downloadElement);
            downloadElement.click();
            document.body.removeChild(downloadElement);
            window.URL.revokeObjectURL(href);
        },

        // 转换为base64
        convertUrlToBase64(url) {
            return new Promise(function (resolve, reject) {
                var img = new Image();
                //设置图片跨越，没有跨越cavas会被污染无法画出
                img.crossOrigin = "Anonymous";
                img.src = url;
                img.onload = function () {
                    var canvas = document.createElement("canvas");
                    canvas.width = img.width;
                    canvas.height = img.height;
                    var ctx = canvas.getContext("2d");
                    ctx.drawImage(img, 0, 0, img.width, img.height);
                    var ext = img.src
                        .substring(img.src.lastIndexOf(".") + 1)
                        .toLowerCase();
                    //转换为base64
                    var dataURL = canvas.toDataURL("image/" + ext);
                    var base64 = {
                        dataURL: dataURL,
                        type: "image/" + ext,
                        ext: ext
                    };
                    resolve(base64);
                };
            });
        },
        // base64转换为blob流
        convertBase64UrlToBlob(base64) {
            var parts = base64.dataURL.split(";base64,");
            var contentType = parts[0].split(":")[1];
            var raw = window.atob(parts[1]);
            var rawLength = raw.length;
            var uInt8Array = new Uint8Array(rawLength);
            for (var i = 0; i < rawLength; i++) {
                uInt8Array[i] = raw.charCodeAt(i);
            }
            return new Blob([uInt8Array], {
                type: contentType
            });
        },
        // 判断浏览器
        myBrowser() {
            var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
            if (userAgent.indexOf("OPR") > -1) {
                return "Opera";
            } //判断是否Opera浏览器 OPR/43.0.2442.991
            if (userAgent.indexOf("Firefox") > -1) {
                return "FF";
            } //判断是否Firefox浏览器  Firefox/51.0
            if (userAgent.indexOf("Trident") > -1) {
                return "IE";
            } //判断是否IE浏览器 Trident/7.0; rv:11.0
            if (userAgent.indexOf("Edge") > -1) {
                return "Edge";
            } //判断是否Edge浏览器 Edge/14.14393
            if (userAgent.indexOf("Chrome") > -1) {
                return "Chrome";
            } // Chrome/56.0.2924.87
            if (userAgent.indexOf("Safari") > -1) {
                return "Safari";
            } //判断是否Safari浏览器 AppleWebKit/534.57.2 Version/5.1.7 Safari/534.57.2
        },

        init(){
            const that = this
            axios.get("/file/findAll", this.formData, ).then((result) => {

                that.filelist = result.data
                that.totalCount=result.data.length
            });

        },
        quit(){
            Cookies.remove('email');
            this.init()
        },
        //重置表单
        resetForm() {
            this.formData = {};
            //this.$refs['myDig'].clearValidate();
            this.$nextTick(() => {
                this.$refs.mYupload.clearFiles()
            })

        },
        //重置解密
        resetDig() {
            this.digList = [];

            this.$nextTick(() => {
                this.$refs.myDig.clearFiles()
            })
        },
        //重置验证
        resetDig() {
            this.verifyList = [];

            this.$nextTick(() => {
                this.$refs.myVerify.clearFiles()
            })
        },

        uploadDig(file){
            this.dig=file.raw;
        },
        handleDig(file) {
            this.rname=file.picture_realname;
            this.resetDig();
            this.dialogFormVisible2 = true;
            console.log(this.dialogFormVisible2)
        },
        handleVerify() {
            this.resetForm();
            this.dialogFormVisible4Edit = true;
        },
        openFile(file,name) {
            let this_vue=this
            //验证的文件
            console.log(name.picture_name)
            var reader = new FileReader();
            reader.onload = function () {
                if (reader.result) {
                    //打印文件内容
                    console.log(reader.result);
                    var p= name.picture_id+";"+Cookies.get('email')+';'+ reader.result
                    console.log("p是："+p)
                    axios.post("/file/verify", p,{headers: { 'Content-Type': 'application/json','data': 'JSON.stringify(Data)'}}).then((res) => {
                        if(res.data!=0){
                            this_vue.$alert('AES密钥(请复制):'+res.data,'验证成功', {
                                confirmButtonText: '确定',
                            });
                        }else{
                            this_vue.$alert("请重新确认",'验证错误', {
                                confirmButtonText: '确定',
                            });
                        }
                    })
                }
            };
            reader.readAsText(file.raw);
            this.resetDig()
        },
        handleEncryptionDig(){
            let file=this.dig
            let this_vue=this
            let aes = this.formData.AES+";"+this.rname;
            let reader = new FileReader();
            reader.onload = function () {
                if (reader.result) {
                    //打印文件内容
                    let str = reader.result+";"+aes+";"+Cookies.get('email');
                    axios.post("/file/encrypt",str,{headers: { 'Content-Type': 'application/json','data': 'JSON.stringify(Data)'}}).then((res) => {
                        if(res.data=="success"){
                            this_vue.$alert('文件签名正确！','验证成功', {
                                confirmButtonText: '确定',
                            });
                        }else if(res.data=="failed"){
                            this_vue.$alert('文件与签名不符，请重新检查！','验证出错', {
                                confirmButtonText: '确定',
                            });
                        }else {
                            this_vue.$alert('服务器可能出错，或密钥格式错误，请稍后重试！','验证出错', {
                                confirmButtonText: '确定',
                            });
                        }

                    });
                }
            };
            reader.readAsText(file);
            this.dialogFormVisible2 = false;
            this.rname=null;
            this.resetDig();
        },


    },
    mounted() {
        this.init();
        window.quit=this.quit
    }
})