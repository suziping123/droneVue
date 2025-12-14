<!-- PathPlanning.vue -->
<template>
  <div class="path-planning-panel">
    <el-row :gutter="20">
      <el-col :span="8">
        <div class="planning-controls">
          <el-form label-width="80px">
            <el-form-item label="规划算法">
              <el-select v-model="algorithm">
                <el-option label="A*" value="a-star"></el-option>
                <el-option label="RRT" value="rrt"></el-option>
                <el-option label="Dijkstra" value="dijkstra"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="安全距离">
              <el-slider
                  v-model="safetyDistance"
                  :min="100"
                  :max="1000"
                  :step="50"
              ></el-slider>
            </el-form-item>

            <el-form-item>
              <el-button
                  type="primary"
                  icon="el-icon-cpu"
                  @click="calculatePath"
              >
                重新规划路径
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>

      <el-col :span="16">
        <div class="path-visualization">
          <div class="path-metrics">
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="metric-item">
                  <label>路径长度</label>
                  <div class="value">{{ pathLength }} km</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="metric-item">
                  <label>危险系数</label>
                  <div class="value">{{ dangerIndex }}</div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="metric-item">
                  <label>预计时间</label>
                  <div class="value">{{ estimatedTime }} min</div>
                </div>
              </el-col>
            </el-row>
          </div>

          <div class="elevation-profile">
            <elevation-chart :data="elevationData"/>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ElevationChart from './ElevationChart'

export default {
  components: { ElevationChart },

  data() {
    return {
      algorithm: 'a-star',
      safetyDistance: 500,
      pathLength: 0,
      dangerIndex: 0,
      estimatedTime: 0,
      elevationData: []
    }
  },

  methods: {
    calculatePath() {
      // 路径规划算法实现...
      this.generateMetrics()
    },

    generateMetrics() {
      this.pathLength = (Math.random() * 50 + 50).toFixed(1)
      this.dangerIndex = (Math.random() * 0.8 + 0.2).toFixed(2)
      this.estimatedTime = (this.pathLength / 120 * 60).toFixed(0)
      this.elevationData = Array.from({length: 20}, (_,i) => ({
        x: i * 5,
        y: 500 + Math.random() * 1000
      }))
    }
  }
}
</script>

<style scoped>
.path-planning-panel {
  padding: 20px;

  .planning-controls {
    border-right: 1px solid #ebeef5;
    padding-right: 20px;
  }

  .path-visualization {
    padding-left: 20px;

    .path-metrics {
      margin-bottom: 20px;

      .metric-item {
        text-align: center;
        padding: 15px;
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

    .elevation-profile {
      height: 200px;
    }
  }
}
</style>