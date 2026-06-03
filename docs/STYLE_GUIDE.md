# Style Guide
Code should focus on readability and clarity.

As a general rule of thumb, class, method, and variable names follow the conventions for whichever language they are in.

## Package Layout
- Package names should be in lowercase ASCII letters.

## Java Styling
- Class names should follow PascalCase / Upper CamelCase, whereas variable and method names follow camelCase.
  - Constant names should be in SCREAMING_SNAKE_CASE.
- Follow [google's style guide](https://google.github.io/styleguide/javaguide.html#s7-javadoc) for documentation.
- Method names should be verb-based, such as `getFoo()`.
- Braces follow "Egyptian brackets" conventions, where the opening brace is on the same line as the control statement, and the closing brace is on its own line, and the body indented one additional level.
- Avoid single-character variable names or similar, even for lambda statements.
- Variable names should be meaningful and intuitive.
- Annotations (e.g. `@Getter`) should be split to individual lines

## HTML
- File names, class names, and element IDs should follow kebab-case, and be distinct and descriptive.
  - Avoid common names that could unintentionally overlap with names in other files
- Follow a JavaDoc-style documentation for files
  - This should be in comments, after `!DOCTYPE` but before the opening `<html>` tag.
  - Describe the page's function
  - Note any mappings that lead to this page from controllers
- Indent all nested elements, including `<head>` and `<body>` tags.
  - Indent by 2 spaces at a time.
- "Non-text" tags (e.g. `<div>`, `<form>`) should be on their own line, and not share lines with other tags.

## CSS
<!-- TODO -->

## JavaScript
- Javascript may be written in a `<script>` tag or linked to with a `src` attribute
- Follow [google's style guide](https://google.github.io/styleguide/jsguide.html#jsdoc) for documentation
- Indent 2 spaces at a time.

## Comments
- This project uses [Comment Anchors](https://marketplace.visualstudio.com/items?itemName=ExodiusStudios.comment-anchors) for comments besides basic code explanation.
  - `SECTION` comment anchors are encouraged for lengthy files.
- Liberal use of comments to describe code function is encouraged.