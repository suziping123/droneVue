<template>
  <el-container>
    <el-main class="enhanced-main">
      <el-row :gutter="24">
        <!-- 图片区域 -->
        <el-col :span="16">
          <el-card class="image-container">
            <video
                class="enhanced-image"
                autoplay
                loop
                muted
                playsinline
                disablePictureInPicture
                controlslist="nodownload nofullscreen noremoteplayback"
            >
              <source :src="require('@/assets/videos/shujuganzhi.mp4')" type="video/mp4">
              <div class="image-overlay">
                <span class="overlay-title">实时图传画面</span>
                <el-tag type="success" effect="dark">传输正常</el-tag>
              </div>
            </video>
          </el-card>
        </el-col>

        <!-- 右侧数据区域 -->
        <el-col :span="8">
          <el-card class="control-panel">
            <div class="control-header">
              <el-select
                  v-model="selectedDrone"
                  placeholder="选择无人机型号"
                  class="drone-select"
                  size="large"
              >
                <el-option
                    v-for="item in droneModels"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                />
              </el-select>
              <div class="status-indicators">
                <div class="status-item">
                  <div class="led" :class="{ connected: isConnected }"></div>
                  <span>连接状态</span>
                </div>
                <div class="status-item">
                  <div class="led" :class="{ warning: batteryLow }"></div>
                  <span>电池状态</span>
                </div>
              </div>
            </div>

            <!-- 增强数据展示 -->
            <div class="data-container">
              <div class="sensor-grid">
                <div
                    v-for="(item, index) in displayData"
                    :key="index"
                    class="sensor-card"
                >
                  <div class="sensor-icon">
                    <i :class="item.icon" class="icon"></i>
                  </div>
                  <div class="sensor-info">
                    <div class="sensor-value">
                      {{ item.value }}
                      <span class="unit">{{ item.unit }}</span>
                    </div>
                    <div class="sensor-label">{{ item.label }}</div>
                  </div>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
export default {
  data() {
    return {
      selectedDrone: 'DJI-M300',
      isConnected: true,
      batteryLow: false,
      droneModels: [
        { value: 'DJI-M300', label: '大疆 Matrice 300' },
        { value: 'DJI-Mavic3', label: '大疆 Mavic 3' },
        { value: 'Autel-Evo2', label: '道通 EVO II' }
      ],
      flightData: {
        speed: 0,
        altitude: 0,
        heading: 0,
        windSpeed: 0,
        humidity: 0,
        temperature: 0,
        battery: 0,
        longitude: 0,
        latitude: 0
      }
    }
  },
  computed: {
    displayData() {
      return [
        {
          label: '飞行速度',
          value: this.flightData.speed,
          unit: 'm/s',
          icon: 'el-icon-aim'
        },
        {
          label: '飞行高度',
          value: this.flightData.altitude,
          unit: 'm',
          icon: 'el-icon-top'
        },
        {
          label: '航向角度',
          value: this.flightData.heading,
          unit: '°',
          icon: 'el-icon-compass'
        },
        {
          label: '当前风速',
          value: this.flightData.windSpeed,
          unit: 'm/s',
          icon: 'el-icon-wind-power'
        },
        {
          label: '环境湿度',
          value: this.flightData.humidity,
          unit: '%',
          icon: 'el-icon-heavy-rain'
        },
        {
          label: '环境温度',
          value: this.flightData.temperature,
          unit: '℃',
          icon: 'el-icon-sunny'
        },
        {
          label: '电池电量',
          value: this.flightData.battery,
          unit: '%',
          icon: 'el-icon-battery'
        },
        {
          label: '纬度坐标',
          value: this.flightData.latitude.toFixed(6),
          unit: '',
          icon: 'el-icon-map-location'
        },
        {
          label: '经度坐标',
          value: this.flightData.longitude.toFixed(6),
          unit: '',
          icon: 'el-icon-map-location'
        }
      ]
    }
  },
  methods: {
    updateData() {
      this.flightData = {
        speed: (Math.random() * 20).toFixed(1),
        altitude: (Math.random() * 500).toFixed(0),
        heading: (Math.random() * 360).toFixed(0),
        windSpeed: (Math.random() * 15).toFixed(1),
        humidity: (Math.random() * 100).toFixed(0),
        temperature: (Math.random() * 50 - 10).toFixed(1),
        battery: (Math.random() * 100).toFixed(0),
        longitude: 116.397428 + Math.random() * 0.01,
        latitude: 39.90923 + Math.random() * 0.01
      }

      // 电池状态警告
      this.batteryLow = this.flightData.battery < 20
    }
  },
  mounted() {
    setInterval(this.updateData, 2000)
    this.updateData()
  }
}
</script>

<style scoped>
.enhanced-main {
  padding: 24px;
  background: #f5f7fa;
}

.image-container {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.enhanced-image {
  height: 680px;
  transition: transform 0.3s;
}

.image-overlay {
  position: absolute;
  bottom: 20px;
  left: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
}

.overlay-title {
  color: #fff;
  font-size: 1.2rem;
  text-shadow: 1px 1px 3px rgba(0,0,0,0.5);
}

.control-panel {
  height: 680px;
  display: flex;
  flex-direction: column;
}

.control-header {
  margin-bottom: 20px;
}

.drone-select {
  width: 100%;
  margin-bottom: 15px;
}

.status-indicators {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}

.status-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.led {
  width: 16px;
  height: 16px;
  border-radius: 50%;
  margin-bottom: 5px;
  background: #ddd;
  transition: all 0.3s;
}

.led.connected {
  background: #67c23a;
  box-shadow: 0 0 8px rgba(103,194,58,0.5);
}

.led.warning {
  background: #e6a23c;
  animation: pulse 1.5s infinite;
}

.sensor-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

.sensor-card {
  background: linear-gradient(145deg, #ffffff, #f8f9fa);
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.05);
  transition: transform 0.3s;
}

.sensor-card:hover {
  transform: translateY(-3px);
}

.sensor-icon {
  background: #409EFF;
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10px;
}

.icon {
  color: white;
  font-size: 20px;
}

.sensor-value {
  font-size: 1.4rem;
  font-weight: 600;
  color: #303133;
  line-height: 1.2;
}

.unit {
  font-size: 0.8rem;
  color: #909399;
  margin-left: 3px;
}

.sensor-label {
  color: #606266;
  font-size: 0.9rem;
  margin-top: 5px;
}

@keyframes pulse {
  0% { opacity: 0.6; }
  50% { opacity: 1; }
  100% { opacity: 0.6; }
}
</style>