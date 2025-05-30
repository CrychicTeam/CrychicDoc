# CryChicDoc 主题样式系统

> [English Documentation](./README_EN.md)

为 CryChicDoc VitePress 主题设计的模块化、可维护的样式组织系统。

## 📁 目录结构

```
styles/
├── config/             # 配置和变量
│   ├── variables.css   # 所有变量的主入口文件
│   ├── base-variables.css      # 基础主题变量
│   ├── component-variables.css # 组件特定变量  
│   └── chart-variables.css     # 图表和数据可视化变量
├── base/               # 基础样式
│   ├── colors.css      # VitePress 颜色系统
│   ├── typography.css  # 字体和文本样式
│   ├── foundation.css  # 全局基础样式
│   └── hero.css        # 首页 hero 样式
├── plugins/            # 插件特定样式
│   ├── algolia.css     # Algolia 搜索样式
│   ├── custom-blocks.css # 自定义块容器
│   ├── code-groups.css # VitePress 代码组标签
│   ├── demo.css        # 演示容器样式
│   └── link-icons.css  # 外部链接图标
├── components/         # 组件特定样式
│   ├── stepper.css     # 步骤器组件样式
│   └── carousel.css    # 轮播图组件样式
└── index.css           # 单一入口文件
```

## 🎯 导入策略

**单一入口点**: 所有样式通过 `styles/index.css` 导入，这是在 `theme/index.ts` 中导入的唯一 CSS 文件。

```typescript
// theme/index.ts
import "./styles/index.css"; // 单一 CSS 入口点
```

## 🔧 CSS 变量组织

### 模块化变量结构

变量现在被组织到单独的文件中，以便更好地维护：

#### **config/variables.css** - 主入口点
```css
/* 导入所有变量模块的单一入口点 */
@import './base-variables.css';
@import './component-variables.css';  
@import './chart-variables.css';
```

#### **config/base-variables.css** - 核心主题变量
```css
:root {
    /* ===== 品牌和主题颜色 ===== */
    --vp-c-brand: #1565c0;
    --vp-c-text-2: #546e7a;
    
    /* ===== 动画和过渡 ===== */
    --transition-duration: 300ms;
    --transition-easing: ease-in;
    --hover-scale: 1.1;
    --fade-duration: 0.5s;
    
    /* ===== 全局间距 ===== */
    --spacing-xs: 4px;
    --spacing-sm: 8px;
    --spacing-md: 16px;
    --spacing-lg: 24px;
    --spacing-xl: 32px;
    
    /* ===== 边框圆角 ===== */
    --border-radius-sm: 4px;
    --border-radius-md: 8px;
    --border-radius-lg: 12px;
    --border-radius-xl: 16px;
    --border-radius-round: 50%;
    
    /* ===== 阴影 ===== */
    --shadow-sm: 0 2px 4px rgba(0, 0, 0, 0.1);
    --shadow-md: 0 4px 8px rgba(0, 0, 0, 0.1);
    --shadow-lg: 0 8px 16px rgba(0, 0, 0, 0.15);
    --shadow-button: 2px 2px 10px 4px rgba(0, 0, 0, 0.15);
}
```

#### **config/component-variables.css** - 组件变量
```css
:root {
    /* ===== 浮动按钮 ===== */
    --button-bg-color: #c5a16b;
    --button-hover-color: #a38348;
    --button-size: 45px;
    --button-border-radius: var(--border-radius-round);
    --button-shadow: var(--shadow-button);
    
    /* ===== 视频组件 ===== */
    --video-border-color: #ccc;
    --video-border-radius: var(--border-radius-md);
    --video-bg-color: #f9f9f9;
    --video-shadow: var(--shadow-md);
    --video-aspect-ratio: 56.25%; /* 16:9 */
    
    /* ===== 导航组件 ===== */
    --m-nav-icon-box-size: 50px;
    --m-nav-icon-size: 45px;
    --m-nav-box-gap: 12px;
    --m-nav-gap: 10px;
}

.dark {
    /* 深色主题组件覆盖 */
    --button-bg-color: #2b4796;
    --button-hover-color: #283d83;
    --video-border-color: #555;
    --video-bg-color: #333;
}
```

#### **config/chart-variables.css** - 图表和数据可视化
```css
:root {
    /* ===== 图表布局 ===== */
    --chart-height: 50vh;
    --chart-min-height: 300px;
    --chart-mobile-height: 40vh;
    --chart-mobile-min-height: 250px;
    
    /* ===== 图表颜色主题 ===== */
    --chart-primary-light: #ff3860;
    --chart-primary-dark: #ff6384;
    --chart-secondary-light: #3273dc;
    --chart-secondary-dark: #36a2eb;
    
    /* ===== 图表渐变 ===== */
    --gradient-primary-1: #1565C0;
    --gradient-primary-2: #1976D2;
    --gradient-primary-3: #2196F3;
    
    /* ===== 数据可视化 ===== */
    --data-grid-color: rgba(128, 128, 128, 0.2);
    --data-axis-color: var(--vp-c-text-2);
    --data-tooltip-bg: var(--vp-c-bg-soft);
}
```

### 组件使用
组件通过语义引用使用这些变量：

```vue
<style scoped>
.floating-button {
    width: var(--button-size);
    height: var(--button-size);
    background-color: var(--button-bg-color);
    border-radius: var(--button-border-radius);
    box-shadow: var(--button-shadow);
    transition: background-color var(--transition-duration);
}

.video-container {
    padding-bottom: var(--video-aspect-ratio);
    border-radius: var(--video-border-radius);
    background-color: var(--video-bg-color);
    box-shadow: var(--video-shadow);
}

.chart-container {
    height: var(--chart-height);
    min-height: var(--chart-min-height);
    border-radius: var(--chart-border-radius);
}
</style>
```

## 📝 样式组织规则

### 应该可配置的变量
- **主题颜色** (品牌颜色、背景色)
- **组件尺寸** (高度、宽度、间距)
- **交互状态** (悬停、激活、焦点颜色)  
- **排版缩放** (响应式设计的字体大小)
- **外部资源 URL** (随主题变化的图标、图片)
- **动画属性** (持续时间、缓动函数)
- **全局设计令牌** (间距比例、边框圆角比例、阴影比例)

### 保留在组件中的样式
- **布局特定样式** (flexbox、grid 配置)
- **组件特定定位** (该组件独有的)
- **动画定义** (组件特定的过渡)
- **结构样式** (display、position 值)

## 🚀 添加新样式

### 1. 新组件
将变量添加到适当的变量文件：
```css
/* config/component-variables.css */
:root {
    --new-component-bg: var(--vp-c-bg-soft);
    --new-component-padding: var(--spacing-md);
    --new-component-border-radius: var(--border-radius-md);
}
```

在组件中使用：
```vue
<style scoped>
.new-component {
    background-color: var(--new-component-bg);
    padding: var(--new-component-padding);
    border-radius: var(--new-component-border-radius);
}
</style>
```

### 2. 新变量
选择适当的变量文件：
- **基础变量**: 主题颜色、全局间距、设计令牌
- **组件变量**: 组件特定样式
- **图表变量**: 数据可视化和图表样式

### 3. 全局设计令牌
添加到 `base-variables.css`：
```css
:root {
    --new-spacing-token: 20px;
    --new-color-token: #1565c0;
    --new-shadow-token: 0 4px 12px rgba(0, 0, 0, 0.1);
}
```

## 🎨 主题系统

### 自动亮色/暗色主题支持
所有组件通过 CSS 变量自动继承主题值：

```css
:root {
    --button-bg-color: #c5a16b;
    --video-bg-color: #f9f9f9;
}

.dark {
    --button-bg-color: #2b4796;
    --video-bg-color: #333;
}
```

无需 JavaScript 主题切换 - 一切都由 CSS 处理！

## 📋 组件样式概述

### **完全组织化的组件**
所有组件都已更新为使用集中式 CSS 变量：

- ✅ **Buttons.vue**: 可配置颜色、尺寸、动画的浮动操作按钮
- ✅ **BilibiliVideo.vue** & **YoutubeVideo.vue**: 响应式视频容器  
- ✅ **ArticleMetadataCN.vue**: 可配置排版的文章元数据显示
- ✅ **ProgressLinear.vue**: 主题感知颜色的进度指示器
- ✅ **MNavLink.vue** & **MNavLinks.vue**: 响应式尺寸的导航组件
- ✅ **Linkcard.vue**: 带悬停效果的链接预览卡片
- ✅ **Footer.vue**: 主题感知文本颜色的网站页脚
- ✅ **PdfViewer.vue**: 一致样式的 PDF 显示组件
- ✅ **minecraft-advanced-damage-chart.vue**: 基于变量的图表组件
- ✅ **CommitsCounter.vue**: 主题集成的折线图
- ✅ **ResponsibleEditor.vue**: 一致样式的编辑器信息

### **关键改进**
- ✅ **模块化变量**: 组织成逻辑、可维护的文件
- ✅ **全局设计令牌**: 所有组件间一致的间距、边框、阴影
- ✅ **语义变量名**: 清晰、描述性的名称便于理解
- ✅ **消除硬编码值**: 所有颜色、尺寸、间距都使用变量
- ✅ **消除 JavaScript 主题切换**: 纯 CSS 主题处理
- ✅ **响应式设计**: 通过 CSS 变量实现移动端/桌面端变体
- ✅ **一致动画**: 标准化过渡持续时间和缓动
- ✅ **跨组件一致性**: 共享设计令牌确保视觉和谐

## ✅ 这种组织的好处

1. **单一导入**: 主题设置只需一个 CSS 导入
2. **模块化变量**: 易于查找和修改特定变量类别
3. **可维护**: 清晰的关注点分离和逻辑组织
4. **可配置**: 通过语义 CSS 变量易于自定义
5. **性能**: 优化加载和最小重复
6. **可扩展**: 易于添加新组件和设计令牌
7. **类型安全**: 与 VitePress TypeScript 配置兼容
8. **主题一致**: 自动亮色/暗色主题支持
9. **无 JavaScript 依赖**: 主题切换完全由 CSS 处理
10. **设计系统**: 所有组件间一致的设计令牌

## 🔄 迁移说明

- ❌ **已移除**: JavaScript 中的动态 CSS 变量设置
- ❌ **已移除**: 组件中的硬编码颜色、尺寸和间距
- ❌ **已移除**: 组件特定的主题切换逻辑
- ❌ **已移除**: 单一的 variables.css 文件
- ✅ **已添加**: 模块化变量组织（基础、组件、图表）
- ✅ **已添加**: 全局设计令牌系统
- ✅ **已添加**: 语义变量命名约定
- ✅ **已添加**: 响应式设计变量
- ✅ **已添加**: 动画和过渡标准化
- ✅ **简化**: 纯 CSS 主题切换
- ✅ **改进**: 组件可维护性和一致性

## 🛠️ 故障排除

### 样式缺失
如果重组后样式显示缺失：
1. 检查 CSS 变量是否在适当的变量文件中定义
2. 验证 `config/variables.css` 导入所有变量模块
3. 确保组件使用 CSS 变量而不是硬编码值
4. 确认为亮色和暗色模式都定义了主题特定变量

### 变量导入问题
如果变量未加载：
1. 检查 `config/variables.css` 中的导入路径
2. 验证所有变量文件存在于 `config/` 目录中
3. 确保 `styles/index.css` 导入 `config/variables.css`

### 主题切换问题
如果主题切换不正常工作：
1. 验证 CSS 变量在 `:root` 和 `.dark` 选择器中都有定义
2. 检查组件使用 CSS 变量而不是计算值
3. 确保没有 JavaScript 覆盖 CSS 变量值

### 性能问题
如果样式加载缓慢：
1. 检查主题设置中只导入了 `styles/index.css`
2. 验证组件中没有重复的 CSS 导入
3. 确保 CSS 变量没有在 JavaScript 中不必要地计算
4. 检查变量文件是否正确优化

## 📊 变量组织摘要

| 文件 | 用途 | 示例 |
|------|------|------|
| `base-variables.css` | 核心主题和设计令牌 | 颜色、间距、边框、阴影、动画 |
| `component-variables.css` | 组件特定样式 | 按钮尺寸、视频纵横比、导航尺寸 |
| `chart-variables.css` | 数据可视化 | 图表尺寸、颜色、渐变、工具提示 |

这种组织为 CryChicDoc 主题提供了可扩展、可维护的基础，可以轻松成长并适应未来的需求。 