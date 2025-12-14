<!-- StatusPanel.vue -->
<template>
  <el-card class="status-panel">
    <el-row :gutter="20">
      <el-col :span="8" v-for="(item, key) in statusItems" :key="key">
        <div class="status-item">
          <div class="label">{{ item.label }}</div>
          <div class="value" :style="{color: item.color}">
            {{ formatValue(item.value, item.unit) }}
          </div>
        </div>
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
export default {
  props: {
    status: Object
  },

  computed: {
    statusItems() {
      return {
        altitude: {
          label: '高度',
          value: this.status.altitude,
          unit: 'm',
          color: '#1890ff'
        },
        speed: {
          label: '速度',
          value: this.status.speed,
          unit: 'm/s',
          color: '#52c41a'
        },
        battery: {
          label: '电量',
          value: this.status.battery,
          unit: '%',
          color: '#ff4d4f'
        }
      }
    }
  },

  methods: {
    formatValue(value, unit) {
      switch(unit) {
        case 'm':
          return `${value.toFixed(1)}m`
        case 'm/s':
          return `${value.toFixed(1)}m/s`
        case '%':
          return `${value.toFixed(0)}%`
        default:
          return value
      }
    }
  }
}
</script>

<style scoped>
.status-item {
  text-align: center;
  padding: 15px;
  background: #fafafa;
  border-radius: 4px;

  .label {
    color: #909399;
    font-size: 12px;
    margin-bottom: 8px;
  }

  .value {
    font-size: 24px;
    font-weight: bold;
  }
}
</style>