<!-- ElevationChart.vue -->
<template>
  <div ref="chart" class="elevation-chart"></div>
</template>

<script>
import echarts from 'echarts'

export default {
  props: {
    data: Array
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
    this.updateChart()
  },

  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
    }
  },

  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.chart)
    },

    updateChart() {
      const option = {
        tooltip: {
          trigger: 'axis',
          formatter: '海拔: {c} 米'
        },
        xAxis: {
          type: 'category',
          data: this.data.map(item => `${item.x} 公里`),
          axisLabel: {
            color: '#606266'
          }
        },
        yAxis: {
          type: 'value',
          name: '海拔 (米)',
          axisLabel: {
            color: '#909399'
          }
        },
        series: [{
          data: this.data.map(item => item.y),
          type: 'line',
          smooth: true,
          areaStyle: {
            color: '#1890ff33'
          },
          lineStyle: {
            color: '#1890ff',
            width: 2
          },
          symbol: 'none'
        }],
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        }
      }
      this.chart.setOption(option)
    }
  }
}
</script>

<style scoped>
.elevation-chart {
  width: 100%;
  height: 200px;
}
</style>