<!-- CommandDetail.vue -->
<template>
  <div class="command-detail">
    <el-descriptions :column="2" border>
      <el-descriptions-item label="指令ID">{{ data.id }}</el-descriptions-item>
      <el-descriptions-item label="指令类型">
        <el-tag :type="typeColor">{{ data.type }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="发送时间">{{ data.timestamp }}</el-descriptions-item>
      <el-descriptions-item label="执行状态">
        <el-tag :type="statusColor">{{ data.status }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="控制参数" :span="2">
        <pre>{{ JSON.stringify(data.parameters, null, 2) }}</pre>
      </el-descriptions-item>
      <el-descriptions-item label="执行结果" :span="2">
        <el-steps :active="executionStep" align-center>
          <el-step title="指令接收" description="系统已接收指令"></el-step>
          <el-step title="参数校验" description="校验控制参数合法性"></el-step>
          <el-step title="指令执行" description="发送至执行机构"></el-step>
          <el-step title="执行确认" description="收到执行反馈"></el-step>
        </el-steps>
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script>
export default {
  props: {
    data: {
      type: Object,
      default: () => ({})
    }
  },

  computed: {
    typeColor() {
      return {
        '航向调整': 'success',
        '速度控制': 'warning',
        '紧急规避': 'danger'
      }[this.data.type]
    },
    statusColor() {
      return {
        '待执行': 'info',
        '执行中': 'warning',
        '已完成': 'success',
        '已取消': 'danger'
      }[this.data.status]
    },
    executionStep() {
      return {
        '待执行': 1,
        '执行中': 2,
        '已完成': 4,
        '已取消': 0
      }[this.data.status]
    }
  }
}
</script>

<style scoped>
pre {
  background: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
}
</style>