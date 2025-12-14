<template>
  <div>
    <!-- 搜索框 -->
    <el-card shadow="hover" style="margin-bottom: 5px" :body-style="{ padding: '10px' }">
      <el-input
          v-model="searchKeyword"
          placeholder="请输入用户名"
          style="width: 300px; margin-right: 10px"
          clearable
          size="small"
      >
        <el-button slot="append" icon="el-icon-search" @click="handleSearch" size="small"></el-button>
      </el-input>
      <el-button type="info" @click="handleReset" size="small">重置</el-button>
    </el-card>

    <!-- 操作按钮 -->
    <el-card shadow="hover" style="margin-bottom: 5px" :body-style="{ padding: '10px' }">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd" size="small">新增</el-button>
      <el-button type="danger" icon="el-icon-delete" @click="handleBatchDelete" size="small">批量删除</el-button>
      <el-button type="warning" icon="el-icon-upload2" @click="handleImport" size="small">导入</el-button>
      <el-button type="success" icon="el-icon-download" @click="handleExport" size="small">导出</el-button>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="hover" :body-style="{ padding: '10px' }">
      <el-table
          :data="paginatedUsers"
          style="width: 100%"
          @selection-change="handleSelectionChange"
          stripe
          border
          fit
      >
        <!-- 选择列 -->
        <el-table-column type="selection" width="55"></el-table-column>

        <!-- 用户名列 -->
        <el-table-column prop="username" label="用户名"></el-table-column>

        <!-- 角色列 -->
        <el-table-column prop="role" label="角色"></el-table-column>

        <el-table-column prop="role2" label="工号"></el-table-column>
        <el-table-column prop="role3" label="部门"></el-table-column>

        <!-- 操作列 -->
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button type="text" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
          style="margin-top: 10px; text-align: right"
          background
          layout="total, prev, pager, next, jumper"
          :total="filteredUsers.length"
          :page-size="pageSize"
          :current-page="currentPage"
          @current-change="handlePageChange"
      ></el-pagination>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="30%">
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role" placeholder="请选择角色">
            <el-option label="管理员" value="管理员"></el-option>
            <el-option label="普通用户" value="普通用户"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleConfirm">确认</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchKeyword: '', // 搜索关键字
      users: [
        {id: 1, username: 'admin', role: '管理员'},
        {id: 2, username: 'user1', role: '普通用户'},
        {id: 3, username: 'user2', role: '普通用户'},
        {id: 4, username: 'user3', role: '普通用户'},
        {id: 5, username: 'user4', role: '普通用户'},
        {id: 6, username: 'user5', role: '普通用户'},
        {id: 7, username: 'user6', role: '普通用户'},
        {id: 8, username: 'user7', role: '普通用户'},
        {id: 9, username: 'user8', role: '普通用户'},
        {id: 10, username: 'user9', role: '普通用户'},
        {id: 11, username: 'user10', role: '普通用户'},
        {id: 12, username: 'user11', role: '普通用户'},
        {id: 13, username: 'user12', role: '普通用户'},
        {id: 14, username: 'user13', role: '普通用户'},
        {id: 15, username: 'user14', role: '普通用户'},
      ], // 用户数据
      selectedUsers: [], // 选中的用户
      currentPage: 1, // 当前页码
      pageSize: 9, // 每页显示条数
      dialogVisible: false, // 对话框是否显示
      dialogTitle: '新增用户', // 对话框标题
      form: {id: null, username: '', role: ''}, // 表单数据
      isEdit: false, // 是否为编辑模式
    };
  },
  computed: {
    // 过滤后的用户数据
    filteredUsers() {
      return this.users.filter(user =>
          user.username.includes(this.searchKeyword))
    },
    // 分页后的用户数据
    paginatedUsers() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredUsers.slice(start, end);
    },
  },
  methods: {
    // 搜索
    handleSearch() {
      this.currentPage = 1; // 搜索后重置到第一页
    },
    // 重置
    handleReset() {
      this.searchKeyword = '';
      this.currentPage = 1;
    },
    // 新增
    handleAdd() {
      this.dialogTitle = '新增用户';
      this.form = {id: null, username: '', role: ''};
      this.isEdit = false;
      this.dialogVisible = true;
    },
    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑用户';
      this.form = {...row};
      this.isEdit = true;
      this.dialogVisible = true;
    },
    // 删除
    handleDelete(row) {
      this.$confirm('确定删除该用户吗？', '提示', {
        type: 'warning',
      }).then(() => {
        this.users = this.users.filter(user => user.id !== row.id);
        this.$message.success('删除成功！');
      });
    },
    // 批量删除
    handleBatchDelete() {
      if (this.selectedUsers.length === 0) {
        this.$message.warning('请选择要删除的用户！');
        return;
      }
      this.$confirm('确定删除选中的用户吗？', '提示', {
        type: 'warning',
      }).then(() => {
        this.users = this.users.filter(user => !this.selectedUsers.includes(user));
        this.$message.success('删除成功！');
      });
    },
    // 导入
    handleImport() {
      this.$message.info('导入功能待实现');
    },
    // 导出
    handleExport() {
      this.$message.info('导出功能待实现');
    },
    // 确认新增/编辑
    handleConfirm() {
      if (this.isEdit) {
        // 编辑逻辑
        const index = this.users.findIndex(user => user.id === this.form.id);
        this.users.splice(index, 1, {...this.form});
        this.$message.success('编辑成功！');
      } else {
        // 新增逻辑
        this.form.id = this.users.length + 1;
        this.users.push({...this.form});
        this.$message.success('新增成功！');
      }
      this.dialogVisible = false;
    },
    // 分页变化
    handlePageChange(page) {
      this.currentPage = page;
    },
    // 选中行变化
    handleSelectionChange(selection) {
      this.selectedUsers = selection;
    },
  },
};
</script>

<style scoped>
.el-card {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
}

.el-table {
  margin-top: 5px;
  width: 100%;
}

.el-pagination {
  margin-top: 10px;
}

/* 自定义“前往”按钮的点击样式 */
.el-pagination .btn-quicknext,
.el-pagination .btn-quickprev {
  transition: background-color 0.3s ease;
}

.el-pagination .btn-quicknext:active,
.el-pagination .btn-quickprev:active {
  background-color: #409EFF; /* 点击时的背景色 */
  color: #fff; /* 点击时的文字颜色 */
}
</style>