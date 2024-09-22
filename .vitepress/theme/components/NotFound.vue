<template>
    <div class="not-found">
        <h1 class="glitch" data-text="404">404</h1>
        <div class="universe">
            <div class="circle"></div>
            <div class="circle"></div>
            <div class="circle"></div>
        </div>
        <p class="message">{{ messages[lang].notFound }}</p>
        <p class="tip">{{ messages[lang].tip }}</p>
        <p class="advice">{{ messages[lang].advice }}</p>
    </div>
</template>

<script setup>
    import { ref, onMounted } from "vue";
    import { useData } from "vitepress";

    const { lang } = useData();

    const messages = {
        "en-US": {
            notFound: "Oops! Page not found",
            tip: "Due to caching mechanisms, if your page was inactive and reactivated without refreshing, you might encounter a 404 error if the page has been updated. In this case, try refreshing the page to reload.",
            advice: "If you still see this page, the page does not exist.",
        },
        "zh-CN": {
            notFound: "哎呀！页面未找到",
            tip: "由于缓存机制，如果你的页面曾处于未激活状态而重新激活后没有刷新过网页，那如果你试图前去的网页有了新的更新，就会导致404问题，这种情况下你可以刷新网页尝试重新加载。",
            advice: "如果依旧出现该页面，则该页面不存在。",
        },
    };

    onMounted(() => {
        const circles = document.querySelectorAll(".circle");
        circles.forEach((circle, index) => {
            circle.style.animationDelay = `${index * 0.2}s`;
        });
    });
</script>

<style scoped>
    .not-found {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        height: 100vh;
        text-align: center;
        background-color: var(--vp-c-bg);
        color: var(--vp-c-text-1);
        font-family: var(--vp-font-family-base);
    }

    .glitch {
        font-size: 8rem;
        font-weight: bold;
        text-transform: uppercase;
        position: relative;
        text-shadow: 0.05em 0 0 var(--vp-c-brand),
            -0.03em -0.04em 0 var(--vp-c-brand-light),
            0.025em 0.04em 0 var(--vp-c-brand-dark);
        animation: glitch 725ms infinite;
    }

    .glitch span {
        position: absolute;
        top: 0;
        left: 0;
    }

    .glitch span:first-child {
        animation: glitch 500ms infinite;
        clip-path: polygon(0 0, 100% 0, 100% 35%, 0 35%);
        transform: translate(-0.04em, -0.03em);
        opacity: 0.75;
    }

    .glitch span:last-child {
        animation: glitch 375ms infinite;
        clip-path: polygon(0 65%, 100% 65%, 100% 100%, 0 100%);
        transform: translate(0.04em, 0.03em);
        opacity: 0.75;
    }

    @keyframes glitch {
        0% {
            text-shadow: 0.05em 0 0 var(--vp-c-brand),
                -0.03em -0.04em 0 var(--vp-c-brand-light),
                0.025em 0.04em 0 var(--vp-c-brand-dark);
        }
        15% {
            text-shadow: 0.05em 0 0 var(--vp-c-brand),
                -0.03em -0.04em 0 var(--vp-c-brand-light),
                0.025em 0.04em 0 var(--vp-c-brand-dark);
        }
        16% {
            text-shadow: -0.05em -0.025em 0 var(--vp-c-brand),
                0.025em 0.035em 0 var(--vp-c-brand-light),
                -0.05em -0.05em 0 var(--vp-c-brand-dark);
        }
        49% {
            text-shadow: -0.05em -0.025em 0 var(--vp-c-brand),
                0.025em 0.035em 0 var(--vp-c-brand-light),
                -0.05em -0.05em 0 var(--vp-c-brand-dark);
        }
        50% {
            text-shadow: 0.05em 0.035em 0 var(--vp-c-brand),
                0.03em 0 0 var(--vp-c-brand-light),
                0 -0.04em 0 var(--vp-c-brand-dark);
        }
        99% {
            text-shadow: 0.05em 0.035em 0 var(--vp-c-brand),
                0.03em 0 0 var(--vp-c-brand-light),
                0 -0.04em 0 var(--vp-c-brand-dark);
        }
        100% {
            text-shadow: -0.05em 0 0 var(--vp-c-brand),
                -0.025em -0.04em 0 var(--vp-c-brand-light),
                -0.04em -0.025em 0 var(--vp-c-brand-dark);
        }
    }

    .universe {
        position: relative;
        width: 200px;
        height: 200px;
        margin: 20px auto;
    }

    .circle {
        position: absolute;
        border-radius: 50%;
        background-color: var(--vp-c-brand);
        animation: orbit 4s linear infinite;
    }

    .circle:nth-child(1) {
        width: 100%;
        height: 100%;
        background-color: var(--vp-c-brand-lighter);
    }

    .circle:nth-child(2) {
        top: 25%;
        left: 25%;
        width: 50%;
        height: 50%;
        background-color: var(--vp-c-brand-light);
    }

    .circle:nth-child(3) {
        top: 40%;
        left: 40%;
        width: 20%;
        height: 20%;
        background-color: var(--vp-c-brand);
    }

    @keyframes orbit {
        0% {
            transform: rotate(0deg);
        }
        100% {
            transform: rotate(360deg);
        }
    }

    .message {
        font-size: 1.5rem;
        margin: 20px 0;
        color: var(--vp-c-text-1);
    }

    .tip,
    .advice {
        max-width: 600px;
        margin: 10px auto;
        font-size: 1rem;
        line-height: 1.5;
        color: var(--vp-c-text-2);
    }
</style>
