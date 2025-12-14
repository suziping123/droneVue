<template>
  <g
      class="uav-marker"
      :transform="`translate(${uav.position.x},${uav.position.y})`"
      @click="handleClick"
  >
    <!-- 机体 -->
    <circle
        r="8"
        :fill="statusColor"
        stroke="#333"
        stroke-width="1"
    />

    <!-- 方向指示器 -->
    <path
        d="M0,-12 L5,0 L-5,0 Z"
        fill="#333"
        :transform="`rotate(${directionAngle})`"
    />

    <!-- 状态标签 -->
    <text
        x="0"
        y="-20"
        text-anchor="middle"
        font-size="12"
        fill="#666"
    >
      {{ uav.name }} ({{ uav.speed }}m/s)
    </text>
  </g>
</template>

<script>
export default {
  props: {
    uav: {
      type: Object,
      required: true
    }
  },
  computed: {
    statusColor() {
      const statusMap = {
        '飞行中': '#67C23A',
        '警告': '#E6A23C',
        '危险': '#F56C6C',
        '离线': '#909399'
      }
      return statusMap[this.uav.status] || '#909399'
    },
    directionAngle() {
      return Math.atan2(this.uav.direction?.y || 0, this.uav.direction?.x || 1) * 180 / Math.PI
    }
  },
  methods: {
    handleClick() {
      this.$emit('click', this.uav)
    }
  }
}
</script>