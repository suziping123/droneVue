<template>
  <el-card>
    <el-table :data="waypoints" style="width: 100%">
      <el-table-column prop="id" label="ID"></el-table-column>
      <el-table-column prop="lat" label="纬度"></el-table-column>
      <el-table-column prop="lng" label="经度"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="danger" size="mini" @click="removeWaypoint(scope.$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script>
export default {
  props: {
    waypoints: {
      type: Array,
      required: true
    }
  },
  methods: {
    removeWaypoint(index) {
      this.$emit('remove-waypoint', index);
    }
  }
};
</script>

<style scoped>
.el-card {
  margin-bottom: 20px;
}
</style>