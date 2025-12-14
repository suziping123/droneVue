<!-- TrackTable.vue -->
<template>
  <el-table
      :data="data"
      style="width: 100%"
      height="400"
      stripe
      @row-click="handleRowClick"
  >
    <el-table-column prop="id" label="ID" width="100">
      <template #default="{row}">
        <el-tag :type="row.type === 'vehicle' ? 'primary' : 'success'">
          {{ row.id }}
        </el-tag>
      </template>
    </el-table-column>

    <el-table-column prop="class" label="类别" width="120">
      <template #default="{row}">
        <div class="class-cell">
          <i :class="getClassIcon(row.class)"></i>
          {{ row.class }}
        </div>
      </template>
    </el-table-column>

    <el-table-column label="位置">
      <template #default="{row}">
        <div class="position-cell">
          <span class="coordinate">({{ row.x.toFixed(1) }}, {{ row.y.toFixed(1) }})</span>
          <el-progress
              :percentage="row.distance / 50 * 100"
              :show-text="false"
              :stroke-width="6"
              :color="getDistanceColor(row.distance)"
          />
        </div>
      </template>
    </el-table-column>

    <el-table-column prop="speed" label="速度" width="120">
      <template #default="{row}">
        <div class="speed-cell">
          <el-tag :type="row.speed > 5 ? 'danger' : 'info'">
            {{ row.speed.toFixed(1) }} m/s
          </el-tag>
        </div>
      </template>
    </el-table-column>

    <el-table-column label="危险度" width="150">
      <template #default="{row}">
        <el-rate
            v-model="row.dangerLevel"
            disabled
            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
            :max="3"
        />
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
export default {
  props: {
    data: Array,
    currentFrame: Number
  },

  methods: {
    handleRowClick(row) {
      this.$emit('track-click', row)
    },

    getClassIcon(className) {
      const icons = {
        'dorne': 'el-icon-Place',
        'build': 'el-icon-user1',
        'tree': 'el-icon-bicycle1',
        'bird': 'el-icon-bicycle1',
        'other':'1'
      }
      return icons[className] || 'el-icon-question'
    },

    getDistanceColor(distance) {
      if (distance < 5) return '#ff4d4f'
      if (distance < 10) return '#faad14'
      return '#52c41a'
    }
  }
}
</script>

<style scoped>
.class-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.position-cell {
  display: flex;
  flex-direction: column;
  gap: 4px;

  .coordinate {
    font-family: monospace;
    color: #606266;
  }
}

.speed-cell {
  display: flex;
  justify-content: flex-start;
}

::v-deep .el-progress-bar {
  padding-right: 0;
  margin-right: 0;
}
</style>