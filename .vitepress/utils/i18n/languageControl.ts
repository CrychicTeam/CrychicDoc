// Language control styles
export const traditionalChineseStyles = `
    /* 全局样式覆盖 */
    :root,
    body,
    .VPDoc,
    .vp-doc,
    .content,
    .content-container,
    main,
    article {
        font-variant-east-asian: traditional !important;
        font-family: "HarmonyOS Sans TC", "Punctuation TC", var(--vp-font-family-base) !important;
    }

    /* 具体元素覆盖 */
    .vp-doc h1,
    .vp-doc h2,
    .vp-doc h3,
    .vp-doc h4,
    .vp-doc h5,
    .vp-doc h6,
    .vp-doc p,
    .vp-doc li,
    .vp-doc a,
    .vp-doc span,
    .vp-doc div,
    .nav-bar-title,
    .VPNavBarTitle,
    .VPNavBar,
    .VPNavBarMenu,
    .VPNavScreen,
    .VPSidebar,
    .VPFooter,
    .VPTeamPage,
    .VPHomeHero,
    .VPFeatures {
        font-variant-east-asian: traditional !important;
        font-family: "HarmonyOS Sans TC", "Punctuation TC", var(--vp-font-family-base) !important;
    }

    /* 通配符覆盖 */
    * {
        font-variant-east-asian: traditional !important;
    }

    /* CSS 变量覆盖 */
    :root {
        --vp-font-family-base: "HarmonyOS Sans TC", "Punctuation TC", -apple-system, BlinkMacSystemFont, 
            "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif !important;
    }
`;

export const checkFontLoading = async () => {
    if (import.meta.env.SSR) return;
    
    try {
        const fonts = await document.fonts.ready;
        const scLoaded = await document.fonts.check('400 12px "HarmonyOS Sans SC"');
        const tcLoaded = await document.fonts.check('400 12px "HarmonyOS Sans TC"');
        
        if (!scLoaded || !tcLoaded) {
            console.warn('Some fonts failed to load:', { scLoaded, tcLoaded });
        }
    } catch (error) {
        console.error('Font loading check error:', error);
    }
};

export const applyTraditionalChinese = () => {
    if (import.meta.env.SSR) return;
    
    const docElement = document.documentElement;
    const styleId = 'traditional-chinese-style';
    let styleElement = document.getElementById(styleId);
    
    if (!styleElement) {
        styleElement = document.createElement('style');
        styleElement.id = styleId;
        styleElement.textContent = traditionalChineseStyles;
        document.head.appendChild(styleElement);
        
        // 添加内联样式到 body
        document.body.style.setProperty('font-variant-east-asian', 'traditional', 'important');
        document.body.style.setProperty('font-family', 
            '"HarmonyOS Sans TC", "Punctuation TC", var(--vp-font-family-base)', 'important');
    }
};

export const setupLanguageControl = () => {
    if (import.meta.env.SSR) return;
    
    const browserLang = navigator.language;
    if (browserLang === 'zh-TW' || browserLang === 'zh-HK') {
        applyTraditionalChinese();
        checkFontLoading();
    }
}; 