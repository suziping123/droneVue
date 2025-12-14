<!-- InstructionFlow.vue -->
<template>
  <el-card class="instruction-flow">
    <div class="flow-header">
      <h4>指令执行流水线</h4>
      <el-button
          size="mini"
          icon="el-icon-refresh"
          @click="toggleAutoScroll"
      >
        {{ autoScroll ? '暂停' : '自动' }}
      </el-button>
    </div>

    <div ref="scrollContainer" class="scroll-container">
      <el-timeline>
        <el-timeline-item
            v-for="(cmd, index) in commands"
            :key="cmd.timestamp"
            :timestamp="formatTime(cmd.timestamp)"
            placement="top"
        >
          <el-card shadow="hover">
            <div class="command-item">
              <div class="command-type">
                <el-tag :type="getCommandType(cmd.type)">
                  {{ cmd.type }}
                </el-tag>
              </div>
              <div class="command-content">
                <div class="command-title">{{ cmd.title }}</div>
                <div class="command-status">
                  <i :class="statusIcon(cmd.status)"></i>
                  {{ cmd.status }}
                </div>
              </div>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </div>
  </el-card>
</template>

<script>
export default {
  props: {
    commands: Array
  },

  data() {
    return {
      autoScroll: true,
      scrollInterval: null
    }
  },

  watch: {
    commands() {
      if (this.autoScroll) {
        this.$nextTick(() => {
          this.scrollToBottom()
        })
      }
    }
  },

  mounted() {
    this.startAutoScroll()
  },

  beforeDestroy() {
    this.stopAutoScroll()
  },

  methods: {
    formatTime(timestamp) {
      return new Date(timestamp).toLocaleTimeString()
    },

    getCommandType(type) {
      const typeMap = {
        navigation: 'success',
        control: 'warning',
        emergency: 'danger'
      }
      return typeMap[type] || 'info'
    },

    statusIcon(status) {
      return {
        pending: 'el-icon-time',
        executing: 'el-icon-loading',
        completed: 'el-icon-success',
        failed: 'el-icon-error'
      }[status]
    },

    toggleAutoScroll() {
      this.autoScroll = !this.autoScroll
      if (this.autoScroll) {
        this.startAutoScroll()
      } else {
        this.stopAutoScroll()
      }
    },

    startAutoScroll() {
      this.scrollInterval = setInterval(() => {
        this.scrollToBottom()
      }, 3000)
    },

    stopAutoScroll() {
      clearInterval(this.scrollInterval)
    },

    scrollToBottom() {
      const container = this.$refs.scrollContainer
      container.scrollTop = container.scrollHeight
    }
  }
}
</script>

<style lang="scss" scoped>
.instruction-flow {
  height: 100%;

  .flow-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
  }

  .scroll-container {
    height: 300px;
    overflow-y: auto;

    .command-item {
      display: flex;
      align-items: center;
      gap: 15px;

      .command-content {
        flex: 1;

        .command-title {
          font-weight: bold;
          margin-bottom: 4px;
        }

        .command-status {
          color: #606266;
          font-size: 12px;

          i {
            margin-right: 5px;
          }
        }
      }
    }
  }

  ::v-deep .el-timeline {
    padding-left: 20px;

    .el-timeline-item__timestamp {
      color: #909399;
      font-size: 12px;
    }
  }
}
</style>