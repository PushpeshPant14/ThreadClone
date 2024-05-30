package com.example.threadclone.navigation

import android.os.Build
import android.window.SplashScreen
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.threadclone.screens.AddThread
import com.example.threadclone.screens.BottomNav
import com.example.threadclone.screens.Home
import com.example.threadclone.screens.Login
import com.example.threadclone.screens.Notification
import com.example.threadclone.screens.OtherUser
import com.example.threadclone.screens.Profile
import com.example.threadclone.screens.Register
import com.example.threadclone.screens.Search
import com.example.threadclone.screens.Splash

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = Routes.Splash.routes
    ) {
        composable(Routes.Splash.routes) {
            Splash(navController)
        }
        composable(Routes.Profile.routes) {
            Profile(navController)
        }
        composable(Routes.Notification.routes) {
            Notification()
        }
        composable(Routes.Search.routes) {
            Search(navController)
        }
        composable(Routes.Home.routes) {
            Home(navController)
        }
        composable(Routes.AddThread.routes) {
            AddThread(navController)
        }
        composable(Routes.BottomNav.routes){
            BottomNav(navController)
        }
        composable(Routes.Login.routes) {
            Login(navController)
        }
        composable(Routes.Register.routes){
            Register(navController)
        }
        composable(Routes.OtherUser.routes){
            val data = it.arguments!!.getString("data")
            OtherUser(navController,data!!)
        }

    }
}
