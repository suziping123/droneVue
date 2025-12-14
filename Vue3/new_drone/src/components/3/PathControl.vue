<template>
  <div class="path-control">
    <el-form label-width="120px">
      <el-form-item label="避险策略">
        <el-select v-model="strategy">
          <el-option label="A*算法" value="A*"></el-option>
          <el-option label="RRT算法" value="RRT"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="安全距离">
        <el-slider v-model="safetyMargin" :min="20" :max="100"></el-slider>
      </el-form-item>

      <el-button type="primary" @click="handleReplan">重新规划路径</el-button>
    </el-form>

    <div class="path-visual">
      <h4>路径对比</h4>
      <div class="path-compare">
        <div class="path-original">
          <span>原始路径</span>
          <div class="path-line" :style="originalStyle"></div>
        </div>
        <div class="path-adjusted">
          <span>调整后路径</span>
          <div class="path-line" :style="adjustedStyle"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ['currentPath', 'adjustedPath'],
  data() {
    return {
      strategy: 'A*',
      safetyMargin: 50
    }
  },
  computed: {
    originalStyle() {
      return {
        backgroundColor: '#409EFF',
        width: '100%',
        height: '2px'
      }
    },
    adjustedStyle() {
      return {
        backgroundColor: '#67C23A',
        width: '100%',
        height: '2px'
      }
    }
  },
  methods: {
    handleReplan() {
      this.$emit('replan', {
        strategy: this.strategy,
        safetyMargin: this.safetyMargin
      })
    }
  }
}
</script>