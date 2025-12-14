<!-- AlertDetail.vue -->
<template>
  <div class="alert-detail">
    <el-descriptions :column="2" border>
      <el-descriptions-item label="预警类型">{{ data.type }}</el-descriptions-item>
      <el-descriptions-item label="预警等级">
        <el-tag :type="levelType">{{ data.level }}级</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="发生时间">{{ data.time }}</el-descriptions-item>
      <el-descriptions-item label="处理状态">
        <el-tag :type="statusType">{{ data.status }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="详细描述" :span="2">
        {{ data.description }}
      </el-descriptions-item>
      <el-descriptions-item label="影响范围" :span="2">
        <el-tag
            v-for="(item, index) in data.affectedSystems"
            :key="index"
            type="info"
            class="affected-tag"
        >
          {{ item }}
        </el-tag>
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
    levelType() {
      return ['', 'success', 'warning', 'danger'][this.data.level]
    },
    statusType() {
      return {
        '未处理': 'danger',
        '处理中': 'warning',
        '已解决': 'success'
      }[this.data.status]
    }
  }
}
</script>

<style scoped>
.affected-tag {
  margin-right: 8px;
  margin-bottom: 8px;
}
</style>