# CryChicDoc Utils Plugin System

An organized utility system for CryChicDoc that provides a clean, plugin-like interface for accessing various utilities.

## Usage

### Import the main utils object

```typescript
import { utils } from '@utils'
// or
import utils from '@utils'
```

### Organized access

```typescript
// Content utilities
const wordCount = utils.content.countWord("Some text");
const readingTime = utils.content.text.getReadingTime("Long text content");
const parser = utils.content.parseGitbook("path/to/summary");

// VitePress utilities  
const sidebar = utils.vitepress.generateSidebar("docs/zh/modpack");
const generator = utils.vitepress.sidebar.generator("docs/en/develop");

// Charts utilities
const theme = utils.charts.config.getDefaultTheme();
const colors = utils.charts.config.palettes.light;

// Direct access (backward compatibility)
const count = utils.countWord("text");
const sidebarGen = new utils.SidebarGenerator("docs/path");
```

### Specific module imports

```typescript
// Import specific modules
import { content, vitepress, charts } from '@utils'

content.countWord("text");
vitepress.generateSidebar("path");
charts.config.getDefaultTheme();
```

### Direct function imports (backward compatibility)

```typescript
// Still works for existing code
import { countWord, SidebarGenerator } from '@utils'
```

## File Organization

```
utils/
├── content/           # Content processing utilities
│   └── index.ts       # Text processing, parsing
├── vitepress/         # VitePress-specific utilities  
│   └── index.ts       # Sidebar generation, navigation
├── charts/            # Chart and visualization utilities
│   └── index.ts       # Chart configuration, data processing
├── i18n/              # Internationalization utilities
│   └── index.ts       # Translation helpers
├── types/             # TypeScript definitions
│   └── index.ts       # All type definitions
├── functions.ts       # Core text functions
├── mdParser.ts        # Markdown/GitBook parser
├── sidebarGenerator.ts # Sidebar generation class
├── type.ts            # Base type definitions
└── index.ts           # Main entry point
```

## Available Utilities

### Content (`utils.content`)
- `countWord(text)` - Multi-language word counting
- `text.getReadingTime(text, wpm)` - Calculate reading time
- `parseGitbook(path)` - Parse GitBook SUMMARY.md files

### VitePress (`utils.vitepress`)
- `generateSidebar(pathname)` - Generate sidebar from directory
- `sidebar.generator(pathname)` - Create sidebar generator instance
- `sidebar.getCorrectedPathname(pathname)` - Get corrected path

### Charts (`utils.charts`)
- `config.getDefaultTheme()` - Get default chart theme
- `config.palettes` - Color palettes for charts
- `data.processData(data)` - Process chart data
- `data.validateData(data)` - Validate chart data

### Types (`utils.types`)
- All TypeScript interfaces and type definitions
- Enhanced types for better development experience

## Backward Compatibility

All existing imports continue to work:
- `import { countWord } from '@utils'`
- `import SidebarGenerator from '@utils/sidebarGenerator'` 
- `import { NavLink } from '@utils/type'`

The new organization provides better structure while maintaining full backward compatibility. 