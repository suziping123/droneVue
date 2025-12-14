<template>
  <div class="log-list">
    <h4>操作日志</h4>
    <div class="log-container">
      <div
          v-for="log in logs"
          :key="log.timestamp"
          class="log-item"
      >
        <div class="log-time">{{ log.timestamp }}</div>
        <div class="log-content">
          <el-tag
              size="mini"
              :type="logType(log.type)"
          >
            {{ log.type }}
          </el-tag>
          {{ log.content }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    logs: Array
  },
  methods: {
    logType(type) {
      const typeMap = {
        '路径规划': 'success',
        '控制指令': 'primary',
        '系统操作': 'info',
        '错误': 'danger'
      }
      return typeMap[type] || ''
    }
  }
}
</script>

<style scoped>
.log-list {
  padding: 15px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
}
.log-container {
  max-height: 250px;
  overflow-y: auto;
}
.log-item {
  padding: 8px 0;
  border-bottom: 1px solid #ebeef5;
}
.log-time {
  font-size: 12px;
  color: #999;
}
.log-content {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 4px;
}
</style>