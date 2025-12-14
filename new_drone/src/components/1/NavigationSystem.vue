<!-- NavigationSystem.vue -->
<template>
  <el-card class="navigation-system">
    <el-row :gutter="20">
      <el-col :span="12">
        <div class="map-container" ref="map"></div>
      </el-col>

      <el-col :span="12">
        <div class="attitude-indicator">
          <div class="artificial-horizon" :style="horizonStyle">
            <div class="pitch-lines">
              <div
                  v-for="deg in [-30, -20, -10, 0, 10, 20, 30]"
                  :key="deg"
                  class="pitch-line"
                  :style="pitchLineStyle(deg)"
              >
                {{ Math.abs(deg) }}
              </div>
            </div>
          </div>
          <div class="yaw-indicator">{{ currentYaw.toFixed(1) }}°</div>
        </div>

        <el-descriptions title="导航参数" :column="2" border>
          <el-descriptions-item label="纬度">{{ position.lat }}</el-descriptions-item>
          <el-descriptions-item label="经度">{{ position.lng }}</el-descriptions-item>
          <el-descriptions-item label="海拔">{{ position.alt.toFixed(1) }}m</el-descriptions-item>
          <el-descriptions-item label="地速">{{ speed.ground.toFixed(1) }}m/s</el-descriptions-item>
          <el-descriptions-item label="空速">{{ speed.air.toFixed(1) }}m/s</el-descriptions-item>
          <el-descriptions-item label="航向">{{ currentYaw.toFixed(1) }}°</el-descriptions-item>
        </el-descriptions>
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
export default {
  data() {
    return {
      currentPitch: 0,
      currentRoll: 0,
      currentYaw: 0,
      position: {
        lat: 31.2304,
        lng: 121.4737,
        alt: 150
      },
      speed: {
        ground: 25,
        air: 28
      }
    }
  },

  computed: {
    horizonStyle() {
      return {
        transform: `rotate(${this.currentRoll}deg) translateY(${this.currentPitch * 2}px)`
      }
    }
  },

  methods: {
    pitchLineStyle(deg) {
      return {
        transform: `translateY(${deg * 3}px)`
      }
    },

    startSimulation() {
      setInterval(() => {
        this.currentPitch = Math.sin(Date.now()/3000) * 10
        this.currentRoll = Math.cos(Date.now()/5000) * 15
        this.currentYaw = (this.currentYaw + Math.random() * 2 - 1) % 360

        this.position.lat += Math.random() * 0.0001 - 0.00005
        this.position.lng += Math.random() * 0.0001 - 0.00005
        this.position.alt += Math.random() * 0.5 - 0.25

        this.speed.ground += Math.random() * 0.5 - 0.25
        this.speed.air += Math.random() * 0.5 - 0.25
      }, 100)
    }
  },

  mounted() {
    this.startSimulation()
  }
}
</script>

<style scoped>
.map-container {
  height: 500px;
  background: #f0f2f5;
  border-radius: 4px;
}

.attitude-indicator {
  position: relative;
  height: 300px;
  background: #2c3e50;
  border-radius: 8px;
  overflow: hidden;

  .artificial-horizon {
    position: absolute;
    width: 200%;
    height: 200%;
    background: linear-gradient(
        to bottom,
        #87CEEB 0%,
        #87CEEB 50%,
        #556B2F 50%,
        #556B2F 100%
    );
    transition: transform 0.3s ease-out;
  }

  .pitch-lines {
    position: absolute;
    width: 100%;
    height: 100%;

    .pitch-line {
      position: absolute;
      left: 50%;
      width: 100px;
      margin-left: -50px;
      border-top: 2px solid #fff;
      color: white;
      text-align: center;
      font-size: 12px;
    }
  }

  .yaw-indicator {
    position: absolute;
    bottom: 20px;
    left: 50%;
    transform: translateX(-50%);
    color: white;
    font-size: 24px;
    text-shadow: 0 0 3px rgba(0,0,0,0.5);
  }
}
</style>