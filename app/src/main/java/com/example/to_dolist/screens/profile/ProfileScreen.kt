package com.example.to_dolist.screens.profile

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.to_dolist.R
import com.example.to_dolist.auth.login.AuthViewModel
import com.example.to_dolist.navigation.NavigationRoute
import com.example.to_dolist.screens.bottombar.BottomBar
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: ProfileViewModel,
    authViewModel: AuthViewModel,
    savedInstanceState: Bundle? = null
    ) {

    val backgroundImage = ImageBitmap.imageResource(R.drawable.laptop_mac_computer_browser)

    val user = FirebaseAuth.getInstance().currentUser

    val userName = user?.displayName ?: "User Name" // Display name (may be null)
    val userEmail = user?.email ?: "Email not available" // User's email (may be null)

    val profileImageUri by viewModel.profileImageUri.collectAsState()


    DisposableEffect(key1 = Unit) {
        onDispose {
//            savedInstanceState?.putString("profileImageUri", profileImageUri?.toString())
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFAF0)) // Background color

    ) {
        // Top box with background image and profile image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        ) {
            // Draw curved background with image
            Canvas(modifier = Modifier.fillMaxWidth().height(400.dp)) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Define the curved path for clipping
                val path = Path().apply {
                    moveTo(0f, 0f)
                    lineTo(0f, canvasHeight * 0.9f)
                    quadraticBezierTo(
                        canvasWidth / 2, // Control point (center of canvas)
                        canvasHeight, // Peak of the curve
                        canvasWidth, // End at right side
                        canvasHeight * 0.9f
                    )
                    lineTo(canvasWidth, 0f)
                    close()
                }

                // Get image dimensions
                val imageWidth = backgroundImage.width.toFloat()
                val imageHeight = backgroundImage.height.toFloat()

                // Calculate scaling factors based on contentScale (ContentScale.Crop)
                val scaleX = canvasWidth / imageWidth
                val scaleY = canvasHeight / imageHeight

                // Choose the larger scale factor to fill the canvas and crop the overflow
                val scale = maxOf(scaleX, scaleY)

                // Offset to center the image horizontally
                val offsetX = (canvasWidth - imageWidth) / 2
                val offsetY = 56f

                // Clip and draw the image
                clipPath(path) {
                    scale(scale, scale) {
                        drawImage(
                            image = backgroundImage,
                            topLeft = Offset(offsetX, offsetY),
                        )
                    }
                }
            }
        }
        // Profile image at the top, centered
        ProfileImage(
            profileImageUri = profileImageUri,
            onEditClick = { uri ->
                viewModel.updateProfileImage(uri)// Update profile image in ViewModel

            },

        )
        // Spacer for offset after the profile image
        Spacer(modifier = Modifier.height(40.dp))

        // User Info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = userName,
                fontSize = 24.sp,
                color = Color.Black,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = userEmail,
                fontSize = 14.sp,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(70.dp))

        Button(
            onClick = {
                // Clear user session and navigate to the Welcome Screen
                authViewModel.logoutUser()

                // Navigate to Welcome Screen and clear ProfileScreen from the back stack
                navController.navigate(NavigationRoute.WelcomeScreen.route) {
                    popUpTo(NavigationRoute.ProfileScreen.route) { inclusive = true }
                    launchSingleTop = true
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Log Out")
        }

        Spacer(modifier = Modifier.height(210.dp))

        BottomBar(
            navController = navController
        )
    }
}

@Composable
fun ProfileImage(profileImageUri: Uri?, onEditClick: (Uri?) -> Unit, ) {
    var hasPermission by remember { mutableStateOf(false) }
    var showPermissionDeniedMessage by remember { mutableStateOf(false) }

    // Photo picker launcher for selecting only one image
    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        // Callback is invoked after the user selects a media item or closes the picker
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            onEditClick(uri) // Pass the selected URI back
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    // Permission launcher for requesting storage permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (!isGranted) {
            showPermissionDeniedMessage = true
            Log.d("Permission", "Permission denied for reading external storage")
        } else {
            Log.d("Permission", "Permission granted for reading external storage")
        }
    }

    val placeholderImage =
        painterResource(id = R.drawable.profilephoto) // Default placeholder image

    Box(
        modifier = Modifier
            .padding(start = 140.dp)
            .size(100.dp)
            .background(Color.LightGray, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = if (profileImageUri != null) {
                rememberAsyncImagePainter(model = profileImageUri)
            } else {
                placeholderImage
            },
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color.White, shape = CircleShape),
            contentScale = ContentScale.Crop
        )



        // Edit button overlay
        IconButton(
            onClick = {
                // Check Android version and handle permissions accordingly
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // For Android 13 and above, request READ_MEDIA_IMAGES
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    // For older Android versions, request READ_EXTERNAL_STORAGE
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }

                // If permission is granted, launch the photo picker with single image selection
                if (hasPermission) {
                    val pickVisualMediaRequest =
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    pickMediaLauncher.launch(pickVisualMediaRequest)
                }
            },
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.BottomEnd)
                .background(Color.White, shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icons8_edit), // Replace with your edit icon
                contentDescription = "Edit Profile",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }

    // Show permission denied message (optional)
    if (showPermissionDeniedMessage) {
        Snackbar {
            Text(text = "Permission denied! Cannot access photos.")
        }
        showPermissionDeniedMessage = false // Reset the flag to avoid showing
    }

    Spacer(modifier = Modifier.height(16.dp))  // Space before logout button


}