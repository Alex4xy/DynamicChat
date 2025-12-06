# 🗨️ DynamicChat

> A modern Android messaging application featuring dynamic layouts, Material 3 theming, and real-time WebSocket communication built with Jetpack Compose.

---

## 📖 Overview

**DynamicChat** is a feature-rich messaging app showcasing modern Android development best practices with clean architecture, reactive state management, and beautiful UI design.

### Key Highlights

- **Multiple Layout Modes:** Classic bubbles, compact view, and innovative hexagonal (Beehive) layout
- **Dynamic Material 3 Theming:** Light, Dark, and High Contrast modes with full component theming
- **Real-Time Messaging:** WebSocket-powered instant communication
- **Clean Architecture:** Multi-layer separation with clear dependency flow
- **Reactive State:** StateFlow-based state management for predictable UI updates
- **Custom UI Components:** Hand-crafted hexagon shapes and layout algorithms

---

## 🚀 Quick Start

### Clone the Repository

**HTTPS:**
```bash
git clone https://github.com/Alex4xy/DynamicChat.git
```

**SSH:**
```bash
git clone git@github.com:Alex4xy/DynamicChat.git
```

### Run the App

1. Open the project in **Android Studio Hedgehog** or newer
2. Click **Run ▶**
3. The app connects automatically to the WebSocket server

---

## 🏗️ Architecture

### Project Structure

```
app/src/main/java/com/alex/dynamicchat/
│
├── core/                           # Shared application components
│   ├── app/                        # Application class, MainActivity, BaseViewModel
│   ├── coroutine/                  # Coroutine dispatcher modules
│   ├── navigation/                 # Navigation graph configuration
│   ├── network/                    # Network monitoring & OkHttp setup
│   ├── providers/                  # Resource provider utilities
│   ├── repository/                 # Repository DI bindings
│   └── usecase/                    # Base use case abstractions
│
├── features/chat/
│   │
│   ├── data/                       # Data layer
│   │   ├── local/                  # DataStore (preferences storage)
│   │   ├── network/
│   │   │   ├── client/             # WebSocket client implementation
│   │   │   └── dto/                # Data transfer objects
│   │   └── repository/             # Repository implementation
│   │
│   ├── domain/                     # Business logic layer
│   │   ├── model/                  # Domain models
│   │   ├── repository/             # Repository interface
│   │   └── usecase/                # Use cases (Connect, Send, Observe)
│   │
│   └── presentation/               # UI layer
│       ├── event/                  # User interaction events
│       ├── state/                  # UI state definitions
│       ├── ui/
│       │   ├── components/         # Reusable UI components
│       │   ├── layouts/            # Layout mode implementations
│       │   ├── models/             # UI-specific models
│       │   ├── screen/             # Main chat screen
│       │   └── theme/              # Theme configuration
│       └── viewmodel/              # ViewModel implementation
│
└── ui/theme/                       # Global Material 3 theme
```

### Clean Architecture Layers

- **Presentation:** Jetpack Compose UI, ViewModels, UI events
- **Domain:** Business logic, use cases, domain models
- **Data:** Repository implementations, WebSocket client, local storage

---

## ✨ Features

### 🎨 Layout Modes

#### Classic Layout
- Traditional chat bubbles with left/right alignment
- Sender names and timestamps
- Unread message indicators
- Optimal for standard messaging experience

#### Compact Layout
- Space-efficient design with reduced padding
- Grouped consecutive messages from same sender
- Smaller typography for information density
- Perfect for quick scanning

#### Beehive Layout
- Unique hexagonal tile design
- Offset-row honeycomb pattern
- Tap-to-expand functionality
- Center-aligned content
- Eye-catching visual experience

### 🌈 Dynamic Theming

All themes support complete UI customization across every component.

#### Light Theme
- Soft neutral backgrounds
- High-contrast readable text
- Blue sender bubbles, gray recipient bubbles
- Clean, professional appearance

#### Dark Theme
- Deep navy background
- Blue and storm-gray message bubbles
- White text for excellent readability
- Easy on the eyes in low-light conditions

#### High Contrast Theme
- Pure black background
- Intense yellow text for maximum visibility
- Bold bubble contrast
- Designed for accessibility
- WCAG-compliant color ratios

**Theme affects:**
- Background colors
- Message bubble colors
- Text (primary/secondary)
- Timestamp visibility
- Hexagonal tile colors
- Input bar styling
- Top app bar
- Button states

### 🌐 Real-Time Messaging

**WebSocket Server:** `wss://ws.postman-echo.com/raw`

**Message Flow:**
1. Connect via `ChatWebSocketClient`
2. Monitor connection states: `Connecting → Connected → Closed/Error`
3. Send messages through input bar
4. Messages render immediately (optimistic UI)
5. Server echoes messages back
6. Data flows: `MessageDto → Domain Model → UI Model`
7. Display updates across all active layouts

**Benefits:**
- No proprietary APIs required
- Public server for testing
- Real bidirectional communication
- Open WebSocket protocol

---

## 🔧 Technology Stack

### UI & Compose
- **Jetpack Compose** - Modern declarative UI toolkit
- **Material 3** - Latest Material Design components
- **Compose Navigation** - Type-safe navigation
- **Custom Layouts** - Hand-built Beehive layout algorithm
- **Custom Shapes** - HexagonShape implementation

### Architecture & State Management
- **AndroidX ViewModel** - Lifecycle-aware state holders
- **Kotlin Coroutines** - Asynchronous programming
- **StateFlow/SharedFlow** - Reactive state streams
- **DataStore Preferences** - Modern data persistence
- **Clean Architecture** - Separation of concerns

### Networking
- **OkHttp WebSocket** - Efficient WebSocket implementation
- **OkHttp Logging Interceptor** - Network debugging

### Dependency Injection
- **Hilt (Dagger)** - Compile-time DI framework

---

## 🧠 State Management

### Reactive Architecture

```kotlin
// Single source of truth
StateFlow<ChatState> // Reactive UI state
SharedFlow<Event>    // One-time events (errors, navigation)
```

**State Components:**
- Message list with ordering
- Current input text
- Selected layout mode
- Active theme mode
- WebSocket connection status
- Send/error indicators

**Persistence:**
- Layout preferences saved to DataStore
- Theme preferences saved to DataStore
- Automatic restoration on app restart

---

## ⚡ Performance Optimizations

- **Efficient Recomposition:** StateFlow updates trigger minimal recomposition
- **Smart Layout Measurement:** Custom layouts minimize remeasure overhead
- **Backpressure Control:** SharedFlow with replay for controlled event emission
- **Async Storage:** DataStore operations never block main thread
- **Lazy Loading:** LazyColumn for efficient list rendering

---

## 📚 Assignment Compliance

✅ Multiple chat layouts implemented  
✅ Dynamic theming with Material 3  
✅ Real-time messaging platform integration  
✅ Clean multi-layer architecture  
✅ Modern state management  
✅ Custom UI components (shapes + layouts)  
✅ Dependency injection  
✅ Persistent user preferences

---

## 📄 License

This project was created as an academic assignment for Android Application Development.

---

## 👨‍💻 Author

**Alex4xy**  
[GitHub Profile](https://github.com/Alex4xy)

---

## 🙏 Acknowledgments

- Jetpack Compose team for the amazing UI toolkit
- OkHttp contributors for robust networking
- Material Design team for design system guidance
- Postman for providing the public WebSocket echo server