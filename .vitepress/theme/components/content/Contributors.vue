<script setup lang="ts">
    import { ref, onMounted, computed } from "vue";
    import { useData } from "vitepress";
    import { Motion } from "motion-v";

    /**
     * Contributors Display Component
     * @description Displays all project contributors with cached avatar support
     * @version 1.0.0
     * @author CrychicDoc Team
     */

    // Internationalization
    interface ContributorsI18n {
        title: string;
        contributors: string;
        totalContributions: string;
        contributions: string;
        loading: string;
        viewProfile: string;
        retry: string;
        noContributors: string;
        times: string;
    }

    const translations: Record<string, ContributorsI18n> = {
        "zh-CN": {
            title: "项目贡献者",
            contributors: "贡献者",
            totalContributions: "总贡献",
            contributions: "次贡献",
            loading: "正在加载贡献者信息...",
            viewProfile: "查看资料",
            retry: "重试",
            noContributors: "暂无贡献者信息",
            times: "次",
        },
        "en-US": {
            title: "Contributors",
            contributors: "Contributors",
            totalContributions: "Total Contributions",
            contributions: "contributions",
            loading: "Loading contributors...",
            viewProfile: "View Profile",
            retry: "Retry",
            noContributors: "No contributors found",
            times: "",
        },
        en: {
            title: "Contributors",
            contributors: "Contributors",
            totalContributions: "Total Contributions",
            contributions: "contributions",
            loading: "Loading contributors...",
            viewProfile: "View Profile",
            retry: "Retry",
            noContributors: "No contributors found",
            times: "",
        },
    };

    interface Contributor {
        id: number;
        login: string;
        avatar_url: string;
        html_url: string;
        contributions: number;
        type: string;
    }

    interface Props {
        /** GitHub repository owner */
        owner?: string;
        /** GitHub repository name */
        repo?: string;
        /** Maximum number of contributors to display */
        maxCount?: number;
        /** Show contribution count */
        showContributions?: boolean;
        /** Enable avatar caching */
        enableCache?: boolean;
        /** Custom title for the component */
        title?: string;
        /** Language locale */
        locale?: string;
    }

    const props = withDefaults(defineProps<Props>(), {
        owner: "PickAID",
        repo: "CrychicDoc",
        maxCount: 200,
        showContributions: true,
        enableCache: true,
        locale: "zh-CN",
    });

    const { site } = useData();

    // Internationalization
    const getCurrentLocale = (): string => {
        if (props.locale) return props.locale;

        // Try to get locale from VitePress
        if (site.value?.lang) {
            return site.value.lang;
        }

        // Fallback to browser language or default
        if (typeof window !== "undefined") {
            const browserLang = navigator.language || "zh-CN";
            return browserLang;
        }

        return "zh-CN";
    };

    const t = computed((): ContributorsI18n => {
        const locale = getCurrentLocale();
        return translations[locale] || translations["zh-CN"];
    });

    // Reactive state
    const contributors = ref<Contributor[]>([]);
    const loading = ref(true);
    const error = ref<string | null>(null);
    const loadingAvatars = ref(new Set<string>());

    // Computed properties
    const sortedContributors = computed(() =>
        contributors.value
            .sort((a, b) => b.contributions - a.contributions)
            .slice(0, props.maxCount)
    );

    /**
     * Separate owner and regular contributors with smart grouping
     */
    const ownerContributor = computed(() => 
        sortedContributors.value.find(c => c.login.toLowerCase() === props.owner.toLowerCase())
    );

    const regularContributors = computed(() => 
        sortedContributors.value.filter(c => c.login.toLowerCase() !== props.owner.toLowerCase())
    );

    /**
     * Group contributors with smart sizing and variable group sizes based on rank
     */
    const contributorGroups = computed(() => {
        const groups = [];
        const contributors = regularContributors.value;
        let currentIndex = 0;
        let groupId = 0;
        
        while (currentIndex < contributors.length) {
            // Determine group size and tier based on position
            let groupSize: number;
            let sizeTier: 'large' | 'medium' | 'small';
            
            if (groupId === 0) {
                // First group: 5 large contributors (rank 1-5)
                groupSize = 5;
                sizeTier = 'large';
            } else if (groupId === 1) {
                // Second group: 5 medium contributors (rank 6-10)
                groupSize = 5;
                sizeTier = 'medium';
            } else {
                // All remaining groups: 10 small contributors each (rank 11+)
                groupSize = 10;
                sizeTier = 'small';
            }
            
            const group = contributors.slice(currentIndex, currentIndex + groupSize);
            
            if (group.length > 0) {
                groups.push({
                    id: groupId,
                    contributors: group,
                    sizeTier,
                    groupSize,
                    delay: groupId * 0.1 // Staggered animation delay
                });
            }
            
            currentIndex += groupSize;
            groupId++;
        }
        
        return groups;
    });

    const totalContributions = computed(() =>
        contributors.value.reduce(
            (sum, contributor) => sum + contributor.contributions,
            0
        )
    );

    /**
     * Get cached avatar path
     */
    const getCachedAvatarPath = (login: string): string => {
        return `/contributors/${login}.png`;
    };

    /**
     * Check if cached avatar exists
     */
    const checkCachedAvatar = async (login: string): Promise<boolean> => {
        if (!props.enableCache) return false;

        try {
            const response = await fetch(getCachedAvatarPath(login), {
                method: "HEAD",
            });
            return response.ok;
        } catch {
            return false;
        }
    };

    /**
     * Get avatar URL with fallback to cached version
     */
    const getAvatarUrl = async (contributor: Contributor): Promise<string> => {
        const { login, avatar_url } = contributor;

        if (!props.enableCache) {
            return `${avatar_url}&s=100`;
        }

        loadingAvatars.value.add(login);

        try {
            // First try to fetch the real avatar
            const response = await fetch(`${avatar_url}&s=100`, {
                method: "HEAD",
                cache: "no-cache",
            });

            if (response.ok) {
                loadingAvatars.value.delete(login);
                return `${avatar_url}&s=100`;
            }
        } catch (error) {
            console.warn(`Failed to fetch avatar for ${login}:`, error);
        }

        // Fallback to cached avatar
        const hasCached = await checkCachedAvatar(login);
        loadingAvatars.value.delete(login);

        return hasCached ? getCachedAvatarPath(login) : `${avatar_url}&s=100`;
    };

    /**
     * Fetch contributors from GitHub API
     */
    const fetchContributors = async () => {
        loading.value = true;
        error.value = null;

        try {
            const response = await fetch(
                `https://api.github.com/repos/${props.owner}/${props.repo}/contributors?per_page=100`,
                {
                    headers: {
                        Accept: "application/vnd.github.v3+json",
                    },
                }
            );

            if (!response.ok) {
                throw new Error(
                    `GitHub API error: ${response.status} ${response.statusText}`
                );
            }

            const data: Contributor[] = await response.json();

            // Filter out bots if needed
            contributors.value = data.filter(
                (contributor) => contributor.type !== "Bot"
            );
        } catch (err) {
            console.error("Failed to fetch contributors:", err);
            error.value =
                err instanceof Error
                    ? err.message
                    : "Failed to fetch contributors";
        } finally {
            loading.value = false;
        }
    };

    /**
     * Handle avatar load error
     */
    const handleAvatarError = async (
        event: Event,
        contributor: Contributor
    ) => {
        const img = event.target as HTMLImageElement;
        const cachedPath = getCachedAvatarPath(contributor.login);

        // If current src is not the cached version, try cached version
        if (img.src !== `${location.origin}${cachedPath}`) {
            const hasCached = await checkCachedAvatar(contributor.login);
            if (hasCached) {
                img.src = cachedPath;
                return;
            }
        }

        // Final fallback to GitHub's default avatar
        img.src = `https://github.com/identicons/${contributor.login}.png`;
    };

    /**
     * Open contributor profile
     */
    const openProfile = (url: string) => {
        window.open(url, "_blank", "noopener,noreferrer");
    };

    onMounted(() => {
        fetchContributors();
    });
</script>

<template>
    <div class="contributors-container">
        <div class="contributors-content">
            <!-- Header -->
            <div class="contributors-header">
            <h2 class="contributors-title">
                {{ title || t.title }}
            </h2>
            <div v-if="!loading && !error" class="contributors-stats">
                <span class="stat-item">
                    <span class="stat-number">{{
                        sortedContributors.length
                    }}</span>
                    <span class="stat-label">{{ t.contributors }}</span>
                </span>
                <span class="stat-divider">·</span>
                <span class="stat-item">
                    <span class="stat-number">{{
                        totalContributions.toLocaleString()
                    }}</span>
                    <span class="stat-label">{{ t.totalContributions }}</span>
                </span>
            </div>
        </div>

        <!-- Loading state -->
        <div v-if="loading" class="loading-container">
            <div class="loading-spinner"></div>
            <p class="loading-text">{{ t.loading }}</p>
        </div>

        <!-- Error state -->
        <div v-else-if="error" class="error-container">
            <div class="error-icon">⚠️</div>
            <p class="error-message">{{ error }}</p>
            <button @click="fetchContributors" class="retry-button">
                {{ t.retry }}
            </button>
        </div>

                    <!-- Owner Section (Special) -->
            <Motion
                v-if="ownerContributor"
                :initial="{ opacity: 0, y: -20 }"
                :animate="{ opacity: 1, y: 0 }"
                :transition="{ duration: 0.6, ease: 'easeOut' }"
                class="owner-section"
            >
                <div class="owner-header">
                    <h3 class="owner-title">Project Owner</h3>
                </div>
                <div
                    class="contributor-card owner-card"
                    @click="openProfile(ownerContributor.html_url)"
                    :title="`${ownerContributor.login} - Project Owner${
                        showContributions
                            ? ` - ${ownerContributor.contributions} ${t.contributions}`
                            : ''
                    }`"
                >
                    <div class="avatar-container">
                        <img
                            :src="`${ownerContributor.avatar_url}&s=120`"
                            :alt="`${ownerContributor.login}'s avatar`"
                            class="contributor-avatar"
                            @error="handleAvatarError($event, ownerContributor)"
                            loading="lazy"
                        />
                        <div class="owner-crown">👑</div>
                        <div
                            v-if="loadingAvatars.has(ownerContributor.login)"
                            class="avatar-loading"
                        >
                            <div class="avatar-spinner"></div>
                        </div>
                    </div>
                    <div class="contributor-info">
                        <h3 class="contributor-name">{{ ownerContributor.login }}</h3>
                        <p class="owner-badge">Project Owner</p>
                        <p v-if="showContributions" class="contributor-contributions">
                            {{ ownerContributor.contributions }} {{ t.contributions }}
                        </p>
                    </div>
                    <div class="card-overlay">
                        <span class="overlay-text">{{ t.viewProfile }}</span>
                    </div>
                </div>
            </Motion>

            <!-- Contributors Groups -->
            <div v-else-if="sortedContributors.length > 0" class="contributors-layout">
                <Motion
                    v-for="group in contributorGroups"
                    :key="group.id"
                    :initial="{ opacity: 0, y: 30 }"
                    :animate="{ opacity: 1, y: 0 }"
                    :transition="{ 
                        duration: 0.6, 
                        ease: 'easeOut',
                        delay: group.delay 
                    }"
                    :class="['contributor-group', `group-${group.sizeTier}`]"
                >
                    <div class="group-grid">
                        <Motion
                            v-for="(contributor, index) in group.contributors"
                            :key="contributor.id"
                            :initial="{ opacity: 0, scale: 0.8 }"
                            :animate="{ opacity: 1, scale: 1 }"
                            :transition="{ 
                                duration: 0.4, 
                                ease: 'backOut',
                                delay: group.delay + (index * 0.05)
                            }"
                            :whileHover="{ scale: 1.05, y: -4 }"
                            :whileTap="{ scale: 0.95 }"
                            class="contributor-card"
                            @click="openProfile(contributor.html_url)"
                            :title="`${contributor.login}${
                                showContributions
                                    ? ` - ${contributor.contributions} ${t.contributions}`
                                    : ''
                            }`"
                        >
                            <div class="avatar-container">
                                <img
                                    :src="`${contributor.avatar_url}&s=100`"
                                    :alt="`${contributor.login}'s avatar`"
                                    class="contributor-avatar"
                                    @error="handleAvatarError($event, contributor)"
                                    loading="lazy"
                                />
                                <div
                                    v-if="loadingAvatars.has(contributor.login)"
                                    class="avatar-loading"
                                >
                                    <div class="avatar-spinner"></div>
                                </div>
                            </div>
                            <div class="contributor-info">
                                <h3 class="contributor-name">{{ contributor.login }}</h3>
                                <p
                                    v-if="showContributions"
                                    class="contributor-contributions"
                                >
                                    {{ contributor.contributions }} {{ t.contributions }}
                                </p>
                            </div>
                            <div class="card-overlay">
                                <span class="overlay-text">{{ t.viewProfile }}</span>
                            </div>
                        </Motion>
                    </div>
                </Motion>
            </div>

                    <!-- Empty state -->
            <div v-else class="empty-container">
                <div class="empty-icon">📭</div>
                <p class="empty-message">{{ t.noContributors }}</p>
            </div>
        </div>
    </div>
</template>

<style scoped>
    .contributors-container {
        width: 100vw;
        margin-left: 50%;
        transform: translateX(-50%);
        padding: 0;
        /* Fixed background colors */
        background: #ffffff;
        position: relative;
        overflow: hidden;
    }

    .dark .contributors-container {
        background: #1B1B1F !important;
    }

    .contributors-content {
        max-width: 1800px; /* Made very wide like CommitsCounter */
        margin: 0 auto;
        padding: 60px 24px;
        background: transparent;
        border: none;
        border-radius: 0;
        position: relative;
    }

    @media (min-width: 640px) {
        .contributors-content {
            padding: 80px 48px;
        }
    }

    @media (min-width: 960px) {
        .contributors-content {
            padding: 100px 64px;
        }
    }

    .contributors-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 32px;
        padding-bottom: 16px;
    }

    .contributors-title {
        margin: 0;
        font-size: clamp(24px, 4vw, 32px);
        font-weight: 700;
        color: var(--vp-c-text-1);
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .contributors-stats {
        display: flex;
        align-items: center;
        gap: 12px;
        font-size: 14px;
    }

    .stat-item {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 2px;
    }

    .stat-number {
        font-size: 18px;
        font-weight: 700;
        color: var(--vp-c-brand-1);
        text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
    }

    .stat-label {
        font-size: 12px;
        color: var(--vp-c-text-2);
        text-transform: uppercase;
        letter-spacing: 0.5px;
    }

    .stat-divider {
        color: var(--vp-c-divider);
        font-weight: bold;
    }

    /* Loading state */
    .loading-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 16px;
        padding: 48px 24px;
    }

    .loading-spinner {
        width: 40px;
        height: 40px;
        border: 3px solid rgba(var(--vp-c-brand-rgb), 0.2);
        border-top: 3px solid var(--vp-c-brand-1);
        border-radius: 50%;
        animation: spin 1s linear infinite;
    }

    .loading-text {
        color: var(--vp-c-text-2);
        font-size: 16px;
        margin: 0;
    }

    @keyframes spin {
        to {
            transform: rotate(360deg);
        }
    }

    /* Error state */
    .error-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 16px;
        padding: 48px 24px;
    }

    .error-icon {
        font-size: 48px;
        filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
    }

    .error-message {
        color: var(--vp-c-text-2);
        font-size: 16px;
        text-align: center;
        margin: 0;
    }

    .retry-button {
        padding: 8px 20px;
        background: var(--vp-c-brand-1);
        color: var(--vp-c-white);
        border: none;
        border-radius: 8px;
        font-size: 14px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        box-shadow: 0 2px 8px rgba(var(--vp-c-brand-rgb), 0.3);
    }

    .retry-button:hover {
        background: var(--vp-c-brand-2);
        transform: translateY(-1px);
        box-shadow: 0 4px 12px rgba(var(--vp-c-brand-rgb), 0.4);
    }

    /* Owner Section */
    .owner-section {
        margin: 32px 0 48px 0;
        text-align: center;
    }

    .owner-header {
        margin-bottom: 24px;
    }

    .owner-title {
        font-size: 20px;
        font-weight: 600;
        color: var(--vp-c-brand-1);
        margin: 0;
        text-transform: uppercase;
        letter-spacing: 1px;
    }

    .owner-card {
        max-width: 280px;
        margin: 0 auto;
        padding: 32px 24px;
        background: linear-gradient(135deg, 
            rgba(var(--vp-c-brand-rgb), 0.1), 
            rgba(var(--vp-c-brand-2-rgb), 0.05)
        );
        border: 2px solid rgba(var(--vp-c-brand-rgb), 0.2);
        position: relative;
        overflow: visible;
    }

    .owner-card .contributor-avatar {
        width: 80px;
        height: 80px;
        border: 3px solid var(--vp-c-brand-1);
    }

    .owner-crown {
        position: absolute;
        top: -8px;
        right: -8px;
        font-size: 24px;
        filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
        z-index: 2;
    }

    .owner-badge {
        background: var(--vp-c-brand-1);
        color: white;
        padding: 4px 12px;
        border-radius: 12px;
        font-size: 12px;
        font-weight: 600;
        text-transform: uppercase;
        letter-spacing: 0.5px;
        margin: 8px 0;
        display: inline-block;
    }

    .contributor-group {
        width: 100%;
    }

    /* Master grid container for perfect alignment */
    .contributors-layout {
        display: flex;
        flex-direction: column;
        gap: 32px;
        margin-top: 32px;
        --container-max-width: 100%;
        --container-padding: 20px;
        --grid-gap: 16px;
    }

    .contributor-group {
        width: 100%;
        max-width: var(--container-max-width);
        margin: 0 auto;
        padding: 0 var(--container-padding);
        box-sizing: border-box;
    }

    .contributor-group .group-grid {
        display: grid;
        width: 100%;
        margin: 0;
        padding: 0;
        box-sizing: border-box;
        justify-items: center;
        align-items: start;
    }

    /* Large Group - responsive columns */
    .group-large .group-grid {
        grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
        gap: var(--grid-gap);
        max-width: 1000px;
        margin: 0 auto;
    }

    .group-large .contributor-card {
        padding: 20px 12px;
        min-height: 160px;
        display: flex;
        flex-direction: column;
        align-items: center;
        background: rgba(var(--vp-c-bg-soft-rgb), 0.3);
        border-radius: 8px;
        transition: all 0.3s ease;
        position: relative;
        cursor: pointer;
        overflow: hidden;
        margin: 0 auto;
        max-width: 200px;
        width: 100%;
    }

    .group-large .avatar-container {
        margin: 0 0 16px 0;
        position: relative;
    }

    .group-large .contributor-avatar {
        width: 80px;
        height: 80px;
        border-width: 3px;
    }

    .group-large .contributor-info {
        text-align: center;
        padding: 0 16px;
    }

    .group-large .contributor-name {
        font-size: 18px;
        font-weight: 700;
        margin: 0 0 8px 0;
    }

    .group-large .contributor-contributions {
        font-size: 14px;
        margin: 0;
    }

    /* Medium Group - responsive columns */
    .group-medium .group-grid {
        grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
        gap: var(--grid-gap);
        max-width: 1000px;
        margin: 0 auto;
    }

    .group-medium .contributor-card {
        padding: 16px 10px;
        min-height: 120px;
        display: flex;
        flex-direction: column;
        align-items: center;
        background: rgba(var(--vp-c-bg-soft-rgb), 0.3);
        border-radius: 8px;
        transition: all 0.3s ease;
        position: relative;
        cursor: pointer;
        overflow: hidden;
        margin: 0 auto;
        max-width: 160px;
        width: 100%;
    }

    .group-medium .avatar-container {
        margin: 0 0 12px 0;
        position: relative;
    }

    .group-medium .contributor-avatar {
        width: 56px;
        height: 56px;
    }

    .group-medium .contributor-info {
        text-align: center;
        padding: 0 12px;
    }

    .group-medium .contributor-name {
        font-size: 14px;
        font-weight: 600;
        margin: 0 0 6px 0;
    }

    .group-medium .contributor-contributions {
        font-size: 12px;
        margin: 0;
    }

    /* Small Group - responsive columns */
    .group-small .group-grid {
        grid-template-columns: repeat(auto-fit, minmax(80px, 1fr));
        gap: var(--grid-gap);
        max-width: 1200px;
        margin: 0 auto;
    }

    .group-small .contributor-card {
        padding: 12px 6px;
        min-height: 80px;
        display: flex;
        flex-direction: column;
        align-items: center;
        background: rgba(var(--vp-c-bg-soft-rgb), 0.3);
        border-radius: 6px;
        transition: all 0.3s ease;
        position: relative;
        cursor: pointer;
        overflow: hidden;
        margin: 0 auto;
        max-width: 120px;
        width: 100%;
    }

    .group-small .avatar-container {
        margin: 0 0 8px 0;
        position: relative;
    }

    .group-small .contributor-avatar {
        width: 40px;
        height: 40px;
    }

    .group-small .contributor-info {
        text-align: center;
        padding: 0 4px;
    }

    .group-small .contributor-name {
        font-size: 11px;
        font-weight: 600;
        margin: 0 0 4px 0;
    }

    .group-small .contributor-contributions {
        font-size: 10px;
        margin: 0;
    }



    .group-small .contributor-info {
        display: none;
    }

    /* Medium screens */
    @media (min-width: 768px) {
        .contributors-layout {
            --container-padding: 32px;
            --grid-gap: 20px;
        }
        
        .group-large .group-grid {
            grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
            max-width: 1000px;
        }
        
        .group-large .contributor-card {
            padding: 24px 16px;
            min-height: 180px;
            max-width: 220px;
        }
        
        .group-medium .group-grid {
            grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
            max-width: 1000px;
        }
        
        .group-medium .contributor-card {
            padding: 20px 12px;
            min-height: 140px;
            max-width: 180px;
        }
        
        .group-small .group-grid {
            grid-template-columns: repeat(auto-fit, minmax(100px, 1fr));
            max-width: 1200px;
        }
        
        .group-small .contributor-card {
            padding: 16px 8px;
            min-height: 100px;
            max-width: 140px;
        }
    }

    /* Large screens */
    @media (min-width: 1200px) {
        .contributors-layout {
            --container-max-width: 1400px;
            --container-padding: 60px;
            --grid-gap: 24px;
        }
        
        .group-large .group-grid {
            grid-template-columns: repeat(5, 1fr);
            max-width: 1200px;
        }
        
        .group-large .contributor-card {
            padding: 32px 20px;
            min-height: 200px;
            max-width: 240px;
        }
        
        .group-large .contributor-avatar {
            width: 88px;
            height: 88px;
        }
        
        .group-large .contributor-name {
            font-size: 20px;
        }
        
        .group-medium .group-grid {
            grid-template-columns: repeat(5, 1fr);
            max-width: 1200px;
        }
        
        .group-medium .contributor-card {
            padding: 28px 16px;
            min-height: 180px;
            max-width: 240px;
        }
        
        .group-medium .contributor-avatar {
            width: 64px;
            height: 64px;
        }
        
        .group-small .group-grid {
            grid-template-columns: repeat(10, 1fr);
            max-width: 1400px;
        }
        
        .group-small .contributor-card {
            padding: 20px 10px;
            min-height: 120px;
            max-width: 140px;
        }
        
        .group-small .contributor-avatar {
            width: 48px;
            height: 48px;
        }
    }

    /* Extra large screens */
    @media (min-width: 1600px) {
        .contributors-layout {
            --container-max-width: 1600px;
            --container-padding: 80px;
        }
        
        .group-large .contributor-card {
            padding: 36px 24px;
            min-height: 220px;
        }
        
        .group-large .contributor-avatar {
            width: 96px;
            height: 96px;
        }
        
        .group-large .contributor-name {
            font-size: 22px;
        }
        
        .group-medium .contributor-card {
            padding: 32px 20px;
            min-height: 200px;
        }
        
        .group-medium .contributor-avatar {
            width: 72px;
            height: 72px;
        }
        
        .group-medium .contributor-name {
            font-size: 16px;
        }
        
        .group-small .contributor-card {
            padding: 24px 12px;
            min-height: 140px;
        }
        
        .group-small .contributor-avatar {
            width: 56px;
            height: 56px;
        }
        
        .group-small .contributor-name {
            font-size: 13px;
        }
    }

    .contributor-card {
        position: relative;
        display: flex;
        flex-direction: column;
        align-items: center;
        padding: 20px 12px;
        background: rgba(var(--vp-c-bg-soft-rgb), 0.3);
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
        overflow: hidden;
    }

    .contributor-card::before {
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: linear-gradient(
            135deg,
            rgba(var(--vp-c-brand-rgb), 0.05),
            rgba(var(--vp-c-brand-2-rgb), 0.03)
        );
        opacity: 0;
        transition: opacity 0.3s ease;
        pointer-events: none;
    }

    .contributor-card:hover::before {
        opacity: 1;
    }

    .contributor-card:hover {
        transform: translateY(-2px) scale(1.01);
        background: rgba(var(--vp-c-bg-soft-rgb), 0.5);
    }

    .avatar-container {
        position: relative;
        margin-bottom: 16px;
    }

    .contributor-avatar {
        width: 64px;
        height: 64px;
        border-radius: 50%;
        border: 2px solid rgba(var(--vp-c-divider-rgb), 0.2);
        transition: all 0.3s ease;
        object-fit: cover;
    }

    .contributor-card:hover .contributor-avatar {
        border-color: var(--vp-c-brand-1);
        transform: scale(1.05);
    }

    .avatar-loading {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        background: rgba(var(--vp-c-bg-rgb), 0.8);
        border-radius: 50%;
        backdrop-filter: blur(2px);
    }

    .avatar-spinner {
        width: 20px;
        height: 20px;
        border: 2px solid rgba(var(--vp-c-brand-rgb), 0.3);
        border-top: 2px solid var(--vp-c-brand-1);
        border-radius: 50%;
        animation: spin 1s linear infinite;
    }

    .contributor-info {
        text-align: center;
        z-index: 1;
    }

    .contributor-name {
        margin: 0 0 8px 0;
        font-size: 16px;
        font-weight: 600;
        color: var(--vp-c-text-1);
        transition: color 0.3s ease;
        text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
    }

    .contributor-card:hover .contributor-name {
        color: var(--vp-c-brand-1);
    }

    .contributor-contributions {
        margin: 0;
        font-size: 13px;
        color: var(--vp-c-text-2);
        font-weight: 500;
    }

    .card-overlay {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: linear-gradient(
            135deg,
            rgba(var(--vp-c-brand-rgb), 0.9),
            rgba(var(--vp-c-brand-2-rgb), 0.8)
        );
        display: flex;
        align-items: center;
        justify-content: center;
        opacity: 0;
        transition: opacity 0.3s ease;
        backdrop-filter: blur(4px);
    }

    .contributor-card:hover .card-overlay {
        opacity: 1;
    }

    .overlay-text {
        color: white;
        font-size: 14px;
        font-weight: 600;
        text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
    }

    /* Empty state */
    .empty-container {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 16px;
        padding: 48px 24px;
    }

    .empty-icon {
        font-size: 48px;
        filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
    }

    .empty-message {
        color: var(--vp-c-text-2);
        font-size: 16px;
        margin: 0;
    }

    /* Dark theme adjustments */
    .dark .contributor-card {
        background: #1B1B1F;
    }

    .dark .contributor-card:hover {
        background: #1B1B1F;
    }

    .dark .owner-card {
        background: linear-gradient(135deg, 
            #1B1B1F, 
            rgba(27, 27, 31, 0.95)
        );
        border: 2px solid rgba(var(--vp-c-brand-rgb), 0.3);
    }

    /* Responsive design */
    @media (max-width: 768px) {
        .contributors-content {
            padding: 40px 16px;
            max-width: 100%;
        }

        .contributors-header {
            flex-direction: column;
            gap: 16px;
            align-items: flex-start;
        }

        .contributors-stats {
            align-self: stretch;
            justify-content: space-around;
        }

        .contributors-layout {
            --container-padding: 16px;
            --grid-gap: 12px;
        }
        
        .group-large .group-grid {
            grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
            max-width: 100%;
        }
        
        .group-large .contributor-card {
            padding: 16px 8px;
            min-height: 140px;
            max-width: 160px;
        }
        
        .group-medium .group-grid {
            grid-template-columns: repeat(auto-fit, minmax(100px, 1fr));
            max-width: 100%;
        }
        
        .group-medium .contributor-card {
            padding: 12px 6px;
            min-height: 120px;
            max-width: 140px;
        }
        
        .group-small .group-grid {
            grid-template-columns: repeat(auto-fit, minmax(70px, 1fr));
            max-width: 100%;
        }
        
        .group-small .contributor-card {
            padding: 10px 4px;
            min-height: 80px;
            max-width: 100px;
        }




        .owner-card {
            max-width: 240px;
            padding: 24px 16px;
        }

        .owner-card .contributor-avatar {
            width: 70px;
            height: 70px;
        }

        .contributor-card {
            padding: 20px 12px;
        }

        .contributor-avatar {
            width: 64px;
            height: 64px;
        }

        .contributor-name {
            font-size: 14px;
        }

        .contributor-contributions {
            font-size: 12px;
        }
    }

    @media (max-width: 960px) {
        .group-large .group-grid {
            grid-template-columns: repeat(5, 1fr);
            gap: 10px;
        }

        .group-large .contributor-card {
            padding: 16px 8px;
        }

        .group-large .contributor-avatar {
            width: 52px;
            height: 52px;
        }

        .group-large .contributor-name {
            font-size: 12px;
        }

        .group-medium .group-grid {
            grid-template-columns: repeat(5, 1fr);
            gap: 8px;
        }

        .group-medium .contributor-card {
            padding: 10px 4px;
        }

        .group-medium .contributor-avatar {
            width: 32px;
            height: 32px;
        }

        .group-medium .contributor-name {
            font-size: 10px;
        }

        .group-small .group-grid {
            grid-template-columns: repeat(10, 1fr);
            gap: 6px;
            width: 100%;
        }

        .group-small .contributor-card {
            padding: 4px 2px;
        }

        .group-small .contributor-avatar {
            width: 20px;
            height: 20px;
        }


    }

    @media (max-width: 480px) {
        .group-large .group-grid {
            grid-template-columns: repeat(5, 1fr);
            gap: 8px;
        }

        .group-large .contributor-card {
            padding: 12px 6px;
        }

        .group-large .contributor-avatar {
            width: 44px;
            height: 44px;
        }

        .group-large .contributor-name {
            font-size: 11px;
        }

        .group-medium .group-grid {
            grid-template-columns: repeat(5, 1fr);
            gap: 6px;
        }

        .group-medium .contributor-card {
            padding: 8px 4px;
        }

        .group-medium .contributor-avatar {
            width: 28px;
            height: 28px;
        }

        .group-medium .contributor-name {
            font-size: 9px;
        }

        .group-small .group-grid {
            grid-template-columns: repeat(10, 1fr);
            gap: 4px;
        }

        .group-small .contributor-card {
            padding: 4px 2px;
        }

        .group-small .contributor-avatar {
            width: 18px;
            height: 18px;
        }



        .owner-card {
            max-width: 220px;
            padding: 20px 12px;
        }

        .owner-card .contributor-avatar {
            width: 60px;
            height: 60px;
        }

        .contributor-card {
            padding: 16px 8px;
        }

        .contributor-avatar {
            width: 48px;
            height: 48px;
        }

        .contributors-layout {
            gap: 24px;
        }
    }

    /* Accessibility */
    @media (prefers-reduced-motion: reduce) {
        * {
            animation-duration: 0.01ms !important;
            animation-iteration-count: 1 !important;
            transition-duration: 0.01ms !important;
        }
    }
</style>
