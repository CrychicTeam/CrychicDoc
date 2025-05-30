---
layout: home

hero:
  name: "CrychicDoc"
  text: "一个由PickAID团队维护的我的世界文档."
  tagline: 存放了整合包与我的世界开发相关的文档与资料索引。
  image:
    light: logo.png
    dark: logodark.png
    alt: PickAID
  actions:
    - theme: alt
      text: 合作
      link: /zh/doc/rules
    - theme: brand
      text: KubeJS
      link: /zh/modpack/kubejs/
    - theme: brand
      text: 模组
      link: /zh/mods/
    - theme: brand
      text: 整合包
      link: /zh/modpack/
    - theme: brand
      text: 开发
      link: /zh/develop/

features:
  - icon : 
      light: /icon/mainindex/material-symbols--markdown-copy-sharp.png
      dark: /icon/mainindex/material-symbols--markdown-copy-sharp-dark-v2.png
    title: 易上手
    details: 使用Vitepress构建文档，只需编辑Markdown文件即可撰写内容。
  
  - icon : 
      light: /icon/mainindex/mdi--professional-hexagon.png
      dark: /icon/mainindex/mdi--professional-hexagon-dark-v2.png
    title: 高拓展
    details: 具备Mermaid、语法高亮与类型变量显示等功能，同时拥有Vuetify的各种组件，帮助丰富分享内容的可读性与专业性。
  - icon : 
      light: /icon/mainindex/dashicons--format-chat.png
      dark: /icon/mainindex/dashicons--format-chat-dark-v2.png
    title: 易合作
    details: 使用Github进行合作，简化共创流程且使得源码较为安全。
  - icon : 
      light: /icon/mainindex/dashicons--privacy.png
      dark: /icon/mainindex/dashicons--privacy-dark-v2.png
    title: 易访问
    details: 使用Github Flow进行构建后发送给私人仓库再转运至物理服务器，优化中国用户的访问速度同时保证了网页的稳定性。
gitChangelog: false
---

<commitsCounter
  username="PickAID"
  repoName="CrychicDoc"
  :daysToFetch="120"
/>