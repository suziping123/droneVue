<!-- ControlSystem.vue -->
<template>
  <div class="control-system">
    <el-table
        :data="commands"
        height="250"
        stripe
        @row-click="showCommandDetail"
    >
      <el-table-column prop="id" label="指令ID" width="120"></el-table-column>
      <el-table-column label="指令类型" width="120">
        <template #default="{row}">
          <el-tag :type="commandTypeTag(row.type)">
            {{ row.type }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="指令内容"></el-table-column>
      <el-table-column label="状态" width="120">
        <template #default="{row}">
          <el-tag
              :type="statusType(row.status)"
              effect="dark"
          >
            {{ row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="timestamp" label="时间" width="180"></el-table-column>
    </el-table>

    <el-dialog
        :title="selectedCommand?.id"
        :visible.sync="detailVisible"
        width="600px"
    >
      <command-detail :data="selectedCommand"/>
    </el-dialog>
  </div>
</template>

<script>
import CommandDetail from './CommandDetail'

export default {
  components: { CommandDetail },

  data() {
    return {
      commands: [
        {
          id: 'CMD1001',
          type: '航向调整',
          description: '调整航向至235度',
          status: '已完成',
          timestamp: '2023-04-15 14:30',
          parameters: {
            targetHeading: 235,
            adjustmentMode: "平滑转向",
            duration: "30秒"
          }
        },
        {
          id: 'CMD1002',
          type: '速度控制',
          description: '全速前进（22节）',
          status: '执行中',
          timestamp: '2023-04-15 14:35',
          parameters: {
            targetSpeed: 22,
            acceleration: "缓加速",
            engineOutput: "85%"
          }
        },
        {
          id: 'CMD1003',
          type: '紧急规避',
          description: '左满舵避让障碍物',
          status: '已取消',
          timestamp: '2023-04-15 14:40',
          parameters: {
            reason: "前方不明漂浮物",
            maneuverType: "紧急左转",
            priority: "最高"
          }
        },
        {
          id: 'CMD1004',
          type: '速度控制',
          description: '减速至10节',
          status: '待执行',
          timestamp: '2023-04-15 14:45',
          parameters: {
            targetSpeed: 10,
            deceleration: "紧急制动",
            engineOutput: "25%"
          }
        },
        {
          id: 'CMD1005',
          type: '航向调整',
          description: '修正航向至180度',
          status: '已完成',
          timestamp: '2023-04-15 14:50',
          parameters: {
            targetHeading: 180,
            adjustmentMode: "直接转向",
            duration: "立即"
          }
        }
      ],
      selectedCommand: null,
      detailVisible: false
    }
  },

  methods: {
    commandTypeTag(type) {
      const typeMap = {
        '航向调整': 'success',
        '速度控制': 'warning',
        '紧急规避': 'danger'
      }
      return typeMap[type] || 'info'
    },

    statusType(status) {
      const statusMap = {
        '待执行': 'info',
        '执行中': 'warning',
        '已完成': 'success',
        '已取消': 'danger'
      }
      return statusMap[status] || ''
    },

    showCommandDetail(command) {
      this.selectedCommand = command
      this.detailVisible = true
    }
  }
}
</script>

<style lang="scss" scoped>
.control-system {
  padding: 20px;

  ::v-deep .el-table {
    .cell {
      padding: 0 8px;
    }

    .el-tag {
      margin: 2px 0;
    }
  }
}
</style>