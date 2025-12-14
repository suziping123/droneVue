<template>
  <div class="map-container">
    <!-- 主地图区域 -->
    <div id="map-container" class="main-map"></div>

    <!-- 控制侧边栏 -->
    <div class="control-panel">
      <el-collapse v-model="activeCollapse">
        <!-- 基础控制 -->
        <el-collapse-item title="基础控制" name="basic">
          <el-button-group>
            <el-button type="primary" @click="addWaypoint">添加航点</el-button>
            <el-button type="warning" @click="clearAll">清空航线</el-button>
          </el-button-group>
          <div class="margin-top">
            <el-switch
                v-model="map3D"
                active-text="3D视图"
                @change="toggle3DView">
            </el-switch>
          </div>
        </el-collapse-item>

        <!-- 航线参数 -->
        <el-collapse-item title="航线参数" name="params">
          <el-form label-width="80px">
            <el-form-item label="飞行高度">
              <el-input-number
                  v-model="flightParams.altitude"
                  :min="50"
                  :max="500"
                  step-strictly
                  controls-position="right"
                  suffix="米">
              </el-input-number>
            </el-form-item>
            <el-form-item label="飞行速度">
              <el-input-number
                  v-model="flightParams.speed"
                  :min="5"
                  :max="20"
                  step-strictly
                  controls-position="right"
                  suffix="m/s">
              </el-input-number>
            </el-form-item>
            <el-form-item label="安全间隔">
              <el-slider
                  v-model="flightParams.safetyMargin"
                  :min="10"
                  :max="100"
                  show-input>
              </el-slider>
            </el-form-item>
          </el-form>
        </el-collapse-item>

        <!-- 避障设置 -->
        <el-collapse-item title="避障设置" name="obstacle">
          <div class="obstacle-control">
            <el-button @click="startDrawObstacle">绘制避障区</el-button>
            <el-button @click="clearObstacles">清空避障区</el-button>
          </div>
          <div class="margin-top">
            <el-alert
                title="避障区将自动进行航线规避"
                type="info"
                :closable="false">
            </el-alert>
          </div>
        </el-collapse-item>

        <!-- 实时监控 -->
        <el-collapse-item title="实时监控" name="monitor">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="飞行状态">
              <el-tag :type="flightStatus.type">{{ flightStatus.text }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="当前速度">
              {{ realtimeData.speed }} m/s
            </el-descriptions-item>
            <el-descriptions-item label="剩余电量">
              <el-progress
                  :percentage="realtimeData.battery"
                  :color="batteryColor"
                  :stroke-width="16"
                  :format="formatBattery">
              </el-progress>
            </el-descriptions-item>
          </el-descriptions>
        </el-collapse-item>
      </el-collapse>

      <!-- 操作记录 -->
      <div class="history-panel">
        <div class="history-title">操作记录</div>
        <el-timeline>
          <el-timeline-item
              v-for="(record, index) in operationHistory"
              :key="index"
              :timestamp="record.time">
            {{ record.action }}
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>

    <!-- 高程剖面图 -->
    <div v-show="elevationVisible" class="elevation-chart">
      <div id="elevation-container" style="width:100%;height:200px"></div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DronePathPlanner',
  data() {
    return {
      map: null,
      map3D: false,
      activeCollapse: ['basic', 'params'],
      flightParams: {
        altitude: 100,
        speed: 10,
        safetyMargin: 50
      },
      realtimeData: {
        speed: 0,
        battery: 100,
        position: null
      },
      flightStatus: {
        type: 'success',
        text: '待命'
      },
      operationHistory: [],
      pathLine: null,
      waypoints: [],
      obstacles: [],
      droneMarker: null,
      elevationVisible: false,
      chartInstance: null
    }
  },
  computed: {
    batteryColor() {
      if (this.realtimeData.battery > 70) return '#67C23A'
      if (this.realtimeData.battery > 30) return '#E6A23C'
      return '#F56C6C'
    }
  },
  mounted() {
    this.initMap()
    this.simulateData()
  },
  methods: {
    // 初始化地图
    initMap() {
      this.map = new AMap.Map('map-container', {
        zoom: 17,
        center: [116.397428, 39.90923],
        viewMode: '3D',
        pitch: 60,
        showIndoorMap: false,
        expandZoomRange: true,
        zooms: [15, 20]
      })

      // 添加地图控件
      this.map.addControl(new AMap.Scale())
      this.map.addControl(new AMap.ToolBar({ position: 'RB' }))
      this.map.addControl(new AMap.HawkEye({ opened: true }))

      // 初始化航线图层
      this.pathLine = new AMap.Polyline({
        strokeColor: '#409EFF',
        strokeWeight: 4,
        lineJoin: 'round'
      })
      this.map.add(this.pathLine)

      // 绑定地图点击事件
      this.map.on('click', this.handleMapClick)
    },

    // 添加航点
    addWaypoint(position) {
      const newPoint = position || this.map.getCenter()
      const marker = new AMap.Marker({
        position: newPoint,
        content: this.createWaypointContent(this.waypoints.length + 1),
        draggable: true
      })

      marker.on('dragging', (e) => this.updatePath())
      marker.on('rightclick', () => this.removeWaypoint(marker))

      this.waypoints.push(marker)
      this.map.add(marker)
      this.updatePath()
      this.recordOperation(`添加航点 ${this.waypoints.length}`)
    },

    // 更新航线
    updatePath() {
      const path = this.waypoints.map(m => m.getPosition())
      this.pathLine.setPath(path)
      this.updateElevationChart()
    },

    // 创建航点标记内容
    createWaypointContent(number) {
      return `<div class="waypoint-marker">
                <div class="waypoint-number">${number}</div>
                <div class="waypoint-dot"></div>
              </div>`
    },

    // 处理地图点击
    handleMapClick(e) {
      if (this.drawingObstacle) {
        this.drawObstacle(e.lnglat)
      } else {
        this.addWaypoint(e.lnglat)
      }
    },

    // 绘制避障区
    startDrawObstacle() {
      this.drawingObstacle = true
      this.tempObstaclePoints = []
      this.recordOperation('开始绘制避障区')
    },

    // 清空所有
    clearAll() {
      this.waypoints.forEach(m => this.map.remove(m))
      this.waypoints = []
      this.pathLine.setPath([])
      this.clearObstacles()
      this.recordOperation('清空所有数据')
    },

    // 更新高程图表
    updateElevationChart() {
      // 模拟高程数据
      const data = this.pathLine.getPath().map((p, i) => ({
        distance: i * 50,
        elevation: Math.random() * 50 + this.flightParams.altitude
      }))

      if (!this.chartInstance) {
        this.chartInstance = echarts.init(document.getElementById('elevation-container'))
      }

      const option = {
        tooltip: { trigger: 'axis' },
        xAxis: { name: '距离 (米)' },
        yAxis: { name: '高程 (米)' },
        series: [{
          data: data.map(d => d.elevation),
          type: 'line',
          smooth: true,
          areaStyle: {}
        }]
      }

      this.chartInstance.setOption(option)
      this.elevationVisible = true
    },

    // 模拟实时数据
    simulateData() {
      setInterval(() => {
        if (this.realtimeData.battery > 0) {
          this.realtimeData.speed = Math.random() * 2 + 8
          this.realtimeData.battery -= 0.1
        }
      }, 1000)
    },

    // 记录操作历史
    recordOperation(action) {
      this.operationHistory.unshift({
        time: new Date().toLocaleTimeString(),
        action: action
      })
      if (this.operationHistory.length > 10) this.operationHistory.pop()
    },

    formatBattery(percentage) {
      return `${percentage.toFixed(1)}%`
    }
  }
}
</script>

<style scoped>
.map-container {
  position: relative;
  width: 100vw;
  height: 100vh;
}

.main-map {
  width: calc(100% - 350px);
  height: 100%;
}

.control-panel {
  position: absolute;
  right: 0;
  top: 0;
  width: 350px;
  height: 100%;
  background: white;
  box-shadow: -2px 0 12px rgba(0,0,0,0.1);
  padding: 20px;
  overflow-y: auto;
}

.history-panel {
  margin-top: 20px;
  border-top: 1px solid #eee;
  padding-top: 15px;
}

.history-title {
  font-weight: bold;
  margin-bottom: 10px;
  color: #606266;
}

.elevation-chart {
  position: absolute;
  bottom: 20px;
  left: 20px;
  width: 400px;
  background: white;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.waypoint-marker {
  position: relative;
  width: 32px;
  height: 32px;
}

.waypoint-number {
  position: absolute;
  top: 2px;
  left: 50%;
  transform: translateX(-50%);
  color: white;
  font-size: 12px;
  font-weight: bold;
  z-index: 1;
}

.waypoint-dot {
  width: 24px;
  height: 24px;
  background: #409EFF;
  border: 2px solid white;
  border-radius: 50%;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  box-shadow: 0 2px 6px rgba(0,0,0,0.3);
}

.margin-top {
  margin-top: 15px;
}
</style>