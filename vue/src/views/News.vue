<template>

  <div style="padding: 20px;width: 100%" >
    <!--    功能区-->
    <div style="margin: 10px 0 ">
      <el-button  type="primary" @click="add">新增</el-button>
<!--      <el-button  type="primary">导入</el-button>-->
<!--      <el-button  type="primary">导出</el-button>-->
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
      <el-table-column prop="id" label="ID" sortable/>
      <el-table-column prop="title" label="标题"  />
<!--      <el-table-column prop="content" label="内容"  />-->
      <el-table-column prop="author" label="作者"  />
      <el-table-column prop="time" label="发布日期"  />
      <el-table-column fixed="right" label="操作" >
        <template #default="scope">
          <el-button type="primary" size="small" @click="details(scope.row)">详情</el-button>
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

      <el-dialog v-model="dialogVisible" title="用户信息"  width="50%">
        <el-form  :model="form"  >
          <el-form-item label="标题" label-width="120px">
            <el-input v-model="form.title" style="width: 50%"></el-input>
          </el-form-item>
          <div id="div1"></div>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisible = false">Cancel</el-button>
                <el-button type="primary" @click="save">Confirm</el-button>
            </span>
        </template>
      </el-dialog>
      <el-dialog v-model="vis" title="详情"  width="50%">
        <el-card>
          <div v-html="detail.content" style="min-height:100px"></div>
        </el-card>
      </el-dialog>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src

import request from "@/utils/request";
import {ElMessage} from "element-plus";
import E from 'wangeditor';
let editor;
export default {
  name: 'News',
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
      tableData: [],
      user:{},
      detail:{},
      vis:false,
    }
  },
  created(){
    this.load()
  },
  mounted() {
  },
  methods: {
    details(row){
      this.detail = row
      this.vis = true
    },
    filesUploadSuccess(res){
      console.log(res)
      this.form.cover = res.data
    },
    load(){
      request.get("/news",{
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
      this.$nextTick(() => {

        if(!editor){
          editor = new E('#div1')
          // 配置 server 接口地址
          editor.config.uploadImgServer = 'http://localhost:9090/files/editor/upload'
          editor.config.uploadFileName = 'file'
          editor.create()
        }
        editor.txt.html("")
      })

    },
    save(){

      this.form.content = editor.txt.html()   //获取编辑器里面的值，再传给后端
      if(this.form.id){
        request.put("/news",this.form).then(res => {
          console.log(res)
          if(res.code === '0'){
            ElMessage({
              type:"success",
              message:"更新成功"
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
        let userStr = sessionStorage.getItem("user") || "{}"
        let user = JSON.parse(userStr)
        this.form.author =  user.username
        request.post("/news",this.form).then(res => {
          console.log(res)
          if(res.code === '0'){
            ElMessage({
              type:"success",
              message:"新增成功"
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
      request.delete("/news/" + id).then(res => {
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

      this.$nextTick(() => {
        if (!editor) {
          editor = new E('#div1')

          // 配置 server 接口地址
          editor.config.uploadImgServer = 'http://localhost:9090/files/editor/upload'
          editor.config.uploadFileName = "file"  // 设置上传参数名称
          editor.create()
        }

        editor.txt.html(row.content)
      })
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
