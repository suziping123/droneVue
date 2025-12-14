<template>
  <div class="data-table">
    <h4>{{ title }}</h4>
    <el-table
        :data="data"
        height="250"
        border
        style="width: 100%"
    >
      <el-table-column
          v-for="col in columns"
          :key="col.prop"
          :prop="col.prop"
          :label="col.label"
          :width="col.width"
      >
        <template #default="{row}">
          <template v-if="col.formatter">
            {{ col.formatter(row[col.prop]) }}
          </template>
          <template v-else>
            {{ row[col.prop] }}
          </template>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  props: {
    title: String,
    columns: Array,
    data: Array
  }
}
</script>

<style scoped>
.data-table {
  padding: 15px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
}
</style>