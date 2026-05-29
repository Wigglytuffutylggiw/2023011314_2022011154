<template>
  <div class="publish-product">
    <el-card style="width: 600px; margin: 50px auto;">
      <h2 style="text-align: center; margin-bottom: 30px;">发布商品</h2>
      <el-form :model="form" label-width="120px">
        <el-form-item label="商品名称">
          <el-input v-model="form.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input type="textarea" v-model="form.description" placeholder="请输入商品描述" />
        </el-form-item>
        <el-form-item label="商品价格">
          <el-input v-model="form.price" placeholder="请输入商品价格" type="number" />
        </el-form-item>
        <el-form-item label="库存数量">
          <el-input-number v-model="form.stock" :min="1" />
        </el-form-item>
        <el-form-item label="新旧程度">
          <el-select v-model="form.productCondition">
            <el-option label="全新" :value="5" />
            <el-option label="九成新" :value="4" />
            <el-option label="八成新" :value="3" />
            <el-option label="七成新" :value="2" />
            <el-option label="六成及以下" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="商品图片">
          <el-upload
            :action="uploadAction"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            list-type="picture"
            :limit="1">
            <el-button size="small" type="primary">点击上传</el-button>
            <template #tip>
              <div class="el-upload__tip">只能上传jpg/png文件，且不超过10MB</div>
            </template>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handlePublish" style="width: 100%;">发布商品</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const API_BASE_URL = 'http://localhost:8080'

export default {
  name: 'PublishProduct',
  setup() {
    const router = useRouter()
    const uploadAction = API_BASE_URL + '/api/file/upload'
    
    const form = ref({
      name: '',
      description: '',
      price: '',
      stock: 1,
      productCondition: 5,
      images: ''
    })

    const handleUploadSuccess = (response) => {
      if (response.code === 200) {
        form.value.images = response.data
        ElMessage.success('图片上传成功')
      } else {
        ElMessage.error(response.message || '上传失败')
      }
    }

    const handleUploadError = () => {
      ElMessage.error('图片上传失败')
    }

    const beforeUpload = (file) => {
      const isJPGOrPNG = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isJPGOrPNG) {
        ElMessage.error('只能上传JPG/PNG格式的图片')
        return false
      }
      if (!isLt10M) {
        ElMessage.error('图片大小不能超过10MB')
        return false
      }
      return true
    }

    const handlePublish = async () => {
      const userStr = localStorage.getItem('user')
      if (!userStr) {
        ElMessage.warning('请先登录')
        router.push('/login')
        return
      }
      const user = JSON.parse(userStr)
      if (user.role !== 1) {
        ElMessage.error('只有商家可以发布商品')
        return
      }

      try {
        const res = await axios.post('/api/product/publish', {
          sellerId: user.id,
          name: form.value.name,
          description: form.value.description,
          price: form.value.price,
          stock: form.value.stock,
          productCondition: form.value.productCondition,
          images: form.value.images
        })
        if (res.data.code === 200) {
          ElMessage.success('发布成功，等待审核')
          router.push('/')
        } else {
          ElMessage.error(res.data.message)
        }
      } catch (e) {
        ElMessage.error('发布失败')
      }
    }

    return { form, uploadAction, handleUploadSuccess, handleUploadError, beforeUpload, handlePublish }
  }
}
</script>

<style scoped>
.publish-product {
  min-height: 60vh;
}
</style>
