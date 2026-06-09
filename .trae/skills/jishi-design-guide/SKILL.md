---
name: "jishi-design-guide"
description: "即时设计 MCP 使用指南。涵盖 API 语法陷阱、页面组织规范、设计风格建议。在通过即时设计 MCP 进行任何 UI 设计工作前必须先阅读此 Skill。触发词：即时设计、jishi-design、画设计图、出设计稿、设计页面、MCP 设计。"
---

# 即时设计 MCP 使用指南

## 何时使用

**必须先调用此 Skill 的场景：**
- 通过即时设计 MCP 创建或编辑设计图
- 用户要求"画设计图"、"出设计稿"、"设计页面"
- 需要在即时设计中批量生成页面

**不需要此 Skill 的场景：**
- 仅查看已有设计节点（get_selection / get_page_nodes 等只读操作）

---

## 环境约束

- `jsDesign` 是全局变量，**不要** `require` 或 `import`
- 脚本运行在即时设计的插件沙箱中，不是 Node.js
- 箭头函数内的 `&&` 可能导致 MCP 参数解析器报 `unexpected token: '&'`，改用 `function` 关键字或拆分逻辑规避

---

## API 陷阱速查

### 1. Stroke 属性结构

**错误：**
```javascript
strokes: [{ type: 'SOLID', color: hexToRgb('#EBEBEB'), strokeWidth: 1 }]
```

**正确：** `strokeWeight` 是节点自身的属性，不是 stroke 对象内部的字段。
```javascript
rect.strokes = [{ type: 'SOLID', color: hexToRgb('#EBEBEB'), visible: true }];
rect.strokeWeight = 1;
rect.strokeAlign = 'INSIDE';
```

Stroke 对象支持的字段：`type`、`color`、`visible`、`opacity`、`blendMode`

### 2. MCP 返回值误报

几乎每次 `execute_script` 执行成功后都会返回 `invalid_union` 错误，**但元素已正确创建**。判断是否成功应调用 `get_page_nodes` 验证节点数量变化，而不是信赖返回值。

### 3. appendChild 不生效

**问题：** 调用 `parent.appendChild(child)` 后，child 仍然是页面级顶层节点，不会嵌套进 parent frame。

**当前解决方案：** 接受扁平结构，按命名规范和坐标分区来组织页面。

### 4. 图片导出路径问题

`save_image` 依赖 `C:\tmp\mcp-debug.log`，若该目录不存在会导致 `ENOENT`。如需导出，先确保 `C:\tmp\` 目录存在。

---

## 页面组织规范

### 坐标分区法

由于 `appendChild` 不生效，所有元素平铺在页面上。必须通过坐标分区来区分不同页面：

```
第 1 页 (x=0,       y=0)      — 首页
第 2 页 (x=1540,    y=0)      — 登录页
第 3 页 (x=3080,    y=0)      — 注册页
第 4 页 (x=0,       y=1400)   — 活动详情页
第 5 页 (x=1540,    y=1400)   — 个人中心页
第 6 页 (x=3080,    y=1400)   — 发起活动页
第 7 页 (x=4620,    y=1400)   — 地图页
```

每页 1440 宽 × 1000~1400 高，页间距 100px，绝对不重叠。

### 命名规范

所有元素使用"页面名-组件名-序号"格式：

```
首页-导航栏-背景
首页-搜索框
首页-活动卡片1-标题
登录页-登录卡片
登录页-用户名输入框
```

---

## 设计流程

### 第一步：确定设计风格（必须）

在动手创建元素之前，**必须先调用 `大厂设计风格` Skill**。这个 Skill 会根据品牌名称自动获取设计规范（配色、字体、圆角、阴影等），避免设计单调。

例如，校园约球平台参考的是 **Airbnb** 风格：
- 主色 #FF5A5F（红）
- 辅助色 #00A699（青）、#FC642D（橙）
- 文字 #484848（深灰）、#767676（浅灰）
- 边框 #EBEBEB、背景 #F7F7F7
- 圆角 8~12px
- 卡片白色 + 1px 边框 + 微阴影

### 第二步：逐个页面创建

**每次执行只创建一个组件的元素**（如一个导航栏、一张卡片、一个表单），不要把所有页面放在一次脚本中。好处：
1. 出错时影响范围小
2. 容易追踪已创建和未创建的内容
3. 每次执行后调用 `get_page_nodes` 验证

### 第三步：验证

每创建完一组元素后，调用 `get_page_nodes` 检查节点数量和名称是否正确。

---

## 示例脚本

### 创建画板（新页面）

```javascript
function hexToRgb(hex) {
  var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
  return result ? {
    r: parseInt(result[1], 16) / 255,
    g: parseInt(result[2], 16) / 255,
    b: parseInt(result[3], 16) / 255
  } : { r: 0, g: 0, b: 0 };
}

// 创建画板 — 页面定位在独立坐标区
var frame = jsDesign.createFrame();
frame.name = '首页';
frame.x = 0;
frame.y = 0;
frame.resize(1440, 1200);
frame.fills = [{ type: 'SOLID', color: { r: 1, g: 1, b: 1 } }];
```

### 创建导航栏（一组元素）

```javascript
// 导航栏背景
var navBg = jsDesign.createRectangle();
navBg.name = '首页-导航栏-背景';
navBg.x = 0;
navBg.y = 0;
navBg.resize(1440, 64);
navBg.fills = [{ type: 'SOLID', color: { r: 1, g: 1, b: 1 } }];

// 导航栏底边框
var navBorder = jsDesign.createRectangle();
navBorder.name = '首页-导航栏-边框';
navBorder.x = 0;
navBorder.y = 63;
navBorder.resize(1440, 1);
navBorder.fills = [{ type: 'SOLID', color: hexToRgb('#EBEBEB') }];

// Logo
var logo = jsDesign.createText();
logo.name = '首页-Logo';
logo.characters = '校园约球';
logo.fontSize = 20;
logo.fontWeight = 700;
logo.x = 120;
logo.y = 20;
logo.fills = [{ type: 'SOLID', color: hexToRgb('#FF5A5F') }];
```

### 创建带描边的矩形

```javascript
var box = jsDesign.createRectangle();
box.x = 400;
box.y = 12;
box.resize(400, 40);
box.cornerRadius = 20;
box.fills = [{ type: 'SOLID', color: { r: 1, g: 1, b: 1 } }];
// strokeWeight 是独立属性！
box.strokes = [{ type: 'SOLID', color: hexToRgb('#EBEBEB'), visible: true }];
box.strokeWeight = 1;
box.strokeAlign = 'INSIDE';
```

---

## 检查清单

执行设计任务前，确认以下条目：

- [ ] 已调用 `大厂设计风格` Skill 确定配色和设计规范
- [ ] 为新页面创建了 Frame，坐标不与其他页面重叠
- [ ] 所有元素命名遵循"页面-组件"格式
- [ ] 每次只创建一组相关元素，创建后立即验证
- [ ] 没有在代码中使用 `require`
- [ ] stroke 对象中没有 `strokeWidth` 字段
- [ ] 没有在参数中使用 `&&`（箭头函数拆分或用 function）
- [ ] 不信任 `execute_script` 返回值，用 `get_page_nodes` 验证
