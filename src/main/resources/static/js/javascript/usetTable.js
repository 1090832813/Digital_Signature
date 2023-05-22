new Vue({
    el:'#app',
    data(){
        return {
            userlist:[],
            search:'',
            // 默认显示第几页
            currentPage:1,
            // 总条数，根据接口获取数据长度(注意：这里不能为空)
            totalCount:1,
            // 个数选择器（可修改）
            pageSizes:[3,5,10],
            // 默认每页显示的条数（可修改）
            PageSize:5,
        }
    },
    methods:{
        init(){
            let that = this;
            axios.get("/user/findAll", this.formData, ).then((result) => {

                that.userlist = result.data
                that.totalCount=result.data.length
            });
        },
        del(user){
            console.log(user)
            axios.post("/user/delete",user,).then((result)=>{
                console.log(result)
                this.init();
            });

        },
        pubkey(str){
            return (str||"").split(";")[0]
        },
        sea(){
            let that = this
            if(this.search=='')
                this.init()
            else {
                axios.post("/user/search",this.search,{headers: { 'Content-Type': 'text/plain'}}).then(res=>{
                    that.userlist = res.data
                    that.totalCount=res.data.length
                })
            }

        },
    },
    mounted() {
        this.init();
    }
})