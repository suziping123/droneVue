<!-- AlertSystem.vue -->
<template>
  <div class="alert-system">
    <div class="alert-toolbar">
      <el-input
          v-model="searchText"
          placeholder="搜索预警信息..."
          prefix-icon="el-icon-search"
          style="width: 200px"
      ></el-input>
      <el-select
          v-model="selectedLevel"
          placeholder="筛选等级"
          clearable
      >
        <el-option
            v-for="level in levels"
            :key="level.value"
            :label="level.label"
            :value="level.value"
        ></el-option>
      </el-select>
    </div>

    <el-table
        :data="filteredAlerts"
        height="250"
        style="width: 100%"
        @row-click="showAlertDetail"
    >
      <el-table-column prop="time" label="时间" width="180"></el-table-column>
      <el-table-column prop="type" label="类型" width="120"></el-table-column>
      <el-table-column label="等级" width="120">
        <template #default="{row}">
          <div class="alert-level" :class="levelClass(row.level)">
            {{ row.level }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
    </el-table>

    <el-dialog
        :title="selectedAlert?.type"
        :visible.sync="detailVisible"
        width="800px"
    >
      <alert-detail :data="selectedAlert"/>
    </el-dialog>
  </div>
</template>

<script>
import AlertDetail from './AlertDetail'

export default {
  components: { AlertDetail },

  data() {
    return {
      searchText: '',
      selectedLevel: null,
      selectedAlert: null,
      detailVisible: false,
      levels: [
        { value: 1, label: '一级预警' },
        { value: 2, label: '二级预警' },
        { value: 3, label: '三级预警' }
      ]
    }
  },

  computed: {
    filteredAlerts() {
      return this.$store.state.alerts.filter(alert => {
        const matchText = alert.description.includes(this.searchText)
        const matchLevel = this.selectedLevel ?
            alert.level === this.selectedLevel : true
        return matchText && matchLevel
      })
    }
  },

  methods: {
    levelClass(level) {
      return {
        'level-1': level === 1,
        'level-2': level === 2,
        'level-3': level === 3
      }
    },

    showAlertDetail(alert) {
      this.selectedAlert = alert
      this.detailVisible = true
    }
  }
}
</script>

<style lang="scss" scoped>
.alert-system {
  padding: 20px;

  .alert-toolbar {
    display: flex;
    gap: 15px;
    margin-bottom: 15px;
  }

  .alert-level {
    padding: 5px 10px;
    border-radius: 4px;
    text-align: center;
    font-weight: bold;

    &.level-1 {
      background: #ffeb3b;
      color: #ff9800;
    }

    &.level-2 {
      background: #ff9800;
      color: #fff;
    }

    &.level-3 {
      background: #f44336;
      color: #fff;
    }
  }
}
</style>