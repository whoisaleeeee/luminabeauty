window.luminaCarousel = window.luminaCarousel || {};

window.luminaCarousel.move = function (carouselId, direction) {
    const carousel = document.getElementById(carouselId);

    if (!carousel) {
        return;
    }

    carousel.scrollBy({
        left: carousel.clientWidth * 0.72 * direction,
        behavior: "smooth"
    });
};