<div align="center"><img height="200" src="docs/public/logo.png" width="200"/></div>

![GitHub Release Date](https://img.shields.io/github/created-at/PickAID/CrychicDoc) [![crychicdoc](https://img.shields.io/badge/CrychicDoc-Maintaining-green)](https://docs.mihono.cn)


 ![GitHub issue custom search in repo](https://img.shields.io/github/issues/PickAID/CrychicDoc.svg) ![GitHub last commit](https://img.shields.io/github/last-commit/PickAID/CrychicDoc) ![Total Commits](https://img.shields.io/github/commit-activity/t/PickAID/CrychicDoc) ![GitHub branch status](https://img.shields.io/github/check-runs/PickAID/CrychicDoc/main)  ![cc-by-sa-shield](https://img.shields.io/badge/License-CC%20BY--SA%204.0-lightgrey.svg)
![GitHub contributors](https://img.shields.io/github/contributors/PickAID/CrychicDoc)

## License

This project is licensed under the [Creative Commons Attribution-ShareAlike 4.0 International License](LICENSE).

Please include a link to this work when reproducing it: https://docs.mihono.cn

> **The project/team logo is created by Commercial commission and is authorized for non-commercial use. Please do not take it for any commercial activities.**

# Hello

你可以在[这里](/README.md)查看中文的README.

This is the source code repository for Cryhic documentation.

I have always believed that the most effective way to foster a creative community is through detailed and engaging documentation. That's why I have planned and maintained this documentation project, to help developers and players have a better development and gaming experience (especially in modded environment).

## Directory Structure

```markdown
- crychicdoc
    - .github/    Auto build scripts
    - .vitepress/
        - config/ Localization configuration files
        - plugins/ Location for mdit plugins
        - theme/ Custom themes and components
        - config.mts Vitepress configuration file
        - index.ts Sidebar configuration file
    - .vscode/ md-snippets
    - docs
        - public Directory for storing static files
        - zh Simplified Chinese content
        - en English content
    - README.md This file
    - .gitignore gitignore file
    - ExtractClassScript.js Please ignore
    - extracted_classes.md Please ignore
    - LICENSE CC BY-SA 4.0
```

## Collaboration

This website uses [Vitepress](https://vitepress.dev/) as a static site generator. We recommend using the VSCode editor for development.

1. Install [Node.js](https://nodejs.org/en/download/)
2. Download and install [yarn](https://classic.yarnpkg.com/en/docs/install/#windows-stable) (Make sure you have the necessary permissions to install yarn. Mac/Linux users should use `sudo -s`, while Windows users need to run Command Prompt (cmd) or PowerShell as an administrator before running `npm install -g yarn`).
3. Clone the repository to your local machine.
4. **Git configuration happens automatically** - If manual setup is needed, run `./setup-git.sh`
5. Install dependencies by running `yarn install` in the terminal.
6. Start the local server by running `npm run dev` in the terminal. You can preview it in your browser.
7. For more detailed and specific tutorials, you can refer to this [document](https://vitepress.yiov.top/preface.html) and the [official documentation](https://vitepress.dev/en/).

### Git Configuration

Git case sensitivity configuration happens **automatically** when you clone the repository. If automatic setup fails, please run:

```bash
./setup-git.sh
```

This will configure Git to properly handle filename case sensitivity, preventing conflicts between different operating systems. For detailed information, see `.git-setup/README.md`.

This documentation uses a secure and stable deployment solution that ensures long-term stability and access for players in different regions:
1. Use [github action](.github/workflows/build.yaml) to build the static files of the website and upload them to a private repository, then forward them to a physical server.
2. Use Aliyun Cloud's GeoDNS to distribute traffic between domestic and international visitors. When international players access the website, the data will be automatically accelerated by [CloudFlare](https://cloudflare.com/).

> The domain name is purchased from Aliyun Cloud.