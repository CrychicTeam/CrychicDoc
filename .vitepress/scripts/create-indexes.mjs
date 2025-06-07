#!/usr/bin/env node

import { readFileSync, writeFileSync, existsSync } from 'fs';
import { join, basename, relative, resolve } from 'path';
import glob from 'fast-glob';

/**
 * Index.md Generator - Create index.md files for directories that don't have them
 * 
 * @author Assistant
 */
class IndexGenerator {
    constructor() {
        this.processedDirs = 0;
        this.skippedDirs = 0;
        this.errorDirs = 0;
    }

    /**
     * Parse command line arguments
     */
    parseArgs(args) {
        const config = {
            path: null,
            template: 'default',
            dryRun: false,
            verbose: false,
            force: false,
            exclude: ['node_modules', '.git', '.vitepress', 'public']
        };

        for (let i = 0; i < args.length; i++) {
            switch (args[i]) {
                case '--path':
                case '-p':
                    config.path = args[++i];
                    break;
                case '--template':
                case '-t':
                    config.template = args[++i];
                    break;
                case '--dry-run':
                case '-d':
                    config.dryRun = true;
                    break;
                case '--verbose':
                case '-v':
                    config.verbose = true;
                    break;
                case '--force':
                case '-f':
                    config.force = true;
                    break;
                case '--exclude':
                    config.exclude.push(args[++i]);
                    break;
                case '--help':
                case '-h':
                    this.showHelp();
                    process.exit(0);
                    break;
            }
        }

        return config;
    }

    /**
     * Show help information
     */
    showHelp() {
        console.log(`
📁 Index.md Generator - Create index.md files for directories

USAGE:
  npm run create-indexes -- [options]

REQUIRED:
  -p, --path <path>          Target directory path

OPTIONS:
  -t, --template <type>      Template type (default/advanced) [default: default]
  -d, --dry-run              Preview changes without creating files
  -v, --verbose              Show detailed information
  -f, --force                Overwrite existing index.md files
      --exclude <pattern>    Exclude directories matching pattern
  -h, --help                 Show this help

TEMPLATES:
  default      Basic index.md with title and root configuration
  advanced     Advanced index.md with progress, state, and sections

EXAMPLES:
  # Create index.md files in docs/zh directory
  npm run create-indexes -- -p docs/zh
  
  # Use advanced template with preview
  npm run create-indexes -- -p docs/zh --template advanced --dry-run
  
  # Force overwrite existing files
  npm run create-indexes -- -p docs/zh --force --verbose
  
  # Exclude specific directories
  npm run create-indexes -- -p docs --exclude "**/temp" --exclude "**/draft"
        `);
    }

    /**
     * Validate configuration
     */
    validateConfig(config) {
        if (!config.path) {
            console.error('❌ Error: --path is required');
            return false;
        }

        const fullPath = resolve(config.path);
        if (!existsSync(fullPath)) {
            console.error(`❌ Error: Path "${config.path}" does not exist`);
            return false;
        }

        if (!['default', 'advanced'].includes(config.template)) {
            console.error('❌ Error: Template must be "default" or "advanced"');
            return false;
        }

        return true;
    }

    /**
     * Get template content for index.md
     */
    getTemplate(dirName, templateType) {
        const title = this.formatTitle(dirName);
        
        if (templateType === 'advanced') {
            return `---
title: ${title}
description: ${title}相关文档
progress: 0
state: draft
root: true
outline: [2, 3]
showComment: true
gitChangelog: true
---

# \$\{ $frontmatter.title \}

## 简述 {#intro}

这是 \`${title}\` 的索引页面。

## 内容概览 {#overview}

请在此添加该部分的介绍内容和导航信息。

## 相关链接 {#links}

- [相关文档](./related.md)
- [更多资源](./resources.md)
`;
        } else {
            return `---
title: ${title}
root: true
---

# \$\{ $frontmatter.title \}

这是 \`${title}\` 的索引页面。

请在此添加该部分的介绍内容。
`;
        }
    }

    /**
     * Format directory name to readable title
     */
    formatTitle(dirName) {
        // Convert kebab-case and snake_case to readable format
        return dirName
            .replace(/[-_]/g, ' ')
            .replace(/\b\w/g, l => l.toUpperCase())
            .trim();
    }

    /**
     * Find all directories that need index.md files
     */
    async findDirectoriesNeedingIndex(config) {
        try {
            // Find all directories
            const patterns = [`${config.path}/**/`];
            const excludePatterns = config.exclude.map(pattern => 
                pattern.startsWith('**/') ? pattern : `**/${pattern}/**`
            );

            const allDirs = await glob(patterns, {
                ignore: excludePatterns,
                onlyDirectories: true,
                absolute: true
            });

            // Filter directories that don't have index.md or need force update
            const dirsNeedingIndex = [];
            
            for (const dir of allDirs) {
                const indexPath = join(dir, 'index.md');
                const hasIndex = existsSync(indexPath);
                
                if (!hasIndex || config.force) {
                    dirsNeedingIndex.push({
                        path: dir,
                        indexPath,
                        hasExisting: hasIndex,
                        name: basename(dir)
                    });
                }
            }

            return dirsNeedingIndex;
        } catch (error) {
            console.error('❌ Error finding directories:', error.message);
            return [];
        }
    }

    /**
     * Create index.md file for a directory
     */
    createIndexFile(dirInfo, config) {
        try {
            const { path: dirPath, indexPath, hasExisting, name } = dirInfo;
            const relativePath = relative(process.cwd(), indexPath);

            if (hasExisting && !config.force) {
                if (config.verbose) {
                    console.log(`⏭️  Skipped ${relativePath} (already exists)`);
                }
                this.skippedDirs++;
                return true;
            }

            const template = this.getTemplate(name, config.template);
            
            if (config.verbose || config.dryRun) {
                const action = hasExisting ? 'Overwriting' : 'Creating';
                console.log(`📝 ${action} ${relativePath}`);
                
                if (config.verbose) {
                    console.log(`   Directory: ${name}`);
                    console.log(`   Template: ${config.template}`);
                    console.log(`   Title: ${this.formatTitle(name)}`);
                }
            }

            if (!config.dryRun) {
                writeFileSync(indexPath, template, 'utf8');
                this.processedDirs++;
            }

            return true;
        } catch (error) {
            console.error(`❌ Error creating index for ${dirInfo.name}: ${error.message}`);
            this.errorDirs++;
            return false;
        }
    }

    /**
     * Process all directories
     */
    async processDirectories(config) {
        console.log('🔍 Finding directories...');
        const dirs = await this.findDirectoriesNeedingIndex(config);

        if (dirs.length === 0) {
            console.log('📭 No directories need index.md files');
            return;
        }

        console.log(`📁 Found ${dirs.length} director${dirs.length === 1 ? 'y' : 'ies'} needing index.md`);

        if (config.dryRun) {
            console.log('🧪 DRY RUN MODE - No files will be created\n');
        } else {
            console.log('✏️  Creating index.md files...\n');
        }

        // Process each directory
        for (const dir of dirs) {
            this.createIndexFile(dir, config);
        }

        // Show summary
        this.showSummary(config);
    }

    /**
     * Show processing summary
     */
    showSummary(config) {
        console.log('\n📊 Summary:');
        
        if (config.dryRun) {
            console.log(`   Would create: ${this.processedDirs} index.md files`);
        } else {
            console.log(`   Created: ${this.processedDirs} index.md files`);
        }
        
        console.log(`   Skipped: ${this.skippedDirs} directories`);
        
        if (this.errorDirs > 0) {
            console.log(`   Errors: ${this.errorDirs} directories`);
        }

        if (config.dryRun && this.processedDirs > 0) {
            console.log('\n💡 Run without --dry-run to create files');
        }

        if (this.processedDirs > 0 && !config.dryRun) {
            console.log(`\n✅ Successfully created ${this.processedDirs} index.md file${this.processedDirs === 1 ? '' : 's'}`);
        }
    }

    /**
     * Main execution function
     */
    async run(args = process.argv.slice(2)) {
        console.log('🚀 Index.md Generator\n');

        const config = this.parseArgs(args);

        if (!this.validateConfig(config)) {
            process.exit(1);
        }

        // Show configuration
        console.log('⚙️  Configuration:');
        console.log(`   Path: ${config.path}`);
        console.log(`   Template: ${config.template}`);
        console.log(`   Dry run: ${config.dryRun ? 'Yes' : 'No'}`);
        console.log(`   Force: ${config.force ? 'Yes' : 'No'}`);
        if (config.exclude.length > 0) {
            console.log(`   Exclude: ${config.exclude.join(', ')}`);
        }
        console.log('');

        await this.processDirectories(config);
    }
}

// Run the script if called directly
if (import.meta.url === `file://${process.argv[1]}`) {
    const generator = new IndexGenerator();
    generator.run().catch(error => {
        console.error('❌ Script failed:', error);
        process.exit(1);
    });
}

export default IndexGenerator; 