<!-- MiniTrailChart.vue -->
<template>
  <div ref="chart" class="mini-trail-chart"></div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  props: {
    points: Array
  },

  data() {
    return {
      chart: null
    }
  },

  watch: {
    points: {
      handler: 'updateChart',
      deep: true
    }
  },

  mounted() {
    this.initChart()
  },

  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
    }
  },

  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.chart)
      this.updateChart()
    },

    updateChart() {
      const option = {
        xAxis: { show: false },
        yAxis: { show: false },
        grid: { top: 2, bottom: 2, left: 2, right: 2 },
        series: [{
          type: 'line',
          data: this.points,
          smooth: true,
          lineStyle: {
            color: '#1890ff',
            width: 2
          },
          showSymbol: false,
          areaStyle: {
            color: 'rgba(24,144,255,0.1)'
          }
        }]
      }
      this.chart.setOption(option)
    }
  }
}
</script>

<style scoped>
.mini-trail-chart {
  width: 100%;
  height: 60px;
}
</style>