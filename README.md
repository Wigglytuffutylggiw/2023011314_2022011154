# 校园二手交易平台

基于Spring Boot + Vue 3的校园二手交易平台项目，适合作为软件开发实践课程作业。

## 技术栈

### 后端
- Spring Boot 2.7.x
- MyBatis Plus
- MySQL 8.0
- Maven

### 前端
- Vue 3
- Element Plus
- Vue Router
- Axios
- Vite

## 项目结构

```
Trae_test/
├── pom.xml                          # Maven配置文件
├── schema.sql                       # 数据库初始化脚本
├── README.md                        # 项目说明文档
├── src/
│   └── main/
│       ├── java/com/campus/market/
│       │   ├── MarketApplication.java       # 启动类
│       │   ├── common/              # 公共类
│       │   ├── controller/          # 控制器层
│       │   ├── entity/              # 实体类
│       │   ├── mapper/              # 数据访问层
│       │   └── service/             # 业务逻辑层
│       └── resources/
│           └── application.yml      # 配置文件
└── frontend/                        # 前端项目
    ├── package.json
    ├── vite.config.js
    ├── index.html
    └── src/
        ├── main.js
        ├── App.vue
        ├── router/
        └── views/
```

## 快速开始

### 1. 环境准备

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Node.js 14+

### 2. 数据库配置

创建数据库并执行初始化脚本：

```sql
-- 在MySQL中执行 schema.sql 文件
source d:/Trash_Projects/Trae_test/schema.sql
```

或者手动执行schema.sql中的SQL语句。

默认测试账号：
- 管理员：admin / admin123
- 商家：seller1 / 123456
- 普通用户：user1 / 123456

### 3. 后端启动

修改 `src/main/resources/application.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/campus_market?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root      # 修改为你的MySQL用户名
    password: root      # 修改为你的MySQL密码
```

在项目根目录执行：

```bash
mvn clean install
mvn spring-boot:run
```

后端服务将在 http://localhost:8080 启动

### 4. 前端启动

进入frontend目录：

```bash
cd frontend
npm install
npm run dev
```

前端服务将在 http://localhost:3000 启动

## 功能模块

### 用户模块
- 用户注册（支持普通用户和商家角色）
- 用户登录
- 角色权限管理（普通用户/商家/管理员）

### 商品模块
- 商品发布
- 商品搜索和浏览
- 商品详情查看

### 交易模块
- 下单购买
- 积分抵扣
- 库存管理

### 钱包与积分
- 钱包余额管理
- 积分获取和消费
- 1元=1积分，100积分抵扣1元

### 商家等级
- 商家等级1-5级
- 预留费率计算接口

## API接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/user/register | 用户注册 |
| POST | /api/user/login | 用户登录 |
| GET | /api/product/search | 商品搜索 |
| GET | /api/product/{id} | 商品详情 |
| POST | /api/order/create | 创建订单 |
| GET | /api/wallet/{userId} | 获取钱包信息 |

## 实验报告要点

### 技术亮点
1. **前后端分离架构**：使用RESTful API进行数据交互
2. **MyBatis Plus**：简化数据库操作，提高开发效率
3. **Vue 3 + Element Plus**：现代化的前端技术栈
4. **事务管理**：订单创建时使用@Transactional保证数据一致性

### 可扩展功能
- 商品图片上传
- 购物车功能
- 订单评价
- 退货申请
- 后台管理系统
- 消息通知

## 注意事项

1. 请确保MySQL服务已启动
2. 数据库用户名和密码请根据实际情况修改
3. 前端开发时会自动代理API请求到后端8080端口
4. 生产环境请修改密码和配置参数

## 作者

软件开发实践课程项目
