package com.example.threadclone.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.threadclone.R
import com.example.threadclone.navigation.Routes
import com.example.threadclone.utils.SharedPref
import com.example.threadclone.viewmodel.AddThreadViewModel
import com.example.threadclone.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AddThread(navHostController: NavHostController) {

    val threadModel : AddThreadViewModel = viewModel()
    val isPosted by threadModel.isPosted.observeAsState(false)

    val context = LocalContext.current

    var thread: String by remember {
        mutableStateOf("")
    }

    //image uri
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val permissionToRequest = if (Build.VERSION.SDK_INT<= Build.VERSION_CODES.UPSIDE_DOWN_CAKE){
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    //to launch content
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            uri : Uri? ->
        imageUri = uri
    }


    //to request permission
    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission() ) {
                isGranted : Boolean ->
            if (isGranted){
                Log.d("Image","Granted")
            }
            else{
                Log.d("Image","Not Granted")
            }
        }

    LaunchedEffect(isPosted) {
        if (isPosted){
            thread = ""
            imageUri = null
            Toast.makeText(context,"Thread Posted",Toast.LENGTH_SHORT).show()
            navHostController.navigate(Routes.Home.routes){
                popUpTo(Routes.AddThread.routes){
                    inclusive = true
                }
            }

        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val (crossPic, text, logo, userName, editText, attachMedia, imageBox) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.baseline_close_24),
            contentDescription = "cross",
            modifier = Modifier
                .constrainAs(crossPic) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .clickable {
                    navHostController.navigate(Routes.Home.routes){
                        popUpTo(Routes.AddThread.routes){
                            inclusive = true
                        }
                    }
                }
        )

        Text(text = "Add thread", style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        ),
            modifier = Modifier
                .constrainAs(text) {
                    top.linkTo(crossPic.top)
                    start.linkTo(crossPic.end, margin = 12.dp)
                    bottom.linkTo(crossPic.bottom)
                }
        )

        Image(
            painter = rememberAsyncImagePainter(model = SharedPref.getImageUrl(context)),
            contentDescription = "user image",
            modifier = Modifier
                .constrainAs(logo) {
                    top.linkTo(text.bottom)
                    start.linkTo(parent.start)
                }
                .size(30.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Text(
            text = SharedPref.getUserName(context),
            style = TextStyle(
                fontSize = 20.sp
            ),
            modifier = Modifier
                .constrainAs(userName) {
                    top.linkTo(logo.top)
                    start.linkTo(logo.end, margin = 12.dp)
                    bottom.linkTo(logo.bottom)
                }
        )

        BasicTextFieldWithHint(
            hint = "Start a Thread...",
            value = thread,
            onValueChange = { thread = it },
            modifier = Modifier
                .constrainAs(editText) {
                    top.linkTo(userName.bottom)
                    start.linkTo(userName.start)
                    end.linkTo(parent.end)
                }
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
        )

        if (imageUri == null){
            Image(
                painter = painterResource(id = R.drawable.baseline_attachment_24),
                contentDescription = "attach",
                modifier = Modifier
                    .constrainAs(attachMedia) {
                        top.linkTo(editText.bottom)
                        start.linkTo(editText.start)
                    }
                    .clickable {
                        val isGranted = ContextCompat.checkSelfPermission(
                            context, permissionToRequest
                        ) == PackageManager.PERMISSION_GRANTED
                        if (isGranted) {
                            launcher.launch("image/*")
                        } else {
                            permissionLauncher.launch(permissionToRequest)
                        }
                    }
            )
        }
        else{
            Box(modifier = Modifier
                .background(Color.Transparent)
                .padding(20.dp)
                .constrainAs(imageBox) {
                    top.linkTo(editText.bottom)
                    start.linkTo(editText.start)
                    end.linkTo(editText.end)
                }
                .height(250.dp)
            ){
                Image(
                    painter = rememberAsyncImagePainter(model = imageUri),
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    contentScale = ContentScale.Crop
                )
                Icon(imageVector = Icons.Default.Close,
                    contentDescription = "Remove Image",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickable {
                            imageUri = null
                        }
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.padding(bottom = 12.dp)
            ){
                Text(
                    text = "Anyone can reply",
                    style = TextStyle(
                        fontSize = 20.sp
                    )
                )
            }
            TextButton(
                onClick = {
                    if (imageUri==null){
                        threadModel.saveData(thread, FirebaseAuth.getInstance().currentUser!!.uid,"")
                    }
                    else{
                        threadModel.saveImage(thread, FirebaseAuth.getInstance().currentUser!!.uid, imageUri!!)
                    }
                },
                modifier = Modifier.clickable {

                }
            ) {
                Text(
                    text = "Post",
                    style = TextStyle(
                        fontSize = 20.sp
                    )
                )
            }
        }
    }

}

@Composable
fun BasicTextFieldWithHint(
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier
) {

    Box(modifier = modifier) {
        if (value.isEmpty()) {
            Text(text = hint, color = Color.Gray)
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle.Default.copy(color = Color.Black),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
//    AddThread()
}