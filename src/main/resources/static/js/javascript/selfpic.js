new Vue({
    el: '#app',
    data() {
        return {
            rname:'',
            par:'',
            AES:'',
            dig:'',
            search:'',
            filelist: [],
            digList:[],
            verify:{},
            verifyList:[],
            formData: {},
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
        //弹出添加窗口
        handleCreate() {
            this.resetForm();
            this.dialogFormVisible = true;
        },
        handlePicturePreview(file) {
            console.log(file)
            console.log(6)
            this.par=file.raw;
            const isLt2M = file.size / 1024 / 1024 < 10;

            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 10MB!');
                this.clearFiles();
                return isLt2M;
            }

        },
        handlePictureCardPreview(file) {
            this.dialogImageUrl = file.url;

            this.dialogVisible = true;
        },
        ifLogin(){
            if(Cookies.get('email')==undefined){
                return false
            }else {
                return Cookies.get('email')
            }
        },
        handleEdit() {
            let that = this
            if(this.ifLogin()) {
                const _file = this.par;
                // 通过 FormData 对象上传文件
                var formData = new FormData();
                formData.append("file", _file);
                this.formData.picture_user= this.ifLogin();
                console.log(this.formData)
                axios.post("/file/upload", formData).then((res) => {
                    this.formData.picture_realname = res.data.split(".")[0]
                    this.formData.picture_type = res.data.split(".")[1]
                    axios.post("/file/save", this.formData).then((result) => {
                        this.resetForm();
                        this.dialogFormVisible = false;
                        this.clearFiles()
                    }).then(function () {
                        that.init()
                    });
                })
            }else {
                this_vue.$alert('还未登录','警告', {
                    confirmButtonText: '确定',
                });
            }

        },
        resetForm() {
            this.formData = {};
            //this.$refs['myDig'].clearValidate();
            this.$nextTick(() => {
                this.$refs.mYupload.clearFiles()
            })

        },
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
        handlePicturePreview(file) {
            console.log(file)
            console.log(6)
            this.par=file.raw;
            const isLt2M = file.size / 1024 / 1024 < 10;

            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 10MB!');
                this.clearFiles();
                return isLt2M;
            }

        },
        handlePreview(file) {
            console.log(file);
        },

        del(user){
            console.log(user)
            axios.post("/file/delete",user,).then((result)=>{
                console.log(result)
                this.init();
            });

        },

        handlePictureCardPreview(file) {
            this.dialogImageUrl = file.url;

            this.dialogVisible = true;
        },
        handlePictureCardPreviewTable(file) {

            this.dialogImageUrl =window.document.location.href+'./img/'+file.picture_realname+'.'+file.picture_type;
            console.log(this.dialogImageUrl)
            this.dialogVisible = true;
        },





        init(){
            const that = this
            console.log(Cookies.get("email"))
            axios.post("/file/selfpic", Cookies.get("email"), {headers: { 'Content-Type': 'text/plain'}}).then((result) => {

                that.filelist = result.data
                that.totalCount=result.data.length
            });

        },


    },
    mounted() {
        this.init();
        window.quit=this.quit
    }
})