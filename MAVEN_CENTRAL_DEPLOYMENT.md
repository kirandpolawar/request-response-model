# Maven Central Deployment Guide

This guide explains how to deploy this project to Maven Central repository.

## Prerequisites

1. **Sonatype JIRA Account**: Create an account at https://issues.sonatype.org/
2. **Create a JIRA Ticket**: Request access to publish to Maven Central
   - Go to https://issues.sonatype.org/secure/CreateIssue.jspa?issuetype=21&pid=10134
   - Provide your groupId (e.g., `com.kp`)
   - Provide your project URL
   - Wait for approval (usually 1-2 business days)
3. **GPG Key**: Generate a GPG key for signing artifacts
   ```bash
   gpg --gen-key
   gpg --list-secret-keys --keyid-format LONG
   gpg --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
   ```

## Configuration Steps

### 1. Update Project Information

Edit `pom.xml` and update the following placeholders:
- `<name>Request Response Model</name>` - Update with your project name
- `<url>https://github.com/yourusername/request-response-model</url>` - Update with your GitHub URL
- `<developer><name>Your Name</name><email>your.email@example.com</email></developer>` - Update with your details
- `<scm>` section - Update with your repository URLs

### 2. Configure Maven Settings

1. Copy `.mvn/settings.xml.template` to your Maven settings location:
   - **Global**: `~/.m2/settings.xml` (Linux/Mac) or `C:\Users\YourUsername\.m2\settings.xml` (Windows)
   - **Project-specific**: `.mvn/settings.xml` (in project root)

2. Update the settings.xml with your credentials:
   - Replace `YOUR_SONATYPE_USERNAME` with your Sonatype username
   - Replace `YOUR_SONATYPE_PASSWORD` with your Sonatype password
   - Optionally set `gpg.keyname` and `gpg.passphrase` if you want to automate GPG signing

### 3. Verify GroupId Ownership

Ensure your groupId matches what you registered in the Sonatype JIRA ticket. The current groupId is `com.kp`.

## Deployment Process

### For Snapshot Releases

```bash
mvn clean deploy
```

Snapshots will be automatically deployed to: https://s01.oss.sonatype.org/content/repositories/snapshots

### For Release Versions

1. **Build and Sign**:
   ```bash
   mvn clean verify
   ```
   This will:
   - Compile the code
   - Generate sources JAR
   - Generate javadoc JAR
   - Sign all artifacts with GPG

2. **Deploy to Staging**:
   ```bash
   mvn deploy
   ```
   Or if GPG passphrase is needed:
   ```bash
   mvn deploy -Dgpg.passphrase=YOUR_PASSPHRASE
   ```

3. **Verify in Nexus Repository Manager**:
   - Go to https://s01.oss.sonatype.org/
   - Login with your Sonatype credentials
   - Navigate to "Staging Repositories"
   - Find your repository and verify the contents
   - Click "Close" to validate the staging repository
   - After validation, click "Release" to publish to Maven Central

4. **Wait for Sync**:
   - It may take 10-30 minutes for artifacts to appear in Maven Central
   - Check: https://repo1.maven.org/maven2/com/kp/

## Important Notes

- **Version Numbers**: 
  - Use `-SNAPSHOT` suffix for snapshot versions (e.g., `1.0.1-SNAPSHOT`)
  - Remove `-SNAPSHOT` for release versions (e.g., `1.0.1`)

- **GPG Signing**: 
  - All artifacts must be signed with GPG
  - The GPG key must be published to a keyserver
  - You'll be prompted for your GPG passphrase during deployment

- **Artifacts Generated**:
  - Main JAR: `request-response-model-core-1.0.0.jar`
  - Sources JAR: `request-response-model-core-1.0.0-sources.jar`
  - Javadoc JAR: `request-response-model-core-1.0.0-javadoc.jar`
  - All signed with `.asc` files

- **Troubleshooting**:
  - If deployment fails, check the error message in the Maven output
  - Verify your credentials in settings.xml
  - Ensure your GPG key is available: `gpg --list-secret-keys`
  - Check Sonatype JIRA ticket status

## After First Release

Once your first release is successful:
1. Your groupId will be permanently registered
2. Future releases will be faster (no manual approval needed)
3. You can use the same process for all future versions

## References

- [OSSRH Guide](https://central.sonatype.org/publish/publish-guide/)
- [Maven Central Requirements](https://central.sonatype.org/publish/requirements/)
- [GPG Signing Guide](https://central.sonatype.org/publish/requirements/gpg/)

