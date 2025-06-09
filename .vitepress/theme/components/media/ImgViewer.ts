import { nextTick } from "vue";
import "@fancyapps/ui/dist/fancybox/fancybox.css";

// 查找图像之前最近的标题
const findNearestHeading = (imgElement) => {
    // 获取 img 元素的父节点
    let currentElement = imgElement;
    // 循环向上查找
    while (currentElement && currentElement !== document.body) {
        // 在当前元素的前一个兄弟节点中查找 h1-h6 标签
        let previousSibling = currentElement.previousElementSibling;
        while (previousSibling) {
            if (previousSibling.tagName.match(/^H[1-6]$/)) {
                return previousSibling.textContent
                    .replace(/\u200B/g, "")
                    .trim(); // 返回找到的标题内容
            }
            previousSibling = previousSibling.previousElementSibling;
        }
        // 如果没有找到，继续向上一级父节点查找
        currentElement = currentElement.parentElement;
    }

    return "";
};

export const bindFancybox = () => {
    nextTick(async () => {
        const { Fancybox } = await import("@fancyapps/ui"); // 采用这种导入方式是为了避免构建报错问题
        const imgs = document.querySelectorAll(".vp-doc img");
        imgs.forEach((img) => {
            const image = img as HTMLImageElement;
            if (!image.hasAttribute("data-fancybox")) {
                image.setAttribute("data-fancybox", "gallery");
            }
            // 赋予 alt 属性
            if (
                !image.hasAttribute("alt") ||
                image.getAttribute("alt") === ""
            ) {
                const heading = findNearestHeading(image);
                image.setAttribute("alt", heading);
            }
            // 赋予 data-caption 属性以便显示图片标题
            const altString = image.getAttribute("alt") || "";
            image.setAttribute("data-caption", altString);
        });

        Fancybox.bind('[data-fancybox="gallery"]', {
            Hash: false, // 禁用hash导航
            caption: false, // 更换标题
            Thumbs: {
                type: "classic", // 经典缩略图，"modern" 现代缩略图
                showOnStart: false, // 开始不显示缩略图列表
            },
            Images: {
                Panzoom: {
                    maxScale: 4, // 最大缩放比例
                },
            },
            Carousel: {
                transition: "slide",
            },
            Toolbar: {
                display: {
                    left: ["infobar"],
                    middle: [
                        "zoomIn",
                        "zoomOut",
                        "toggle1to1",
                        "rotateCCW",
                        "rotateCW",
                        "flipX",
                        "flipY",
                    ],
                    right: ["slideshow", "thumbs", "close"], // 'slideshow' 自动播放
                },
            },
        });
    });
};

export const destroyFancybox = async () => {
    const { Fancybox } = await import("@fancyapps/ui");
    Fancybox.destroy();
};
