var b=document.querySelector("body")
b.style.height=document.documentElement.clientHeight +'px'

new Vue({
    el: '#app',
    data() {
        return {
            form: {},

            rules: {
                user_name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
                email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
                password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
                passwordAgain:[{ required: true, message: '请输入密码', trigger: 'blur' }]
            },
        }
    },
    methods: {

        onSubmit() {
            const that = this
            if(this.form.password==this.form.passwordAgain){
                axios.post("/user/save",that.form,).then((result)=>{
                    if(result.data=="exist"){
                        this.$alert('邮箱已被使用，请重新输入','警告', {
                            confirmButtonText: '确定',
                        }).then(function () {
                            that.$refs.email.focus();
                        });
                    }else{
                        this.$alert('注册成功','恭喜', {
                            confirmButtonText: '确定',
                        }).then(function () {
                            window.location.href="Login.html"
                        });
                    }
                })
            }else {
                const pwda = this.$refs.pwda;

                this.$alert('两次密码不同，请重新输入','警告', {
                    confirmButtonText: '确定',
                }).then(function () {
                    //聚焦再次输入密码
                    that.$refs.pwda.focus();
                });
            }
        }
    }
});
