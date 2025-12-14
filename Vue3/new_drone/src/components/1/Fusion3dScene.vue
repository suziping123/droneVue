<!-- Fusion3dScene.vue -->
<template>
  <div ref="scene" class="fusion-scene"></div>
</template>

<script>
import * as THREE from 'three'
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls'

export default {
  props: {
    points: Array
  },

  data() {
    return {
      scene: null,
      camera: null,
      renderer: null,
      controls: null
    }
  },

  mounted() {
    this.initScene()
    this.animate()
  },

  methods: {
    initScene() {
      // 场景初始化
      this.scene = new THREE.Scene()
      this.camera = new THREE.PerspectiveCamera(
          75,
          this.$refs.scene.offsetWidth / this.$refs.scene.offsetHeight,
          0.1,
          1000
      )

      this.renderer = new THREE.WebGLRenderer({ antialias: true })
      this.renderer.setSize(
          this.$refs.scene.offsetWidth,
          this.$refs.scene.offsetHeight
      )
      this.$refs.scene.appendChild(this.renderer.domElement)

      // 添加光源
      const light = new THREE.DirectionalLight(0xffffff, 1)
      light.position.set(0, 10, 10)
      this.scene.add(light)
      this.scene.add(new THREE.AmbientLight(0x404040))

      // 添加坐标轴
      const axesHelper = new THREE.AxesHelper(20)
      this.scene.add(axesHelper)

      // 设置相机位置
      this.camera.position.z = 50
      this.controls = new OrbitControls(this.camera, this.renderer.domElement)
    },

    updatePoints() {
      // 清除旧点云
      this.scene.children = this.scene.children.filter(
          obj => obj.type !== 'Points'
      )

      // 创建新点云
      const geometry = new THREE.BufferGeometry()
      const positions = new Float32Array(this.points.length * 3)

      this.points.forEach((point, i) => {
        positions[i*3] = point.x
        positions[i*3+1] = point.y
        positions[i*3+2] = point.z
      })

      geometry.setAttribute(
          'position',
          new THREE.BufferAttribute(positions, 3)
      )

      const material = new THREE.PointsMaterial({
        color: 0x1890ff,
        size: 0.1
      })

      const pointCloud = new THREE.Points(geometry, material)
      this.scene.add(pointCloud)
    },

    animate() {
      requestAnimationFrame(this.animate)
      this.controls.update()
      this.renderer.render(this.scene, this.camera)
    }
  },

  watch: {
    points: {
      handler: 'updatePoints',
      deep: true
    }
  }
}
</script>

<style scoped>
.fusion-scene {
  width: 100%;
  height: 500px;
  background: #000;
}
</style>