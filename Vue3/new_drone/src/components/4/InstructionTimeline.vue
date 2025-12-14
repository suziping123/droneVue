<!-- InstructionTimeline.vue -->
<template>
  <el-timeline>
    <el-timeline-item
        v-for="(item, index) in commands"
        :key="index"
        :timestamp="item.time"
        placement="top"
    >
      <el-card>
        <h4>{{ item.command }}</h4>
        <p>
          <el-tag :type="item.icon === 'success' ? 'success' : 'warning'">
            {{ item.status }}
          </el-tag>
        </p>
      </el-card>
    </el-timeline-item>
  </el-timeline>
</template>

<script>
export default {
  props: {
    commands: Array,
    height: String
  }
}
</script>

<style scoped>
.el-timeline {
  padding: 20px;
  height: v-bind(height);
  overflow-y: auto;
}

.el-timeline-item__timestamp {
  color: #909399;
  font-size: 12px;
}
</style>