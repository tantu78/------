# 校园约球平台

一个专为高校学生打造的运动约球平台，支持篮球、羽毛球、乒乓球等多种运动的活动发布、报名、好友推荐、地图选点等功能。

## 功能列表

### 基础功能
✅ 用户注册/登录，支持选择运动喜好
✅ 活动发布：创建约球活动，选择运动类型、时间、场地、参与人数
✅ 活动浏览：按运动类型筛选活动，查看活动详情
✅ 活动报名：加入/退出活动，查看参与人员列表
✅ 活动评价：活动结束后对组织者和参与者进行评分
✅ 场地信息：查看所有运动场地详情

### 特色功能
✅ 智能推荐：根据用户运动喜好推荐相关活动
✅ 地图选点：静态校园地图点击点位，筛选对应场地活动
✅ 好友系统：添加好友、好友活动推荐、消息通知
✅ 个人中心：我的活动管理、运动数据统计、账号设置

### 进阶功能
✅ 分页查询
✅ 用户权限管理
✅ 数据统计

## 技术栈

### 前端
- HTML5 + CSS3 + JavaScript (原生)
- Tailwind CSS (样式框架)
- Font Awesome (图标库)
- 响应式设计，支持PC/移动端

### 后端
- Spring Boot 2.7.x
- Spring Data JPA (持久层框架)
- MySQL 8.x (云端数据库)
- Maven (项目管理)

## 项目结构

```
校园约球平台/
├── index.html                      # 首页
├── activity-detail.html            # 活动详情页
├── map.html                        # 地图筛选页
├── login.html                      # 登录页
├── register.html                   # 注册页
├── profile.html                    # 个人中心页
├── create-activity.html            # 发起活动页
├── css/
│   └── styles.css                  # 全局样式
├── js/
│   └── main.js                     # 公共脚本
├── assets/
│   ├── images/                     # 图片资源
│   └── icons/                      # 图标资源
├── backend/                        # 后端项目目录
│   ├── src/
│   ├── pom.xml
│   └── application.yml
├── README.md                       # 项目说明文档
└── 大作业要求.txt                  # 需求文档
```

### 后端运行
1. 确保本地安装了 JDK 1.8+ 和 Maven
2. 进入 `backend` 目录
3. 执行 `mvn spring-boot:run` 启动后端服务
4. 后端服务默认运行在 `http://localhost:8082`
点击`http://localhost:8082/backend`进入网页

## 数据库表设计

| 表名 | 说明 |
|------|------|
| user | 用户表 |
| venue | 场地表 |
| activity | 活动表 |
| activity_participant | 活动参与表 |
| friend_relation | 好友关系表 |
| activity_recommend | 活动推荐表 |
| evaluation | 评价表 |

## 部署说明

### 前端部署
将所有HTML、CSS、JS、assets文件上传到静态服务器即可，支持Nginx、Apache、CDN等部署方式。

### 后端部署
1. 执行 `mvn package` 打包生成jar包
2. 上传jar包到服务器
3. 执行 `java -jar campus-ball-0.0.1-SNAPSHOT.jar` 启动服务
4. 建议使用Docker容器化部署，配合Nginx做反向代理

## 开发规范
- 代码注释：关键业务逻辑必须添加注释
- 命名规范：类名大驼峰，方法名小驼峰，数据库字段下划线分隔
- 接口规范：统一返回格式 `{code: 200, message: "success", data: {}}`
- 错误码：200成功，400参数错误，401未登录，403无权限，500服务器错误

## 版本历史
- v1.0.0 (2025-05-30)：完成基础功能开发，包含前后端完整实现

## 许可证
MIT License

## 联系方式
如有问题或建议，请提交Issue或联系开发团队。
