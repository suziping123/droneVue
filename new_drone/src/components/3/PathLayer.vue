<template>
  <g class="path-layer">
    <!-- 危险区域 -->
    <circle
        v-for="zone in dangerZones"
        :key="zone.id"
        :cx="zone.position.x"
        :cy="zone.position.y"
        :r="zone.radius"
        fill="#F56C6C"
        fill-opacity="0.2"
        stroke="#F56C6C"
        stroke-width="1"
    />

    <!-- 原始路径 -->
    <path
        v-if="path.length > 0"
        :d="pathToD(path)"
        fill="none"
        stroke="#409EFF"
        stroke-width="2"
        stroke-dasharray="5 2"
    />

    <!-- 调整后路径 -->
    <path
        v-if="adjustedPath.length > 0"
        :d="pathToD(adjustedPath)"
        fill="none"
        stroke="#67C23A"
        stroke-width="2"
    />
  </g>
</template>

<script>
export default {
  props: {
    path: Array,
    adjustedPath: Array,
    dangerZones: Array
  },
  methods: {
    pathToD(points) {
      return points.map((p, i) =>
          `${i === 0 ? 'M' : 'L'}${p.x} ${p.y}`
      ).join(' ')
    }
  }
}
</script>