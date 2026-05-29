<template>
  <div class="home">
    <div style="margin-bottom: 20px;">
      <el-input v-model="keyword" placeholder="搜索商品" style="width: 400px;" clearable>
        <template #append>
          <el-button @click="searchProducts">搜索</el-button>
        </template>
      </el-input>
    </div>

    <el-row :gutter="20">
      <el-col v-for="product in products" :key="product.id" :span="6" style="margin-bottom: 20px;">
        <el-card shadow="hover" @click="$router.push('/product/' + product.id)" style="cursor: pointer;">
          <div style="height: 150px; background: #f5f5f5; display: flex; align-items: center; justify-content: center; overflow: hidden;">
            <img v-if="product.images" :src="getImageUrl(product.images)" style="max-width: 100%; max-height: 100%; object-fit: cover;" />
            <span v-else style="color: #999;">暂无图片</span>
          </div>
          <h3 style="margin: 10px 0;">{{ product.name }}</h3>
          <p v-if="product.sellerShopName" style="color: #409eff; font-size: 12px; margin-bottom: 5px;">
            <i class="el-icon-shopping-bag-2"></i> {{ product.sellerShopName }}
          </p>
          <p style="color: #666; font-size: 14px; height: 40px; overflow: hidden;">{{ product.description }}</p>
          <div style="display: flex; justify-content: space-between; margin-top: 10px;">
            <span style="color: #f56c6c; font-size: 18px; font-weight: bold;">¥{{ product.price }}</span>
            <span style="color: #909399;">销量: {{ product.sales }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-pagination
      v-model:current-page="page"
      :total="total"
      :page-size="size"
      @current-change="searchProducts"
      style="margin-top: 20px; text-align: center;"
    />
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080'

export default {
  name: 'Home',
  setup() {
    const keyword = ref('')
    const products = ref([])
    const page = ref(1)
    const size = ref(12)
    const total = ref(0)

    const getImageUrl = (url) => {
      if (!url) return ''
      if (url.startsWith('http')) return url
      if (url.startsWith('/')) return API_BASE_URL + url
      return API_BASE_URL + '/' + url
    }

    const searchProducts = async () => {
      try {
        const res = await axios.get('/api/product/search', {
          params: { keyword: keyword.value, page: page.value, size: size.value }
        })
        if (res.data.code === 200) {
          products.value = res.data.data.records
          total.value = res.data.data.total
        }
      } catch (e) {
        console.error(e)
      }
    }

    onMounted(searchProducts)

    return { keyword, products, page, size, total, searchProducts, getImageUrl }
  }
}
</script>

<style scoped>
.home {
  padding: 20px 0;
}
</style>
