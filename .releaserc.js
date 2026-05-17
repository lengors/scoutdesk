const branch = process.env.GITHUB_REF_NAME;

const isPreview =
    branch.startsWith("feat/") ||
    branch.startsWith("bug/");

module.exports = {
    branches: [
        "main",
        {name: "beta", prerelease: true},
        {name: "alpha", prerelease: true},
        {name: "dev", prerelease: true},
        {name: "feat/**", prerelease: "preview"},
        {name: "bug/**", prerelease: "preview"},
    ],

    plugins: isPreview
        ? [
            "semantic-release-gitmoji",
            ["@semantic-release/changelog", {
                changelogFile: "CHANGELOG.md",
            }],
            "@semantic-release/github",
        ]
        : [
            "semantic-release-gitmoji",
            ["@semantic-release/changelog", {
                changelogFile: "CHANGELOG.md",
            }],
            "@semantic-release/github",
            ["@semantic-release/git", {
                assets: ["CHANGELOG.md"],
                message: "🔖 Release ${nextRelease.version} [skip ci]",
            }],
        ],
};