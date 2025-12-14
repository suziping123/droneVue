<!-- ResourceMonitor.vue -->
<template>
  <el-card class="resource-monitor">
    <div class="monitor-header">
      <h4>系统资源监控</h4>
      <el-tag :type="statusType">{{ statusText }}</el-tag>
    </div>

    <div class="metric-grid">
      <div class="metric-item" v-for="(item, key) in metrics" :key="key">
        <div class="metric-info">
          <div class="label">{{ item.label }}</div>
          <div class="value">{{ item.value }}%</div>
        </div>
        <el-progress
            :percentage="item.value"
            :color="item.color"
            :stroke-width="12"
            :show-text="false"
        />
      </div>
    </div>
  </el-card>
</template>

<script>
export default {
  props: {
    stats: Object
  },

  data() {
    return {
      localStats: {
        cpu: 0,
        memory: 0,
        network: 0
      }
    }
  },

  watch: {
    stats: {
      handler(newVal) {
        this.localStats = {...newVal}
      },
      deep: true,
      immediate: true
    }
  },

  computed: {
    metrics() {
      return {
        cpu: {
          label: 'CPU使用率',
          value: this.localStats.cpu,
          color: this.getColor(this.localStats.cpu)
        },
        memory: {
          label: '内存占用',
          value: this.localStats.memory,
          color: this.getColor(this.localStats.memory)
        },
        network: {
          label: '网络负载',
          value: this.localStats.network,
          color: this.getColor(this.localStats.network)
        }
      }
    },

    statusType() {
      const max = Math.max(...Object.values(this.localStats))
      return max > 90 ? 'danger' : max > 70 ? 'warning' : 'success'
    },

    statusText() {
      return {
        danger: '资源紧张',
        warning: '正常负载',
        success: '运行良好'
      }[this.statusType]
    }
  },

  methods: {
    getColor(value) {
      const hue = 120 - (value * 1.2)
      return `hsl(${hue}, 70%, 50%)`
    }
  }
}
</script>

<style lang="scss" scoped>
.resource-monitor {
  .monitor-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }

  .metric-grid {
    display: grid;
    gap: 15px;

    .metric-item {
      .metric-info {
        display: flex;
        justify-content: space-between;
        margin-bottom: 8px;

        .label {
          color: #606266;
        }

        .value {
          font-weight: bold;
          color: #303133;
        }
      }
    }
  }

  ::v-deep .el-progress-bar__outer {
    border-radius: 6px;
  }
}
</style>