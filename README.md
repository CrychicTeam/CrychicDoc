
<div align="center"><img height="200" src="docs/public/logo.png" width="200"/></div>

## 协议

本项目使用自由的[MIT许可](LICENSE)开源。

# 你好

You may find English version of readme on [here](/READMEEN.md).

这里是Cryhic文档的源代码库。

## 目录介绍

```markdown
- crychicdoc
    - .github    自动构建脚本
    - .vitepress
        - config VitePress的配置文件
        - theme  自定义主题和组件
    - .vscode md snippets
    - docs
        - public 存放静态文件
        - zh 简体中文内容
        - en 英文内容
    - public     存放静态文件如图片
    - README.md  本文件
```

## 共创

本网站使用[Vitepress](https://vitepress.dev/)作为静态站点生成器。我们推荐使用VSCode编辑器进行开发。

1. 安装环境[Node.js](https://nodejs.org/zh-cn/download/prebuilt-installer)
2. 下载安装[yarn](https://classic.yarnpkg.com/lang/en/docs/install/#windows-stable)(你需要确保自己确实有着安装yarn的权限，mac/linux应使用sudo -s而Windows用户需要以管理员身份运行命令提示符（cmd）或 PowerShell后再npm install -g yarn)
3. 克隆仓库到本地
4. 在终端输入`yarn install`安装依赖
5. 在终端输入`npm run dev`启动本地服务，可在浏览器预览


本文档使用安全稳定的的部署方案，可长期稳定运行并保证不同地区玩家的访问：
1. 使用[github action](.github/workflows/build.yaml)构建好网页的静态文件后上传至private仓库并转发至物理服务器。
2. 使用阿里云的GeoDNS将国内外的流量分流，国外玩家访问时将自动访问[CloudFlare](https://cloudflare.com/)加速后的数据。

> 域名在阿里云购买