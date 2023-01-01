var simplemde = new SimpleMDE({
    element: document.getElementById("blog-content"),
    spellChecker: false,
    maxHeight: "500px",
    renderingConfig: {
        codeSyntaxHighlighting: true
    }
});

// console.log("hello")
// $(".select2").select2();

const category =$("#blog-category");
category.select2();