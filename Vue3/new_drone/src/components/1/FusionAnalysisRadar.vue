<!-- FusionAnalysisRadar.vue -->
<template>
  <el-card class="fusion-radar">
    <div ref="chart" class="chart-container"></div>
  </el-card>
</template>

<script>
import * as echarts from 'echarts'

export default {
  props: {
    data: Object
  },

  data() {
    return {
      chart: null
    }
  },

  watch: {
    data: {
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
      const option = {
        title: {
          text: '多源融合置信度',
          left: 'center'
        },
        radar: {
          indicator: this.data.indicators.map(i => ({
            name: i,
            max: 1
          }))
        },
        series: [{
          type: 'radar',
          data: this.data.values.map(v => ({
            value: v.data,
            name: v.name,
            areaStyle: {
              color: this.getRandomColor()
            }
          }))
        }]
      }
      this.chart.setOption(option)
    },

    getRandomColor() {
      const hue = Math.floor(Math.random() * 360)
      return `hsla(${hue}, 60%, 50%, 0.3)`
    },

    updateChart() {
      this.chart.setOption({
        radar: {
          indicator: this.data.indicators.map(i => ({
            name: i,
            max: 1
          }))
        },
        series: [{
          data: this.data.values.map(v => ({
            value: v.data,
            name: v.name
          }))
        }]
      })
    }
  }
}
</script>

<style scoped>
.chart-container {
  height: 280px;
}
</style>