<!-- CameraSystem.vue -->
<template>
  <el-card class="camera-system">
    <div class="camera-wrapper">
      <video-canvas :streamUrl="streamUrl" :detections="detections"/>
      <div class="camera-overlay">
        <el-tag type="danger">RGB流</el-tag>
        <el-tag type="info">深度图</el-tag>
      </div>
    </div>

    <el-row :gutter="20" class="camera-stats">
      <el-col :span="8">
        <div class="stat-item">
          <label>目标数量</label>
          <div class="stat-value">{{ detections.length }}</div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-item">
          <label>检测延迟</label>
          <div class="stat-value">{{ latency }}ms</div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-item">
          <label>处理帧率</label>
          <div class="stat-value">{{ fps }} FPS</div>
        </div>
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
import VideoCanvas from './VideoCanvas'

export default {
  components: { VideoCanvas },

  data() {
    return {
      streamUrl: require('@/assets/videos/drone_video.mp4'),
      // streamUrl: 'https://www.bilibili.com/video/BV1azx5zVENj?t=1.0',
      detections: [],
      latency: 0,
      fps: 0
    }
  },

  mounted() {
    setInterval(() => {
      this.detections = this.generateDetections()
      this.latency = Math.floor(Math.random() * 50)
      this.fps = 28 + Math.random() * 4
    }, 1000)
  },

  methods: {
    generateDetections() {
      return Array.from({length: Math.floor(Math.random()*6)}, (_,i) => ({
        id: `obj_${Date.now()}_${i}`,
        class: ['car', 'person', 'drone'][i%3],
        confidence: Math.random().toFixed(2),
        bbox: [
          Math.random()*1280,
          Math.random()*720,
          Math.random()*1280,
          Math.random()*720
        ]
      }))
    }
  }
}
</script>

<style scoped>
.camera-wrapper {
  position: relative;
  height: 400px;
  background: #000;

  .camera-overlay {
    position: absolute;
    top: 10px;
    left: 10px;
  }
}

.camera-stats {
  margin-top: 20px;

  .stat-item {
    text-align: center;
    padding: 10px;
    background: #fafafa;
    border-radius: 4px;

    label {
      display: block;
      color: #909399;
      font-size: 12px;
    }

    .stat-value {
      font-size: 24px;
      font-weight: bold;
      color: #303133;
    }
  }
}
</style>