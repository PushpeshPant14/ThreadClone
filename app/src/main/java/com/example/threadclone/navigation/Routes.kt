package com.example.threadclone.navigation

sealed class Routes(val routes:String) {


    object Home: Routes("home")
    object Notification: Routes("notification")
    object Profile: Routes("profile")
    object Search: Routes("search")
    object AddThread: Routes("addThread")
    object Splash: Routes("splash")
    object BottomNav: Routes("bottomNav")
    object Login: Routes("login")
    object Register: Routes("register")
    object OtherUser: Routes("other_users/{data}")
}