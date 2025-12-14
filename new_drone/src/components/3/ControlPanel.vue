<template>
  <div class="control-panel">
    <el-form label-width="100px">
      <el-form-item label="响应速度">
        <el-slider v-model="params.responseSpeed"></el-slider>
      </el-form-item>

      <el-form-item label="最大高度">
        <el-input-number v-model="params.maxAltitude" :min="500" :max="3000"></el-input-number>
      </el-form-item>

      <el-button-group class="control-buttons">
        <el-button type="primary" @click="sendCommand('speedUp')">加速</el-button>
        <el-button type="warning" @click="sendCommand('slowDown')">减速</el-button>
        <el-button type="success" @click="sendCommand('altitudeUp')">爬升</el-button>
        <el-button type="info" @click="sendCommand('altitudeDown')">下降</el-button>
      </el-button-group>
    </el-form>
  </div>
</template>

<script>
export default {
  props: ['controlParams'],
  data() {
    return {
      params: {...this.controlParams}
    }
  },
  methods: {
    sendCommand(type) {
      this.$emit('command-send', {
        type,
        params: this.params
      })
    }
  }
}
</script>