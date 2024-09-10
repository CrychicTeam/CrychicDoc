export interface NavLink {
    /** 站点图标 */
    icon?: NavIcon | NavThemeIcon
    badge?:
        |string
        |   {
                text?: string
                type?: 'info' | 'tip' | 'warning' | 'danger'
            }
    /** 站点名称 */
    title: string
    /** 站点描述 */
    desc?: string
    /** 站点链接 */
    link: string
}
    
export interface NavData {
    title: string
    items: NavLink[]
}

export type NavIcon = string | NavSvg

export interface NavSvg {
    svg: string
}

export interface NavThemeIcon {
    dark: NavIcon
    light: NavIcon
}