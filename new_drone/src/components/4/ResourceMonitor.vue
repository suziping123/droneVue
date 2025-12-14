<!-- ResourceMonitor.vue -->
<template>
  <div ref="chart" :style="{height: height}"></div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  props: {
    cpu: Number,
    memory: Number,
    network: Number,
    height: {
      type: String,
      default: '240px'
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    cpu() {
      this.updateChart()
    },
    memory() {
      this.updateChart()
    },
    network() {
      this.updateChart()
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
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        xAxis: {
          type: 'category',
          data: ['CPU', '内存', '网络'],
          axisLabel: {
            color: '#606266'
          }
        },
        yAxis: {
          type: 'value',
          max: 100,
          axisLabel: {
            color: '#909399'
          }
        },
        series: [{
          data: [this.cpu, this.memory, this.network],
          type: 'bar',
          itemStyle: {
            color: (params) => {
              const colors = ['#409EFF', '#67C23A', '#E6A23C']
              return colors[params.dataIndex]
            }
          }
        }]
      }
      this.chart.setOption(option)
    },
    updateChart() {
      if (this.chart) {
        const option = this.chart.getOption()
        option.series[0].data = [this.cpu, this.memory, this.network]
        this.chart.setOption(option)
      }
    }
  }
}
</script>