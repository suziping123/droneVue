<!-- StatusIndicator.vue -->
<template>
  <div class="status-indicator">
    <div class="led-container">
      <div
          class="led"
          :class="statusClass"
          :style="ledStyle"
      ></div>
    </div>
    <div class="status-info">
      <div class="status-label">{{ statusLabel }}</div>
      <div class="status-detail" v-if="showDetail">{{ statusDetail }}</div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    status: {
      type: String,
      required: true,
      validator: value => ['normal', 'warning', 'danger'].includes(value)
    },
    showDetail: {
      type: Boolean,
      default: false
    }
  },

  computed: {
    statusClass() {
      return `status-${this.status}`
    },

    statusLabel() {
      const labels = {
        normal: '系统正常',
        warning: '部分告警',
        danger: '严重故障'
      }
      return labels[this.status]
    },

    statusDetail() {
      const details = {
        normal: '所有系统运行正常',
        warning: '检测到次要系统异常',
        danger: '关键系统发生故障'
      }
      return details[this.status]
    },

    ledStyle() {
      const colors = {
        normal: '#67C23A',
        warning: '#E6A23C',
        danger: '#F56C6C'
      }
      return {
        backgroundColor: colors[this.status],
        boxShadow: `0 0 15px ${colors[this.status]}80`
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.status-indicator {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px;
  background: #fafafa;
  border-radius: 6px;

  .led-container {
    position: relative;
    width: 24px;
    height: 24px;

    .led {
      width: 100%;
      height: 100%;
      border-radius: 50%;
      animation: pulse 1.5s infinite;

      &.status-warning {
        animation-duration: 1s;
      }

      &.status-danger {
        animation-duration: 0.5s;
      }
    }
  }

  .status-info {
    line-height: 1.4;

    .status-label {
      font-weight: 500;
      color: #303133;
    }

    .status-detail {
      font-size: 12px;
      color: #909399;
    }
  }
}

@keyframes pulse {
  0% {
    opacity: 0.8;
    transform: scale(0.95);
  }
  50% {
    opacity: 0.3;
    transform: scale(1.05);
  }
  100% {
    opacity: 0.8;
    transform: scale(0.95);
  }
}
</style>