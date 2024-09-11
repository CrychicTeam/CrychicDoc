---
layout: doc
title: 项目合作教程
noguide: true
root:
  title: 文档相关
  collapsed: false
  children:
      - title: 项目合作教程
        path: /
        noScan: true
        file: index
      - title: 环境配置与预览
        path: /
        noScan:  true
        file:  dev
      - title: 侧边栏设置教程
        path: /
        noScan: true
        file: sidebarTutorial
      - title: 文档插件示例
        path: /
        noScan: true
        file: samples
      - title: 类型检查示例
        path: /
        noScan: true
        file: test
      - title: 文档样式一览
        path: /
        noScan: true
        file: styleList
prev: false
next: false
---

# 项目合作教程

> 本文简要概述了如何通过Git命令行来初始化项目并进行合作开发。

> 你需要[Git](https://git-scm.com/downloads)来进行合作开发。

## 初始化项目

1. **Fork 项目：**

打开你要进行内容修改的项目仓库，下文以CrychicDoc为例进行说明。

打开[CrychicDoc 仓库](https://github.com/CrychicTeam/CrychicDoc)，点击右上角的 "Fork" 按钮，将项目 Fork 到你的 GitHub 账户中。

2. **初始化仓库：**
   
打开命令行或终端或直接在你的实例文件夹执行以下命令：

::: code-group
```bash [git]
# 修改为实际的文件夹地址如.minecraft或启动器内的版本名称，或者任一你选择的空白文件夹。
# 在这种情况下，输入您想要存放CrychicDoc代码的文件夹。
cd path/to/your/project
git init
# 此处以CrychicDoc举例，要为它提供代码的话，需要修改拉取fork后的仓库代码。
# 将YourUsername修改为你的GithubID，你可以直接使用fork后的仓库链接。
git remote add origin https://github.com/YourUsername/CrychicDoc.git
git pull origin main
```
:::

3. **设置上游仓库：**

为了保持与原始仓库的同步，设置上游仓库：

::: code-group
```bash [git]
git remote add upstream https://github.com/CrychicTeam/CrychicDoc.git
```
:::

## 合作流程

4. **创建并切换到新分支：**

首先，建议从主分支创建一个新的特性分支：

::: code-group
```bash [git]
git checkout -b feature-branch
```
::: 

5. **修改代码：**

在你的Minecraft实例文件夹中，编辑项目的代码、资源文件或配置文件，根据你的需求进行修改和调整。

6. **提交更改：**

保存修改后，使用以下命令将更改提交到你的本地仓库：

::: code-group
```bash [git]
# 一般直接add .不会出现问题，但保险起见可以将.替换为具体的文件路径。
git add .
git commit -m "描述你的修改内容"
```
:::

7. **推送到远程仓库：**

如果你准备将你的更改合并到主分支中，将你的本地分支推送到远程仓库：

::: code-group
```bash [git]
git push
```
:::


## 提交 Pull Request (PR)

8. **创建 Pull Request：**

打开你 Fork 的 GitHub 仓库页面，选择你刚刚推送的特性分支，点击“Compare & pull request”，填写相关信息并提交PR。

9.  **等待审查和合并：**

最好能在提交前提前告知开发者。

## 结束合作

10. **删除本地和远程分支：**

一旦你的PR被合并，你可以安全地删除新建的特性分支：

::: code-group
```bash [git]
git checkout main
git branch -d feature-branch # 删除本地分支
git push origin --delete feature-branch # 删除远程分支
```
:::

::: details 工作流示意图
<ClientOnly>

```mermaid
graph TD
    A([开始]) --> B[Fork 项目]
    B --> C[初始化本地仓库]
    C --> D[设置上游仓库]
    D --> E[创建新分支]
    E --> F[修改代码]
    F --> G[提交更改]
    G --> H[推送到远程仓库]
    H --> I[创建 Pull Request]
    I --> J[等待审查和合并]
    J --> K{PR 是否被合并?}
    K -->|是| L[删除本地和远程分支]
    K -->|否| F
    L --> M[更新 CHANGELOG 和 DevDocuments]
    M --> N([结束])

    class A,N fill:#e6f3ff,stroke:#4a86e8,stroke-width:2px
    class B,C,D,E,F,G,H,I,J,L,M fill:#fff2cc,stroke:#d6b656,stroke-width:2px
    class K fill:#d5e8d4,stroke:#82b366,stroke-width:2px

    linkStyle default stroke:#4a86e8,stroke-width:2px
```

</ClientOnly>

:::

## 规范

在每次进行上述步骤时，如果你的仓库落后于原仓库，请点击`Sync fork`按钮来同步进度。


::: details
> 如果你参与的项目为Crychic的Minecraft-Hunt，在你完成某项工作时，需要在CHANGELOG与DevDocuments中写入你所提供的贡献。<br/>
> 如果你作为外部开发者为Crychic尤其为CrychicDoc贡献了任何资料与文档的内容，请在新增事项后留下您的署名，非常感谢您的付出。<br/>
> 如果您希望规范自己在CrychicDoc中的署名使其更加得体。请联系我们。<br/>
> 例如：我完成了这个更改 => 2024/某月、某日 - 我的名字
:::

:::v-success 恭喜你！
你现在已经是Pull Request大师啦！
:::
