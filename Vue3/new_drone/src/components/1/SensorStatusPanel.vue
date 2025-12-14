<!-- SensorStatusPanel.vue -->
<template>
  <el-card class="sensor-status">
    <el-row :gutter="20">
      <el-col v-for="sensor in sensors" :key="sensor.name" :span="6">
        <div class="sensor-item">
          <div class="sensor-icon">
            <i :class="getSensorIcon(sensor.type)"></i>
            <status-indicator :status="sensor.status"/>
          </div>
          <div class="sensor-info">
            <h4>{{ sensor.name }}</h4>
            <div class="sensor-detail">
              <template v-if="sensor.type === 'camera'">
                <span>{{ sensor.fps }} FPS</span>
              </template>
              <template v-else-if="sensor.type === 'lidar'">
                <span>{{ sensor.points.toLocaleString() }} points</span>
              </template>
              <template v-else-if="sensor.type === 'radar'">
                <span>{{ sensor.targets }} targets</span>
              </template>
              <template v-else>
                <span>{{ sensor.satellites }} satellites</span>
              </template>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
import StatusIndicator from './StatusIndicator'

export default {
  components: { StatusIndicator },

  props: {
    sensors: Array
  },

  methods: {
    getSensorIcon(type) {
      const icons = {
        camera: 'el-icon-video-camera',
        lidar: 'el-icon-map-location',
        radar: 'el-icon-data-line',
        gps: 'el-icon-place'
      }
      return icons[type]
    }
  }
}
</script>

<style scoped>
.sensor-item {
  display: flex;
  align-items: center;
  padding: 12px;
  background: #fafafa;
  border-radius: 4px;

  .sensor-icon {
    position: relative;
    font-size: 24px;
    margin-right: 12px;

    .status-indicator {
      position: absolute;
      bottom: -4px;
      right: -4px;
    }
  }

  .sensor-info {
    h4 {
      margin: 0;
      font-size: 14px;
      color: #303133;
    }

    .sensor-detail {
      font-size: 12px;
      color: #909399;
    }
  }
}
</style>