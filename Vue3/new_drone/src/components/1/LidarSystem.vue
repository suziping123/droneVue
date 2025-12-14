<!-- LidarSystem.vue -->
<template>
  <el-card class="lidar-system">
    <div ref="pointCloud" class="point-cloud-container"></div>

    <el-row :gutter="20" class="stats-row">
      <el-col :span="8">
        <div class="stat-item">
          <div class="stat-label">点云数量</div>
          <div class="stat-value">{{ pointCount.toLocaleString() }}</div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-item">
          <div class="stat-label">扫描频率</div>
          <div class="stat-value">{{ scanRate }}Hz</div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-item">
          <div class="stat-label">有效目标</div>
          <div class="stat-value">{{ validTargets }}</div>
        </div>
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
import * as echarts from 'echarts'
import 'echarts-gl'

export default {
  data() {
    return {
      chart: null,
      pointCount: 0,
      scanRate: 10,
      validTargets: 0
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
      this.chart = echarts.init(this.$refs.pointCloud)
      const option = {
        backgroundColor: '#000',
        grid3D: {
          viewControl: {
            autoRotate: true,
            distance: 200
          },
          light: {
            main: {
              intensity: 1.5
            }
          }
        },
        xAxis3D: {
          type: 'value',
          name: 'X轴 (m)'
        },
        yAxis3D: {
          type: 'value',
          name: 'Y轴 (m)'
        },
        zAxis3D: {
          type: 'value',
          name: 'Z轴 (m)'
        },
        series: [{
          type: 'scatter3D',
          data: [],
          itemStyle: {
            color: function(params) {
              return echarts.color.modifyHSL(
                  '#5470c6',
                  Math.abs(params.data[2]) * 10
              )
            },
            opacity: 0.8
          }
        }]
      }
      this.chart.setOption(option)
    },

    generatePointCloud() {
      const points = []
      const clusterCount = Math.floor(Math.random() * 5) + 3

      for (let i = 0; i < clusterCount; i++) {
        const centerX = Math.random() * 40 - 20
        const centerY = Math.random() * 40 - 20
        const centerZ = Math.random() * 10

        for (let j = 0; j < 500; j++) {
          points.push([
            centerX + Math.random() * 4 - 2,
            centerY + Math.random() * 4 - 2,
            centerZ + Math.random() * 2,
            Math.random() * 100
          ])
        }
      }

      return points
    },

    startDataSimulation() {
      setInterval(() => {
        const points = this.generatePointCloud()
        this.pointCount = points.length
        this.validTargets = Math.floor(Math.random() * 8) + 2
        this.scanRate = 10 + Math.random() * 5

        this.chart.setOption({
          series: [{
            data: points
          }]
        })
      }, 1000)
    }
  }
}
</script>

<style scoped>
.point-cloud-container {
  height: 500px;
}

.stats-row {
  margin-top: 20px;

  .stat-item {
    background: #fafafa;
    padding: 15px;
    border-radius: 4px;
    text-align: center;

    .stat-label {
      color: #909399;
      font-size: 12px;
    }

    .stat-value {
      font-size: 24px;
      font-weight: bold;
      color: #303133;
    }
  }
}
</style>