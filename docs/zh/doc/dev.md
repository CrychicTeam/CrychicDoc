---
title: 环境配置与预览
outline: [2,2]
---

# {{ $frontmatter.title }}

## 简述

在您向本站贡献文件时，并无环境配置需求，正常编写md文档并使用git进行合作即可。内容在审核通过，且合并到主分支后，即可在文档网址查看最新的内容。

若您并无++本站前端开发++或++本地预览内容++需求，可放心略过此文档。

## 前置库 {#lib}

由于本站基于[VitePress](https://vitepress.dev/zh/)构建，该框架需求[Node.js](https://nodejs.org/zh-cn)（下文简称“node”）版本为18及以上。

因此，请先使用命令行管理器（CLI）（下文简称“命令行”）运行以下命令以检查您的node版本

::: code-group
```sh [sh]
node --version
```
:::

若您未安装node或版本过低，请先 安装/升级 node。

笔者推荐使用[nvm](https://nvm.uihtm.com/)进行node版本管理，详细使用教程请阅读nvm官网文档。

## 包管理器 {#package-manager}

尽管在node安装时，会附带原生的包管理器[NPM](https://dev.nodejs.cn/learn/an-introduction-to-the-npm-package-manager/)，但由于其使用单线程解析依赖项，加之大陆网络环境问题，使用体验相对较差。

因此，我们可以选用Facebook开发的包管理器[Yarn](https://yarnpkg.com/)，互联网上有很多相关的详尽安装教程，在此便不多赘述。

::: details 那PNPM呢？

由于PNPM采用了不同的依赖项解析方法，在本项目使用其将会导致依赖缺失问题，故不推荐。

:::

## 安装 {#install}

>[!CAUTION] 警告
>使用PNPM将会导致依赖缺失问题。

您需要在您克隆到本地的项目根目录打开命令行并输入以下指令

::: code-group
```sh [npm]
npm install
```
```sh [yarn]
yarn install
```
:::

## 本地预览 {#preview}

### 使用包管理器 {#use-pm}

>[!CAUTION] 警告
>使用PNPM将会导致依赖缺失问题。

在项目根目录打开命令行并输入以下指令

::: code-group
```sh [npm]
npm docs:dev
```
```sh [yarn]
yarn docs:dev
```
:::

### 使用Visual Studio Code {#use-vsc}

当您使用Visual Studio Code（下文简称“vsc”）作为本项目IDE时，在将项目根目录添加到工作区后，可以在vsc任意位置按下`F5`以快速启动预览服务器。

编辑器会自动为您在终端中执行`yarn docs:dev`命令

当您的命令行或vsc终端中出现如下字样，即为成功启动预览服务器

```sh
vitepress v1.3.1

➜  Local:   http://localhost:5173/
➜  Network: use --host to expose
➜  press h to show help
```

此时打开本地连接`http://localhost:5173/`即可预览您编写的内容。

>[!NOTE] 提示
> 在命令行或vsc终端中键入`O`即可使用默认浏览器快速打开链接。  
> 键入`Q`以关闭预览服务器，亦可使用`Ctrl+C`中断进程。