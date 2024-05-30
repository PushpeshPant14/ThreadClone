package com.example.threadclone.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import com.example.threadclone.R
import com.example.threadclone.navigation.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay


@Composable
fun Splash(navController: NavHostController) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
            .padding(horizontal = 5.dp)
    ) {
        val image = createRef()

        Image(
            painter = painterResource(id = R.drawable.threadlogo),
            contentDescription = "logo",
            modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(300.dp)
        )

    }



    LaunchedEffect(true) {
        delay(3000L)
        if(FirebaseAuth.getInstance().currentUser!=null)
        navController.navigate(Routes.BottomNav.routes){
            popUpTo(navController.graph.startDestinationId)  //backstack
            launchSingleTop=true
        }
        else
            navController.navigate(Routes.Login.routes){
                popUpTo(navController.graph.startDestinationId)  //backstack
                launchSingleTop=true
            }
    }

}