var b=document.querySelector("body")
b.style.height=document.documentElement.clientHeight+'px'

new Vue({
    el: '#app',
    data() {
        return {
            form: {},

            rules: {
                email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
                password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
            },
        }
    },
    methods: {
        onSubmit() {
            const that = this
            axios.post("/user/login",this.form,).then((result)=>{

                if(result.data=="not_exist"){
                    this.$alert('账号不存在，请重新输入','警告', {
                            confirmButtonText: '确定',
                        }).then(function () {
                            that.$refs.email.focus();
                        });
                }else if(result.data=="password_error"){
                    this.$alert('密码错误，请重新输入','警告', {
                        confirmButtonText: '确定',
                    }).then(function () {
                        that.$refs.psw.focus();
                    });
                }else{
                    Cookies.set('email', this.form.email, { expires: 1 });
                    let lo = window.location.href.indexOf("/Login.html")
                    let newUrl =window.location.href.substring(0,lo)
                    window.location.href= newUrl
                }
            })
        }
    }
});
