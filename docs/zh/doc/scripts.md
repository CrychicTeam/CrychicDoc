---
title: 脚本工具使用指南
description: 本站脚本工具的完整使用指南，包括 frontmatter 编辑器、侧边栏构建器、标题同步器和 index.md 生成器
progress: 100
state: published
priority: 10000000
---

# {{ $frontmatter.title }}

## 简述 {#intro}

本站提供了多个强大的脚本工具来帮助维护和管理文档内容。这些工具可以自动化许多重复性任务，提高文档维护效率。

所有脚本都位于 `.vitepress/scripts/` 目录下，并已集成到 `package.json` 中，可以通过 npm 命令直接使用。

## 可用脚本概览 {#overview}

| 脚本名称               | 命令                         | 用途                                 | 状态        |
| ---------------------- | ---------------------------- | ------------------------------------ | ----------- |
| **Frontmatter 编辑器** | `npm run update-frontmatter` | 批量更新 markdown 文件的 frontmatter | ✅ 可用     |
| **标题同步器**         | `npm run update-titles`      | 同步 index.md 标题到 locales.json    | ✅ 可用     |
| **侧边栏构建器**       | `npm run docs:build`         | 构建侧边栏配置                       | ✅ 自动集成 |
| **Index.md 生成器**    | `npm run create-indexes`     | 为缺少 index.md 的文件夹创建索引文件 | ✅ 可用     |

## Frontmatter 编辑器 {#frontmatter-editor}

### 功能描述 {#frontmatter-desc}

Frontmatter 编辑器是最强大的脚本工具，可以批量更新 markdown 文件的 frontmatter 字段。支持递归处理文件夹及其子文件夹中的所有 markdown 文件。

### 支持的字段 {#supported-fields}

| 字段名         | 类型                | 描述              | 示例                                     |
| -------------- | ------------------- | ----------------- | ---------------------------------------- |
| `title`        | string              | 文章标题          | `"环境配置指南"`                         |
| `editor`       | string              | 编辑者名称        | `"张三"`                                 |
| `authors`      | string[]            | 作者列表          | `["李四", "王五"]`                       |
| `description`  | string              | 文章描述          | `"本文介绍如何配置开发环境"`             |
| `progress`     | number              | 编写进度 (0-100)  | `85`                                     |
| `state`        | string              | 文章状态          | `"published"` / `"draft"` / `"outdated"` |
| `outline`      | number[] \| boolean | 大纲层级          | `[2, 3]` / `false`                       |
| `showComment`  | boolean             | 显示评论区        | `true` / `false`                         |
| `gitChangelog` | boolean             | 显示 Git 变更日志 | `true` / `false`                         |
| `noguide`      | boolean             | 是否在侧边栏显示  | `true` / `false`                         |
| `backPath`     | string              | 返回按钮路径      | `"../"`                                  |
| `layout`       | string              | 页面布局          | `"doc"` / `"home"`                       |
| `tags`         | string[]            | 文章标签          | `["教程", "配置"]`                       |

### 基本用法 {#frontmatter-usage}

#### 更新单个字段

```bash
# 为 docs/zh 目录下所有 md 文件设置编辑者
npm run update-frontmatter -- -p docs/zh -e "张三"

# 为 docs/zh/guide 目录设置作者
npm run update-frontmatter -- -p docs/zh/guide -a "李四,王五"

# 设置文章标题
npm run update-frontmatter -- -p docs/zh/api --title "API 参考文档"
```

#### 更新多个字段

```bash
# 同时设置多个字段
npm run update-frontmatter -- -p docs/zh/tutorial \
  --title "教程文档" \
  --editor "张三" \
  --authors "李四,王五" \
  --description "完整的教程文档" \
  --progress 90 \
  --state published
```

#### 安全操作选项

```bash
# 预览更改（不实际修改文件）
npm run update-frontmatter -- -p docs/zh/guide -e "张三" --dry-run

# 创建备份文件
npm run update-frontmatter -- -p docs/zh/guide -e "张三" --backup

# 显示详细信息
npm run update-frontmatter -- -p docs/zh/guide -e "张三" --verbose

# 强制覆盖现有值
npm run update-frontmatter -- -p docs/zh/guide -e "新编辑者" --force
```

### 高级功能 {#frontmatter-advanced}

#### 文件过滤

```bash
# 排除特定文件夹
npm run update-frontmatter -- -p docs -e "张三" --exclude "**/temp/**" --exclude "**/draft/**"

# 只处理特定模式的文件
npm run update-frontmatter -- -p docs --include "**/*guide*.md" -e "张三"
```

#### 作者管理操作

```bash
# 添加新作者到现有列表
npm run update-frontmatter -- -p docs/zh/guide --add-author "新作者"

# 移除特定作者
npm run update-frontmatter -- -p docs/zh/guide --remove-author "旧作者"

# 替换作者名称
npm run update-frontmatter -- -p docs/zh/guide --replace-author "旧名称,新名称"

# 组合操作：先移除旧作者，再添加新作者
npm run update-frontmatter -- -p docs/zh/guide --remove-author "作者A" --add-author "作者B"
```

#### 批量状态管理

```bash
# 将所有草稿标记为已发布
npm run update-frontmatter -- -p docs/zh --state "published" --dry-run

# 批量设置进度
npm run update-frontmatter -- -p docs/zh/completed --progress 100
```

### 完整参数列表 {#frontmatter-params}

| 参数               | 简写 | 描述                           | 示例                           |
| ------------------ | ---- | ------------------------------ | ------------------------------ |
| `--path`           | `-p` | 目标文件夹路径（必需）         | `-p docs/zh`                   |
| `--title`          | `-t` | 设置标题                       | `-t "我的文档"`                |
| `--editor`         | `-e` | 设置编辑者                     | `-e "张三"`                    |
| `--authors`        | `-a` | 设置作者（逗号分隔，替换全部） | `-a "李四,王五"`               |
| `--add-author`     |      | 添加新作者到现有列表           | `--add-author "张三"`          |
| `--remove-author`  |      | 从列表中移除作者               | `--remove-author "李四"`       |
| `--replace-author` |      | 替换指定作者                   | `--replace-author "旧名,新名"` |
| `--description`    | `-d` | 设置描述                       | `-d "文档描述"`                |
| `--progress`       |      | 设置进度                       | `--progress 85`                |
| `--state`          |      | 设置状态                       | `--state published`            |
| `--outline`        |      | 设置大纲                       | `--outline "2,3"`              |
| `--no-comment`     |      | 禁用评论                       | `--no-comment`                 |
| `--no-changelog`   |      | 禁用变更日志                   | `--no-changelog`               |
| `--no-guide`       |      | 不在侧边栏显示                 | `--no-guide`                   |
| `--layout`         |      | 设置布局                       | `--layout doc`                 |
| `--tags`           |      | 设置标签                       | `--tags "教程,配置"`           |
| `--dry-run`        |      | 预览模式                       | `--dry-run`                    |
| `--backup`         | `-b` | 创建备份                       | `--backup`                     |
| `--verbose`        | `-v` | 详细输出                       | `--verbose`                    |
| `--force`          | `-f` | 强制覆盖                       | `--force`                      |
| `--help`           | `-h` | 显示帮助                       | `--help`                       |

## 标题同步器 {#title-sync}

### 功能描述 {#title-sync-desc}

标题同步器可以自动将 `index.md` 文件中的 `title` frontmatter 同步到对应的 `locales.json` 文件中，用于侧边栏显示。

### 使用方法 {#title-sync-usage}

```bash
# 更新所有语言的标题
npm run update-titles

# 只更新中文标题
npm run update-titles zh

# 更新多个语言
npm run update-titles en zh

# 查看帮助
npm run update-titles -- --help
```

### 工作原理 {#title-sync-workflow}

1. **扫描文件**：查找包含 `title` frontmatter 的 `index.md` 文件
2. **提取标题**：解析 frontmatter 中的 `title` 字段
3. **同步配置**：将标题写入对应的 `locales.json` 文件
4. **保护数据**：完全保留其他已有配置

::: demo 处理示例
**处理前**：

```yaml
# docs/zh/guide/index.md
---
title: 用户指南
root: true
---
```

**自动同步后**：

```json
// .vitepress/config/sidebar/zh/guide/locales.json
{
    "_self_": "用户指南",
    "getting-started.md": "快速开始",
    "advanced.md": "高级功能"
}
```

:::

## Index.md 生成器 {#index-generator}

### 功能描述 {#index-gen-desc}

Index.md 生成器可以扫描指定目录，为所有缺少 `index.md` 文件的文件夹自动创建索引文件。生成的 index.md 文件包含适当的 frontmatter 配置。

### 使用方法 {#index-gen-usage}

```bash
# 为指定目录创建缺少的 index.md 文件
npm run create-indexes -- -p docs/zh/guide

# 预览将要创建的文件
npm run create-indexes -- -p docs/zh --dry-run

# 使用自定义模板
npm run create-indexes -- -p docs/zh --template advanced

# 显示详细信息
npm run create-indexes -- -p docs/zh --verbose
```

### 模板类型 {#index-templates}

#### 基础模板 (default)

```yaml
---
title: 目录名称
root: true
---
# {{ $frontmatter.title }}

这是 `目录名称` 的索引页面。

请在此添加该部分的介绍内容。
```

#### 高级模板 (advanced)

```yaml
---
title: 目录名称
description: 目录名称相关文档
progress: 0
state: draft
root: true
outline: [2, 3]
showComment: true
gitChangelog: true
---

# {{ $frontmatter.title }}

## 简述 {#intro}

这是 `目录名称` 的索引页面。

## 内容概览 {#overview}

请在此添加该部分的介绍内容和导航信息。

## 相关链接 {#links}

- [相关文档](./related.md)
- [更多资源](./resources.md)
```

### 参数选项 {#index-gen-params}

| 参数         | 简写 | 描述                 | 默认值    |
| ------------ | ---- | -------------------- | --------- |
| `--path`     | `-p` | 目标目录路径（必需） | -         |
| `--template` | `-t` | 模板类型             | `default` |
| `--dry-run`  | `-d` | 预览模式             | `false`   |
| `--verbose`  | `-v` | 详细输出             | `false`   |
| `--force`    | `-f` | 覆盖现有文件         | `false`   |
| `--exclude`  |      | 排除模式             | `[]`      |

## 侧边栏构建器 {#sidebar-builder}

### 功能描述 {#sidebar-desc}

侧边栏构建器在文档构建过程中自动运行，负责生成侧边栏配置。详细配置请参考 [侧边栏配置指南](./sidebarTutorial.md)。

### 自动触发 {#sidebar-auto}

```bash
# 构建文档时自动运行
npm run docs:build

# 开发模式下自动监听
npm run docs:dev
```

### 配置文件 {#sidebar-config}

-   **全局配置**：`.sidebarrc.yml`
-   **本地配置**：各目录的 `index.md` frontmatter
-   **覆盖配置**：`.vitepress/config/sidebar/` 下的 JSON 文件

## 最佳实践 {#best-practices}

### 工作流建议 {#workflow}

1. **项目初始化**：

    ```bash
    # 1. 为新项目创建 index.md 文件
    npm run create-indexes -- -p docs/zh --template advanced

    # 2. 设置基础 frontmatter
    npm run update-frontmatter -- -p docs/zh --editor "你的名字" --state draft

    # 3. 同步标题
    npm run update-titles
    ```

2. **内容维护**：

    ```bash
    # 1. 预览更改
    npm run update-frontmatter -- -p docs/zh/guide --progress 90 --dry-run

    # 2. 应用更改
    npm run update-frontmatter -- -p docs/zh/guide --progress 90 --backup

    # 3. 同步标题
    npm run update-titles zh
    ```

3. **发布准备**：

    ```bash
    # 1. 标记为已完成
    npm run update-frontmatter -- -p docs/zh/completed --progress 100 --state published

    # 2. 构建文档
    npm run docs:build
    ```

### 安全注意事项 {#safety}

::: v-warning 重要提醒

-   **始终先使用 `--dry-run`** 预览更改
-   **重要操作前使用 `--backup`** 创建备份
-   **避免使用 `--force`** 除非确实需要覆盖
-   **定期提交到 Git** 保存工作进度
    :::

### 性能优化 {#performance}

-   **批量操作**：一次处理多个字段比多次单独操作更高效
-   **路径精确化**：指定具体路径而不是根目录可以提高速度
-   **排除无关文件**：使用 `--exclude` 跳过不需要的文件夹

## 故障排除 {#troubleshooting}

### 常见问题 {#common-issues}

#### 权限错误

```bash
# 确保有文件写入权限
chmod +w docs/zh/target-folder/
```

#### 路径不存在

```bash
# 检查路径是否正确
ls -la docs/zh/your-path/
```

#### frontmatter 解析错误

```bash
# 检查 frontmatter 语法
npm run update-frontmatter -- -p docs/zh/problematic-file.md --verbose
```

### 调试技巧 {#debugging}

```bash
# 使用详细模式查看处理过程
npm run update-frontmatter -- -p docs/zh --verbose --dry-run

# 查看具体错误信息
DEBUG=* npm run update-frontmatter -- -p docs/zh -e "测试"

# 检查生成的配置
cat .vitepress/config/sidebar/zh/guide/locales.json
```

### 恢复和备份 {#backup-restore}

```bash
# 恢复备份文件
mv docs/zh/guide/index.md.bak docs/zh/guide/index.md

# 批量恢复备份
find docs/zh -name "*.bak" -exec sh -c 'mv "$1" "${1%.bak}"' _ {} \;

# 清理备份文件
find docs/zh -name "*.bak" -delete
```

## 脚本开发 {#development}

### 添加新脚本 {#new-script}

1. **创建脚本文件**：在 `.vitepress/scripts/` 下创建 `.mjs` 文件
2. **更新 package.json**：添加对应的 npm 命令
3. **遵循现有模式**：使用类似的参数解析和错误处理
4. **添加文档**：更新本文档说明新脚本的用法

### 贡献指南 {#contributing}

如果您发现脚本问题或有改进建议，欢迎：

-   🐛 提交 Issue 报告问题
-   💡 提出功能建议
-   🔧 提交 Pull Request 改进脚本
-   📚 完善文档说明

---

## 总结 {#summary}

本站的脚本工具集提供了强大的文档管理能力：

-   **📝 Frontmatter 编辑器**：批量管理文档元数据
-   **🔄 标题同步器**：自动同步侧边栏标题
-   **📁 Index.md 生成器**：快速创建索引文件
-   **⚙️ 侧边栏构建器**：智能生成导航结构

合理使用这些工具可以大大提高文档维护效率，确保内容的一致性和规范性。

::: v-success 提示
建议收藏本页面，在日常文档维护工作中随时参考各种命令和最佳实践。
:::
