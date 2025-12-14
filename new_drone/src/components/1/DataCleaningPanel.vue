<!-- DataCleaningPanel.vue -->
<template>
  <el-card class="cleaning-panel">
    <div class="header">
      <h4>数据清洗分析</h4>
      <el-tag :type="cleaningStats.outliers > 5000 ? 'danger' : 'success'">
        {{ cleaningStats.outliers }} 异常点
      </el-tag>
    </div>

    <el-row :gutter="20">
      <el-col :span="12">
        <div class="gauge-chart" ref="cleaningGauge"></div>
      </el-col>
      <el-col :span="12">
        <div class="stats">
          <div class="stat-item">
            <label>原始数据量</label>
            <div class="value">{{ cleaningStats.rawPoints.toLocaleString() }}</div>
          </div>
          <div class="stat-item">
            <label>有效数据量</label>
            <div class="value">{{ cleaningStats.cleanPoints.toLocaleString() }}</div>
          </div>
          <div class="stat-item">
            <label>清洗率</label>
            <div class="value">
              {{ ((cleaningStats.cleanPoints / cleaningStats.rawPoints)*100 ).toFixed(1) }}%
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
import * as echarts from 'echarts'

export default {
  props: {
    stats: Object
  },

  data() {
    return {
      gaugeChart: null
    }
  },

  watch: {
    stats: {
      handler: 'updateChart',
      deep: true
    }
  },

  mounted() {
    this.initGaugeChart()
  },

  beforeDestroy() {
    if (this.gaugeChart) {
      this.gaugeChart.dispose()
    }
  },

  methods: {
    initGaugeChart() {
      this.gaugeChart = echarts.init(this.$refs.cleaningGauge)
      const option = {
        series: [{
          type: 'gauge',
          center: ['50%', '60%'],
          startAngle: 180,
          endAngle: 0,
          min: 0,
          max: 100,
          splitNumber: 10,
          axisLine: {
            lineStyle: {
              width: 20,
              color: [
                [0.3, '#67C23A'],
                [0.7, '#E6A23C'],
                [1, '#F56C6C']
              ]
            }
          },
          pointer: {
            length: '80%',
            width: 8
          },
          detail: {
            formatter: '{value}%',
            fontSize: 24
          },
          data: [{
            value: 0,
            name: '异常率'
          }]
        }]
      }
      this.gaugeChart.setOption(option)
    },

    updateChart() {
      const abnormalRate = (this.stats.outliers / this.stats.rawPoints * 100).toFixed(1)
      this.gaugeChart.setOption({
        series: [{
          data: [{
            value: abnormalRate,
            name: '异常率'
          }]
        }]
      })
    }
  }
}
</script>

<style scoped>
.cleaning-panel {
  height: 100%;

  .header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }

  .gauge-chart {
    height: 200px;
  }

  .stats {
    .stat-item {
      margin: 15px 0;
      padding: 10px;
      background: #fafafa;
      border-radius: 4px;

      label {
        color: #909399;
        font-size: 12px;
      }

      .value {
        font-size: 18px;
        font-weight: bold;
        color: #303133;
      }
    }
  }
}
</style>