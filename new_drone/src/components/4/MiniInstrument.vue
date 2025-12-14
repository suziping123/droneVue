<!-- MiniInstrument.vue -->
<template>
  <div class="mini-instrument" :style="{width: size, height: size}">
    <svg class="progress-circle" :viewBox="`0 0 ${sizeNumber} ${sizeNumber}`">
      <circle
          class="progress-background"
          :cx="center"
          :cy="center"
          :r="radius"
      />
      <circle
          class="progress-foreground"
          :cx="center"
          :cy="center"
          :r="radius"
          :stroke-dasharray="dashArray"
          :stroke-dashoffset="dashOffset"
      />
    </svg>
    <div class="value-display">
      <div class="value" :style="{color: color}">{{ formattedValue }}</div>
      <div class="unit">{{ unit }}</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MiniInstrument',
  props: {
    value: Number,
    max: {
      type: Number,
      default: 100
    },
    unit: String,
    color: {
      type: String,
      default: '#409EFF'
    },
    size: {
      type: String,
      default: '120px'
    }
  },
  computed: {
    sizeNumber() {
      return parseInt(this.size)
    },
    center() {
      return this.sizeNumber / 2
    },
    radius() {
      return this.sizeNumber / 2 - 8
    },
    circumference() {
      return 2 * Math.PI * this.radius
    },
    dashArray() {
      return this.circumference
    },
    dashOffset() {
      return this.circumference * (1 - this.value / this.max)
    },
    formattedValue() {
      return Math.round(this.value)
    }
  }
}
</script>

<style scoped>
.mini-instrument {
  position: relative;
  margin: 0 auto;
}

.progress-circle {
  transform: rotate(-90deg);
}

.progress-background {
  fill: none;
  stroke: #ebeef5;
  stroke-width: 8;
}

.progress-foreground {
  fill: none;
  stroke: v-bind(color);
  stroke-width: 8;
  stroke-linecap: round;
  transition: stroke-dasharray 0.5s ease;
}

.value-display {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
}

.value {
  font-size: 24px;
  font-weight: 600;
}

.unit {
  font-size: 12px;
  color: #909399;
}
</style>