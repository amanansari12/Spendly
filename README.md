# Spendly

> **Status:** In active development
> 
> **Current feature:** Add Transaction
> 
> **Next major feature:** Budgets

Spendly is a local-first personal finance application for Android. The application is designed around a simple principle: financial data should remain usable even without an internet connection, with the local database acting as the primary source of truth. A backend service using Python/FastAPI is planned for backup, synchronization, and multi-device restore in a later stage.

---

## 1. Project Vision

Spendly is intended to help users understand and control their personal finances through:

- Income and expense tracking
- Category-based transaction management
- Monthly budgeting
- Budget allocation across categories
- Overspending handling
- Spending analytics
- Persistent local storage
- Future cloud backup and multi-device restoration

The application is being built as a real product rather than a simple CRUD demonstration. The architecture therefore separates UI, state management, data access, repositories, and persistence.

---

## 2. Current Development Status

### Completed / Implemented

- Android application foundation
- Jetpack Compose UI
- Navigation structure
- Onboarding flow
- User profile persistence
- Default category seeding
- Room database
- Hilt dependency injection
- MVVM-based presentation architecture
- Monthly budget data model
- Budget allocation data model
- Transaction data model
- Home screen financial summary
- Recent transaction display
- Add Transaction flow
- Income transaction persistence
- Expense transaction persistence
- Transaction history filtering model
- Overspending detection
- Overspending budget adjustment flow
- Move budget from another category flow
- Transaction soft deletion support
- Currency-aware amount storage and formatting
- DataStore-based onboarding completion state

### Currently Working On

**Add Transaction**

The transaction feature is currently the main active development area. The flow supports both income and expense transactions and integrates the transaction write with the appropriate budget state inside a Room database transaction.

### Next Major Feature

**Budgets**

The next development phase is to turn the existing budget data layer into a complete user-facing budget management experience. This includes creating and editing category allocations, viewing monthly budget status, handling unallocated funds, and implementing month-to-month budget behavior.

---

## 3. Technology Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Architecture | MVVM |
| Dependency Injection | Hilt |
| Local Database | Room |
| Async / Reactive State | Kotlin Coroutines + Flow |
| Preferences | Jetpack DataStore |
| Navigation | Navigation Compose |
| Database | SQLite through Room |
| Backend (planned) | Python + FastAPI |
| Backend Database (planned) | PostgreSQL |
| Version Control | Git / GitHub |
| IDE | Android Studio |

---

## 4. High-Level Architecture

Spendly follows a layered Android architecture:

```text
                    ┌─────────────────────────┐
                    │      Jetpack Compose     │
                    │     Screens / UI State   │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │       ViewModel          │
                    │  UI State + User Actions │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │       Repository         │
                    │ Business/Data Operations │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │           DAO            │
                    │   Room Database Access   │
                    └────────────┬────────────┘
                                 │
                                 ▼
                    ┌─────────────────────────┐
                    │       Room Database      │
                    │         SQLite            │
                    └─────────────────────────┘
```

Hilt supplies dependencies such as the database and DAOs to repositories and ViewModels.

The application is intentionally local-first. UI operations do not need an active network connection because application data is persisted locally first.

---

## 5. Package Structure

The source tree is organized by feature and responsibility rather than putting all UI and data classes into a single package.

```text
com.amanansari.spendly
│
├── components/
│   ├── AmountInputField.kt
│   ├── Components.kt
│   ├── SwipeToDelete.kt
│   ├── TransactionTypeToggle.kt
│   └── WheelPicker.kt
│
├── data/
│   ├── local/
│   │   ├── converters/
│   │   ├── dao/
│   │   ├── db/
│   │   ├── defaults/
│   │   ├── entity/
│   │   └── preferences/
│   │
│   └── repository/
│
├── home/
│   ├── screen/
│   ├── state/
│   └── viewmodel/
│
├── model/
│
├── navigation/
│   ├── graph/
│   ├── route/
│   └── BottomBarItem.kt
│
├── onBoarding/
│   ├── screen/
│   ├── state/
│   └── viewmodel/
│
├── transaction/
│   ├── screen/
│   ├── state/
│   └── viewmodel/
│
├── ui/theme/
├── utils/
├── MainActivity.kt
├── MainScreen.kt
└── SpendlyApplication.kt
```

---

## 6. Database Design

Room currently contains five persistent entities:

1. `UserEntity`
2. `BudgetEntity`
3. `BudgetAllocationEntity`
4. `CategoryEntity`
5. `TransactionEntity`

The Room database is currently at **version 9**.

### 6.1 User

`UserEntity` stores the local user's identity and preferences.

```text
users
├── userId : UUID
├── name : String
├── email : String
├── currencyCode : String
├── theme : String
├── createdAt : Long
└── updatedAt : Long
```

The application currently assumes a local primary user and uses the generated UUID as the stable identity.

---

### 6.2 Categories

`CategoryEntity` represents transaction categories.

```text
categories
├── categoryId : String
├── name : String
├── type : String
├── isSystem : Boolean
├── isActive : Boolean
└── sortOrder : Int
```

Default categories are seeded by `CategoryRepository` when required. A mutex prevents multiple concurrent seeding operations.

Categories are also referenced by transactions and budget allocations.

---

### 6.3 Monthly Budget

`BudgetEntity` stores the monthly financial budget state.

```text
monthly_budget
├── monthlyBudgetId : UUID
├── userId : UUID
├── monthKey : String       # YYYY-MM
├── openingBalance : Long
├── totalIncome : Long
├── allocatedAmount : Long
├── closingBalance : Long
├── copiedFromMonthKey : String?
├── isAutoCopied : Boolean
├── createdAt : Long
├── updatedAt : Long
├── deletedAt : Long?
└── rowVersion : Int
```

There is a unique index on:

```text
(userId, monthKey)
```

This enforces one monthly budget record per user and month.

---

### 6.4 Budget Allocation

`BudgetAllocationEntity` stores category-level allocation within a monthly budget.

```text
budget_allocation
├── allocationId : UUID
├── monthlyBudgetId : UUID
├── userId : UUID
├── monthKey : String
├── categoryId : String
├── allocatedAmount : Long
├── amountSpent : Long
├── isCustomised : Boolean
├── createdAt : Long
├── updatedAt : Long
├── deletedAt : Long?
└── rowVersion : Int
```

Relationships:

```text
Monthly Budget 1 ──────── * Budget Allocation
Category      1 ──────── * Budget Allocation
```

The monthly budget uses cascade deletion for its allocations, while category deletion is restricted when the category is still referenced.

---

### 6.5 Transactions

`TransactionEntity` is the core financial event record.

```text
transactions
├── transactionId : UUID
├── userId : UUID
├── categoryId : String
├── type : TransactionType
├── currencyCode : String
├── amount : Long
├── occurredAt : Long
├── monthKey : String
├── note : String?
├── isDeleted : Boolean
├── createdAt : Long
├── updatedAt : Long
├── rowVersion : Int
└── sourceDeviceId : String?
```

Transaction types currently are:

```kotlin
enum class TransactionType {
    INCOME,
    EXPENSE
}
```

Amounts are stored as `Long`, allowing the application to represent currency values in minor units rather than floating-point values. For example, `₹125.50` can be represented internally as `12550`.

The `occurredAt` field is the actual transaction timestamp and is used for date-sensitive filtering. `monthKey` supports efficient month-based queries.

---

## 7. Data Access Layer

### UserDao

Responsibilities:

- Insert the local user
- Observe the current user

### CategoryDao

Responsibilities:

- Seed categories
- Check whether categories already exist

### BudgetDao

Responsibilities:

- Insert monthly budgets
- Read a monthly budget
- Read all budgets
- Add income to a month's financial state
- Record additional budget allocation

### BudgetAllocationDao

Responsibilities:

- Insert category allocations
- Insert allocation batches
- Read allocations for a month
- Read budget totals
- Increment category spending
- Adjust category allocations

### TransactionDao

Responsibilities:

- Insert transactions
- Update transactions
- Fetch recent transactions
- Fetch monthly transactions
- Fetch a transaction by ID
- Soft-delete transactions
- Filter transaction history by month and type

---

## 8. Repository Layer

Repositories hide direct DAO access from ViewModels and coordinate operations that touch multiple tables.

### `TransactionRepository`

This is currently the most important repository because transaction creation changes more than the `transactions` table.

It supports:

- Add expense
- Add income
- Add expense after increasing the target category budget
- Add expense after moving allocation from another category
- Read the current budget
- Read budget allocations

Transaction writes that also affect budget state are performed through `database.withTransaction { ... }`, ensuring the related database changes are atomic.

### `BudgetRepository`

Provides access to monthly budgets and budget mutations.

### `BudgetAllocationRepository`

Provides reactive category allocation data by month.

### `CategoryRepository`

Owns default category seeding and protects the seed operation with a `Mutex`.

### `HomeRepository`

Combines data needed by the Home screen, including:

- User
- Monthly budget
- Total allocated budget
- Recent transactions
- Category-level budget information

### `OnboardingRepository`

Creates the initial user, initial budget, first income transaction, and initial category allocations in a single Room transaction when onboarding is completed.

---

## 9. Onboarding Flow

The onboarding process currently collects the information needed to establish the local financial profile.

```text
User Information
       ↓
Initial Budget / Income
       ↓
Initial Category Allocations
       ↓
Income Source
       ↓
Create User + Budget + Initial Transaction + Allocations
       ↓
Persist onboarding_completed in DataStore
       ↓
Main Application
```

The completion operation is transactional. This prevents a partially initialized application state where the user exists but the initial budget or transaction does not.

---

## 10. Add Transaction Feature

This is the current active feature.

### Transaction Form

The transaction form currently manages:

- Transaction type
- Amount
- Category
- Note
- Date
- Currency

The ViewModel maintains these inputs and creates a `TransactionEntity` at save time.

### Validation

Before saving, the ViewModel validates:

1. Amount must be non-zero.
2. A category must be selected.
3. A note must be provided.
4. The current user must be loaded.

Validation failures are surfaced through `TransactionCompletionState.Error`.

---

## 11. Expense Processing

Expense creation is not treated as a simple insert because it affects budget state.

The current logic is:

```text
User presses Save
        ↓
Validate input
        ↓
Expense?
   ├── No → Save income
   │
   └── Yes
        ↓
Find category allocation
        ↓
Calculate remaining category budget
        ↓
Will the transaction exceed it?
   ├── No → Save transaction + update spent amount
   │
   └── Yes → Show budget adjustment modal
```

### Budget-safe expense

When sufficient budget remains:

```text
Insert Transaction
        +
Increase amountSpent for category
```

These operations execute within a Room database transaction.

---

## 12. Overspending Flow

Spendly currently provides an explicit decision flow when an expense exceeds the category's available allocation.

The modal offers two active strategies:

### A. Allocate More

Additional unallocated money is added to the target category.

```text
Available unallocated money
          ↓
Increase target category allocation
          ↓
Insert expense transaction
          ↓
Increase amountSpent
          ↓
Record additional allocation in monthly budget
```

The action is disabled when there is not enough unallocated money to cover the overspend.

### B. Move From Another Category

The user can transfer budget from another category that has enough remaining allocation.

```text
Source Category
       ↓
Decrease source allocation
       ↓
Increase target allocation
       ↓
Insert transaction
       ↓
Increase target amountSpent
```

All changes are committed atomically.

### Success State

After a successful transaction, the UI presents a success sheet with options to:

- Finish the transaction flow
- Add another transaction

The "Add Another" action clears the previous transaction input state.

---

## 13. Income Processing

Income follows a simpler path:

```text
Create TransactionEntity(INCOME)
             ↓
Insert transaction
             ↓
Increase monthly totalIncome
             ↓
Increase monthly closingBalance
```

This operation is also performed transactionally.

The initial onboarding income is stored as an income transaction with a note such as `Initial Balance`.

---

## 14. Transaction History

A separate transaction history feature is already modeled in the codebase.

Current state supports filtering by:

- Month
- Transaction type
- Category selection state

The DAO supports month and type filtering directly in SQL.

The UI is still being expanded and the navigation route currently indicates that the complete history screen remains a work in progress.

---

## 15. Home Screen

The Home ViewModel combines multiple reactive Room flows into a single `HomeUiState`.

The current Home data includes:

- User name
- Currency
- Opening balance
- Total income
- Total allocated amount
- Amount spent from allocations
- Closing balance
- Previous month source for copied budgets
- Recent transactions
- Category-level allocation information

Because these values are exposed through `Flow` and combined in the ViewModel, UI state reacts to database changes automatically.

---

## 16. Navigation

The application uses Navigation Compose with typed routes.

Current primary routes include:

```text
Home
Analytics
Budget
Profile
AddTransaction
TransactionHistory
```

The bottom navigation currently exposes:

```text
Home | Analytics | Budget | Profile
```

`AddTransaction` is navigated to from Home quick actions and can receive an optional category ID for quick category selection.

Some routes, such as Analytics, Budget, Profile, and the full Transaction History experience, are still under active development.

---

## 17. State Management

The project uses two complementary forms of state management.

### Compose State

Feature ViewModels use Compose state such as:

```kotlin
mutableStateOf(...)
mutableLongStateOf(...)
```

This is useful for form state and ephemeral UI state such as:

- Selected category
- Amount input
- Note
- Transaction type
- Modal state
- Completion state

### Kotlin Flow

Room queries return `Flow`, which is used for persistent application state such as:

- User
- Monthly budgets
- Budget allocations
- Recent transactions
- Transaction history

This creates a reactive pipeline:

```text
Room DB
  ↓
DAO Flow
  ↓
Repository
  ↓
ViewModel
  ↓
Compose UI
```

---

## 18. DataStore

Jetpack DataStore is currently used for lightweight application preferences.

The implemented preference is:

```text
onboarding_completed : Boolean
```

This value determines whether the application should enter onboarding or the main application flow.

DataStore is intentionally separated from Room because it stores application preferences rather than relational financial data.

---

## 19. Dependency Injection

Hilt is used to provide dependencies throughout the application.

The dependency chain is conceptually:

```text
Hilt
 ↓
SpendlyDatabase
 ↓
DAOs
 ↓
Repositories
 ↓
ViewModels
 ↓
Compose Screens
```

This reduces manual object creation and makes feature dependencies explicit.

---

## 20. Money Representation

Financial values are stored using `Long` rather than `Double` or `Float`.

Example:

```text
₹1.00   → 100
₹10.50  → 1050
₹999.99 → 99999
```

This avoids floating-point precision problems common in monetary calculations.

The transaction ViewModel converts decimal user input into minor units using `BigDecimal`.

---

## 21. Date and Month Model

Spendly uses two concepts for dates:

### `occurredAt`

The exact transaction timestamp stored as epoch milliseconds.

### `monthKey`

A normalized month identifier in the format:

```text
YYYY-MM
```

Example:

```text
2026-08
```

This makes monthly financial queries straightforward while retaining the exact transaction timestamp for ordering and date-specific behavior.

---

# 22. Budget Feature Roadmap

The next major phase is the **Budget Management** feature.

The existing database and repository layer already provide much of the foundation needed for this feature. The next step is to expose the functionality through a complete UI and establish the final business rules.

## Phase 1: Budget Overview

Create the main Budget screen with:

- Selected month
- Total income
- Opening balance
- Available amount
- Total allocated amount
- Total spent
- Remaining amount
- Closing balance
- Unallocated amount

Example conceptual model:

```text
Opening Balance
      +
Income
      =
Available Money
      ↓
Allocated Across Categories
      ↓
Category Spending
      ↓
Remaining Budget
```

## Phase 2: Category Allocations

Display each category with:

```text
Category
Allocated
Spent
Remaining
Progress
```

Users should be able to modify the allocation amount directly.

## Phase 3: Allocation Rules

Implement rules for:

- Adding allocation
- Reducing allocation
- Preventing invalid negative allocations
- Tracking customized allocations
- Updating monthly budget totals
- Preserving category-level consistency

## Phase 4: Unallocated Money

Clearly expose money that has not yet been assigned to a category.

The intended relationship is:

```text
Unallocated
= Available Money - Total Allocated
```

The exact calculation should remain consistent with the final definition of `openingBalance`, `totalIncome`, `allocatedAmount`, and `closingBalance`.

## Phase 5: Month-to-Month Budget Carry Forward

The data model already contains:

- `openingBalance`
- `copiedFromMonthKey`
- `isAutoCopied`

These support monthly continuity.

Planned behavior:

```text
Previous Month
      ↓
Closing Balance
      ↓
Opening Balance of New Month
      ↓
Optional Copy of Previous Category Allocations
      ↓
User Can Customize Current Month
```

The application should provide settings to control whether carry-forward and automatic copying are enabled.

---

## 23. Planned FastAPI Backend

A Python/FastAPI backend is planned as a later phase of the project.

The backend should **not** replace Room as the immediate source of truth for offline operation. Instead, the intended architecture is:

```text
                 ┌──────────────────────┐
                 │      Android App     │
                 │  Kotlin + Compose    │
                 └──────────┬───────────┘
                            │
                       Local First
                            │
                            ▼
                 ┌──────────────────────┐
                 │       Room DB         │
                 │   Primary Local Data  │
                 └──────────┬───────────┘
                            │
                       Sync / Backup
                            │
                            ▼
                 ┌──────────────────────┐
                 │      FastAPI API      │
                 │   Python + AsyncIO    │
                 └──────────┬───────────┘
                            │
                            ▼
                 ┌──────────────────────┐
                 │      PostgreSQL       │
                 └──────────────────────┘
```

### Planned Backend Responsibilities

- User/account management
- Authentication
- Backup of local financial data
- Restore to a new device
- Multi-device synchronization
- Conflict handling
- Server-side data validation
- API versioning

The Android application should remain functional when the backend is unavailable.

---

## 24. Synchronization Design Considerations

The existing data model already contains fields that can support future synchronization:

```text
rowVersion
createdAt
updatedAt
deletedAt / isDeleted
sourceDeviceId
UUID primary keys
```

These should eventually support conflict detection and synchronization between the local Room database and the FastAPI service.

Potential sync pattern:

```text
Local mutation
      ↓
Store locally
      ↓
Mark / identify unsynchronized change
      ↓
Sync worker
      ↓
FastAPI
      ↓
PostgreSQL
      ↓
Resolve conflicts
      ↓
Return server state
      ↓
Update local Room DB
```

A dedicated sync model should be designed before backend implementation begins so that local and server records do not drift semantically.

---

## 25. Future Analytics

The Analytics section is planned to use transaction and budget data to provide:

- Daily spending trends
- Monthly spending trends
- Category spending distribution
- Budget utilization
- Highest spending categories
- Income versus expense comparison
- Remaining monthly budget
- Historical comparisons

The existing `occurredAt`, `monthKey`, category IDs, transaction type, and amount fields provide the core data needed for these queries.

---

## 26. Future Settings

Settings are intended to control behavior such as:

- Default currency
- Carry-forward behavior
- Automatic budget copying
- Theme
- Other application preferences

A `SettingsEntity` is already present in the codebase, although it is not currently registered in `SpendlyDatabase` as one of the active Room entities. Before relying on it for production behavior, its persistence design should be finalized and integrated properly.

---

## 27. Important Engineering Principles

### Local-first

Financial records should be usable without a network connection.

### Atomic financial updates

Operations that modify a transaction and budget state together should use Room transactions.

### Reactive UI

Persistent application state should flow from Room through repositories and ViewModels into Compose.

### Stable IDs

UUIDs are used for primary identifiers where records may eventually participate in synchronization.

### Monetary correctness

Money is stored as integer minor units to avoid floating-point precision errors.

### Soft deletion where synchronization matters

Transaction deletion is currently implemented as a soft delete using `isDeleted`, preserving the record for future synchronization considerations.

---

## 28. Current Priority Order

The project should currently progress in this order:

```text
1. Finish Add Transaction
      ↓
2. Verify all expense / income edge cases
      ↓
3. Build Budget UI
      ↓
4. Finalize budget calculation rules
      ↓
5. Complete Transaction History
      ↓
6. Build Analytics
      ↓
7. Build Profile / Settings
      ↓
8. Design FastAPI API contract
      ↓
9. Implement FastAPI + PostgreSQL
      ↓
10. Implement backup / restore / synchronization
```

The priority is intentional: the local data model and business rules should become stable before introducing network synchronization.

---

## 29. Resume / Portfolio Description

### One-line

**Spendly** is an offline-first personal finance Android application built with Kotlin, Jetpack Compose, Room, Hilt, MVVM, Coroutines, and Flow, with a planned FastAPI/PostgreSQL backend for backup and synchronization.

### Resume-ready project description

- Developing an offline-first personal finance Android application using Kotlin and Jetpack Compose, with Room for persistent local storage and Hilt-based dependency injection following MVVM architecture.
- Implemented income and expense tracking, category-based transactions, monthly budgets, and atomic budget updates using Room database transactions.
- Built reactive data flows using Kotlin Coroutines and Flow, including budget-aware overspending detection and category budget reallocation.
- Planning a Python/FastAPI and PostgreSQL backend for secure backup, restore, and multi-device synchronization.

---

## 30. Project Definition

Spendly is fundamentally a **local-first personal finance system**, not just an expense CRUD app.

Its core domain model is:

```text
User
 │
 ├── Transactions
 │      ├── Income
 │      └── Expense
 │
 ├── Monthly Budgets
 │      └── Category Allocations
 │             └── Spending
 │
 └── Settings
```

The central business relationship is:

```text
Income + Opening Balance
          ↓
     Available Money
          ↓
   Category Allocations
          ↓
      Transactions
          ↓
    Amounts Spent
          ↓
   Remaining / Closing State
```

The next major milestone is to turn this domain model into a complete, user-friendly **Budget Management** experience while keeping the local database and transactional business logic consistent.

---

## 31. Development Notes

This document describes the project based on the current source tree. Some areas are intentionally marked as planned or in progress because their navigation, UI, or backend implementation is not yet complete.

The most important current development focus is:

> **Finish and harden Add Transaction, then build the complete Budget feature on top of the existing Room budget and allocation model.**
