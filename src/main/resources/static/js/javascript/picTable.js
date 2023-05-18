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
            axios.get("/file/findAll", this.formData, ).then((result) => {

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