<!-- StatusIndicator.vue -->
<template>
  <div class="status-indicator">
    <div class="led-container">
      <div
          class="led"
          :class="statusClass"
          :style="ledStyle"
      />
    </div>
    <div class="status-text">
      {{ statusText }}
    </div>
  </div>
</template>

<script>
export default {
  props: {
    status: Object
  },

  computed: {
    statusClass() {
      if (this.status.cpu > 90) return 'danger'
      if (this.status.mem > 4096) return 'warning'
      return 'normal'
    },

    statusText() {
      return {
        normal: '正常运行',
        warning: '资源警告',
        danger: '系统过载'
      }[this.status.status]
    },

    ledStyle() {
      const colors = {
        normal: '#52c41a',
        warning: '#faad14',
        danger: '#ff4d4f'
      }
      return {
        backgroundColor: colors[this.statusClass],
        boxShadow: `0 0 10px ${colors[this.statusClass]}`
      }
    }
  }
}
</script>

<style scoped>
.status-indicator {
  display: flex;
  align-items: center;
  gap: 12px;
}

.led-container {
  position: relative;
  width: 24px;
  height: 24px;
}

.led {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% { opacity: 0.8; }
  50% { opacity: 0.3; }
  100% { opacity: 0.8; }
}

.status-text {
  font-weight: 500;
  color: #606266;
}
</style>