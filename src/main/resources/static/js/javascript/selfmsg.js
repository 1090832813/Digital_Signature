new Vue({
    el:'#app',
    data(){
        return{
            labelPosition: 'right',
            formData:{},
            path:'index.html'
        }
    },
    methods:{
        init(){
            let that = this
            axios.post("/user/find","1090832813@qq.com",{headers: { 'Content-Type': 'application/json','data': 'JSON.stringify(Data)'}}).then((result)=>{
                console.log(result)
                that.formData=result.data
            });
        },
        updateMsg(){
            console.log(this.formData)
            let that = this
            axios.post("/user/update",this.formData,).then((result)=>{
                console.log(result)
                if(result.data=="success"){
                    that.open2()
                    that.init()
                }else if(result.data=="exist"){
                    that.open4()
                }
            })
        },
        open2() {
            this.$message({
                message: '恭喜你，信息修改成功！',
                type: 'success'
            });
        },
        open4() {
            this.$message.error('邮箱已被使用，请重试！');
        }
    },
    mounted(){
        this.init()
    }
})