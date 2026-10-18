**MyStats**

MyStats is an Android application designed for planning, scheduling, and tracking workout routines. It provides a weekly scheduler, a custom workout builder with exercise management, and an interval timer for rest periods.

**Tech Stack**

Java, Android SDK (API 21-34), Room Database (SQLite), MVVM Architecture, LiveData, ViewModel, Material Design Components, SharedPreferences, RecyclerView with DiffUtil, Background Executors.

**Implemented Features**

The application includes user authentication with local session persistence, a 7-day visual workout scheduler, and full CRUD operations for custom workouts and exercises (tracking sets and reps). It features a library of pre-defined workout templates, duplicate prevention when scheduling, and a customizable rest timer with pause/resume functionality and system notifications. The UI supports dark mode, expandable list items for exercise details, and bottom navigation.

<img width="380" height="840" alt="image" src="https://github.com/user-attachments/assets/c5e7c734-8896-4e28-9600-0112a173e659" />
<img width="360" height="840" alt="image" src="https://github.com/user-attachments/assets/122fecba-b8f5-4175-9f19-c48594b4ed6e" />

<img width="380" height="840" alt="image" src="https://github.com/user-attachments/assets/5667c83e-d58f-4d31-8510-b60ae3f08b0f" />
<img width="360" height="840" alt="image" src="https://github.com/user-attachments/assets/b939cccb-6d75-4821-b1bd-67ed4c77cf63" />

<img width="380" height="840" alt="image" src="https://github.com/user-attachments/assets/ab479d50-0331-4305-a7c0-6ced74ba2453" />
<img width="360" height="840" alt="image" src="https://github.com/user-attachments/assets/bfea3ace-a77f-4859-8022-cb3775764839" />

<img width="360" height="860" alt="image" src="https://github.com/user-attachments/assets/4d18e261-5655-498c-985e-da5af0fe0aa2" />
<img width="360" height="840" alt="image" src="https://github.com/user-attachments/assets/95db8487-ec50-4abb-919e-4e0aefd375a9" />

**Architecture**

The project follows the MVVM (Model-View-ViewModel) architectural pattern combined with the Repository pattern. The data layer is built on Room, utilizing relational entities and Data Access Objects (DAOs) for type-safe database queries. The Repository layer abstracts data sources, providing a single source of truth for the ViewModels. UI components observe LiveData streams, ensuring lifecycle-aware and reactive updates without direct database coupling. Background database operations are handled via dedicated executors to maintain UI thread responsiveness.

**Project Structure**

The codebase is organized by responsibility: model/ (Room Entities), data/dao/ (Data Access Objects), data/database/ (AppDatabase singleton), data/repository/ (Data abstraction layer), viewmodel/ (Business logic and UI state), ui/ (Activities and Fragments), adapter/ (RecyclerView adapters), and utils/ (SharedPreferences wrapper, constants, and notification helpers).
**Build & Run**
Clone the repository and open it in Android Studio. Sync Gradle dependencies and run the application on an emulator or physical device (API 21+).
