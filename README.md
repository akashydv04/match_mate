# MatchMate

## Project Description
MatchMate is an Android app that emulates a matrimonial service, displaying match cards from retrieved user info. Accept/Decline interactions are implemented, and there is local persistence of info and complete offline capability. The app places much of an emphasis on good error management, clean design, and an intuitive interface.

## Table of Contents
1.  [Setup](#setup)
2.  [Key Libraries](#key-libraries)
3.  [Architecture](#architecture)
4.  [Custom Features](#custom-features)
    * [Added Fields](#added-fields-education-and-religion)
    * [Match Score Algorithm](#match-score-algorithm)
5.  [Resilience](#resilience-offline-mode-and-error-handling)
6.  [Design Constraints](#design-constraint-handling-no-profile-images)
7.  [Reflection](#reflection-future-improvements)

---

### Setup

1.  **Clone Repository:**
    ```bash
    git clone https://github.com/akashydv04/match_mate.git
    cd MatchMate
    ```
2.  **Open in Android Studio:** Open the project, ensure Gradle syncs successfully.
3.  **Run:** Build and deploy to an Android device or emulator.

### Key Libraries

* **Retrofit & OkHttp:** For efficient network requests to `randomuser.me` API.
* **Gson:** For JSON parsing of API responses.
* **Room:** For local data persistence and offline support.
* **Glide:** For efficient image loading and caching.
* **Kotlin Coroutines:** For asynchronous operations and reactive data flow.
* **Dagger Hilt:** For dependency injection, improving modularity and testability.
* **AndroidX Lifecycle (ViewModel & StateFlow):** For lifecycle-aware UI data management.
* **Material Components & ConstraintLayout:** For modern, responsive UI design.
* **View Binding:** For safe and direct view interactions.

### Architecture

The app follows the **MVVM (Model-View-ViewModel)** architectural pattern with a **Repository Pattern** for clear separation of concerns and testability.

* **View (MainActivity, Adapter):** Displays UI and handles user input. Observes `ViewModel` states.
* **ViewModel (MainViewModel):** Manages UI-related state, handles business logic, and interacts with the `Repository`. Exposes `StateFlow`s to the View.
* **Repository (UserRepository):** Single source of truth. Coordinates data from API (Retrofit) and local database (Room). Handles syncing, retries, and data mapping.
* **Data Sources (API, Room):** Provide raw data. Mappers transform data between layers (API -> DB -> UI).
* **Dagger Hilt:** Injects dependencies across these layers.

### Custom Features

#### Added Fields: Education and Religion
To enhance realism, `education` and `religion` fields are inferred (randomly assigned) during API-to-DB mapping. These fields are essential in matrimonial contexts for compatibility assessment.

#### Match Score Algorithm
A local algorithm in `MatchScoreCalculator.kt` determines a "Match Score" (0-100%) based on:
* **Age Proximity:** Higher score for smaller age differences (up to 60 points).
* **City Match:** 40 points if cities are the same (case-insensitive).
  *(Simulated current user: Age 30, City "London")*

### Resilience: Offline Mode and Error Handling

* **Offline Mode:** User profiles and statuses are stored in a **Room database**, ensuring the app functions fully offline by displaying cached data.
* **Flaky Network Simulation:** A `FlakyNetworkInterceptor` (30% failure rate) is integrated into OkHttp to simulate network unreliability.
* **Retry Mechanism:** The `UserRepository` implements an exponential backoff retry strategy (up to 3 retries) for network failures.
* **Error Handling:** The `MainViewModel` catches network and other errors, updating an `errorMessage` `StateFlow` for UI display, while the app continues to show available cached data.

### Design Constraint Handling: "No Profile Images"

**Scenario:** A hypothetical legal change restricts displaying user profile images.

**UI Adaptation:**
* A `showImages` toggle (accessible via `MainActivity`'s options menu) controls the visibility of `ImageViews` in `MatchCardAdapter`.
* When `showImages` is false, `iv_user_image` is set to `View.GONE`, hiding the image and collapsing its space.
* This ensures the app remains functional and compliant without affecting core logic.

### Reflection: Future Improvements

A significant future improvement would be to implement **Real-time Chat Functionality** between accepted matches.

* **Significance:** Enables direct communication, crucial for matrimonial apps, enhancing engagement and retention.
* **Approach:**
    * **Backend:** Integrate with a real-time messaging service (e.g., Firebase).
    * **Database:** Extend Room for local chat history persistence.
    * **UI:** Add chat list and detailed chat screens.
    * **Notifications:** Implement push notifications for new messages.

---