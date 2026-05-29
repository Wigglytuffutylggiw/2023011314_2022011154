<template>
  <div class="admin-panel">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="待审核用户" name="users">
        <el-table :data="pendingUsers" stripe>
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="username" label="用户名" />
          <el-table-column label="角色" width="120">
            <template #default="{ row }">
              {{ row.role === 0 ? '普通用户' : row.role === 1 ? '商家' : '管理员' }}
            </template>
          </el-table-column>
          <el-table-column prop="phone" label="手机号" />
          <el-table-column prop="email" label="邮箱" />
          <el-table-column label="操作" width="180">
            <template #default="{ row }">
              <el-button type="success" size="small" @click="approveUser(row.id)">通过</el-button>
              <el-button type="danger" size="small" @click="rejectUser(row.id)">拒绝</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="待审核商品" name="products">
        <el-table :data="pendingProducts" stripe>
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="name" label="商品名称" />
          <el-table-column prop="price" label="价格" width="100">
            <template #default="{ row }">¥{{ row.price }}</template>
          </el-table-column>
          <el-table-column prop="stock" label="库存" width="100" />
          <el-table-column label="操作" width="180">
            <template #default="{ row }">
              <el-button type="success" size="small" @click="approveProduct(row.id)">通过</el-button>
              <el-button type="danger" size="small" @click="rejectProduct(row.id)">拒绝</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export default {
  name: 'AdminPanel',
  setup() {
    const activeTab = ref('users')
    const pendingUsers = ref([])
    const pendingProducts = ref([])

    const loadPendingUsers = async () => {
      try {
        const res = await axios.get('/api/admin/users/pending')
        if (res.data.code === 200) {
          pendingUsers.value = res.data.data.records
        }
      } catch (e) {
        console.error(e)
      }
    }

    const loadPendingProducts = async () => {
      try {
        const res = await axios.get('/api/product/pending')
        if (res.data.code === 200) {
          pendingProducts.value = res.data.data.records
        }
      } catch (e) {
        console.error(e)
      }
    }

    const approveUser = async (id) => {
      try {
        const res = await axios.post('/api/admin/users/' + id + '/approve')
        if (res.data.code === 200) {
          ElMessage.success('审核通过')
          loadPendingUsers()
        } else {
          ElMessage.error(res.data.message)
        }
      } catch (e) {
        ElMessage.error('操作失败')
      }
    }

    const rejectUser = async (id) => {
      try {
        const res = await axios.post('/api/admin/users/' + id + '/reject')
        if (res.data.code === 200) {
          ElMessage.success('已拒绝')
          loadPendingUsers()
        } else {
          ElMessage.error(res.data.message)
        }
      } catch (e) {
        ElMessage.error('操作失败')
      }
    }

    const approveProduct = async (id) => {
      try {
        const res = await axios.post('/api/product/' + id + '/approve')
        if (res.data.code === 200) {
          ElMessage.success('商品已上架')
          loadPendingProducts()
        } else {
          ElMessage.error(res.data.message)
        }
      } catch (e) {
        ElMessage.error('操作失败')
      }
    }

    const rejectProduct = async (id) => {
      try {
        const res = await axios.post('/api/product/' + id + '/reject')
        if (res.data.code === 200) {
          ElMessage.success('已拒绝')
          loadPendingProducts()
        } else {
          ElMessage.error(res.data.message)
        }
      } catch (e) {
        ElMessage.error('操作失败')
      }
    }

    onMounted(() => {
      loadPendingUsers()
      loadPendingProducts()
    })

    return {
      activeTab,
      pendingUsers,
      pendingProducts,
      approveUser,
      rejectUser,
      approveProduct,
      rejectProduct
    }
  }
}
</script>

<style scoped>
.admin-panel {
  padding: 20px;
}
</style>
