<!-- NasArchitecture.vue -->
<template>
  <div ref="chart" class="nas-architecture"></div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  props: {
    layers: Array
  },

  data() {
    return {
      chart: null,
      chartOption: {
        title: {
          text: 'TreeNAS 架构',
          left: 'center'
        },
        tooltip: {
          formatter: ({data}) => `
            ${data.name}<br/>
            类型: ${data.type}<br/>
            通道: ${data.channels}<br/>
            参数: ${data.params}K
          `
        },
        series: [{
          type: 'tree',
          data: [],
          orient: 'vertical',
          label: {
            position: 'top',
            verticalAlign: 'middle',
            align: 'center',
            fontSize: 12
          },
          leaves: {
            label: {
              position: 'bottom',
              verticalAlign: 'middle',
              align: 'center'
            }
          },
          expandAndCollapse: false,
          animationDuration: 550,
          animationDurationUpdate: 750
        }]
      }
    }
  },

  watch: {
    layers: {
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
      this.chartOption.series[0].data = [this.formatTreeData()]
      this.chart.setOption(this.chartOption)
    },

    formatTreeData() {
      return {
        name: '输入',
        type: 'stem',
        children: this.layers.map(layer => ({
          name: layer.name,
          type: layer.type,
          channels: layer.channels,
          params: layer.params,
          children: layer.blocks.map(block => ({
            name: block.name,
            type: block.type,
            channels: block.channels,
            params: block.params
          }))
        }))
      }
    }
  }
}
</script>

<style scoped>
.nas-architecture {
  width: 100%;
  height: 280px;
}
</style>