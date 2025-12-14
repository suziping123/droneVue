<template>
  <div class="app-container">
    <el-container>
      <!-- 侧边导航 -->
      <el-aside width="280px" class="nav-panel">
        <div class="system-status">
          <h3 class="panel-title">飞行状态总览</h3>
          <status-panel :status="systemStatus"/>
        </div>

        <el-menu
            :default-active="activeNav"
            class="sensor-menu"
            @select="handleNavSelect"
        >
          <el-menu-item index="camera">
            <i class="el-icon-camera"></i>
            <span>视觉感知系统</span>
          </el-menu-item>
          <el-menu-item index="lidar">
            <i class="el-icon-map-location"></i>
            <span>激光雷达系统</span>
          </el-menu-item>
          <el-menu-item index="radar">
            <i class="el-icon-data-line"></i>
            <span>毫米波雷达系统</span>
          </el-menu-item>
          <el-menu-item index="navigation">
            <i class="el-icon-place"></i>
            <span>导航定位系统</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 主内容区 -->
      <el-main class="main-content">
        <!-- 传感器数据总览 -->
        <el-row :gutter="20">
          <el-col :span="24">
            <sensor-status-panel :sensors="sensorStatus"/>
          </el-col>
        </el-row>

        <!-- 动态内容区 -->
        <component :is="activeComponent" class="dynamic-panel"/>

        <!-- 数据预处理看板 -->
        <el-card class="preprocessing-board">
          <el-tabs v-model="preprocessTab">
            <el-tab-pane label="数据同步" name="sync">
              <sync-timeline :timestamps="syncData"/>
            </el-tab-pane>
            <el-tab-pane label="数据校正" name="calibration">
              <calibration-comparison :rawImage="rawImage" :processedImage="processedImage"/>
            </el-tab-pane>
            <el-tab-pane label="数据融合" name="fusion">
              <fusion-3d-scene :points="fusionData"/>
            </el-tab-pane>
          </el-tabs>
        </el-card>

        <!-- 处理分析看板 -->
        <el-row :gutter="20" class="analysis-row">
          <el-col :span="8">
            <data-cleaning-panel :stats="cleaningStats"/>
          </el-col>
          <el-col :span="8">
            <feature-extraction-chart :features="featureData"/>
          </el-col>
          <el-col :span="8">
            <fusion-analysis-radar :data="fusionAnalysis"/>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script>
// 导入子组件
import StatusPanel from '@/components/1/StatusPanel'
import SensorStatusPanel from '@/components/1/SensorStatusPanel'
import CameraSystem from '@/components/1/CameraSystem'
import LidarSystem from '@/components/1/LidarSystem'
import RadarSystem from '@/components/1/RadarSystem'
import NavigationSystem from '@/components/1/NavigationSystem'
import SyncTimeline from '@/components/1/SyncTimeline'
import CalibrationComparison from '@/components/1/CalibrationComparison'
import Fusion3dScene from '@/components/1/Fusion3dScene'
import DataCleaningPanel from '@/components/1/DataCleaningPanel'
import FeatureExtractionChart from '@/components/1/FeatureExtractionChart'
import FusionAnalysisRadar from '@/components/1/FusionAnalysisRadar'

export default {
  components: {
    StatusPanel,
    SensorStatusPanel,
    CameraSystem,
    LidarSystem,
    RadarSystem,
    NavigationSystem,
    SyncTimeline,
    CalibrationComparison,
    Fusion3dScene,
    DataCleaningPanel,
    FeatureExtractionChart,
    FusionAnalysisRadar
  },

  data() {
    return {
      activeNav: 'camera',
      preprocessTab: 'sync',
      systemStatus: {
        altitude: 0,
        speed: 0,
        pitch: 0,
        roll: 0,
        yaw: 0,
        temperature: 0,
        battery: 100
      },
      sensorStatus: [],
      syncData: [],
      rawImage: require('@/assets/images/suanfa.jpg'),
      processedImage: require('@/assets/images/processed_sample.jpg'),
      fusionData: [],
      cleaningStats: {},
      featureData: {},
      fusionAnalysis: {},
      componentsMap: {
        camera: 'CameraSystem',
        lidar: 'LidarSystem',
        radar: 'RadarSystem',
        navigation: 'NavigationSystem'
      }
    }
  },

  computed: {
    activeComponent() {
      return this.componentsMap[this.activeNav]
    }
  },

  methods: {
    handleNavSelect(key) {
      this.activeNav = key
    },

    generateSensorData() {
      // 生成传感器状态数据
      this.sensorStatus = [
        { name: '主摄像头', type: 'camera', status: 'normal', fps: 30 },
        { name: 'LiDAR', type: 'lidar', status: 'warning', points: 160000 },
        { name: '前向雷达', type: 'radar', status: 'error', targets: 5 },
        { name: 'GPS', type: 'gps', status: 'normal', satellites: 12 }
      ]
    },

    generateSystemStatus() {
      // 生成系统状态数据
      this.systemStatus = {
        altitude: 150 + Math.random() * 50,
        speed: 25 + Math.random() * 10,
        pitch: Math.random() * 10 - 5,
        roll: Math.random() * 8 - 4,
        yaw: Math.random() * 360,
        temperature: 40 + Math.random() * 20,
        battery: 100 - Math.random() * 5
      }
    },

    generateSyncData() {
      // 生成同步时间戳数据
      const now = Date.now()
      this.syncData = [
        { sensor: 'Camera', timestamp: now - 50 },
        { sensor: 'LiDAR', timestamp: now - 48 },
        { sensor: 'Radar', timestamp: now - 52 },
        { sensor: 'IMU', timestamp: now - 49 }
      ]
    },

    generateFusionData() {
      // 生成融合点云数据
      this.fusionData = Array.from({length: 500}, () => ({
        x: Math.random() * 100 - 50,
        y: Math.random() * 100 - 50,
        z: Math.random() * 30,
        value: Math.random() * 100
      }))
    },

    generateAnalysisData() {
      // 生成分析数据
      this.cleaningStats = {
        rawPoints: 250000,
        cleanPoints: 245000,
        outliers: 5000
      }

      this.featureData = {
        dimensions: ['尺寸', '速度', '距离', '高度', '强度'],
        data: [
          {
            name: '特征分布',
            values: Array.from({length: 5}, () => Math.random() * 100)
          }
        ]
      }

      this.fusionAnalysis = {
        indicators: ['视觉', 'LiDAR', '雷达', '定位'],
        values: [
          { name: '置信度', data: Array.from({length: 4}, () => Math.random()) }
        ]
      }
    },

    startDataSimulation() {
      setInterval(() => {
        this.generateSensorData()
        this.generateSystemStatus()
        this.generateSyncData()
        this.generateFusionData()
        this.generateAnalysisData()
      }, 1000)
    }
  },

  mounted() {
    this.startDataSimulation()
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  height: 100vh;
  background: #f5f7fa;

  .nav-panel {
    background: #fff;
    border-right: 1px solid #ebeef5;
    padding: 20px;

    .panel-title {
      color: #303133;
      margin-bottom: 15px;
    }

    .sensor-menu {
      border-right: none;
    }
  }

  .main-content {
    .preprocessing-board {
      margin-top: 20px;

      /* 推荐写法一：带作用域包裹 */
      ::v-deep {
        .el-tabs__header {
          margin: 0;

          .el-tabs__nav-wrap {
            padding: 0 20px;
          }
        }
      }
    }
  }
}
</style>