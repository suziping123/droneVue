<template>
  <div class="app-container">
    <!-- 主布局容器 -->
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="300px" class="left-panel">
        <div class="panel-section">
          <h3>系统状态监控</h3>
          <el-card shadow="hover">
            <status-indicator :status="systemStatus"/>
            <div class="status-info">
              <p>帧率: {{ systemStatus.fps }} FPS</p>
              <p>延迟: {{ systemStatus.latency }}ms</p>
              <p>CPU使用率: {{ systemStatus.cpu }}%</p>
              <p>内存占用: {{ systemStatus.mem }}MB</p>
            </div>
          </el-card>
        </div>

        <div class="panel-section">
          <h3>感知参数控制</h3>
          <el-card shadow="hover">
            <el-form label-width="120px">
              <el-form-item label="检测置信度">
                <el-slider v-model="params.confidence" :min="0" :max="1" :step="0.1"/>
              </el-form-item>
              <el-form-item label="跟踪最大年龄">
                <el-input-number v-model="params.maxAge" :min="1" :max="30"/>
              </el-form-item>
              <el-form-item label="危险预测阈值">
                <el-slider v-model="params.dangerThreshold" :min="0" :max="1" :step="0.1"/>
              </el-form-item>
              <el-form-item label="场景图更新频率">
                <el-select v-model="params.graphFreq">
                  <el-option label="实时" value="realtime"/>
                  <el-option label="1Hz" value="1hz"/>
                  <el-option label="5Hz" value="5hz"/>
                </el-select>
              </el-form-item>
            </el-form>
          </el-card>
        </div>
      </el-aside>

      <!-- 主内容区 -->
      <el-main class="main-content">
        <!-- 第一行：视频显示和3D场景 -->
        <el-row :gutter="20">
          <el-col :span="16">
            <el-card class="video-card">
              <div slot="header" class="card-header">
                <span>实时感知视图</span>
                <el-button-group class="card-actions">
                  <el-button icon="el-icon-refresh" @click="switchVideo"/>
                  <el-button icon="el-icon-download" @click="downloadVideo"/>
                </el-button-group>
              </div>
              <video
                  ref="videoPlayer"
                  class="video-player"
                  autoplay
                  loop
                  muted
                  :src="currentVideo"
              >
                您的浏览器不支持 HTML5 视频
              </video>
            </el-card>
          </el-col>

          <el-col :span="8">
            <el-card class="scene-graph-card">
              <div slot="header" class="card-header">
                <span>动态场景图</span>
                <el-tag type="warning">v1.1</el-tag>
              </div>
              <scene-graph
                  :nodes="sceneGraph.nodes"
                  :edges="sceneGraph.edges"
                  @node-click="handleNodeClick"
              />
            </el-card>
          </el-col>
        </el-row>

        <!-- 第二行：数据可视化 -->
        <el-row :gutter="20" class="data-row">
          <el-col :span="8">
            <el-card class="chart-card">
              <div slot="header" class="card-header">
                <span>目标检测统计</span>
              </div>
              <detection-chart :data="detectionStats"/>
            </el-card>
          </el-col>

          <el-col :span="8">
            <el-card class="chart-card">
              <div slot="header" class="card-header">
                <span>危险预测概率</span>
              </div>
              <danger-radar :data="dangerPredictions"/>
            </el-card>
          </el-col>

          <el-col :span="8">
            <el-card class="chart-card">
              <div slot="header" class="card-header">
                <span>网络架构搜索</span>
                <el-tag type="success">TreeNAS</el-tag>
              </div>
              <nas-architecture :layers="nasLayers"/>
            </el-card>
          </el-col>
        </el-row>

        <!-- 第三行：详细数据表格 -->
        <el-row>
          <el-card class="table-card">
            <div slot="header" class="card-header">
              <span>实时目标列表</span>
              <el-input
                  v-model="trackFilter"
                  placeholder="过滤跟踪目标..."
                  prefix-icon="el-icon-search"
                  style="width:200px"
              />
            </div>
            <track-table
                :data="filteredTracks"
                :current-frame="currentFrame"
                @track-click="handleTrackClick"
            />
          </el-card>
        </el-row>
      </el-main>
    </el-container>

    <!-- 详细信息对话框 -->
    <el-dialog
        :title="selectedNode ? `节点详情 - ${selectedNode.id}` : '对象详情'"
        :visible.sync="detailVisible"
        width="600px"
    >
      <node-detail :data="selectedNode"/>
    </el-dialog>
  </div>
</template>

<script>
// 导入子组件
import SceneGraph from '@/components/SceneGraph'
import DetectionChart from '@/components/DetectionChart'
import DangerRadar from '@/components/DangerRadar'
import NasArchitecture from '@/components/NasArchitecture'
import TrackTable from '@/components/TrackTable'
import NodeDetail from '@/components/NodeDetail'
import StatusIndicator from '@/components/StatusIndicator'

export default {
  components: {
    SceneGraph,
    DetectionChart,
    DangerRadar,
    NasArchitecture,
    TrackTable,
    NodeDetail,
    StatusIndicator
  },

  data() {
    return {
      // 系统状态
      systemStatus: {
        fps: 0,
        latency: 0,
        cpu: 0,
        mem: 0,
        status: 'normal'
      },

      // 控制参数
      params: {
        confidence: 0.6,
        maxAge: 5,
        dangerThreshold: 0.7,
        graphFreq: 'realtime'
      },

      // 视频相关数据
      videoList: [
        require('@/assets/videos/sf.mp4')
      ],
      currentVideoIndex: 0,

      // 感知数据
      detections: [],
      sceneGraph: {
        nodes: [],
        edges: []
      },
      dangerPredictions: {},
      detectionStats: {},

      // 跟踪数据
      tracks: [],
      trackFilter: '',

      // NAS数据
      nasLayers: [],

      // 交互状态
      currentFrame: 0,
      selectedNode: null,
      detailVisible: false
    }
  },

  computed: {
    filteredTracks() {
      return this.tracks.filter(track =>
          track.id.includes(this.trackFilter) ||
          track.class.includes(this.trackFilter)
      )
    },
    currentVideo() {
      return this.videoList[this.currentVideoIndex]
    }
  },

  methods: {
    switchVideo() {
      this.currentVideoIndex = (this.currentVideoIndex + 1) % this.videoList.length
      this.$refs.videoPlayer.load()
      this.$refs.videoPlayer.play()
    },

    downloadVideo() {
      const link = document.createElement('a')
      link.href = this.currentVideo
      link.download = `snapshot_${new Date().getTime()}.avi`
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
    },

    handleFrameProcessed({graphData, stats}) {
      this.sceneGraph = graphData
      this.detectionStats = stats
      this.currentFrame++
    },

    handleNodeClick(node) {
      this.selectedNode = node
      this.detailVisible = true
    },

    handleTrackClick(track) {
      this.selectedNode = this.sceneGraph.nodes.find(n => n.id === track.id)
      this.detailVisible = true
    },

    // 模拟数据生成
    generateMockData() {
      setInterval(() => {
        this.detections = this.generateDetections()
        this.sceneGraph = this.generateSceneGraph()
        this.dangerPredictions = this.generateDangerPrediction()
        this.detectionStats = this.generateStats()
        this.tracks = this.generateTracks()
        this.nasLayers = this.generateNASLayers()
      }, 100)
    },

    generateDetections() {
      const classes = ['bird1', 'bird2', 'bicycle', 'motorcycle', 'truck']
      const colors = ['#ff4d4f', '#1890ff', '#52c41a', '#faad14', '#722ed1']

      return Array.from({length: Math.floor(Math.random()*8)+3}, (_,i) => ({
        id: `det_${this.currentFrame}_${i}`,
        class: classes[Math.floor(Math.random()*classes.length)],
        confidence: Math.random().toFixed(2),
        bbox: [
          Math.random()*1280,
          Math.random()*720,
          Math.random()*1280,
          Math.random()*720
        ],
        color: colors[i%colors.length]
      }))
    },

    generateSceneGraph() {
      const nodes = [
        {
          id: 'ego',
          label: '自车',
          category: '自车',
          x: 640,
          y: 600,
          speed: 10 + Math.random()*5,
          confidence: 0.95
        }
      ]

      Array.from({length: 5}).forEach((_,i) => {
        nodes.push({
          id: `obj_${this.currentFrame}_${i}`,
          label: `对象${i}`,
          category: ['车辆', '行人', '道路'][i%3],
          x: 640 + (Math.random()-0.5)*200,
          y: 360 + (Math.random()-0.5)*200,
          speed: Math.random()*15,
          confidence: Math.random().toFixed(2)
        })
      })

      const edges = []
      nodes.slice(1).forEach(node => {
        edges.push({
          source: 'ego',
          target: node.id,
          relation: ['distance', 'position', 'danger'][Math.floor(Math.random()*3)],
          value: Math.random().toFixed(2)
        })
      })

      return { nodes, edges }
    },

    generateDangerPrediction() {
      return {
        collision: Math.random().toFixed(2),
        rearEnd: Math.random().toFixed(2),
        ghost: Math.random().toFixed(2)
      }
    },

    generateStats() {
      return {
        dorne: Math.random().toFixed(2),
        build: Math.random().toFixed(2),
        tree: Math.random().toFixed(2),
        bird: Math.random().toFixed(2)
      }
    },

    generateTracks() {
      return Array.from({length: 5}, (_,i) => ({
        id: `track_${i}`,
        class: ['dorne', 'bird', 'other'][i%3],
        x: 640 + (Math.random()-0.5)*200,
        y: 360 + (Math.random()-0.5)*200,
        speed: Math.random()*15,
        distance: Math.random()*50,
        dangerLevel: Math.floor(Math.random()*3)+1
      }))
    },

    generateNASLayers() {
      const layerTypes = ['Conv', 'Bottleneck', 'CSP', 'SPP', 'Focus']
      return Array.from({length: 5}, (_,i) => ({
        name: `Layer_${i+1}`,
        type: layerTypes[Math.floor(Math.random()*layerTypes.length)],
        channels: Math.floor(Math.random()*256)+64,
        params: Math.floor(Math.random()*500)+100,
        blocks: Array.from({length: 3}, (_,j) => ({
          name: `Block_${j+1}`,
          type: ['MBConv', 'Residual', 'Dense'][j%3],
          channels: Math.floor(Math.random()*128)+32,
          params: Math.floor(Math.random()*100)+20
        }))
      }))
    }
  },

  mounted() {
    this.generateMockData()
    // 确保视频自动播放
    this.$nextTick(() => {
      const video = this.$refs.videoPlayer
      video.play().catch(error => {
        console.log('自动播放被阻止:', error)
      })
    })
  }
}
</script>

<style lang="scss">
.app-container {
  height: 100vh;
  background: #f0f2f5;

  .left-panel {
    background: #fff;
    padding: 20px;
    border-right: 1px solid #ebeef5;

    .panel-section {
      margin-bottom: 20px;

      h3 {
        color: #606266;
        margin-bottom: 10px;
      }
    }
  }

  .main-content {
    padding: 20px;

    .video-card {
      height: 500px;

      .video-player {
        width: 100%;
        height: 450px;
        background-color: #000;
        border-radius: 4px;
        object-fit: contain;
        /* 隐藏控制条 */
        &::-webkit-media-controls {
          display: none !important;
        }
        &::-webkit-media-controls-enclosure {
          display: none !important;
        }
      }
    }

    .scene-graph-card {
      height: 500px;
    }

    .chart-card {
      height: 300px;
    }

    .table-card {
      margin-top: 20px;
    }

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }
}

.status-info {
  p {
    margin: 8px 0;
    color: #909399;
    font-size: 14px;
  }
}
</style>