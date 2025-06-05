import { SidebarGenerator as NewSidebarGenerator } from './sidebar'
import { SidebarItem } from './sidebar/types'

// Legacy wrapper for backward compatibility
export default class SidebarGenerator {
    private generator: NewSidebarGenerator
    private _sidebar: SidebarItem
    private pathname: string
    
    constructor(pathname: string, language: string = 'zh') {
        this.pathname = pathname
        this.generator = new NewSidebarGenerator(pathname, language)
        this._sidebar = { text: '', items: [] }
        
        // Run async generation
        this.generate()
    }

    private async generate() {
        try {
            this._sidebar = await this.generator.generate()
        } catch (error) {
            console.error(`Failed to generate sidebar for ${this.pathname}:`, error)
        }
    }
    
    get sidebar() {
        return this._sidebar
    }
    
    get correctedPathname() {
        // Legacy compatibility - return the path without 'docs/'
        return this.pathname.replace(/^docs\//, '/')
    }
}

// Export types for compatibility
export type { SidebarItem as Sidebar, SidebarItem as FileItem } from './sidebar/types'