<!-- FeatureExtractionChart.vue -->
<template>
  <el-card class="feature-chart">
    <div ref="chart" class="chart-container"></div>
  </el-card>
</template>

<script>
import * as echarts from 'echarts'

export default {
  props: {
    features: Object
  },

  data() {
    return {
      chart: null
    }
  },

  watch: {
    features: {
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
          text: '特征分布雷达图',
          left: 'center'
        },
        radar: {
          indicator: this.features.dimensions.map(d => ({ name: d, max: 100 }))
        },
        series: [{
          type: 'radar',
          data: this.features.data.map(d => ({
            value: d.values,
            name: d.name,
            areaStyle: {
              color: 'rgba(24, 144, 255, 0.3)'
            }
          }))
        }]
      }
      this.chart.setOption(option)
    },

    updateChart() {
      this.chart.setOption({
        radar: {
          indicator: this.features.dimensions.map(d => ({ name: d, max: 100 }))
        },
        series: [{
          data: this.features.data.map(d => ({
            value: d.values,
            name: d.name
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