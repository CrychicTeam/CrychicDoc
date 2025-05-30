<template>
    <div class="not-found">
        <h1 class="glitch" data-text="404">404</h1>
        <button
            @click="refreshPage"
            class="floating-button refresh-button"
            :title="messages.tip[lang]"
        >
            <svg
                xmlns="http://www.w3.org/2000/svg"
                x="0px"
                y="0px"
                width="30"
                height="30"
                viewBox="0,0,250,250"
            >
                <g
                    fill="#ffffff"
                    fill-rule="nonzero"
                    stroke="none"
                    stroke-width="1"
                    stroke-linecap="butt"
                    stroke-linejoin="miter"
                    stroke-miterlimit="10"
                    stroke-dasharray=""
                    stroke-dashoffset="0"
                    font-family="none"
                    font-weight="none"
                    font-size="none"
                    text-anchor="none"
                    style="mix-blend-mode: normal"
                >
                    <g transform="scale(10.66667,10.66667)">
                        <path
                            d="M21,15v-5c0,-3.866 -3.134,-7 -7,-7h-3c-0.552,0 -1,0.448 -1,1v0c0,1.657 1.343,3 3,3h1c1.657,0 3,1.343 3,3v5h-1.294c-0.615,0 -0.924,0.742 -0.491,1.178l3.075,3.104c0.391,0.395 1.03,0.395 1.421,0l3.075,-3.104c0.432,-0.436 0.122,-1.178 -0.492,-1.178z"
                            opacity="0.35"
                        ></path>
                        <path
                            d="M3,9v5c0,3.866 3.134,7 7,7h3c0.552,0 1,-0.448 1,-1v0c0,-1.657 -1.343,-3 -3,-3h-1c-1.657,0 -3,-1.343 -3,-3v-5h1.294c0.615,0 0.924,-0.742 0.491,-1.178l-3.075,-3.105c-0.391,-0.395 -1.03,-0.395 -1.421,0l-3.074,3.105c-0.433,0.436 -0.123,1.178 0.491,1.178z"
                        ></path>
                    </g>
                </g>
            </svg>
        </button>
        <p class="message">{{ messages.notFound[lang] }}</p>
        <p class="tip">{{ messages.tip[lang] }}</p>
        <p class="advice">{{ messages.advice[lang] }}</p>
    </div>
</template>

<script setup>
    import { ref, onMounted } from "vue";
    import { useData } from "vitepress";

    const { lang } = useData();
    const messages = {
        notFound: {
            "en-US": "Oops! Page not found",
            "zh-CN": "哎呀！页面未找到",
        },
        tip: {
            "en-US": "Click to refresh the page.",
            "zh-CN": "点击刷新页面。",
        },
        advice: {
            "en-US": "If you still see this page, the page does not exist.",
            "zh-CN": "如果依旧出现该页面，则该页面不存在。",
        },
    };

    const refreshPage = () => {
        window.location.reload();
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
        justify-content: flex-start;
        height: 100vh;
        text-align: center;
        background-color: var(--vp-c-bg);
        color: var(--vp-c-text-1);
        font-family: var(--vp-font-family-base);
        padding-top: 20vh;
        margin: 10px 0;
        box-sizing: border-box;
    }

    .glitch {
        font-size: 10rem;
        font-weight: bold;
        text-transform: uppercase;
        position: relative;
        text-shadow: 0.05em 0 0 var(--vp-c-brand),
            -0.03em -0.04em 0 var(--vp-c-brand-light),
            0.025em 0.04em 0 var(--vp-c-brand-dark);
        margin: 60px 0;
    }

    .floating-button.refresh-button {
        cursor: pointer;
        width: 50px;
        height: 50px;
        border-radius: 50%;
        background-color: var(--vp-c-brand);
        border: none;
        padding: 10px;
        box-shadow: 2px 2px 10px 4px rgba(0, 0, 0, 0.15);
        transition: background-color 0.3s, transform 0.3s;
        display: flex;
        margin: 60px 0;
        align-items: center;
        justify-content: center;
        position: static; /* 移除任何绝对定位 */
    }

    .floating-button.refresh-button:hover {
        background-color: var(--button-hover-color);
        transform: scale(1.1);
    }

    .message,
    .tip,
    .advice {
        max-width: 600px;
        line-height: 1.5;
        color: var(--vp-c-text-2);
    }

    .message {
        font-size: 1.5rem;
        margin: 20px 0;
        color: var(--vp-c-text-1);
    }
    .tip,
    .advice {
        font-size: 1rem;
    }
</style>
