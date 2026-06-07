# Welcome to Scoutdesk &middot; [![GitHub license](https://img.shields.io/github/license/lengors/scoutdesk?color=blue)](https://github.com/lengors/scoutdesk/blob/main/LICENSE) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=lengors_scoutdesk&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=lengors_scoutdesk)

Welcome to **scoutdesk**, the official management service for Webscout. Built with **Spring Boot**, and **virtual threads**, Scoutdesk is designed to be highly scalable and efficient.

## Getting Started

#### Clone the repository

```bash
git clone https://github.com/lengors/scoutdesk.git
cd scoutdesk
```

#### Build the project

Ensure you have Gradle and JDK installed. Run:

```bash
./gradlew clean build
```

#### Run tests

```bash
./gradlew clean test
```

### Build & Deployment

- **Docker Support**: Includes a `Dockerfile` to deploy the service as a container. Modify it as needed for your deployment scenario.
- **CI/CD Pipelines**: Fully automated pipelines for code quality checks, build, testing, publishing, and deployment.

## Documentation and Resources

For detailed guides and additional information, please refer to
our [GitHub Wiki](https://github.com/lengors/scoutdesk/wiki).

If you wish to check an example of the generated API documentation, visit
the [Dokka generated reference](https://lengors.github.io/scoutdesk) page.

## Contributing

Contributions are welcome! Please refer to our [Contribution Guidelines](./CONTRIBUTING.md) for more information on how
to get involved.

## License

This project is licensed under [Apache License Version 2.0](./LICENSE), which places it in the public domain.