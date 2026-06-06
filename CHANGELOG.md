# [v1.0.0-dev.2](https://github.com/lengors/scoutdesk/compare/v1.0.0-dev.1...v1.0.0-dev.2) (2026-06-06)

## ✨ New Features
- [`fc7c308`](https://github.com/lengors/scoutdesk/commit/fc7c308)  Add OAuth2 support 

## 🐛 Bug Fixes
- [`79fccc8`](https://github.com/lengors/scoutdesk/commit/79fccc8)  Change authentication principal type 
- [`220eddd`](https://github.com/lengors/scoutdesk/commit/220eddd)  Fix connection reset by removing connection pools

# v1.0.0-dev.1 (2026-05-17)

## ✨ New Features
- [`e75d51e`](https://github.com/lengors/scoutdesk/commit/e75d51e)  Implement Spring Security with Authentik integration and role management 
- [`001fada`](https://github.com/lengors/scoutdesk/commit/001fada)  Add support for user owned scraper specification management 
- [`c04eb30`](https://github.com/lengors/scoutdesk/commit/c04eb30)  Add support for user owned scraper profile management 
- [`3422641`](https://github.com/lengors/scoutdesk/commit/3422641)  Add support for user owned scraper strategies management 
- [`b291fd8`](https://github.com/lengors/scoutdesk/commit/b291fd8)  Add support for scraping with strategies 
- [`842f0fa`](https://github.com/lengors/scoutdesk/commit/842f0fa)  Add observation capabilities 
- [`3c74e05`](https://github.com/lengors/scoutdesk/commit/3c74e05)  Add support for querying for authenticated user information 
- [`2b1e365`](https://github.com/lengors/scoutdesk/commit/2b1e365)  Add validation of requirements on profile 
- [`32c7dfe`](https://github.com/lengors/scoutdesk/commit/32c7dfe)  Allow profiles to be used directly for scraping 
- [`ef51301`](https://github.com/lengors/scoutdesk/commit/ef51301)  Add endpoint to upload specification via file 
- [`c6162b5`](https://github.com/lengors/scoutdesk/commit/c6162b5)  Add endpoint to fetch profile requirements 
- [`722e5b1`](https://github.com/lengors/scoutdesk/commit/722e5b1)  Add endpoint to fetch single specification from specified developer 
- [`5473e48`](https://github.com/lengors/scoutdesk/commit/5473e48)  Make profile&#x27;s inputs encrypted using Jasypt 

## 🐛 Bug Fixes
- [`c325770`](https://github.com/lengors/scoutdesk/commit/c325770)  Fix database username&#x27;s environment variable name 
- [`6705529`](https://github.com/lengors/scoutdesk/commit/6705529) ⬆️ Bump protoscout reference 
- [`4cda859`](https://github.com/lengors/scoutdesk/commit/4cda859)  Fix errors not being properly reported from the root data structure 
- [`1333d50`](https://github.com/lengors/scoutdesk/commit/1333d50)  Make scraper command remove owner prefix from specification names 
- [`abc21b2`](https://github.com/lengors/scoutdesk/commit/abc21b2)  Require authentication for API only 
- [`f57f496`](https://github.com/lengors/scoutdesk/commit/f57f496)  Remove logging of authentik client service token 

## 💥 Breaking Changes
- [`aa41eac`](https://github.com/lengors/scoutdesk/commit/aa41eac)  Change validation to be performed through constraints

# [v1.0.0-dev.14](https://github.com/lengors/scoutdesk/compare/1.0.0-dev.13...1.0.0-dev.14) (2026-05-10)

## 🐛 Bug Fixes
- [`f57f496`](https://github.com/lengors/scoutdesk/commit/f57f496)  Remove logging of authentik client service token

# [v1.0.0-dev.13](https://github.com/lengors/scoutdesk/compare/1.0.0-dev.12...1.0.0-dev.13) (2026-03-07)

## ✨ New Features
- [`c6162b5`](https://github.com/lengors/scoutdesk/commit/c6162b5)  Add endpoint to fetch profile requirements 
- [`722e5b1`](https://github.com/lengors/scoutdesk/commit/722e5b1)  Add endpoint to fetch single specification from specified developer 

## 🐛 Bug Fixes
- [`1333d50`](https://github.com/lengors/scoutdesk/commit/1333d50)  Make scraper command remove owner prefix from specification names 
- [`abc21b2`](https://github.com/lengors/scoutdesk/commit/abc21b2)  Require authentication for API only

# [v1.0.0-dev.12](https://github.com/lengors/scoutdesk/compare/1.0.0-dev.11...1.0.0-dev.12) (2025-11-27)

## 🐛 Bug Fixes
- [`4cda859`](https://github.com/lengors/scoutdesk/commit/4cda859)  Fix errors not being properly reported from the root data structure

# [v1.0.0-dev.11](https://github.com/lengors/scoutdesk/compare/1.0.0-dev.10...1.0.0-dev.11) (2025-11-13)

## ✨ New Features
- [`ef51301`](https://github.com/lengors/scoutdesk/commit/ef51301)  Add endpoint to upload specification via file

# [v1.0.0-dev.10](https://github.com/lengors/scoutdesk/compare/1.0.0-dev.9...1.0.0-dev.10) (2025-11-07)

## ✨ New Features
- [`32c7dfe`](https://github.com/lengors/scoutdesk/commit/32c7dfe)  Allow profiles to be used directly for scraping

# [v1.0.0-dev.9](https://github.com/lengors/scoutdesk/compare/1.0.0-dev.8...1.0.0-dev.9) (2025-11-06)

## ✨ New Features
- [`2b1e365`](https://github.com/lengors/scoutdesk/commit/2b1e365)  Add validation of requirements on profile 

## 💥 Breaking Changes
- [`aa41eac`](https://github.com/lengors/scoutdesk/commit/aa41eac)  Change validation to be performed through constraints

# [v1.0.0-dev.8](https://github.com/lengors/scoutdesk/compare/1.0.0-dev.7...1.0.0-dev.8) (2025-08-16)

## ✨ New Features
- [`3c74e05`](https://github.com/lengors/scoutdesk/commit/3c74e05)  Add support for querying for authenticated user information

# [v1.0.0-dev.7](https://github.com/lengors/scoutdesk/compare/1.0.0-dev.6...1.0.0-dev.7) (2025-05-18)

## 🐛 Bug Fixes
- [`6705529`](https://github.com/lengors/scoutdesk/commit/6705529) ⬆️ Bump protoscout reference

# [v1.0.0-dev.6](https://github.com/lengors/scoutdesk/compare/1.0.0-dev.5...1.0.0-dev.6) (2025-05-17)

## 🐛 Bug Fixes
- [`c325770`](https://github.com/lengors/scoutdesk/commit/c325770)  Fix database username&#x27;s environment variable name

# [v1.0.0-dev.5](https://github.com/lengors/scoutdesk/compare/1.0.0-dev.4...1.0.0-dev.5) (2025-05-17)

## ✨ New Features
- [`842f0fa`](https://github.com/lengors/scoutdesk/commit/842f0fa)  Add observation capabilities

# [v1.0.0-dev.4](https://github.com/lengors/scoutdesk/compare/1.0.0-dev.3...1.0.0-dev.4) (2025-05-17)

## ✨ New Features
- [`b291fd8`](https://github.com/lengors/scoutdesk/commit/b291fd8)  Add support for scraping with strategies

# [v1.0.0-dev.3](https://github.com/lengors/scoutdesk/compare/1.0.0-dev.2...1.0.0-dev.3) (2025-05-17)

## ✨ New Features
- [`3422641`](https://github.com/lengors/scoutdesk/commit/3422641)  Add support for user owned scraper strategies management

# [v1.0.0-dev.2](https://github.com/lengors/scoutdesk/compare/1.0.0-dev.1...1.0.0-dev.2) (2025-05-17)

## ✨ New Features
- [`c04eb30`](https://github.com/lengors/scoutdesk/commit/c04eb30)  Add support for user owned scraper profile management

# v1.0.0-dev.1 (2025-05-17)

## ✨ New Features
- [`e75d51e`](https://github.com/lengors/scoutdesk/commit/e75d51e)  Implement Spring Security with Authentik integration and role management 
- [`001fada`](https://github.com/lengors/scoutdesk/commit/001fada)  Add support for user owned scraper specification management
