<template>
  <div style="width: 100%;height: 100vh;background-color: powderblue;overflow:hidden">
      <div style="width:400px;margin: 150px auto;">
        <div style="color: azure;font-size: 30px;text-align: center;padding:30px 0 "> 欢迎注册</div>
        <el-form ref="form" :model="form" size="normal" :rules="rules">
          <el-form-item prop="username">

            <el-input v-model="form.username"  >
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password"  show-password ></el-input>
          </el-form-item>
          <el-form-item  prop="confirm">
            <el-input v-model="form.confirm"  show-password ></el-input>
          </el-form-item>
          <el-form-item>
           <el-button style="width: 100% " type="primary" @click="register"> 注册</el-button>
          </el-form-item>
        </el-form>
      </div>
  </div>
</template>

<script>

import {ElMessage} from "element-plus";
import request from "@/utils/request";

export default {
  name: "Register",
  data(){
    return{
      form:{},
      rules:{
        username:[
          {
            required: true,
            message: '请输入用户名',
            trigger: 'blur',
          },
        ],
        password:[
          {
            required: true,
            message: '请输入密码',
            trigger: 'blur',
          },
        ],
        confirm:[
          {
            required: true,
            message: '请输入确认密码',
            trigger: 'blur',
          },
        ]
      }
    }
  },
  methods:{
    register(){

      if (this.form.password !== this.form.confirm){
        ElMessage({
          type:"error",
          message:"两次密码不相同"
        })
        return
      }

      this.$refs['form'].validate((valid) => {
        if (valid) {
          alert('submit!')

          request.post("/user/register",this.form).then(res => {
            if(res.code === '0'){
              ElMessage({
                type:"success",
                message:"z注册成功"
              })
              this.$router.push("/login") //登录成功之后进行跳转
            }else{
              ElMessage({
                type:"error",
                message: res.msg
              })
            }
          })

        } else {
          console.log('error submit!!')
          return false
        }
      })


    }
  }
}
</script>

<style scoped>

</style>