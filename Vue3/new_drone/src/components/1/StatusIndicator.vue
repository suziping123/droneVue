<!-- StatusIndicator.vue -->
<template>
  <div class="status-indicator" >
    <div class="led" :class="status"></div>
    <span>{{ statusText }}</span>
  </div>
</template>

<script>
export default {
  props: {
    status: {
      type: String,
      default: 'normal'
    }
  },
  computed: {
    statusText() {
      const map = {
        normal: '正常',
        warning: '警告',
        error: '故障'
      }
      return map[this.status]
    }
  }
}
</script>

<style scoped>
.status-indicator {
  display: flex;
  align-items: center;
  gap: -18px;

  .led {
    width: 12px;
    height: 12px;
    border-radius: 50%;

    &.normal { background: #67C23A; }
    &.warning { background: #E6A23C; }
    &.error { background: #F56C6C; }
  }
}
</style>