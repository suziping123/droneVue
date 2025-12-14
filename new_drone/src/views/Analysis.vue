<template>
  <div class="intelligent-decision-container">
    <el-container>
      <!-- 左侧三维地图 -->
      <el-aside width="65%" class="map-container">
        <div id="map-container" style="height:100vh;"></div>
      </el-aside>

      <!-- 右侧控制面板 -->
      <el-main class="control-panel">
        <!-- 状态仪表盘 -->
        <el-row :gutter="20" class="status-row">
          <el-col :span="8">
            <el-card class="instrument-card">
              <h4 class="instrument-title">飞行高度</h4>
              <mini-instrument
                  :value="flightData.altitude"
                  :max="3000"
                  unit="m"
                  color="#409EFF"
                  size="120px"
              />
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="instrument-card">
              <h4 class="instrument-title">飞行速度</h4>
              <mini-instrument
                  :value="flightData.speed"
                  :max="300"
                  unit="m/s"
                  color="#67C23A"
                  size="120px"
              />
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card class="instrument-card">
              <h4 class="instrument-title">航向角度</h4>
              <mini-instrument
                  :value="flightData.heading"
                  :max="360"
                  unit="°"
                  color="#E6A23C"
                  size="120px"
              />
            </el-card>
          </el-col>
        </el-row>

        <!-- 自动避障指令 -->
        <el-row :gutter="20" class="data-row">
          <el-col :span="24">
            <el-card class="data-card">
              <div class="table-header">
                <h3 class="table-title">自动避障指令</h3>
                <div>
                  <el-tag type="danger">实时更新</el-tag>
                  <el-tag type="warning" style="margin-left:10px">最新指令：{{ latestCommand }}</el-tag>
                </div>
              </div>
              <el-table
                  :data="avoidanceCommands"
                  height="240"
                  stripe
                  style="width: 100%"
              >
                <el-table-column
                    prop="timestamp"
                    label="时间"
                    width="150"
                    align="center"
                />
                <el-table-column
                    prop="type"
                    label="指令类型"
                    width="180"
                    align="center"
                />
                <el-table-column
                    prop="distance"
                    label="障碍距离"
                    width="150"
                    align="center"
                >
                  <template slot-scope="{row}">
                    <span :style="{color: row.distance < 50 ? 'red' : '#67C23A', fontWeight: 600}">
                      {{ row.distance }}m
                    </span>
                  </template>
                </el-table-column>
                <el-table-column
                    prop="action"
                    label="规避动作"
                    width="200"
                    align="center"
                />
                <el-table-column
                    label="执行状态"
                    width="150"
                    align="center"
                >
                  <template slot-scope="{row}">
                    <el-tag :type="row.status === '执行成功' ? 'success' : 'warning'">
                      {{ row.status }}
                    </el-tag>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>
        </el-row>

        <!-- 路径规划 -->
        <el-row :gutter="20" class="monitor-row">
          <el-col :span="24">
            <el-card class="monitor-card">
              <div class="table-header">
                <h3 class="monitor-title">路径规划</h3>
                <div>
                  <el-button
                      type="primary"
                      size="mini"
                      @click="handlePathOptimize"
                  >
                    路径优化
                  </el-button>
                  <el-button
                      type="success"
                      size="mini"
                      @click="addWaypoint"
                      style="margin-left:10px"
                  >
                    添加航点
                  </el-button>
                </div>
              </div>
              <el-table
                  :data="pathPoints"
                  height="300"
                  stripe
                  style="width: 100%"
              >
                <el-table-column
                    prop="index"
                    label="航点"
                    width="100"
                    align="center"
                />
                <el-table-column
                    prop="lat"
                    label="纬度"
                    width="200"
                    align="center"
                />
                <el-table-column
                    prop="lng"
                    label="经度"
                    width="200"
                    align="center"
                />
                <el-table-column
                    prop="alt"
                    label="高度(m)"
                    width="150"
                    align="center"
                />
                <el-table-column
                    label="操作"
                    width="120"
                    align="center"
                >
                  <template slot-scope="scope">
                    <el-button
                        type="danger"
                        size="mini"
                        @click="deleteWaypoint(scope.$index)"
                    >
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-card>
          </el-col>
        </el-row>

        <!-- 指令执行流水 -->
        <el-row :gutter="20" class="timeline-row">
          <el-col :span="24">
            <el-card class="timeline-card">
              <h3 class="timeline-title">指令执行流水线</h3>
              <instruction-timeline
                  :commands="executionTimeline"
                  height="200px"
              />
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import MiniInstrument from '@/components/4/MiniInstrument'
import InstructionTimeline from '@/components/4/InstructionTimeline'

export default {
  components: {
    MiniInstrument,
    InstructionTimeline
  },

  data() {
    return {
      map: null,
      flightData: {
        altitude: 1500,
        speed: 120,
        heading: 45,
        position: [31.2304, 121.4737]
      },
      currentPath: [],
      dangerZones: [],
      avoidanceCommands: [],
      executionTimeline: [],
      pathPoints: [
        { index: 1, lat: 31.2304, lng: 121.4737, alt: 1500 },
        { index: 2, lat: 31.2354, lng: 121.4787, alt: 1550 },
        { index: 3, lat: 31.2404, lng: 121.4837, alt: 1600 }
      ],
      simulationInterval: null
    }
  },

  computed: {
    latestCommand() {
      return this.avoidanceCommands[0]?.type || '无最新指令'
    }
  },

  methods: {
    // 初始化地图
    initMap() {
      AMap.plugin(['AMap.Scale', 'AMap.ToolBar'], () => {
        this.map = new AMap.Map('map-container', {
          viewMode: '3D',
          zoom: 14,
          center: [121.4737, 31.2304],
          pitch: 45,
          rotation: 0
        })

        // 添加比例尺控件
        this.map.addControl(new AMap.Scale({
          position: 'LB',
          visible: true
        }))

        // 添加工具条控件
        this.map.addControl(new AMap.ToolBar({
          position: 'RB',
          offset: [10, 40]
        }))
      })
    },

    loadAMap() {
      const key = 'your_amap_key'
      if (!window.AMap) {
        const mapScript = document.createElement('script')
        mapScript.src = `https://webapi.amap.com/maps?v=2.0&key=${key}&plugin=AMap.Scale,AMap.ToolBar`
        mapScript.onload = () => {
          this.initMap()
          this.startDataSimulation()
        }
        document.head.appendChild(mapScript)
      } else {
        this.initMap()
        this.startDataSimulation()
      }
    },

    // 路径优化处理
    handlePathOptimize() {
      this.$confirm('是否执行路径优化计算？', '路径优化', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.generateOptimizedPath()
      })
    },

    // 生成优化路径
    generateOptimizedPath() {
      const newPath = this.pathPoints.map(point => ({
        lng: point.lng + Math.random() * 0.001 - 0.0005,
        lat: point.lat + Math.random() * 0.001 - 0.0005,
        alt: point.alt + Math.random() * 50 - 25
      }))
      this.currentPath = newPath
      this.$message.success('路径优化完成')
    },

    // 添加航点
    addWaypoint() {
      const lastPoint = this.pathPoints[this.pathPoints.length - 1]
      const newPoint = {
        index: this.pathPoints.length + 1,
        lat: lastPoint.lat + 0.005,
        lng: lastPoint.lng + 0.005,
        alt: lastPoint.alt + 50
      }
      this.pathPoints.push(newPoint)
    },

    // 删除航点
    deleteWaypoint(index) {
      this.pathPoints.splice(index, 1)
      this.pathPoints = this.pathPoints.map((p, i) => ({
        ...p,
        index: i + 1
      }))
    },

    // 模拟数据更新
    startDataSimulation() {
      this.simulationInterval = setInterval(() => {
        this.updateFlightData()
        this.updateDangerZones()
        this.generateAvoidanceCommands()
        this.updateExecutionTimeline()
      }, 1000)
    },

    updateFlightData() {
      this.flightData = {
        altitude: 1500 + Math.random() * 100,
        speed: 120 + Math.random() * 20,
        heading: (this.flightData.heading + 0.5) % 360,
        position: [
          31.2304 + Math.random() * 0.001 - 0.0005,
          121.4737 + Math.random() * 0.001 - 0.0005
        ]
      }
    },

    updateDangerZones() {
      this.dangerZones = Array.from({ length: 3 }, (_, i) => ({
        id: `danger_${Date.now()}_${i}`,
        position: [
          31.2304 + Math.random() * 0.01 - 0.005,
          121.4737 + Math.random() * 0.01 - 0.005
        ],
        radius: 300 + Math.random() * 700,
        level: Math.floor(Math.random() * 3) + 1
      }))
    },

    generateAvoidanceCommands() {
      const commandTypes = ['紧急爬升', '侧向规避', '减速绕行', '路径重规划']
      const newCommand = {
        timestamp: new Date().toLocaleTimeString(),
        type: commandTypes[Math.floor(Math.random() * commandTypes.length)],
        distance: Math.floor(Math.random() * 200),
        action: ['左转30°', '上升50m', '降速20%', '右转45°'][Math.floor(Math.random() * 4)],
        status: ['执行成功', '执行中'][Math.floor(Math.random() * 2)]
      }

      if (this.avoidanceCommands.length >= 10) this.avoidanceCommands.pop()
      this.avoidanceCommands.unshift(newCommand)
    },

    updateExecutionTimeline() {
      const newEntry = {
        time: new Date().toLocaleTimeString(),
        command: this.avoidanceCommands[0]?.type || '待机状态',
        status: this.avoidanceCommands[0]?.status || '等待确认',
        icon: this.avoidanceCommands[0]?.status === '执行成功' ? 'success' : 'warning'
      }

      if (this.executionTimeline.length >= 8) this.executionTimeline.pop()
      this.executionTimeline.unshift(newEntry)
    }
  },

  mounted() {
    this.loadAMap()
  },

  beforeDestroy() {
    clearInterval(this.simulationInterval)
    if (this.map) {
      this.map.destroy()
    }
  }
}
</script>

<style lang="scss">
.intelligent-decision-container {
  height: 100vh;
  background: #f0f2f5;

  .map-container {
    background: #2c3e50;
    box-shadow: 2px 0 8px rgba(0, 0, 0, 0.15);
    position: relative;
    overflow: hidden;
  }

  .control-panel {
    padding: 20px;
    height: 100vh;
    display: flex;
    flex-direction: column;
    background: linear-gradient(to bottom, #ffffff, #f8f9fa);

    .status-row {
      margin-bottom: 20px;

      .instrument-card {
        text-align: center;
        transition: transform 0.3s ease;
        height: 200px;
        display: flex;
        flex-direction: column;
        justify-content: center;

        &:hover {
          transform: translateY(-5px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
        }

        .instrument-title {
          margin: 0 0 15px 0;
          color: #606266;
          font-size: 16px;
          font-weight: 500;
        }
      }
    }

    .data-row {
      margin-bottom: 20px;

      .data-card {
        height: 320px;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

        .table-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 15px;
          padding: 0 15px;

          .table-title {
            margin: 0;
            color: #303133;
            font-size: 18px;
            font-weight: 600;
          }
        }

        .el-table {
          border-radius: 8px;
          overflow: hidden;

          th {
            background: #f8f9fa !important;
            font-weight: 600;
            font-size: 14px;
          }

          td {
            transition: background 0.3s ease;
            font-size: 13px;
          }
          tr:hover td {
            background: #f5f7fa !important;
          }
        }
      }
    }

    .monitor-row {
      margin-bottom: 20px;

      .monitor-card {
        height: 400px;
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

        .table-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 15px;
          padding: 0 15px;

          .monitor-title {
            margin: 0;
            color: #303133;
            font-size: 18px;
            font-weight: 600;
          }
        }

        .el-table {
          th {
            font-size: 14px;
          }
          td {
            font-size: 13px;
          }
        }
      }
    }

    .timeline-row {
      .timeline-card {
        border-radius: 12px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

        .timeline-title {
          margin: 0 0 15px 15px;
          color: #303133;
          font-size: 18px;
          font-weight: 600;
        }
      }
    }
  }
}

.el-card {
  border: none;
  border-radius: 12px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);

  &:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15) !important;
  }
}

.el-button {
  transition: all 0.2s ease;

  &:active {
    transform: scale(0.95);
  }
}

@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.05); }
  100% { transform: scale(1); }
}

.alert-pulse {
  animation: pulse 1.5s infinite;
}
</style>