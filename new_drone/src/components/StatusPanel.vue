<template>
  <el-card>
    <h3>状态信息</h3>
    <p>总航点数量: {{ waypoints.length }}</p>
    <p>总距离: {{ totalDistance.toFixed(2) }} km</p>
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
  computed: {
    totalDistance() {
      let distance = 0;
      for (let i = 1; i < this.waypoints.length; i++) {
        const prev = this.waypoints[i - 1];
        const current = this.waypoints[i];
        distance += this.calculateDistance(prev.lat, prev.lng, current.lat, current.lng);
      }
      return distance;
    }
  },
  methods: {
    calculateDistance(lat1, lng1, lat2, lng2) {
      const R = 6371; // Radius of the earth in km
      const dLat = this.deg2rad(lat2 - lat1);
      const dLng = this.deg2rad(lng2 - lng1);
      const a =
          Math.sin(dLat / 2) * Math.sin(dLat / 2) +
          Math.cos(this.deg2rad(lat1)) * Math.cos(this.deg2rad(lat2)) *
          Math.sin(dLng / 2) * Math.sin(dLng / 2);
      const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
      return R * c;
    },
    deg2rad(deg) {
      return deg * (Math.PI / 180);
    }
  }
};
</script>

<style scoped>
.el-card {
  margin-top: 20px;
}
</style>