var page = require("webpage").create(),
    url = "${url}",
    imagePath = "${imagePath}";

page.open(url, function (status) {
    if (status === "success") {
        page.render(imagePath, {
            format: "jpeg",
            quality: 100
        });
    }
    console.log(status);
    phantom.exit();
});