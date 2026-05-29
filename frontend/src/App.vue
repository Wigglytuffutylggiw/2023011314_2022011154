<template>
  <div id="app">
    <el-container>
      <el-header style="background: #409EFF; color: white;">
        <div style="display: flex; justify-content: space-between; align-items: center; height: 100%;">
          <div style="display: flex; align-items: center; gap: 20px;">
            <h2 style="margin: 0; cursor: pointer;" @click="$router.push('/')">校园二手交易平台</h2>
            <el-button type="success" @click="$router.push('/')">首页</el-button>
          </div>
          <div>
            <el-button v-if="!user" type="primary" @click="$router.push('/login')">登录</el-button>
            <el-button v-if="!user" @click="$router.push('/register')">注册</el-button>
            <template v-else>
              <el-button type="success" @click="$router.push('/wallet')">钱包</el-button>
              <el-button @click="$router.push('/orders')">订单</el-button>
              <el-button v-if="user.role === 1" type="success" @click="$router.push('/publish')">发布商品</el-button>
              <el-button v-if="user.role === 1" @click="$router.push('/my-products')">我的商品</el-button>
              <el-button v-if="user.role === 2" type="danger" @click="$router.push('/admin')">后台管理</el-button>
              <span>欢迎，{{ user.username }}</span>
              <el-button type="danger" size="small" @click="logout" style="margin-left: 10px;">退出</el-button>
            </template>
          </div>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { ref, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'

export default {
  name: 'App',
  setup() {
    const router = useRouter()
    const route = useRoute()
    const user = ref(null)

    const loadUser = () => {
      const userStr = localStorage.getItem('user')
      if (userStr) {
        user.value = JSON.parse(userStr)
      } else {
        user.value = null
      }
    }

    onMounted(loadUser)
    watch(
      () => route.path,
      () => loadUser()
    )

    const logout = () => {
      localStorage.removeItem('user')
      localStorage.removeItem('token')
      user.value = null
      router.push('/')
    }

    return { user, logout }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  min-height: 100vh;
}
.el-header {
  padding: 0 20px;
}
</style>
