<!-- AlertStatistics.vue -->
<template>
  <el-card class="alert-statistics">
    <div class="chart-header">
      <h4>预警统计分布</h4>
      <el-select
          v-model="timeRange"
          size="mini"
          @change="updateChart"
      >
        <el-option label="最近1小时" value="1h"></el-option>
        <el-option label="最近24小时" value="24h"></el-option>
        <el-option label="最近7天" value="7d"></el-option>
      </el-select>
    </div>

    <div ref="chart" class="chart-container"></div>
  </el-card>
</template>

<script>
import echarts from 'echarts'

export default {
  props: {
    data: Object
  },

  data() {
    return {
      chart: null,
      timeRange: '1h'
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
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: this.data.levels.map(l => l.name),
          axisLabel: {
            color: '#606266'
          }
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            color: '#909399'
          }
        },
        series: [{
          data: this.data.levels.map(l => l.count),
          type: 'bar',
          itemStyle: {
            color: params => {
              const colors = ['#67C23A', '#E6A23C', '#F56C6C']
              return colors[params.dataIndex % colors.length]
            }
          },
          barWidth: '30%'
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

<style lang="scss" scoped>
.alert-statistics {
  height: 100%;

  .chart-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
  }

  .chart-container {
    height: 300px;
  }
}
</style>