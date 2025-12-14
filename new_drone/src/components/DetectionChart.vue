<!-- DetectionChart.vue -->
<template>
  <div ref="chart" class="detection-chart"></div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  props: {
    data: Object
  },

  data() {
    return {
      chart: null,
      chartOption: {
        title: {
          text: '目标检测统计',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: [],
          bottom: 10
        },
        xAxis: {
          type: 'category',
          data: []
        },
        yAxis: {
          type: 'value'
        },
        series: [],
        grid: {
          top: 60,
          bottom: 80,
          left: 60,
          right: 40
        }
      }
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
      this.updateChart()
    },

    updateChart() {
      const categories = Object.keys(this.data)
      const timeData = Array.from({length: 10}, (_,i) => `T-${10-i}s`)

      this.chartOption.legend.data = categories
      this.chartOption.xAxis.data = timeData
      this.chartOption.series = categories.map(cat => ({
        name: cat,
        type: 'line',
        smooth: true,
        data: this.generateSeriesData(cat),
        lineStyle: {
          width: 3
        },
        showSymbol: false,
        areaStyle: {
          opacity: 0.1
        }
      }))

      this.chart.setOption(this.chartOption)
    },

    generateSeriesData(category) {
      return Array.from({length: 10}, () =>
          Math.floor(Math.random() * 20) + this.data[category] * 10
      )
    }
  }
}
</script>

<style scoped>
.detection-chart {
  width: 100%;
  height: 280px;
}
</style>