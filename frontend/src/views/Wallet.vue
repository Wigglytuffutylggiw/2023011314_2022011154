<template>
  <div class="wallet">
    <el-card style="width: 600px; margin: 50px auto;">
      <h2 style="text-align: center; margin-bottom: 30px;">我的钱包</h2>
      <div style="margin-bottom: 30px;">
        <p>当前余额：<span style="color: #f56c6c; font-size: 24px; font-weight: bold;">¥{{ wallet?.balance || 0 }}</span></p>
        <p>当前积分：<span style="color: #409eff; font-size: 20px;">{{ wallet?.points || 0 }}</span></p>
      </div>
      <el-divider />
      <h3 style="margin: 20px 0;">充值</h3>
      <el-form :model="rechargeForm" label-width="100px">
        <el-form-item label="充值金额">
          <el-input-number v-model="rechargeForm.amount" :min="1" :max="10000" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRecharge" style="width: 100%;">充值</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export default {
  name: 'Wallet',
  setup() {
    const wallet = ref(null)
    const rechargeForm = ref({
      amount: 100
    })

    const loadWallet = async () => {
      const userStr = localStorage.getItem('user')
      if (!userStr) {
        ElMessage.warning('请先登录')
        return
      }
      const user = JSON.parse(userStr)
      try {
        const res = await axios.get('/api/wallet/' + user.id)
        if (res.data.code === 200) {
          wallet.value = res.data.data
        } else {
          wallet.value = { balance: 0, points: 0 }
        }
      } catch (e) {
        wallet.value = { balance: 0, points: 0 }
      }
    }

    const handleRecharge = async () => {
      const userStr = localStorage.getItem('user')
      if (!userStr) {
        ElMessage.warning('请先登录')
        return
      }
      const user = JSON.parse(userStr)
      try {
        const res = await axios.post('/api/wallet/recharge', {
          userId: user.id,
          amount: rechargeForm.value.amount
        })
        if (res.data.code === 200) {
          ElMessage.success('充值成功')
          loadWallet()
        } else {
          ElMessage.error(res.data.message)
        }
      } catch (e) {
        ElMessage.error('充值失败')
      }
    }

    onMounted(loadWallet)

    return { wallet, rechargeForm, handleRecharge }
  }
}
</script>

<style scoped>
.wallet {
  min-height: 60vh;
}
</style>
