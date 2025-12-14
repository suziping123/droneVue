<!-- CalibrationComparison.vue -->
<template>
  <el-row :gutter="20">
    <el-col :span="12">
      <div class="image-box">
        <h4>原始图像</h4>
        <img :src="rawImage" class="comparison-image">
      </div>
    </el-col>
    <el-col :span="12">
      <div class="image-box">
        <h4>处理后图像</h4>
        <img :src="processedImage" class="comparison-image">
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  props: {
    rawImage: String,
    processedImage: String
  }
}
</script>

<style scoped>
.image-box {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 10px;

  h4 {
    margin: 0 0 10px 0;
    color: #606266;
  }
}

.comparison-image {
  width: 100%;
  height: 300px;
  object-fit: contain;
  background: #f0f2f5;
}
</style>