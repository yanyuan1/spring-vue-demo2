<template>
  <div>

        <el-menu
            style="width: 200px;min-height: calc(100vh - 50px)"
            :default-active="path"
            class="el-menu-vertical-demo"
            router
            >
            <el-sub-menu >
              <template #title>系统管理</template>
              <el-menu-item index="/user">用户管理</el-menu-item>
            </el-sub-menu>
            <el-menu-item index="/book" >  书籍管理</el-menu-item>
<!--          <el-menu-item index="3" disabled>
            <el-icon><document /></el-icon>
            <span>Navigator Three</span>
          </el-menu-item>                             -->
        </el-menu>

  </div>
</template>

<script>


import { defineComponent } from 'vue'
import {
  Location,
  Document,
  Menu as IconMenu,
  Setting,
} from '@element-plus/icons'

export default defineComponent({
  components: {
    Location,
    Document,
    Setting,
    IconMenu,
  },
  name: "Aside",
  data(){
    return {
      path:this.$route.path
    }
  }

})
</script>

