
<div align="center"><img height="200" src="docs/public/logo.png" width="200"/></div>

## License

This project is licensed under the [MIT License](LICENSE).

# Hello

You can find the English version of the readme [here](/READMEEN.md).

This is the source code repository for Cryhic documentation.

## Directory Structure

```markdown
- crychicdoc
    - .github    Automated build scripts
    - .vitepress
        - config VitePress configuration file
        - theme  Custom themes and components
    - .vscode md snippets
    - docs
        - public Stores static files
        - zh    Simplified Chinese content
        - en    English content
    - public     Stores static files such as images
    - README.md  This file
```

## Collaboration

This website uses [Vitepress](https://vitepress.dev/) as a static site generator. We recommend using the VSCode editor for development.

1. Install [Node.js](https://nodejs.org/en/download/)
2. Download and install [yarn](https://classic.yarnpkg.com/en/docs/install/#windows-stable) (Make sure you have the necessary permissions to install yarn. Mac/Linux users should use `sudo -s`, while Windows users need to run Command Prompt (cmd) or PowerShell as an administrator before running `npm install -g yarn`).
3. Clone the repository to your local machine.
4. Install dependencies by running `yarn install` in the terminal.
5. Start the local server by running `npm run dev` in the terminal. You can preview it in your browser.

This documentation uses a secure and stable deployment solution that ensures long-term stability and access for players in different regions:
1. Use [github action](.github/workflows/build.yaml) to build the static files of the website and upload them to a private repository, then forward them to a physical server.
2. Use Aliyun Cloud's GeoDNS to distribute traffic between domestic and international visitors. When international players access the website, the data will be automatically accelerated by [CloudFlare](https://cloudflare.com/).

> The domain name is purchased from Aliyun Cloud.