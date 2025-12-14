<!-- FlightMap.vue -->
<template>
  <div ref="mapContainer" class="flight-map-container">
    <div class="map-controls">
      <el-button-group>
        <el-button icon="el-icon-zoom-in" @click="zoomIn"></el-button>
        <el-button icon="el-icon-zoom-out" @click="zoomOut"></el-button>
        <el-button icon="el-icon-refresh" @click="resetView"></el-button>
      </el-button-group>
    </div>
  </div>
</template>

<script>
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

export default {
  props: {
    path: Array,
    dangerZones: Array
  },

  data() {
    return {
      map: null,
      pathLayer: null,
      dangerLayers: []
    }
  },

  watch: {
    path: 'updatePath',
    dangerZones: 'updateDangerZones'
  },

  mounted() {
    this.initMap()
    this.updatePath()
    this.updateDangerZones()
  },

  methods: {
    initMap() {
      this.map = L.map(this.$refs.mapContainer, {
        center: [31.2304, 121.4737],
        zoom: 12
      })

      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(this.map)
    },

    updatePath() {
      if (this.pathLayer) {
        this.map.removeLayer(this.pathLayer)
      }

      const latLngs = this.path.map(p => [p.lat, p.lng])
      this.pathLayer = L.polyline(latLngs, {
        color: '#1890ff',
        weight: 3
      }).addTo(this.map)
    },

    updateDangerZones() {
      this.dangerLayers.forEach(layer => this.map.removeLayer(layer))
      this.dangerLayers = this.dangerZones.map(zone => {
        return L.circle([zone.position[0], zone.position[1]], {
          color: this.getZoneColor(zone.level),
          radius: zone.radius
        }).addTo(this.map)
      })
    },

    getZoneColor(level) {
      switch(level) {
        case 1: return '#ffeb3b'
        case 2: return '#ff9800'
        case 3: return '#f44336'
        default: return '#9e9e9e'
      }
    },

    zoomIn() {
      this.map.zoomIn()
    },

    zoomOut() {
      this.map.zoomOut()
    },

    resetView() {
      this.map.setView([31.2304, 121.4737], 12)
    }
  }
}
</script>

<style scoped>
.flight-map-container {
  width: 100%;
  height: 100%;
  position: relative;

  .map-controls {
    position: absolute;
    top: 20px;
    left: 20px;
    z-index: 1000;
    background: rgba(255,255,255,0.8);
    padding: 10px;
    border-radius: 4px;
  }
}
</style>