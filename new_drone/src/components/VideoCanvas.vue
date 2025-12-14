<template>
  <div class="video-container">
    <canvas ref="canvas" class="video-canvas"/>
    <video
        ref="video"
        class="video-element"
        :src="streamUrl"
        @play="onVideoPlay"
        @timeupdate="onVideoUpdate"
    />
  </div>
</template>

<script>
export default {
  props: {
    streamUrl: String,
    detections: Array
  },

  data() {
    return {
      ctx: null,
      videoWidth: 1280,
      videoHeight: 720,
      animationFrame: null,
      lastDraw: 0
    }
  },

  mounted() {
    this.ctx = this.$refs.canvas.getContext('2d')
    this.initCanvas()
  },

  methods: {
    initCanvas() {
      this.$refs.canvas.width = this.videoWidth
      this.$refs.canvas.height = this.videoHeight
    },

    toggleStream(play) {
      if (play) {
        this.$refs.video.play()
        this.drawFrame()
      } else {
        this.$refs.video.pause()
        cancelAnimationFrame(this.animationFrame)
      }
    },

    drawFrame() {
      if (!this.$refs.video.paused) {
        this.ctx.drawImage(
            this.$refs.video,
            0, 0,
            this.videoWidth, this.videoHeight
        )

        this.drawDetections()
        this.animationFrame = requestAnimationFrame(this.drawFrame)
      }
    },

    drawDetections() {
      this.detections.forEach(det => {
        const [x1, y1, x2, y2] = det.bbox
        this.ctx.strokeStyle = det.color
        this.ctx.lineWidth = 2
        this.ctx.strokeRect(x1, y1, x2 - x1, y2 - y1)

        // 绘制标签
        this.ctx.fillStyle = det.color
        this.ctx.fillRect(x1, y1 - 20, 80, 20)
        this.ctx.fillStyle = 'white'
        this.ctx.font = '14px Arial'
        this.ctx.fillText(`${det.class} ${det.confidence.toFixed(2)}`, x1 + 5, y1 - 5)
      })
    },

    captureFrame() {
      const dataUrl = this.$refs.canvas.toDataURL('image/jpeg')
      const link = document.createElement('a')
      link.download = `frame_${Date.now()}.jpg`
      link.href = dataUrl
      link.click()
    },

    onVideoPlay() {
      this.drawFrame()
    },

    onVideoUpdate() {
      this.$emit('frame-processed', {
        timestamp: Date.now(),
        detections: this.detections
      })
    }
  }
}
</script>

<style scoped>
.video-container {
  position: relative;
  width: 100%;
  height: 100%;
}

.video-canvas {
  position: absolute;
  top: 0;
  left: 0;
}

.video-element {
  display: none;
}
</style>