<!-- SceneGraph.vue -->
<template>
  <div ref="chart" class="scene-graph-container"></div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  props: {
    nodes: Array,
    edges: Array
  },

  data() {
    return {
      chart: null,
      chartOption: {
        title: {
          text: '动态场景图',
          subtext: 'v1.1',
          left: 'center'
        },
        tooltip: {},
        animationDurationUpdate: 1500,
        animationEasingUpdate: 'quinticInOut',
        series: [{
          type: 'graph',
          layout: 'force',
          force: {
            repulsion: 1000,
            edgeLength: [100, 300]
          },
          data: [],
          links: [],
          categories: [
            { name: '自机' },
            { name: '车辆' },
            { name: '行人' },
            { name: '道路' }
          ],
          roam: true,
          label: {
            show: true,
            position: 'right',
            formatter: '{b}'
          },
          lineStyle: {
            color: 'source',
            curveness: 0.3
          },
          emphasis: {
            focus: 'adjacency',
            lineStyle: {
              width: 5
            }
          }
        }]
      }
    }
  },

  watch: {
    nodes: {
      handler: 'updateChart',
      deep: true
    },
    edges: {
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
      this.chart.on('click', params => {
        if (params.dataType === 'node') {
          this.$emit('node-click', params.data)
        }
      })
    },

    updateChart() {
      const nodeData = this.nodes.map(node => ({
        id: node.id,
        name: node.label,
        category: node.category,
        symbolSize: node.symbolSize || 30,
        itemStyle: {
          color: this.getNodeColor(node.category)
        },
        ...node
      }))

      const edgeData = this.edges.map(edge => ({
        source: edge.source,
        target: edge.target,
        label: {
          show: true,
          formatter: this.getEdgeLabel(edge.relation),
          fontSize: 12
        },
        lineStyle: {
          color: this.getEdgeColor(edge.relation)
        }
      }))

      this.chartOption.series[0].data = nodeData
      this.chartOption.series[0].links = edgeData
      this.chart.setOption(this.chartOption)
    },

    getNodeColor(category) {
      const colors = {
        '自车': '#ff4d4f',
        '车辆': '#1890ff',
        '行人': '#52c41a',
        '道路': '#faad14'
      }
      return colors[category] || '#722ed1'
    },

    getEdgeLabel(relation) {
      const labels = {
        'distance': '距离',
        'position': '方位',
        'danger': '危险度'
      }
      return labels[relation] || relation
    },

    getEdgeColor(relation) {
      const colors = {
        'distance': '#8c8c8c',
        'position': '#13c2c2',
        'danger': '#f5222d'
      }
      return colors[relation] || '#eb2f96'
    }
  }
}
</script>

<style scoped>
.scene-graph-container {
  width: 100%;
  height: 480px;
}
</style>