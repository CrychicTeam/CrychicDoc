import { getSidebar, getConfiguredLanguages, configureSidebar } from "../utils/sidebar/index.ts";

async function buildSidebars() {
    console.log("🚀 Starting sidebar generation...");
    configureSidebar({
        languages: ["zh", "en"],
        debug: true,
        rootDir: process.cwd(),
        docsDir: "./docs",
        cacheDir: "./.vitepress/cache/sidebar",
    });
    const languages = getConfiguredLanguages();
    console.log(`📚 Using configured languages: ${languages.join(", ")}`);
    for (const lang of languages) {
        console.log(`\n📖 Generating sidebar for language: ${lang || "root"}`);

        const sidebar = await getSidebar(lang);

        if (Object.keys(sidebar).length > 0) {
            console.log(
                `✅ Successfully generated sidebar for ${lang || "root"}`
            );
            console.log(`   Generated ${Object.keys(sidebar).length} route(s)`);

            for (const [route, items] of Object.entries(sidebar)) {
                console.log(`   📄 ${route}: ${items.length} item(s)`);
            }
        }
    }
}

buildSidebars();
