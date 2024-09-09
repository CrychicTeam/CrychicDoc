<script setup lang="ts">
    import { reactive, ref, onMounted, onUnmounted } from "vue";

    const show = ref(false);
    const previewImageInfo = reactive<{
        url: string;
        list: string[];
        idx: number;
    }>({
        url: "",
        list: [],
        idx: 0,
    });

    const prefixBlacklist = ref<string[]>([
        "https://avatars.githubusercontent.com/",
    ]);

    function previewImage(e: Event) {
        const target = e.target as HTMLElement;
        const currentTarget = e.currentTarget as HTMLElement;
        if (
            target.tagName.toLowerCase() === "img" &&
            !target.classList.contains("non-preview-image")
        ) {
            const url = (target as HTMLImageElement).src;

            const isPrefixBlacklisted = prefixBlacklist.value.some(prefix => url.startsWith(prefix));
            if (isPrefixBlacklisted) {
                console.log("This image's URL has a blacklisted prefix and cannot be previewed.");
                return;
            }

            const imgs = currentTarget.querySelectorAll<HTMLImageElement>(
                ".content-container .main img:not(.non-preview-image)"
            );
            const idx = Array.from(imgs).findIndex((el) => el === target);
            const urls = Array.from(imgs).map((el) => el.src);

            previewImageInfo.url = url;
            previewImageInfo.list = urls;
            previewImageInfo.idx = idx;

            if (idx === -1 && url) {
                previewImageInfo.list.push(url);
                previewImageInfo.idx = previewImageInfo.list.length - 1;
            }
            console.log("Preview Image Info: ", previewImageInfo);
            show.value = true;
        }
    }

    onMounted(() => {
        const docDomContainer = document.querySelector("#VPContent");
        docDomContainer?.addEventListener("click", previewImage);
    });

    onUnmounted(() => {
        const docDomContainer = document.querySelector("#VPContent");
        docDomContainer?.removeEventListener("click", previewImage);
    });
</script>
