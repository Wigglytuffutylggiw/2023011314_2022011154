<template>
  <div class="order-center">
    <el-tabs v-model="activeTab">
      <el-tab-pane label="我的订单" name="buyer" v-if="currentUser?.role === 0">
        <el-table :data="buyerOrders" stripe>
          <el-table-column prop="orderNo" label="订单号" width="180" />
          <el-table-column prop="productName" label="商品名称" />
          <el-table-column prop="quantity" label="数量" width="80" />
          <el-table-column prop="totalPrice" label="金额" width="100">
            <template #default="{ row }">¥{{ row.totalPrice }}</template>
          </el-table-column>
          <el-table-column label="状态" width="120">
            <template #default="{ row }">
              <el-tag :type="getOrderStatusType(row.status)">{{ getOrderStatus(row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="下单时间" width="180" />
          <el-table-column label="操作">
            <template #default="{ row }">
              <el-button v-if="row.status === 1" type="primary" size="small" @click="handleReceive(row.id)">确认收货</el-button>
              <el-button v-if="row.status === 1 || row.status === 2" type="warning" size="small" @click="handleRefund(row.id)">申请退货</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <el-tab-pane label="销售订单" name="seller" v-if="currentUser?.role === 1">
        <el-table :data="sellerOrders" stripe>
          <el-table-column prop="orderNo" label="订单号" width="180" />
          <el-table-column prop="productName" label="商品名称" />
          <el-table-column prop="quantity" label="数量" width="80" />
          <el-table-column prop="totalPrice" label="金额" width="100">
            <template #default="{ row }">¥{{ row.totalPrice }}</template>
          </el-table-column>
          <el-table-column label="状态" width="120">
            <template #default="{ row }">
              <el-tag :type="getOrderStatusType(row.status)">{{ getOrderStatus(row.status) }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="下单时间" width="180" />
          <el-table-column label="操作">
            <template #default="{ row }">
              <el-button v-if="row.status === 0" type="primary" size="small" @click="handleShip(row.id)">发货</el-button>
              <el-button v-if="row.status === 3" type="success" size="small" @click="handleApproveRefund(row.id)">同意退货</el-button>
              <el-button v-if="row.status === 3" type="danger" size="small" @click="handleRejectRefund(row.id)">拒绝退货</el-button>
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
  name: 'OrderCenter',
  setup() {
    const activeTab = ref('buyer')
    const currentUser = ref(null)
    const buyerOrders = ref([])
    const sellerOrders = ref([])

    const getOrderStatus = (status) => {
      const statusMap = {
        0: '待发货',
        1: '已发货',
        2: '已完成',
        3: '退货中',
        4: '已退货'
      }
      return statusMap[status] || '未知'
    }

    const getOrderStatusType = (status) => {
      const typeMap = {
        0: 'warning',
        1: 'primary',
        2: 'success',
        3: 'info',
        4: 'success'
      }
      return typeMap[status] || ''
    }

    const loadCurrentUser = () => {
      const userStr = localStorage.getItem('user')
      if (userStr) {
        currentUser.value = JSON.parse(userStr)
      }
    }

    const loadBuyerOrders = async () => {
      if (!currentUser.value) return
      try {
        const res = await axios.get('/api/order/buyer/' + currentUser.value.id)
        if (res.data.code === 200) {
          buyerOrders.value = res.data.data.records
        }
      } catch (e) {
        console.error(e)
      }
    }

    const loadSellerOrders = async () => {
      if (!currentUser.value) return
      try {
        const res = await axios.get('/api/order/seller/' + currentUser.value.id)
        if (res.data.code === 200) {
          sellerOrders.value = res.data.data.records
        }
      } catch (e) {
        console.error(e)
      }
    }

    const handleReceive = async (orderId) => {
      try {
        const res = await axios.post('/api/order/' + orderId + '/receive')
        if (res.data.code === 200) {
          ElMessage.success('确认收货成功')
          loadBuyerOrders()
        } else {
          ElMessage.error(res.data.message)
        }
      } catch (e) {
        ElMessage.error('操作失败')
      }
    }

    const handleRefund = async (orderId) => {
      try {
        const res = await axios.post('/api/order/' + orderId + '/refund/apply')
        if (res.data.code === 200) {
          ElMessage.success('退货申请已提交')
          loadBuyerOrders()
        } else {
          ElMessage.error(res.data.message)
        }
      } catch (e) {
        ElMessage.error('操作失败')
      }
    }

    const handleShip = async (orderId) => {
      try {
        const res = await axios.post('/api/order/' + orderId + '/ship')
        if (res.data.code === 200) {
          ElMessage.success('发货成功')
          loadSellerOrders()
        } else {
          ElMessage.error(res.data.message)
        }
      } catch (e) {
        ElMessage.error('操作失败')
      }
    }

    const handleApproveRefund = async (orderId) => {
      try {
        const res = await axios.post('/api/order/' + orderId + '/refund/approve')
        if (res.data.code === 200) {
          ElMessage.success('已同意退货并退款')
          loadSellerOrders()
        } else {
          ElMessage.error(res.data.message)
        }
      } catch (e) {
        ElMessage.error('操作失败')
      }
    }

    const handleRejectRefund = async (orderId) => {
      try {
        const res = await axios.post('/api/order/' + orderId + '/refund/reject')
        if (res.data.code === 200) {
          ElMessage.success('已拒绝退货申请')
          loadSellerOrders()
        } else {
          ElMessage.error(res.data.message)
        }
      } catch (e) {
        ElMessage.error('操作失败')
      }
    }

    onMounted(() => {
      loadCurrentUser()
      if (currentUser.value) {
        if (currentUser.value.role === 0) {
          activeTab.value = 'buyer'
          loadBuyerOrders()
        } else if (currentUser.value.role === 1) {
          activeTab.value = 'seller'
          loadSellerOrders()
        }
      }
    })

    return {
      activeTab,
      currentUser,
      buyerOrders,
      sellerOrders,
      getOrderStatus,
      getOrderStatusType,
      handleReceive,
      handleRefund,
      handleShip,
      handleApproveRefund,
      handleRejectRefund
    }
  }
}
</script>

<style scoped>
.order-center {
  padding: 20px;
}
</style>
