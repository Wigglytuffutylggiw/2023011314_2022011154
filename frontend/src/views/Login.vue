<template>
  <div class="login">
    <el-card style="width: 400px; margin: 100px auto;">
      <h2 style="text-align: center; margin-bottom: 30px;">用户登录</h2>
      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" style="width: 100%;">登录</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align: center;">
        还没有账号？<el-link type="primary" @click="$router.push('/register')">立即注册</el-link>
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
  name: 'Login',
  setup() {
    const router = useRouter()
    const form = ref({
      username: '',
      password: ''
    })

    const handleLogin = async () => {
      try {
        const res = await axios.post('/api/user/login', form.value)
        if (res.data.code === 200) {
          localStorage.setItem('user', JSON.stringify(res.data.data.user))
          localStorage.setItem('token', res.data.data.token)
          ElMessage.success('登录成功')
          router.push('/')
        } else {
          ElMessage.error(res.data.message)
        }
      } catch (e) {
        ElMessage.error('登录失败')
      }
    }

    return { form, handleLogin }
  }
}
</script>

<style scoped>
.login {
  min-height: 60vh;
}
</style>
