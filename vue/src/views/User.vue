<template>

  <div style="padding: 20px;width: 100%" >
<!--    功能区-->
    <div style="margin: 10px 0 ">
      <el-button  type="primary" @click="add">新增</el-button>
      <el-button  type="primary">导入</el-button>
      <el-button  type="primary">导出</el-button>
    </div>
<!--    搜索区域-->
    <div style="margin: 10px 0"  >
      <el-input v-model="search" placeholder="Please input key word" style="width: 20%" clearable>
      </el-input>

        <el-button type="primary" style="margin-left: 10px" @click="load"> 查询</el-button>


    </div>


    <el-table :data="tableData"
              border
              stripe >
      <el-table-column prop="id" label="ID"   sortable/>
      <el-table-column prop="username" label="用户名"  />
      <el-table-column prop="nickName" label="昵称"  />
      <el-table-column prop="age" label="年龄"  />
      <el-table-column prop="sex" label="性别"  />
      <el-table-column prop="address" label="地址"  />
      <el-table-column fixed="right" label="Operations" >
        <template #default="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除吗?" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button type="danger" size="small" >删除</el-button>
            </template>
          </el-popconfirm>

        </template>
      </el-table-column>
    </el-table>
    <div>
      <el-pagination
          v-model:currentPage="currentPage"
          :page-sizes="[5,10,20]"
          :page-size="pageSize"
          layout="total,sizes,prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      >
      </el-pagination>

      <el-dialog
          v-model="dialogVisible"
          title="用户信息"
          width="30%">
        <el-form  :model="form"  >
          <el-form-item label="用户名" label-width="120px">
            <el-input v-model="form.username" style="width: 50%"></el-input>
          </el-form-item>
          <el-form-item label="昵称" label-width="120px">
            <el-input v-model="form.nickName" style="width: 50%"></el-input>
          </el-form-item>
          <el-form-item label="年龄" label-width="120px">
            <el-input v-model="form.age" style="width: 50%"></el-input>
          </el-form-item>
          <el-form-item label="性别" label-width="120px">
            <el-radio v-model="form.sex" label="男" style="margin-left: 20px">男</el-radio>
            <el-radio v-model="form.sex" label="女">女</el-radio>
            <el-radio v-model="form.sex" label="未知">未知</el-radio>

<!--            <el-input v-model="form.sex" style="width: 50%"></el-input>-->



<!--            <el-select v-model="form.sex" >
              <el-option
                  v-for="item in options"
                  :label="food"
                  :value="make"
              >
              </el-option>
            </el-select>      -->
          </el-form-item>
          <el-form-item label="地址" label-width="120px">
            <el-input v-model="form.address" style="width: 50%"></el-input>
          </el-form-item>

        </el-form>

        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisible = false">Cancel</el-button>
                <el-button type="primary" @click="save">Confirm</el-button>
            </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src

import request from "@/utils/request";
import {ElMessage} from "element-plus";

export default {
  name: 'Home',
  components: {
  },



  data() {
    return {
      form:{},
      dialogVisible:false,
      search:'',
      currentPage:1,
      total:10,
      pageSize:10,
      tableData: [

      ]
    }
  },
  created(){
    this.load()
  },
  methods: {
    load(){
      request.get("/user",{
        params:{
          pageNum:this.currentPage,
          pageSize:this.pageSize,
          search:this.search
        }

      }).then( res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    add(){
      this.dialogVisible = true
      this.form = {}
    },
    save(){
      if(this.form.id){
        request.put("/user",this.form).then(res => {
          console.log(res)
          if(res.code === '0'){
            ElMessage({
              type:"success",
              message:"修改成功"
            })
          }else {
            ElMessage({
              type:"error",
              message:res.msg
            })
          }

          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗

        })
      }else{
        request.post("/user",this.form).then(res => {
          console.log(res)
          if(res.code === '0'){
            ElMessage({
              type:"success",
              message:"增加成功"
            })
          }else {
            ElMessage({
              type:"error",
              message:res.msg
            })
          }

          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })
      }

    },
    handleDelete(id){
      console.log(id)
      request.delete("/user/" + id).then(res => {
        if(res.code === '0'){
          ElMessage({
            type:"success",
            message:"删除成功"
          })
        }else {
          ElMessage({
            type:"error",
            message:res.msg
          })
        }
        this.load()
      })
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    handleSizeChange(pageSize){  //改变当前每页个数
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){ //改变当前页码触发
      this.currentPage =pageNum
      this.load()
    }
  },
}
</script>
