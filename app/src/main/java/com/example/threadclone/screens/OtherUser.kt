package com.example.threadclone.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadclone.item_view.ThreadItem
import com.example.threadclone.navigation.Routes
import com.example.threadclone.utils.SharedPref
import com.example.threadclone.viewmodel.AuthViewModel
import com.example.threadclone.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun OtherUser(navHostController: NavHostController, uid: String) {

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)
    val context = LocalContext.current

    val userViewModel: UserViewModel = viewModel()
    val threads by userViewModel.threads.observeAsState()
    val users by userViewModel.users.observeAsState()
    val followerList by userViewModel.followerList.observeAsState()
    val followingList by userViewModel.followingList.observeAsState()


    userViewModel.fetchThread(uid)
    userViewModel.fetchUser(uid)
    userViewModel.getFollowers(uid)
    userViewModel.getFollowing(uid)


    var currentUserId = ""
    if (FirebaseAuth.getInstance().currentUser != null) currentUserId =
        FirebaseAuth.getInstance().currentUser!!.uid


    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null) {
            navHostController.navigate(Routes.Login.routes) {
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    LazyColumn {
        item {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                val (text, logo, userName, bio, follower, following, button) = createRefs()


                Text(text = users!!.name, style = TextStyle(
                    fontSize = 24.sp, fontWeight = FontWeight.ExtraBold
                ), modifier = Modifier.constrainAs(text) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                })

                Image(painter = rememberAsyncImagePainter(model = users!!.image),
                    contentDescription = "user image",
                    modifier = Modifier
                        .constrainAs(logo) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                        .size(130.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop)

                Text(text = users!!.userName, style = TextStyle(
                    fontSize = 20.sp
                ), modifier = Modifier.constrainAs(userName) {
                    top.linkTo(text.bottom)
                    start.linkTo(parent.start)
                })

                Text(text = users!!.bio, style = TextStyle(
                    fontSize = 20.sp
                ), modifier = Modifier.constrainAs(bio) {
                    top.linkTo(userName.bottom)
                    start.linkTo(parent.start)
                })

                Text(text = "${followerList?.size} Follower", style = TextStyle(
                    fontSize = 20.sp
                ), modifier = Modifier.constrainAs(follower) {
                    top.linkTo(bio.bottom)
                    start.linkTo(parent.start)
                })
                Text(text = "${followingList?.size} following", style = TextStyle(
                    fontSize = 20.sp
                ), modifier = Modifier.constrainAs(following) {
                    top.linkTo(follower.bottom)
                    start.linkTo(parent.start)
                })
                ElevatedButton(onClick = {
                    if (currentUserId != "") userViewModel.followUsers(uid, currentUserId)
                }, modifier = Modifier.constrainAs(button) {
                        top.linkTo(following.bottom)
                        start.linkTo((parent.start))
                    }

                ) {
                    Text(
                        text = if (followerList != null && followerList!!.isNotEmpty() && followerList!!.contains(
                                currentUserId
                            )
                        ) {
                            "following"
                        } else {
                            "follow"
                        }
                    )
                }

            }
        }
        if (threads != null && users != null) {
            items(threads ?: emptyList()) { pair ->
                ThreadItem(
                    thread = pair,
                    users = users!!,
                    navHostController = navHostController,
                    userId = SharedPref.getUserName(context)
                )
            }
        }

    }


}