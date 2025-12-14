<!-- DangerRadar.vue -->
<template>
  <div ref="chart" class="danger-radar"></div>
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
          text: '危险预测概率',
          left: 'center'
        },
        radar: {
          indicator: [
            { name: '碰撞', max: 1 },
            { name: '追尾', max: 1 },
            { name: '鬼探头', max: 1 }
          ],
          shape: 'circle',
          splitArea: {
            areaStyle: {
              color: ['rgba(24,144,255,0.1)']
            }
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(24,144,255,0.8)'
            }
          }
        },
        series: [{
          type: 'radar',
          data: [],
          areaStyle: {
            color: 'rgba(24,144,255,0.4)'
          },
          lineStyle: {
            width: 2
          }
        }]
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
      const values = [
        this.data.collision || 0,
        this.data.rearEnd || 0,
        this.data.ghost || 0
      ]

      this.chartOption.series[0].data = [{
        value: values,
        name: '危险概率'
      }]
      this.chart.setOption(this.chartOption)
    }
  }
}
</script>

<style scoped>
.danger-radar {
  width: 100%;
  height: 280px;
}
</style>