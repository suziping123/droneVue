<template>
  <div class="uav-detail">
    <el-descriptions
        title="飞行器详细信息"
        :column="1"
        border
    >
      <el-descriptions-item label="编号">
        {{ uav.id }}
      </el-descriptions-item>

      <el-descriptions-item label="类型">
        <el-tag>{{ uav.type }}</el-tag>
      </el-descriptions-item>

      <el-descriptions-item label="当前位置">
        ({{ uav.position.x }}, {{ uav.position.y }})
      </el-descriptions-item>

      <el-descriptions-item label="飞行速度">
        {{ uav.speed }} m/s
      </el-descriptions-item>

      <el-descriptions-item label="飞行高度">
        {{ uav.altitude }} m
      </el-descriptions-item>

      <el-descriptions-item label="当前状态">
        <el-tag :type="statusType">
          {{ uav.status }}
        </el-tag>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script>
export default {
  props: {
    uav: {
      type: Object,
      default: () => ({})
    }
  },
  computed: {
    statusType() {
      const typeMap = {
        '飞行中': 'success',
        '警告': 'warning',
        '危险': 'danger',
        '离线': 'info'
      }
      return typeMap[this.uav.status] || ''
    }
  }
}
</script>

<style scoped>
.uav-detail {
  padding: 10px;
}
</style>