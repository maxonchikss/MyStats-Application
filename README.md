MyFit Application
A native Android fitness companion built with modern architecture patterns and local-first data management. The app helps users track workouts, build custom training programs, and maintain consistency through visual progress indicators.

Core Features

-Personalized Training Programs – Create, customize, and schedule workouts with a dynamic exercise builder supporting multiple sets and reps configuration
-Workout Calendar & Streaks – Visual week view with color-coded activity markers and automatic session tracking to maintain motivation
-Smart Exercise Library – Categorized routines (Full Body, Split, Functional) with expandable cards and detailed movement breakdowns
-Rest Timer – Configurable interval timer with pause, skip, and extension controls for precise rest period management
-Adaptive UI – Seamless light/dark theme switching following system preferences with Material Design components
Technical Implementation

Built using Java with MVVM architecture, leveraging Room for persistent storage and LiveData for reactive UI updates. The app employs RecyclerView with DiffUtil for efficient list rendering, SharedPreferences for settings management, and Fragment-based navigation with BottomNavigationView. All data persists locally ensuring full offline functionality with API 26+ compatibility.
