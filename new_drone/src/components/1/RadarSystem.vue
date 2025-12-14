<!-- RadarSystem.vue -->
<template>
  <el-card class="radar-system">
    <div ref="chart" class="radar-chart"></div>

    <el-table
        :data="targets"
        height="200"
        stripe
        class="target-table"
    >
      <el-table-column prop="id" label="ID" width="80"/>
      <el-table-column label="距离 (m)" width="120">
        <template #default="{row}">
          <el-progress
              :percentage="row.distance/200*100"
              :show-text="false"
              :color="distanceColor"
          />
          <span class="distance-value">{{ row.distance.toFixed(1) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="速度 (m/s)" width="120">
        <template #default="{row}">
          <el-tag :type="speedTagType(row.speed)">
            {{ row.speed.toFixed(1) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="方位角" prop="angle"/>
    </el-table>
  </el-card>
</template>

<script>
import * as echarts from 'echarts'

export default {
  data() {
    return {
      chart: null,
      targets: [],
      distanceColor: [
        { offset: 0, color: '#67C23A' },
        { offset: 0.7, color: '#E6A23C' },
        { offset: 1, color: '#F56C6C' }
      ]
    }
  },

  mounted() {
    this.initChart()
    this.startDataSimulation()
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
        radar: {
          indicator: [
            { name: '前向', max: 200 },
            { name: '右前', max: 200 },
            { name: '右侧', max: 200 },
            { name: '右后', max: 200 },
            { name: '后向', max: 200 },
            { name: '左后', max: 200 },
            { name: '左侧', max: 200 },
            { name: '左前', max: 200 }
          ],
          shape: 'circle',
          splitArea: {
            areaStyle: {
              color: ['rgba(103, 194, 58, 0.1)']
            }
          }
        },
        series: [{
          type: 'radar',
          data: [],
          areaStyle: {
            color: 'rgba(103, 194, 58, 0.3)'
          }
        }]
      }
      this.chart.setOption(option)
    },

    generateTargets() {
      return Array.from({length: 5}, (_,i) => ({
        id: `T${i+1}`,
        distance: 50 + Math.random() * 150,
        speed: Math.random() * 30 - 15,
        angle: Math.floor(Math.random() * 360)
      }))
    },

    speedTagType(speed) {
      const absSpeed = Math.abs(speed)
      if (absSpeed > 10) return 'danger'
      if (absSpeed > 5) return 'warning'
      return 'success'
    },

    startDataSimulation() {
      setInterval(() => {
        this.targets = this.generateTargets()

        const radarData = this.targets.reduce((acc, target) => {
          const angleIndex = Math.floor(target.angle / 45)
          acc[angleIndex] = Math.max(acc[angleIndex] || 0, target.distance)
          return acc
        }, Array(8).fill(0))

        this.chart.setOption({
          series: [{
            data: [radarData]
          }]
        })
      }, 1500)
    }
  }
}
</script>

<style scoped>
.radar-chart {
  height: 400px;
}

.target-table {
  margin-top: 20px;

  .distance-value {
    display: inline-block;
    margin-left: 10px;
    font-family: monospace;
  }
}
</style>