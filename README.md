# Supervisor Java SDK

Official Java SDK for the [Supervisor](https://supervisor.gg) content moderation API.

Uses `java.net.http.HttpClient` — only dependency is Jackson for JSON.

## Installation

### Gradle

```kotlin
dependencies {
    implementation("gg.supervisor:supervisor-sdk:0.2.0")
}
```

### Maven

```xml
<dependency>
    <groupId>gg.supervisor</groupId>
    <artifactId>supervisor-sdk</artifactId>
    <version>0.2.0</version>
</dependency>
```

## Quick Start

```java
import gg.supervisor.sdk.SupervisorClient;
import gg.supervisor.sdk.models.*;

var client = SupervisorClient.builder()
        .apiKey("sk-...")
        .build();

var result = client.moderate(ModerationRequest.builder()
        .text("check this text")
        .model(ModerationModel.SENTINEL)
        .build());

System.out.println("Flagged: " + result.isFlagged());
System.out.println("Labels: " + result.labels());
```

## Usage

### Moderate Text

```java
var result = client.moderate(ModerationRequest.builder()
        .text("some text to check")
        .model(ModerationModel.ARBITER)
        .enabledLabels(List.of(ModerationLabel.HARASSMENT, ModerationLabel.TOXICITY))
        .build());
```

### Batch Moderation

```java
var results = client.moderateBatch(BatchModerationRequest.builder()
        .texts(List.of("first", "second", "third"))
        .build());

for (var r : results) {
    System.out.println("Flagged: " + r.isFlagged() + ", Labels: " + r.labels());
}
```

### Username Check

```java
var result = client.checkUsername("username123");
System.out.println("Flagged: " + result.isFlagged() + ", Score: " + result.score());
```

### Get Labels

```java
var labels = client.getLabels();
```

## Platform API

```java
import gg.supervisor.sdk.PlatformClient;

var platform = PlatformClient.builder()
        .clientId("...")
        .clientSecret("...")
        .build();

// Provision a user
var user = platform.provisionUser("user@example.com");

// Moderate on behalf of a user
var result = platform.moderate(PlatformModerationRequest.builder()
        .userEmail("user@example.com")
        .text("check this")
        .build());

// Create checkout session
var checkout = platform.createCheckout(PlatformCheckoutRequest.builder()
        .userEmail("user@example.com")
        .tier(Tier.STANDARD)
        .billingCycle(BillingCycle.MONTHLY)
        .successUrl("https://yourapp.com/success")
        .cancelUrl("https://yourapp.com/cancel")
        .build());

// List linked users
var users = platform.listUsers();

// Get a specific linked user by ID
var linked = platform.getUser("usr_123");
System.out.println("Authorized: " + linked.authorized() + ", Tier: " + linked.tier());

// Confirm a user's authorization with the code they provided
var confirmation = platform.confirmAuthorization("auth-code-from-user");
System.out.println("Confirmed: " + confirmation.email());

// Check Stripe Connect onboarding status
var status = platform.getConnectStatus();
System.out.println("Onboarding complete: " + status.onboardingComplete());

// Change the plan of an active subscription
var change = platform.changePlan(PlatformChangePlanRequest.builder()
        .userEmail("user@example.com")
        .tier(Tier.PREMIUM)
        .billingCycle(BillingCycle.ANNUAL)
        .build());
System.out.println("Subscription: " + change.subscriptionId());
```

### Checkout and plan changes

`createCheckout` returns 403 if the user has not authorized the platform, and 400 if the user already has an active subscription (use `changePlan` instead). `changePlan` returns 403 if the subscription was not originated by this platform, and 400 if there is no active subscription. Revenue share is set at subscription creation and preserved across plan changes.

## Configuration

```java
var client = SupervisorClient.builder()
        .apiKey("sk-...")
        .baseUrl("https://supervisor.gg")  // default
        .timeout(30)                            // seconds, default
        .build();
```

## Error Handling

```java
import gg.supervisor.sdk.exceptions.SupervisorException;

try {
    var result = client.moderate(request);
} catch (SupervisorException e) {
    if (e.isAuthError()) {
        System.err.println("Invalid API key");
    } else if (e.isRateLimit()) {
        System.err.println("Too many requests");
    } else {
        System.err.println("API error [" + e.getStatusCode() + "]: " + e.getMessage());
    }
}
```

## Requirements

- Java 17+
- Jackson Databind 2.17+

## License

MIT
