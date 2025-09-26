# OWASP Top 10 — Spring Boot Demo

This mirrors the Python demo you shared, with **insecure** and **secure** endpoints for several OWASP categories.

## Run (quickest)

```bash
# Generate Maven wrapper locally (first time only)
mvn -q -N wrapper:wrapper

# Build and run
./mvnw -q spring-boot:run
```

Then try endpoints:

- A01 Broken Access Control
  - `GET /a01/insecure/secret`
  - `GET /a01/secure/secret` (send `X-Role: ADMIN` header)

- A02 Cryptographic Failures
  - `POST /a02/insecure/store?password=secret`
  - `POST /a02/secure/store?password=secret`

- A03 Injection (simulated)
  - `GET /a03/insecure/find?username=alice' OR '1'='1`
  - `GET /a03/secure/find?username=alice`

- A04 Insecure Design (rate limit 5/10s)
  - `GET /a04/insecure/ping`
  - `GET /a04/secure/ping` (uses `X-Real-IP` or `X-Forwarded-For` for identity)

- A05 Security Misconfiguration
  - `GET /a05/insecure/config`
  - `GET /a05/secure/config`
  - (set `DEMO_SECRET` env var to see effect)

- A07 Identification & Authentication Failures (JWT)
  - `POST /a07/insecure/parse` with `jwt=...` (no signature verification)
  - `POST /a07/secure/parse` verifies with `JWT_SECRET` env (HS256)

- A08 Software & Data Integrity Failures
  - `POST /a08/insecure/deserialize` with `b64=...` (Java deserialization — don't send untrusted data)
  - `POST /a08/secure/deserialize` JSON body `{ "title": "t", "body": "b" }`

- A09 Security Logging & Monitoring Failures
  - `POST /a09/insecure/login?username=a&password=b` (logs secrets — BAD)
  - `POST /a09/secure/login?username=a&password=b`

- A10 SSRF
  - `GET /a10/insecure/fetch?url=http://example.com`
  - `GET /a10/secure/fetch?url=http://example.com` (allow-list + private range checks)

## One-zip with dependencies

This repository includes a helper to **vendor dependencies** so you can make a single distributable zip:

```bash
# 1) Ensure wrapper exists
mvn -q -N wrapper:wrapper

# 2) Pre-fetch dependencies into target/dependency
./mvnw -q -B -Dmaven.repo.local=.m2/repository dependency:go-offline
./mvnw -q -B -DincludeScope=runtime org.apache.maven.plugins:maven-dependency-plugin:copy-dependencies

# 3) Package a self-contained zip (sources + runtime jars)
./mvnw -q -B -DskipTests package
bash scripts/make_bundle.sh
```

The output will be `bundle/owasp10-spring-demo-with-deps.zip` containing:
- `target/dependency/*.jar` (runtime deps)
- `target/owasp10-spring-demo-*.jar` (fat jar you can run with `java -jar`)
- project sources and this README

> Note: I did not fetch third‑party jars in this environment, but the commands above will do it on your machine.

## License
For demo/educational use.
