<template>
  <div class="register">
    <el-card style="width: 450px; margin: 80px auto;">
      <h2 style="text-align: center; margin-bottom: 30px;">用户注册</h2>
      <el-form :model="form" label-width="100px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="confirmPassword" type="password" placeholder="请再次输入密码" />
        </el-form-item>
        <el-form-item label="角色">
          <el-radio-group v-model="form.role">
            <el-radio :label="0">普通用户</el-radio>
            <el-radio :label="1">商家</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.role === 1" label="店铺名称">
          <el-input v-model="form.shopName" placeholder="请输入您的店铺名称" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister" style="width: 100%;">注册</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align: center;">
        已有账号？<el-link type="primary" @click="$router.push('/login')">立即登录</el-link>
      </div>
    </el-card>
  </div>
</template>

<script>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export default {
  name: 'Register',
  setup() {
    const router = useRouter()
    const confirmPassword = ref('')
    const form = ref({
      username: '',
      password: '',
      role: 0,
      shopName: ''
    })

    const handleRegister = async () => {
      if (form.value.password !== confirmPassword.value) {
        ElMessage.error('两次密码输入不一致')
        return
      }
      try {
        const res = await axios.post('/api/user/register', form.value)
        if (res.data.code === 200) {
          ElMessage.success('注册成功，请等待审核')
          router.push('/login')
        } else {
          ElMessage.error(res.data.message)
        }
      } catch (e) {
        ElMessage.error('注册失败')
      }
    }

    return { form, confirmPassword, handleRegister }
  }
}
</script>

<style scoped>
.register {
  min-height: 60vh;
}
</style>
