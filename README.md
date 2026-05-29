# 校园二手交易平台

一个基于 Spring Boot 和 Vue 3 的校园二手商品交易平台。课程作业。

## 技术栈

### 后端
- Spring Boot 2.7.18
- MyBatis Plus 3.5.3.1
- MySQL 8.0.33
- Lombok

### 前端
- Vue 3.3.4
- Vue Router 4.2.4
- Element Plus 2.3.14
- Axios 1.5.0
- Vite 4.4.9

## 功能模块

### 用户功能
- 用户注册/登录
- 个人信息管理
- 钱包充值与管理
- 商品浏览与搜索
- 商品详情查看
- 发布商品
- 订单管理
- 我的商品管理

### 管理员功能
- 用户管理
- 商品审核
- 订单管理

## 项目结构

```
Trae_test/
├── frontend/                 # 前端项目
│   ├── src/
│   │   ├── router/          # 路由配置
│   │   ├── utils/           # 工具类
│   │   ├── views/           # 页面组件
│   │   ├── App.vue          # 根组件
│   │   └── main.js          # 入口文件
│   ├── index.html
│   └── package.json
├── src/                     # 后端项目
│   └── main/
│       ├── java/com/campus/market/
│       │   ├── common/      # 公共类
│       │   ├── config/      # 配置类
│       │   ├── controller/  # 控制器
│       │   ├── entity/      # 实体类
│       │   ├── mapper/      # 数据访问层
│       │   └── service/     # 业务逻辑层
│       └── resources/
│           └── application.yml
├── uploads/                 # 上传文件目录
└── pom.xml
```

## 快速开始

### 环境要求
- JDK 1.8+
- Node.js 14+
- MySQL 8.0+

### 后端启动

1. 创建数据库
```sql
CREATE DATABASE campus_market CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改数据库配置
编辑 `src/main/resources/application.yml`，修改数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/campus_market?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root
    password: your_password
```

3. 修改文件上传路径
编辑 `src/main/resources/application.yml`，修改文件上传路径：
```yaml
file:
  upload-path: D:/Trash_Projects/Trae_test/uploads/
```

4. 启动后端
```bash
mvn spring-boot:run
```

后端服务将在 http://localhost:8080 启动

### 前端启动

1. 进入前端目录
```bash
cd frontend
```

2. 安装依赖
```bash
npm install
```

3. 启动开发服务器
```bash
npm run dev
```

前端服务将在 http://localhost:5173 启动

## 数据库表

### user (用户表)
- id: 用户ID
- username: 用户名
- password: 密码
- phone: 手机号
- email: 邮箱
- realName: 真实姓名
- role: 角色（0-普通用户，1-商家，2-管理员）
- status: 状态
- createTime: 创建时间
- updateTime: 更新时间

### product (商品表)
- id: 商品ID
- sellerId: 卖家ID
- name: 商品名称
- description: 商品描述
- price: 价格
- stock: 库存
- productCondition: 商品成色
- images: 商品图片
- status: 状态（0-待审核，1-上架，2-下架）
- sales: 销量
- rating: 评分
- createTime: 创建时间
- updateTime: 更新时间

### order (订单表)
- id: 订单ID
- userId: 用户ID
- totalAmount: 总金额
- status: 订单状态
- createTime: 创建时间
- updateTime: 更新时间

### wallet (钱包表)
- id: 钱包ID
- userId: 用户ID
- balance: 余额
- createTime: 创建时间
- updateTime: 更新时间

## 许可证

MIT License
