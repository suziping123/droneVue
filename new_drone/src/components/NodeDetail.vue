<!-- NodeDetail.vue -->
<template>
  <el-descriptions
      title="对象属性"
      :column="2"
      border
      class="node-detail"
  >
    <el-descriptions-item label="ID">{{ data.id }}</el-descriptions-item>
    <el-descriptions-item label="类型">{{ data.category }}</el-descriptions-item>
    <el-descriptions-item label="置信度">
      <el-progress
          :percentage="data.confidence * 100"
          :color="getConfidenceColor(data.confidence)"
          :show-text="false"
      />
    </el-descriptions-item>
    <el-descriptions-item label="位置">
      X: {{ data.x.toFixed(2) }}<br/>
      Y: {{ data.y.toFixed(2) }}
    </el-descriptions-item>
    <el-descriptions-item label="速度">
      <el-tag :type="data.speed > 5 ? 'danger' : 'info'">
        {{ data.speed.toFixed(1) }} m/s
      </el-tag>
    </el-descriptions-item>
    <el-descriptions-item label="加速度">
      {{ data.acceleration.toFixed(2) }} m/s²
    </el-descriptions-item>
    <el-descriptions-item label="历史轨迹" :span="2">
      <mini-trail-chart :points="data.trail"/>
    </el-descriptions-item>
    <el-descriptions-item label="关联对象" :span="2">
      <div class="relations">
        <el-tag
            v-for="rel in data.relations"
            :key="rel.id"
            :type="getRelationType(rel.type)"
            class="relation-tag"
        >
          {{ rel.type }}: {{ rel.target }}
        </el-tag>
      </div>
    </el-descriptions-item>
  </el-descriptions>
</template>

<script>
import MiniTrailChart from './MiniTrailChart'

export default {
  components: { MiniTrailChart },

  props: {
    data: Object
  },

  methods: {
    getConfidenceColor(confidence) {
      const hue = Math.round(confidence * 120)
      return `hsl(${hue}, 100%, 50%)`
    },

    getRelationType(type) {
      const types = {
        'distance': 'info',
        'position': 'success',
        'danger': 'danger'
      }
      return types[type] || 'warning'
    }
  }
}
</script>

<style scoped>
.node-detail {
  padding: 20px;

  ::v-deep .el-descriptions__body {
    background: #fafafa;
  }
}

.relations {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.relation-tag {
  cursor: pointer;
  transition: transform 0.2s;

  &:hover {
    transform: translateY(-2px);
  }
}
</style>