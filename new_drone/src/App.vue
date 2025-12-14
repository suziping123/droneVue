<template>
  <el-container>
    <el-aside :width="asideWidth" style="min-height: 100vh; background-color: #001529">
      <div style="height: 60px; color: white; display: flex; align-items: center; justify-content: center;">
        <img src="@/assets/logo.png" style="width: 40px; height: 40px">
        <span class="logo-title" v-show="!isCollapse">首页</span>
      </div>
      <el-menu
          :collapse="isCollapse"
          router
          background-color="#001529"
          text-color="rgba(255,255,255,0.65)"
          active-text-color="#fff"
          style="border: none"
          :default-active="$route.path"
      >
        <el-menu-item index="/home">
          <el-icon><HomeFilled /></el-icon>
          <template #title>
            <span>首页</span>
          </template>
        </el-menu-item>

        <el-menu-item index="/pathplanning">
          <el-icon><Location /></el-icon>
          <template #title>
            <span>路径规划</span>
          </template>
        </el-menu-item>
        <el-menu-item index="/monitor">
          <el-icon><VideoCamera /></el-icon>
          <template #title>
            <span>实时监控</span>
          </template>
        </el-menu-item>
        <el-menu-item index="/prediction">
          <el-icon><Warning /></el-icon>
          <template #title>
            <span>危险预测</span>
          </template>
        </el-menu-item>
        <el-menu-item index="/analysis">
          <el-icon><DataLine /></el-icon>
          <template #title>
            <span>数据处理</span>
          </template>
        </el-menu-item>
        <el-menu-item index="/shujuganzhi">
          <el-icon><Odometer /></el-icon>
          <template #title>
            <span>数据感知</span>
          </template>
        </el-menu-item>

        <el-sub-menu index="6">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/management/user">
            <el-icon><User /></el-icon>
            <template #title>
              <span>用户管理</span>
            </template>
          </el-menu-item>
          <el-menu-item index="/management/device">
            <el-icon><Cpu /></el-icon>
            <template #title>
              <span>设备管理</span>
            </template>
          </el-menu-item>
          <el-menu-item index="/management/log">
            <el-icon><Notebook /></el-icon>
            <template #title>
              <span>日志管理</span>
            </template>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header>
        <el-icon :size="26" style="cursor: pointer;" @click="handleCollapse">
          <component :is="collapseIcon" />
        </el-icon>
        <el-breadcrumb separator-class="el-icon-arrow-right" style="margin-left: 20px">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item :to="{ path: $route.path }">{{ currentPageName }}</el-breadcrumb-item>
        </el-breadcrumb>
        <span style="font-size: 26px; margin-left: 600px;">危险预测系统</span>
        <div style="flex: 1; width: 0; display: flex; align-items: center; justify-content: flex-end">
          <el-dropdown placement="bottom">
            <div style="display: flex; align-items: center; cursor: default; margin-right: 5px">
              <img src="@/assets/people.jpg" style="width: 40px; height: 40px;">
              <span><strong>管理员</strong></span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="">个人信息</el-dropdown-item>
                <el-dropdown-item @click="">修改密码</el-dropdown-item>
                <el-dropdown-item @click="">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed, watch, markRaw } from 'vue';
import { useRoute } from 'vue-router';
// 导入 Element Plus Icons 组件
import {
  Fold,
  Expand,
  HomeFilled,
  Location,
  VideoCamera,
  Warning,
  DataLine,
  Odometer,
  Setting,
  User,
  Cpu,
  Notebook
} from '@element-plus/icons-vue';


// 使用 Composition API 获取路由实例
const route = useRoute();

// ------------------- Data / State -------------------

// 使用 ref 声明响应式数据，并添加类型
const isCollapse = ref<boolean>(false);
const asideWidth = ref<string>('200px');
const currentPageName = ref<string>('首页');

// ------------------- Computed Properties -------------------

// collapseIcon 改为计算属性，返回 Element Plus Icons 组件
const collapseIcon = computed(() => {
  return isCollapse.value ? markRaw(Expand) : markRaw(Fold);
});

// ------------------- Methods -------------------

// 切换侧边栏折叠状态
const handleCollapse = () => {
  isCollapse.value = !isCollapse.value;
  asideWidth.value = isCollapse.value ? '64px' : '200px';
  // collapseIcon 的值由计算属性自动更新，不再需要手动设置
};

// ------------------- Watchers -------------------

// 监听路由变化，更新面包屑导航 (使用 Vue 3 的 watch API)
watch(
  () => route.path,
  (newPath) => {
    switch (newPath) {
      case '/monitor':
        currentPageName.value = '实时监控';
        break;
      case '/prediction':
        currentPageName.value = '危险预测';
        break;
      case '/analysis':
        currentPageName.value = '数据分析';
        break;
      case '/management/user':
        currentPageName.value = '用户管理';
        break;
      case '/management/device':
        currentPageName.value = '设备管理';
        break;
      case '/management/log':
        currentPageName.value = '日志管理';
        break;
      case '/home/dashboard':
        currentPageName.value = '仪表盘';
        break;
      case '/home/statistics':
        currentPageName.value = '统计';
        break;
      case '/pathplanning':
        currentPageName.value = '路径规划';
        break;
      default:
        currentPageName.value = '首页';
    }
  },
  { immediate: true } // 立即执行一次以初始化 currentPageName
);
</script>

<style>
.el-menu--inline .el-menu-item {
  background-color: #000c17 !important;
  padding-left: 49px !important;
}

.el-menu-item:hover, .el-submenu__title:hover {
  color: #fff !important;
}

.el-submenu__title:hover i {
  color: #fff !important;
}

.el-menu-item.is-active {
  background-color: #1890ff !important;
  border-radius: 5px !important;
  margin-left: 4px;
  width: calc(100% - 8px);
}

.el-menu-item.is-active i, .el-menu-item .el-tooltip {
  margin-left: -4px;
}

.el-menu-item {
  height: 40px !important;
  line-height: 40px !important;
  margin: 4px;
}

.el-submenu__title {
  height: 40px !important;
  line-height: 40px !important;
}

.el-menu--inline {
  background-color: #000c17 !important;
}

.le-submenu .el-menu-item {
  min-width: 0 !important;
}

.el-menu--inline .el-menu-item.is-active {
  padding-left: 45px !important;
}

.el-aside {
  transition: width 0.3s;
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
}

.logo-title {
  margin-left: 5px;
  font-size: 20px;
  transition: all 0.3s;
}

.el-header {
  box-shadow: 2px 0 6px rgba(0, 21, 41, 0.35);
  display: flex;
  align-items: center;
  background-color: #409EFF;
  color: #fff;
  line-height: 60px;
}

.el-main {
  background-color: #f5f7fa;
  padding: 10px !important;
}
</style>
