<template>
  <div style="width: 100%;height: 100vh;background-color: green;overflow:hidden">
      <div style="width:400px;margin: 150px auto;">
        <div style="color: azure;font-size: 30px;text-align: center;padding:30px 0 "> 欢迎登陆</div>
        <el-form ref="form" :model="form" size="normal" :rules="rules">
          <el-form-item prop="username">
            <el-input v-model="form.username"  >
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password"  show-password ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button style="width: 100% " type="primary" @click="login"> 登录</el-button>
          </el-form-item>
          <el-form-item>
            <el-button style="width: 100% " type="primary" @click="$router.push('/register')"> 注册</el-button>
          </el-form-item>

        </el-form>
      </div>
  </div>
</template>

<script>

import {ElMessage} from "element-plus";
import request from "@/utils/request";

export default {
  name: "Login",
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
        ]
      }
    }
  },
  methods:{
    login(){
      this.$refs['form'].validate((valid) => {
        if (valid) {
          request.post("/user/login",this.form).then(res => {
            if(res.code === '0'){
              ElMessage({
                type:"success",
                message:"登录成功"
              })
              sessionStorage.setItem("user",JSON.stringify(res.data))
              this.$router.push("/user") //登录成功之后进行跳转
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