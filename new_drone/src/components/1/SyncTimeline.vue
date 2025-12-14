<!-- SyncTimeline.vue -->
<template>
  <el-timeline>
    <el-timeline-item
        v-for="(item, index) in timestamps"
        :key="index"
        :timestamp="formatTimestamp(item.timestamp)"
        placement="top"
    >
      <el-card>
        <h4>{{ item.sensor }} 数据</h4>
        <p>延迟: {{ Date.now() - item.timestamp }}ms</p>
        <p>状态:
          <el-tag :type="syncStatusType(item.timestamp)" size="small">
            {{ syncStatusText(item.timestamp) }}
          </el-tag>
        </p>
      </el-card>
    </el-timeline-item>
  </el-timeline>
</template>

<script>
export default {
  props: {
    timestamps: Array
  },

  methods: {
    formatTimestamp(ts) {
      return new Date(ts).toLocaleTimeString()
    },

    syncStatusType(ts) {
      const diff = Date.now() - ts
      if (diff > 100) return 'danger'
      if (diff > 50) return 'warning'
      return 'success'
    },

    syncStatusText(ts) {
      const diff = Date.now() - ts
      if (diff > 100) return '超时'
      if (diff > 50) return '延迟'
      return '正常'
    }
  }
}
</script>

<style scoped>
.el-timeline {
  padding-left: 20px;
}

::v-deep .el-timeline-item__timestamp {
  color: #666;
  font-size: 12px;
}
</style>