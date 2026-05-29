<template>
  <div class="my-products">
    <h2 style="margin: 20px 0;">我的商品</h2>
    <el-table :data="products" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="商品名称" />
      <el-table-column prop="price" label="价格" width="100">
        <template #default="{ row }">¥{{ row.price }}</template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="100" />
      <el-table-column label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : row.status === 0 ? 'warning' : 'info'">
            {{ row.status === 1 ? '已上架' : row.status === 0 ? '待审核' : '已下架' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="sales" label="销量" width="100" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button v-if="row.status === 1" type="danger" size="small" @click="handleOffShelf(row.id)">下架</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export default {
  name: 'MyProducts',
  setup() {
    const products = ref([])

    const loadMyProducts = async () => {
      const userStr = localStorage.getItem('user')
      if (!userStr) {
        ElMessage.warning('请先登录')
        return
      }
      const user = JSON.parse(userStr)
      try {
        const res = await axios.get('/api/product/seller/' + user.id)
        if (res.data.code === 200) {
          products.value = res.data.data.records
        }
      } catch (e) {
        console.error(e)
      }
    }

    const handleOffShelf = async (id) => {
      const userStr = localStorage.getItem('user')
      if (!userStr) {
        ElMessage.warning('请先登录')
        return
      }
      const user = JSON.parse(userStr)
      try {
        const res = await axios.post('/api/product/' + id + '/offshelf', { sellerId: user.id })
        if (res.data.code === 200) {
          ElMessage.success('商品已下架')
          loadMyProducts()
        } else {
          ElMessage.error(res.data.message)
        }
      } catch (e) {
        ElMessage.error('操作失败')
      }
    }

    onMounted(loadMyProducts)

    return { products, handleOffShelf }
  }
}
</script>

<style scoped>
.my-products {
  padding: 20px;
}
</style>
