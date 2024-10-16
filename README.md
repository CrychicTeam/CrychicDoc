<div align="center"><img height="200" src="docs/public/logo.png" width="200"/></div>

![GitHub Release Date](https://img.shields.io/github/created-at/CrychicTeam/CrychicDoc) [![crychicdoc](https://img.shields.io/badge/CrychicDoc-Maintaining-green)](https://docs.mihono.cn)


 ![GitHub issue custom search in repo](https://img.shields.io/github/issues/CrychicTeam/CrychicDoc.svg) ![GitHub last commit](https://img.shields.io/github/last-commit/CrychicTeam/CrychicDoc) ![Total Commits](https://img.shields.io/github/commit-activity/t/CrychicTeam/CrychicDoc) ![GitHub branch status](https://img.shields.io/github/check-runs/CrychicTeam/CrychicDoc/main)  ![cc-by-sa-shield](https://img.shields.io/badge/License-CC%20BY--SA%204.0-lightgrey.svg)
![GitHub contributors](https://img.shields.io/github/contributors/CrychicTeam/CrychicDoc)

## 协议

本项目遵循[知识共享署名-相同方式共享 4.0 国际许可协议
](LICENSE)进行许可。

转载请附上本作品链接： https://docs.mihono.cn

> **本项目/团队Logo由[幻月の银翼](https://www.pixiv.net/users/7865026)老师创作经授权进行非商业行为的使用，请勿取走来用作任何商业行为。**

# 你好

You may find English version of readme on [here](/READMEEN.md).

这里是Cryhic文档的源代码库。

我一直认为最能够推动社区创作环境的是详细而生动的文档，因此我计划并维护了这个文档计划，来帮助开发者与玩家获得更好的开发与游玩体验（尤其在模组环境）。

## 目录介绍

```markdown
- crychicdoc
    - .github/    自动构建脚本
    - .vitepress/
        - config/ 本地化配置文件。
        - plugins/ mdit插件存放位置
        - theme/  自定义主题和组件
        - config.mts Vitepress的配置文件
        - index.ts 侧边栏的配置文件。
    - .vscode/ md-snippets
    - docs
        - public 存放静态文件
        - zh 简体中文内容
        - en 英文内容
    - README.md  本文件
    - .gitignore gitignore文件。
    - ExtractClassScript.js 请忽视
    - extracted_classes.md 请忽视
    - LICENSE CC BY-SA 4.0
```

## 共创

本网站使用[Vitepress](https://vitepress.dev/)作为静态站点生成器。我们推荐使用VSCode编辑器进行开发。

1. 安装环境[Node.js](https://nodejs.org/zh-cn/download/prebuilt-installer)
2. 下载安装[yarn](https://classic.yarnpkg.com/lang/en/docs/install/#windows-stable)(你需要确保自己确实有着安装yarn的权限，mac/linux应使用sudo -s而Windows用户需要以管理员身份运行命令提示符（cmd）或 PowerShell后再npm install -g yarn)
3. 克隆仓库到本地
4. 在终端输入`yarn install`安装依赖
5. 在终端输入`npm run dev`启动本地服务，可在浏览器预览
6. 剩下的可以查看该[文档](https://vitepress.yiov.top/preface.html)与[官方文档](https://vitepress.dev/zh/)来获得更详细具体的教程。


本文档使用安全稳定的的部署方案，可长期稳定运行并保证不同地区玩家的访问：
1. 使用[github action](.github/workflows/build.yaml)构建好网页的静态文件后上传至private仓库并转发至物理服务器。
2. 使用阿里云的GeoDNS将国内外的流量分流，国外玩家访问时将自动访问[CloudFlare](https://cloudflare.com/)加速后的数据。

> 域名在阿里云购买
