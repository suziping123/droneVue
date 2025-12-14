<!-- VideoCanvas.vue -->
<template>
  <div class="video-canvas">
    <canvas ref="canvas"></canvas>
    <video ref="video" class="video-source" :src="streamUrl" @play="onPlay"></video>
  </div>
</template>

<script>
export default {
  props: {
    streamUrl: {
      type: String,
      required: true
    },
    detections: {
      type: Array,
      default: () => []
    }
  },

  data() {
    return {
      ctx: null,
      animationFrameId: null,
      lastDraw: 0,
      frameInterval: 1000 / 30 // 30fps
    }
  },

  watch: {
    streamUrl(newVal) {
      this.initVideo(newVal)
    },
    detections: {
      handler() {
        this.drawDetections()
      },
      deep: true
    }
  },

  mounted() {
    this.initCanvas()
    this.initVideo(this.streamUrl)
  },

  beforeDestroy() {
    this.cleanup()
  },

  methods: {
    initCanvas() {
      const canvas = this.$refs.canvas
      canvas.width = 1280
      canvas.height = 720
      this.ctx = canvas.getContext('2d')
    },

    initVideo(url) {
      const video = this.$refs.video
      if (!video) return

      // 重置视频源
      video.pause()
      video.src = url
      video.muted = true // 静音避免自动播放限制
      video.playsInline = true

      video.load()
      video.play().catch(error => {
        console.error('视频播放失败:', error)
        this.$emit('error', error)
      })
    },

    onPlay() {
      this.startDrawing()
    },

    startDrawing() {
      const drawFrame = (timestamp) => {
        if (timestamp - this.lastDraw >= this.frameInterval) {
          this.drawVideoFrame()
          this.lastDraw = timestamp
        }
        this.animationFrameId = requestAnimationFrame(drawFrame)
      }
      this.animationFrameId = requestAnimationFrame(drawFrame)
    },

    drawVideoFrame() {
      const video = this.$refs.video
      if (video.readyState < 2) return // 数据未准备好时跳过

      this.ctx.drawImage(
          video,
          0, 0,
          this.ctx.canvas.width,
          this.ctx.canvas.height
      )

      // 绘制检测框（在视频帧之上）
      this.drawDetections()
    },

    drawDetections() {
      if (!this.detections?.length) return

      this.detections.forEach(detection => {
        const [x, y, width, height] = detection.bbox
        const label = `${detection.class} (${Math.round(detection.confidence * 100)}%)`

        // 绘制边界框
        this.ctx.beginPath()
        this.ctx.lineWidth = 2
        this.ctx.strokeStyle = detection.color || '#FF0000'
        this.ctx.rect(x, y, width, height)
        this.ctx.stroke()

        // 绘制标签背景
        this.ctx.fillStyle = detection.color || '#FF0000'
        const textWidth = this.ctx.measureText(label).width
        this.ctx.fillRect(
            x - 1,
            y - 20,
            textWidth + 10,
            20
        )

        // 绘制标签文字
        this.ctx.fillStyle = '#FFFFFF'
        this.ctx.font = '14px Arial'
        this.ctx.fillText(label, x + 5, y - 5)
      })
    },

    cleanup() {
      // 停止绘制循环
      if (this.animationFrameId) {
        cancelAnimationFrame(this.animationFrameId)
      }

      // 释放视频资源
      const video = this.$refs.video
      if (video) {
        video.pause()
        video.src = ''
        video.load()
      }
    }
  }
}
</script>

<style scoped>
.video-canvas {
  position: relative;
  width: 100%;
  height: 100%;
  background: #000;
}

.video-source {
  display: none; /* 隐藏原生视频控件 */
}
</style>