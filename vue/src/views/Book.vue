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
      <el-table-column prop="id" label="ID"   sortable/>
      <el-table-column prop="name" label="名称"  />
      <el-table-column prop="price" label="价格"  />
      <el-table-column prop="author" label="作者"  />
      <el-table-column prop="createTime" label="出版日期"  />
      <el-table-column  label="封面"  >
        <template #default="scope">
          <el-image
              style="width: 100px; height: 100px"
              :src="scope.row.cover"
              :preview-src-list="[scope.row.cover]"
              :initial-index="1"
          >
          </el-image>
        </template>
      </el-table-column>
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
          <el-form-item label="名称" label-width="120px">
            <el-input v-model="form.name" style="width: 50%"></el-input>
          </el-form-item>
          <el-form-item label="价格" label-width="120px">
            <el-input v-model="form.price" style="width: 50%"></el-input>
          </el-form-item>
          <el-form-item label="作者" label-width="120px">
            <el-input v-model="form.author" style="width: 50%"></el-input>
          </el-form-item>
          <el-form-item label="出版日期" label-width="120px">
            <el-date-picker  v-model="form.createTime" value-format="YYYY-MM-DD" type="date" style="width: 80%" clearable>
            </el-date-picker>
          </el-form-item>
          <el-form-item label="封面"  label-width="120px">
            <el-upload  ref="upload"  action="http://localhost:9090/files/upload"  :on-success="filesUploadSuccess">
              <el-button size="small" type="primary">Click to upload</el-button>
            </el-upload>
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
  name: 'Book',
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
    filesUploadSuccess(res){
      console.log(res)
      this.form.cover = res.data
    },
    load(){
      request.get("/book",{
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
      this.$refs['upload'].clearFiles()
    },
    save(){
      if(this.form.id){
        request.put("/book",this.form).then(res => {
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
        request.post("/book",this.form).then(res => {
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
      request.delete("/book/" + id).then(res => {
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
        this.$refs['upload'].clearFiles()
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
