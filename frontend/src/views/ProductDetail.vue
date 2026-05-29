<template>
  <div class="product-detail" v-if="product">
    <el-row :gutter="40">
      <el-col :span="12">
        <div style="height: 400px; background: #f5f5f5; display: flex; align-items: center; justify-content: center; border-radius: 8px; overflow: hidden;">
          <img v-if="product.images" :src="getImageUrl(product.images)" style="max-width: 100%; max-height: 100%; object-fit: cover;" />
          <span v-else style="font-size: 24px; color: #999;">暂无图片</span>
        </div>
      </el-col>
      <el-col :span="12">
        <h1>{{ product.name }}</h1>
        <div v-if="product.sellerShopName" style="margin: 10px 0; padding: 10px; background: #f0f9ff; border-radius: 4px;">
          <span style="color: #409eff; font-size: 14px;">
            <i class="el-icon-shop"></i> 店铺名称：{{ product.sellerShopName }}
          </span>
        </div>
        <div style="margin: 20px 0; padding: 20px; background: #fff5f5; border-radius: 8px;">
          <span style="color: #f56c6c; font-size: 28px; font-weight: bold;">¥{{ product.price }}</span>
        </div>
        <p style="color: #666; margin: 20px 0;">{{ product.description }}</p>
        <div style="margin: 20px 0;">
          <span>库存: {{ product.stock }}</span>
          <span style="margin-left: 20px;">销量: {{ product.sales }}</span>
          <span style="margin-left: 20px;">评分: {{ product.rating }}</span>
        </div>
        <div style="margin: 20px 0;">
          <span>数量: </span>
          <el-input-number v-model="quantity" :min="1" :max="product.stock" />
        </div>
        <div style="margin: 20px 0;" v-if="wallet">
          <el-checkbox v-model="usePoints">使用积分抵扣 (当前积分: {{ wallet.points }})</el-checkbox>
          <el-input-number v-if="usePoints" v-model="pointsToUse" :min="0" :max="wallet.points" style="margin-left: 10px;" />
        </div>
        <div style="margin-top: 30px;">
          <el-button type="primary" size="large" @click="handleBuy">立即购买</el-button>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const API_BASE_URL = 'http://localhost:8080'

export default {
  name: 'ProductDetail',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const product = ref(null)
    const quantity = ref(1)
    const wallet = ref(null)
    const usePoints = ref(false)
    const pointsToUse = ref(0)

    const getImageUrl = (url) => {
      if (!url) return ''
      if (url.startsWith('http')) return url
      if (url.startsWith('/')) return API_BASE_URL + url
      return API_BASE_URL + '/' + url
    }

    const fetchProduct = async () => {
      try {
        const res = await axios.get('/api/product/' + route.params.id)
        if (res.data.code === 200) {
          product.value = res.data.data
        }
      } catch (e) {
        console.error(e)
      }
    }

    const fetchWallet = async () => {
      const userStr = localStorage.getItem('user')
      if (userStr) {
        const user = JSON.parse(userStr)
        try {
          const res = await axios.get('/api/wallet/' + user.id)
          if (res.data.code === 200) {
            wallet.value = res.data.data
          }
        } catch (e) {
          console.error(e)
        }
      }
    }

    const handleBuy = async () => {
      const userStr = localStorage.getItem('user')
      if (!userStr) {
        ElMessage.warning('请先登录')
        router.push('/login')
        return
      }
      const user = JSON.parse(userStr)
      try {
        const res = await axios.post('/api/order/create', {
          buyerId: user.id,
          productId: product.value.id,
          quantity: quantity.value,
          usePoints: usePoints.value ? pointsToUse.value : 0
        })
        if (res.data.code === 200) {
          ElMessage.success('购买成功')
          router.push('/')
        } else {
          ElMessage.error(res.data.message)
        }
      } catch (e) {
        ElMessage.error('购买失败')
      }
    }

    onMounted(() => {
      fetchProduct()
      fetchWallet()
    })

    return { product, quantity, wallet, usePoints, pointsToUse, handleBuy, getImageUrl }
  }
}
</script>

<style scoped>
.product-detail {
  padding: 20px 0;
}
h1 {
  font-size: 28px;
  margin-bottom: 10px;
}
</style>
