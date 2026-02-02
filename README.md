
# 🍽️ Tandoori Tadka – Restaurant Order Taking App

A **production-ready Android app** built for **real restaurant usage**, designed to handle order creation, tracking, and status updates with **Clean Architecture**, **offline-first support**, and **modern Android best practices**.

This app is actively used in a restaurant environment to manage takeaway and dine-in orders efficiently.

---

## 🚀 Features

### 🧾 Order Management

* Create orders with multiple items
* Auto-generated **readable order numbers** (Order #1, #2, #3… reset daily)
* Offline-first order creation (works without internet)

### 🔄 Order Lifecycle

* **CREATED → READY → DELIVERED**
* Forward-only status updates (business-rule enforced)
* One-tap status update from Orders screen

### 📦 Address Handling

* Capture and save takeaway address with each order
* Address linked safely to order using foreign keys

### 📋 Orders Screen

* Clean, readable cart-style UI
* Latest orders shown first
* Manual **Swipe to Refresh**
* Status chip for quick visibility

### 🔔 Notifications

* Notification shown when new order arrives
* Tapping notification opens **Orders screen directly**

### 🎨 UI/UX

* Minimal, distraction-free design
* Light grey background for better readability
* White cards with clear typography
* Icons instead of heavy images (fast & clean)

---

## 🏗️ Architecture

This app strictly follows **Clean Architecture** principles.

```
UI (Compose)
   ↓
ViewModel
   ↓
UseCase
   ↓
Repository
   ↓
Local DB (Room) / Remote (Firebase – planned)
```

### Why Clean Architecture?

* Easy to maintain
* Easy to test
* No accidental coupling
* Real production stability (no hidden bugs)

---

## 🧠 Key Architectural Decisions

* **Room is the source of truth**
* Orders are saved locally first (offline-safe)
* UI observes data via `Flow`
* Business rules live inside **UseCases**
* ViewModels never access DAOs or Repositories directly
* Firebase sync is isolated (not blocking UI)

---

## 🛠️ Tech Stack

* **Kotlin**
* **Jetpack Compose**
* **Material 3**
* **Room (SQLite)**
* **Hilt (DI)**
* **Kotlin Coroutines & Flow**
* **Accompanist SwipeRefresh**
* **Firebase (for sync & notifications – extensible)**

---

## 📱 Screens & Flow

### Order Flow

1. Select items
2. Enter address
3. Place order
4. Order saved locally
5. Order appears in Orders list
6. Status updated as order progresses

### Orders Screen

* Shows all active & completed orders
* Swipe down to refresh
* Tap button to move order to next status

---

## 🔐 Reliability & Performance

* Atomic database transactions
* No network dependency for order creation
* Battery-friendly (no background polling)
* Manual refresh avoids unnecessary sync
* Safe foreign-key relationships

---

## 📂 Project Structure (Simplified)

```
domain/
 ├── model
 ├── repository
 └── usecase

data/
 ├── local (Room)
 ├── mapper
 └── repository

ui/
 ├── orders
 ├── cart
 └── address
```

---

## 🧪 Testing Ready

* Domain layer is framework-independent
* Repositories & UseCases can be unit-tested
* UI logic is state-driven (`UiState`)

---

## 🔮 Future Improvements

* Firebase bidirectional sync
* Admin dashboard
* Analytics (daily order count, revenue)
* Order detail screen
* Tablet-optimized UI
* Printer integration

---

## 👨‍🍳 Real-World Usage

This app is **not a demo project**.
It is designed and structured to be used in a **real restaurant setup**, handling real orders and real workflows.

---

## 📜 License

This project is open-source and available for learning and extension.
You are free to fork and adapt it for your own restaurant needs.

---

## 🙌 Author

Developed with a strong focus on **clean code**, **long-term maintainability**, and **real-world reliability**.

---
