
# KubeJS Tutorial-1.20.1

This is the content sharing for KubeJS 1.20.1, including tutorials and projects.

- Access the project directly on [GitBook webpage](https://gumeng.gitbook.io/kubejs-jiao-cheng-1.20.1)
- The project's open source address on [Gitee](https://gitee.com/gumengmengs/kubejs-course)
- The project's open source address on [GitHub](https://github.com/Gu-meng/kubejs-course) (GitHub does not support content submission)

<!-- Repository activity -->
<!-- ::: info Repository Activity
<div style="display: flex; justify-content: center;">
<commitsCounter
  username="Gu-meng"
  repoName="kubejs-course"
  :daysToFetch="90"
/>
</div>
::: -->

- [Issue feedback](https://gitee.com/gumengmengs/kubejs-course/issues/new/choose)

## About Writing Documentation
### Documentation Requirements
1. The first letter of files and folders should be capitalized.
2. All newly created files need to be written in the SUMMARY for directory compilation.
3. Document content must have a clear topic and avoid inconsistency.
4. Documents should be simple and easy to understand. If there are any technical terms, explain them separately.

### About Documentation Folders
- KubeJS-Basics: Contains basic KubeJS content.
- KubejsAdvanced: Contains advanced KubeJS content.
- KubejsAddon: Contains content related to KubeJS peripheral modules.
- Digression: Contains information unrelated to KubeJS code.
- Texture: Contains assets resource content.
- KubejsProjects: Contains projects written by everyone (the folder path represents the corresponding sharer).
- Files: Contains downloadable resources mentioned in the document.
- imgs: Contains images mentioned in the document, with the corresponding path representing their location (use absolute paths).
- Code: Contains shared code.
  - Projects: Contains code for project sharing.
  - This: Contains files mentioned in the tutorial.
  - You can also create your own folders for classification, with the folder name matching the document name.

### About Documentation Rules
- The document contains three types of table markers: `-`, `~`, and `?`. `-` represents none, `~` represents to be edited, and `?` represents unknown.
* There are three types of table markers in the document: `-`, `~`, and `?`. `-` represents none, `~` represents to be edited, and `?` represents unknown.
* Images must use **absolute paths**.

<script setup>
import { VPTeamMembers } from 'vitepress/theme'

const members = [
  {
    avatar: 'https://www.github.com/Gu-meng.png',
    name: 'Gu-meng',
    title: 'The maintainer of Original Documentation',
    links: [
      { icon: 'github', link: 'https://github.com/Gu-meng' },
      { icon: {
        svg: '<svg t="1725476092474" class="icon" viewBox="0 0 1129 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4271" width="200" height="200"><path d="M234.909 9.656a80.468 80.468 0 0 1 68.398 0 167.374 167.374 0 0 1 41.843 30.578l160.937 140.82h115.07l160.936-140.82a168.983 168.983 0 0 1 41.843-30.578A80.468 80.468 0 0 1 930.96 76.445a80.468 80.468 0 0 1-17.703 53.914 449.818 449.818 0 0 1-35.406 32.187 232.553 232.553 0 0 1-22.531 18.508h100.585a170.593 170.593 0 0 1 118.289 53.109 171.397 171.397 0 0 1 53.914 118.288v462.693a325.897 325.897 0 0 1-4.024 70.007 178.64 178.64 0 0 1-80.468 112.656 173.007 173.007 0 0 1-92.539 25.75h-738.7a341.186 341.186 0 0 1-72.421-4.024A177.835 177.835 0 0 1 28.91 939.065a172.202 172.202 0 0 1-27.36-92.539V388.662a360.498 360.498 0 0 1 0-66.789A177.03 177.03 0 0 1 162.487 178.64h105.414c-16.899-12.07-31.383-26.555-46.672-39.43a80.468 80.468 0 0 1-25.75-65.984 80.468 80.468 0 0 1 39.43-63.57M216.4 321.873a80.468 80.468 0 0 0-63.57 57.937 108.632 108.632 0 0 0 0 30.578v380.615a80.468 80.468 0 0 0 55.523 80.469 106.218 106.218 0 0 0 34.601 5.632h654.208a80.468 80.468 0 0 0 76.444-47.476 112.656 112.656 0 0 0 8.047-53.109v-354.06a135.187 135.187 0 0 0 0-38.625 80.468 80.468 0 0 0-52.304-54.719 129.554 129.554 0 0 0-49.89-7.242H254.22a268.764 268.764 0 0 0-37.82 0z m0 0" fill="#20B0E3" p-id="4272"></path><path d="M348.369 447.404a80.468 80.468 0 0 1 55.523 18.507 80.468 80.468 0 0 1 28.164 59.547v80.468a80.468 80.468 0 0 1-16.094 51.5 80.468 80.468 0 0 1-131.968-9.656 104.609 104.609 0 0 1-10.46-54.719v-80.468a80.468 80.468 0 0 1 70.007-67.593z m416.02 0a80.468 80.468 0 0 1 86.102 75.64v80.468a94.148 94.148 0 0 1-12.07 53.11 80.468 80.468 0 0 1-132.773 0 95.757 95.757 0 0 1-12.875-57.133V519.02a80.468 80.468 0 0 1 70.007-70.812z m0 0" fill="#20B0E3" p-id="4273"></path></svg>'
      }, link: 'https://space.bilibili.com/16632546' },
      { icon: {
          svg: '<svg t="1725435852282" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5271" width="200" height="200"><path d="M512 1000.12c-268.466 0-488.12-219.654-488.12-488.12S243.533 23.88 512 23.88 1000.12 243.533 1000.12 512 780.467 1000.12 512 1000.12z m247.111-543.034H481.492c-12.203 0-24.406 12.203-24.406 24.406v61.016c0 12.203 12.203 24.406 24.406 24.406h167.792c12.203 0 24.406 12.203 24.406 24.406v12.203c0 39.66-33.558 73.218-73.218 73.218H371.665c-12.203 0-24.406-12.203-24.406-24.406V420.477c0-39.66 33.559-73.218 73.218-73.218h338.634c12.203 0 24.406-12.203 24.406-24.406v-61.015c0-12.203-12.203-24.406-24.406-24.406H420.477c-100.675 0-179.994 82.37-179.994 179.995V756.06c0 12.203 12.203 24.406 24.406 24.406h356.938c88.472 0 161.69-73.218 161.69-161.69V481.492c0-12.203-12.203-24.406-24.406-24.406z" fill="#C71D23" p-id="5272"></path></svg>'
        }, link: 'https://gitee.com/gumengmengs' }
    ]
  },
  {
    avatar: 'https://www.github.com/CrychicTeam.png',
    name: 'CrychicTeam',
    title: 'Main Maintainer for Third-Party Documentation',
    links: [
      { icon: 'github', link: 'https://github.com/CrychicTeam' },
      // { icon: {
      //   svg: '<svg t="1725476092474" class="icon" viewBox="0 0 1129 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4271" width="200" height="200"><path d="M234.909 9.656a80.468 80.468 0 0 1 68.398 0 167.374 167.374 0 0 1 41.843 30.578l160.937 140.82h115.07l160.936-140.82a168.983 168.983 0 0 1 41.843-30.578A80.468 80.468 0 0 1 930.96 76.445a80.468 80.468 0 0 1-17.703 53.914 449.818 449.818 0 0 1-35.406 32.187 232.553 232.553 0 0 1-22.531 18.508h100.585a170.593 170.593 0 0 1 118.289 53.109 171.397 171.397 0 0 1 53.914 118.288v462.693a325.897 325.897 0 0 1-4.024 70.007 178.64 178.64 0 0 1-80.468 112.656 173.007 173.007 0 0 1-92.539 25.75h-738.7a341.186 341.186 0 0 1-72.421-4.024A177.835 177.835 0 0 1 28.91 939.065a172.202 172.202 0 0 1-27.36-92.539V388.662a360.498 360.498 0 0 1 0-66.789A177.03 177.03 0 0 1 162.487 178.64h105.414c-16.899-12.07-31.383-26.555-46.672-39.43a80.468 80.468 0 0 1-25.75-65.984 80.468 80.468 0 0 1 39.43-63.57M216.4 321.873a80.468 80.468 0 0 0-63.57 57.937 108.632 108.632 0 0 0 0 30.578v380.615a80.468 80.468 0 0 0 55.523 80.469 106.218 106.218 0 0 0 34.601 5.632h654.208a80.468 80.468 0 0 0 76.444-47.476 112.656 112.656 0 0 0 8.047-53.109v-354.06a135.187 135.187 0 0 0 0-38.625 80.468 80.468 0 0 0-52.304-54.719 129.554 129.554 0 0 0-49.89-7.242H254.22a268.764 268.764 0 0 0-37.82 0z m0 0" fill="#20B0E3" p-id="4272"></path><path d="M348.369 447.404a80.468 80.468 0 0 1 55.523 18.507 80.468 80.468 0 0 1 28.164 59.547v80.468a80.468 80.468 0 0 1-16.094 51.5 80.468 80.468 0 0 1-131.968-9.656 104.609 104.609 0 0 1-10.46-54.719v-80.468a80.468 80.468 0 0 1 70.007-67.593z m416.02 0a80.468 80.468 0 0 1 86.102 75.64v80.468a94.148 94.148 0 0 1-12.07 53.11 80.468 80.468 0 0 1-132.773 0 95.757 95.757 0 0 1-12.875-57.133V519.02a80.468 80.468 0 0 1 70.007-70.812z m0 0" fill="#20B0E3" p-id="4273"></path></svg>'
      // }, link: 'https://space.bilibili.com/16632546' },
      { icon: {
          svg: '<svg t="1725435852282" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5271" width="200" height="200"><path d="M512 1000.12c-268.466 0-488.12-219.654-488.12-488.12S243.533 23.88 512 23.88 1000.12 243.533 1000.12 512 780.467 1000.12 512 1000.12z m247.111-543.034H481.492c-12.203 0-24.406 12.203-24.406 24.406v61.016c0 12.203 12.203 24.406 24.406 24.406h167.792c12.203 0 24.406 12.203 24.406 24.406v12.203c0 39.66-33.558 73.218-73.218 73.218H371.665c-12.203 0-24.406-12.203-24.406-24.406V420.477c0-39.66 33.559-73.218 73.218-73.218h338.634c12.203 0 24.406-12.203 24.406-24.406v-61.015c0-12.203-12.203-24.406-24.406-24.406H420.477c-100.675 0-179.994 82.37-179.994 179.995V756.06c0 12.203 12.203 24.406 24.406 24.406h356.938c88.472 0 161.69-73.218 161.69-161.69V481.492c0-12.203-12.203-24.406-24.406-24.406z" fill="#C71D23" p-id="5272"></path></svg>'
        }, link: 'https://gitee.com/CrychicTeam' }
    ]
  },
]
</script>

<style>
.responsive-container {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 16px;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.responsive-container > * {
  flex: 1 1 calc(33.3333% - 16px);
  min-width: 200px;
  box-sizing: border-box;
}

@media (max-width: 1024px) {
  .responsive-container > * {
    flex: 1 1 calc(50% - 16px);
  }
}

@media (max-width: 768px) {
  .responsive-container > * {
    flex: 1 1 100%;
  }
}
</style>