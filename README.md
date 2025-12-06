🗨️ DynamicChat
Dynamic Chat Layouts & Theming with Real-Time Messaging (Jetpack Compose)

Android Application Development — Assignment Project

📖 Overview

DynamicChat is a modern messaging app built with Jetpack Compose, supporting:

Multiple layout modes (Classic, Compact, Beehive/Hex)

Dynamic Material 3 theming (Light, Dark, High Contrast)

Real-time messaging over a public WebSocket server

A clean multi-layer architecture

Reactive state with StateFlow

Custom shapes and layouts (Hexagon tiles)

This project demonstrates advanced UI techniques, architecture, and real-time communication — fully satisfying all assignment requirements.

🔗 Clone the Repository

HTTPS

git clone https://github.com/Alex4xy/DynamicChat.git


SSH

git clone git@github.com:Alex4xy/DynamicChat.git

🧱 Project Structure
High-Level Architecture View
app/
└── src/main/java/com/alex/dynamicchat
├── core/                # Shared app-level components
│    ├── app/            # App.kt, MainActivity, BaseViewModel
│    ├── coroutine/      # Dispatcher modules
│    ├── navigation/     # NavGraph
│    ├── network/        # Connectivity observer
│    ├── providers/      # ResourceProvider
│    ├── repository/     # DI bindings
│    └── usecase/        # Base UseCase
│
├── features/
│    └── chat/
│         ├── data/      
│         │    ├── local/         # DataStore for theme/layout
│         │    ├── network/       # WebSocket client (OkHttp)
│         │    ├── dto/           # Message DTO
│         │    └── repository/    # ChatRepositoryImpl
│         │
│         ├── domain/
│         │    ├── model/         # Message, ConnectionState, WebSocketError
│         │    ├── repository/    # ChatRepository interface
│         │    └── usecase/       # Connect/Disconnect/Observe/Send use cases
│         │
│         └── presentation/
│              ├── event/         # ChatEvent
│              ├── state/         # ChatState, EmptyState
│              ├── ui/            
│              │    ├── components/   # TopBar, InputBar
│              │    ├── layouts/      # Classic, Compact, Beehive
│              │    ├── models/       # MessageUi mapper
│              │    ├── screen/       # ChatScreen (main UI)
│              │    └── theme/        # ChatThemeColors + Modes
│              └── viewmodel/         # ChatViewModel
│
└── ui/theme/    # Global M3 theme (colors, typography)

🎨 Features
✔ Multiple Layout Modes

Classic Layout (LazyColumn, bubbles, timestamps)

Compact Layout (Grouped messages, reduced padding)

Beehive Layout (Custom hexagonal tile layout & shape)

✔ Dynamic Material 3 Theming

Light

Dark

High Contrast (Accessibility)

Theme changes affect:

Backgrounds

Bubble colors

Text colors

Timestamps

Hex tiles

App bar + input bar

Buttons & icons

✔ Real-Time Messaging

Using OkHttp WebSockets:

wss://ws.postman-echo.com/raw


The app communicates with an open echo server that responds in real time.

🌐 Messaging Platform Integration

The app uses a public open WebSocket protocol — meeting assignment requirements for integrating with an open messaging platform.

Workflow:

Connect using ChatWebSocketClient

Observe connection state (Connecting → Connected → Closed/Error)

Send message via WebSocket

Receive echoed messages

Convert DTO → Domain → UI models

Render in all layout modes

🧠 State Management

The app uses:

StateFlow for reactive state streams

MutableStateFlow inside ChatViewModel

DataStore for layout/theme persistence

SharedFlow for real-time WebSocket events

ViewModel state includes:

Messages

Input text

Layout mode

Theme mode

Connection state

Sending/error states

📚 Libraries & Technologies Used
UI / Jetpack Compose

Jetpack Compose

Material 3

Compose Navigation

Custom Layouts (Beehive)

Custom Shapes (HexagonShape)

Architecture & State

AndroidX ViewModel

Kotlin Coroutines & StateFlow

DataStore Preferences

Clean Architecture (Domain / Data / Presentation)

Networking

OkHttp (WebSocket)

OkHttp Logging Interceptor

Dependency Injection

Hilt (Dagger)

🎨 Theming Details
Light Theme

Soft neutral background

High-contrast text

Blue “Me” bubbles, gray “Other” bubbles

Dark Theme

Deep navy background

Blue and storm-gray bubbles

White text with subtle secondary tones

High Contrast Theme

Black background

Yellow primary & secondary text

Strong bubble contrast

Adjusted hex tiles for visibility

🧩 Layout Details
1️⃣ Classic Chat Layout

LazyColumn

Left/right bubble alignment

Sender name, message text, timestamps

Unread indicators

2️⃣ Compact Layout

Tighter spacing

Smaller typography

Grouped sender headers

3️⃣ Beehive / Hexagonal Layout

Custom-built using:

HexagonShape (custom Path shape)

Custom arrangement logic (offset hex rows)

Centered text

Tap to expand message

Not a LazyVerticalGrid — true custom layout.

⚙️ How to Run

Clone the project:

git clone https://github.com/Alex4xy/DynamicChat.git


Open in Android Studio Hedgehog or newer

Click Run ▶

Chat will auto-connect to the WebSocket server.

🚀 Performance Considerations

StateFlow used for efficient Compose recomposition

Custom layouts optimized to minimize remeasure

DataStore use is async & non-blocking

WebSocket streams use backpressure-friendly SharedFlow

UI recomposes only when relevant state changes

🏁 Summary

DynamicChat demonstrates:

✔ Multi-layout chat UI (Classic, Compact, Beehive)

✔ Dynamic Material 3 theming

✔ Real-time WebSocket messaging

✔ Clean Architecture structure

✔ Modern state management with StateFlow

✔ Custom layouts + custom shapes

✔ DI with Hilt

✔ Persistent UI settings (DataStore)