<template>
  <el-card>
    <el-button type="primary" @click="addWaypoint">添加航点</el-button>
    <el-button type="danger" @click="clearRoute">清除路线</el-button>
  </el-card>
</template>

<script>
export default {
  methods: {
    addWaypoint() {
      this.$emit('add-waypoint', { id: Date.now(), lat: 51.505, lng: -0.09 });
    },
    clearRoute() {
      this.$emit('clear-route');
    }
  }
};
</script>

<style scoped>
.el-card {
  margin-bottom: 20px;
}
</style>