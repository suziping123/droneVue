<template>
  <el-container>
    <!-- 侧边栏 -->
    <el-aside width="200px" style="background-color: #001529; min-height: 100vh;">
      <el-menu
          router
          background-color="#001529"
          text-color="rgba(255,255,255,0.65)"
          active-text-color="#fff"
          style="border: none"
          :default-active="$route.path"
      >
        <!-- 系统管理模块 -->
        <el-submenu index="system-management">
          <template slot="title">
            <i class="el-icon-setting"></i>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/management/user">用户管理</el-menu-item>
          <el-menu-item index="/management/device">设备管理</el-menu-item>
          <el-menu-item index="/management/log">日志管理</el-menu-item>
        </el-submenu>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-main>
      <router-view></router-view>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: 'Management'
};
</script>

<style scoped>
.el-aside {
  transition: width 0.3s;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
}

.el-main {
  background-color: #f5f7fa;
  padding: 20px;
}
</style>