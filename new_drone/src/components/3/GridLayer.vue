<template>
  <g class="grid-layer">
    <!-- 水平网格线 -->
    <line
        v-for="y in yLines"
        :key="'h'+y"
        :x1="0"
        :y1="y"
        :x2="grid.width"
        :y2="y"
        :stroke="grid.color"
        stroke-width="0.5"
    />

    <!-- 垂直网格线 -->
    <line
        v-for="x in xLines"
        :key="'v'+x"
        :x1="x"
        :y1="0"
        :x2="x"
        :y2="grid.height"
        :stroke="grid.color"
        stroke-width="0.5"
    />
  </g>
</template>

<script>
export default {
  props: {
    grid: {
      type: Object,
      default: () => ({
        width: 800,
        height: 600,
        spacing: 50,
        color: '#eee'
      })
    }
  },
  computed: {
    xLines() {
      return Array.from(
          { length: Math.floor(this.grid.width / this.grid.spacing) },
          (_, i) => (i + 1) * this.grid.spacing
      )
    },
    yLines() {
      return Array.from(
          { length: Math.floor(this.grid.height / this.grid.spacing) },
          (_, i) => (i + 1) * this.grid.spacing
      )
    }
  }
}
</script>