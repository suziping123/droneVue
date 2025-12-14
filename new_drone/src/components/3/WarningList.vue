<template>
  <div class="warning-list">
    <el-alert
        v-for="warning in warnings"
        :key="warning.id"
        :title="warning.content"
        :type="alertType(warning.level)"
        :closable="false"
        show-icon
        class="warning-item"
    >
      <template #default>
        <div class="warning-meta">
          <span>{{ warning.timestamp }}</span>
          <el-tag
              size="mini"
              :type="tagType(warning.type)"
          >
            {{ warning.type }}
          </el-tag>
        </div>
      </template>
    </el-alert>
  </div>
</template>

<script>
export default {
  props: {
    warnings: Array
  },
  methods: {
    alertType(level) {
      const typeMap = {
        high: 'error',
        medium: 'warning',
        low: 'info'
      }
      return typeMap[level] || 'info'
    },
    tagType(warningType) {
      const typeMap = {
        '碰撞预警': 'danger',
        '气象预警': 'warning',
        '设备故障': 'info'
      }
      return typeMap[warningType] || ''
    }
  }
}
</script>

<style scoped>
.warning-list {
  max-height: 400px;
  overflow-y: auto;
}
.warning-item {
  margin-bottom: 10px;
}
.warning-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 5px;
  font-size: 12px;
  color: #666;
}
</style>