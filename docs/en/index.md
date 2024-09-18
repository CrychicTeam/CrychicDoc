---
layout: home

hero:
  name: "CrychicDoc"
  text: "A Minecraft doc maintained by Crychic team."
  tagline: Contains documentation and resource indexes related to modpacks and Minecraft development.
  image: 
    light: /logo.png
    dark: /logodark.png
    alt: crychic
  actions:
    - theme: alt
      text: Collaboration
      link: /en/doc/rules
    - theme: brand
      text: KubeJS
      link: /en/modpack/kubejs/
    - theme: brand
      text: Mods
      link: /en/mods/
    - theme: brand
      text: Modpacks
      link: /en/modpack/
    - theme: brand
      text: Developers
      link: /en/developers/

features:
  - icon : 
      light: /icon/mainindex/material-symbols--markdown-copy-sharp.png
      dark: /icon/mainindex/material-symbols--markdown-copy-sharp-dark-v2.png
    title: Easy to Get Started
    details: Build documentation using Vitepress, simply edit Markdown files to write content.
  
  - icon : 
      light: /icon/mainindex/mdi--professional-hexagon.png
      dark: /icon/mainindex/mdi--professional-hexagon-dark-v2.png
    title: Highly Extensible
    details: Equipped with features like Mermaid, syntax highlighting, and type variable display. It also includes various components from Vuetify to enhance the readability and professionalism of shared content.
  - icon : 
      light: /icon/mainindex/dashicons--format-chat.png
      dark: /icon/mainindex/dashicons--format-chat-dark-v2.png
    title: Easy Collaboration
    details: Collaborate using GitHub to simplify the co-creation process and ensure the security of source code.
  - icon : 
      light: /icon/mainindex/dashicons--privacy.png
      dark: /icon/mainindex/dashicons--privacy-dark-v2.png
    title: Easy Accessibility
    details: Build using GitHub Flow, send it to a private repository, and then transfer it to a physical server. This optimizes the access speed for users in China while ensuring the stability of the website for all users by using Cloudflare CDN.
gitChangelog: false
---

<commitsCounter
  username="CrychicTeam"
  repoName="CrychicDoc"
  :daysToFetch="60"
/>