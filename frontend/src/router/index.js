import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import ProductDetail from '../views/ProductDetail.vue'
import PublishProduct from '../views/PublishProduct.vue'
import Wallet from '../views/Wallet.vue'
import OrderCenter from '../views/OrderCenter.vue'
import AdminPanel from '../views/AdminPanel.vue'
import MyProducts from '../views/MyProducts.vue'

const routes = [
  { path: '/', component: Home },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { path: '/product/:id', component: ProductDetail },
  { path: '/publish', component: PublishProduct },
  { path: '/wallet', component: Wallet },
  { path: '/orders', component: OrderCenter },
  { path: '/admin', component: AdminPanel },
  { path: '/my-products', component: MyProducts }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
