package com.example.smartfit.screens

import android.service.autofill.UserData
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartfit.R
import com.example.smartfit.components.CustomButton
import com.example.smartfit.components.Heading1
import com.example.smartfit.components.Heading2
import com.example.smartfit.components.HeadlineText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mmk.kmpauth.firebase.apple.AppleButtonUiContainer
import com.mmk.kmpauth.firebase.github.GithubButtonUiContainer
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import com.mmk.kmpauth.google.GoogleButtonUiContainer
import com.mmk.kmpauth.google.GoogleUser
import com.mmk.kmpauth.uihelper.apple.AppleSignInButton
import com.mmk.kmpauth.uihelper.apple.AppleSignInButtonIconOnly
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import com.mmk.kmpauth.uihelper.google.GoogleSignInButtonIconOnly
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

//class LoginViewModel : ViewModel() {
//    val googleAuth =
//        GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = "1065171240992-se3tam5dm3d1iq47epb8kuv6pihdbo9l.apps.googleusercontent.com"))
//    //val googleAuth = GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = stringResource(R.string.default_web_client_id)))
//
//    fun logOut() {
//        viewModelScope.launch {
//            try {
//                googleAuth.signOut()
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                if (e is CancellationException) throw e
//            }
//        }
//    }
//}

data class FBUser(
    val id: String? = "",
    val displayName: String? = "",
    val profilePicUrl: String? = null,
)


@Composable
fun LoginScreen() {

//    var signedInUserName: String by remember { mutableStateOf("") }
//    var signedInUserFirebaseUser: FirebaseUser? = null
//    var signedInUser: FBUser? = null
    //val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
//    var fbUser: FirebaseUser? by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser) }
//    var user: Any?
//    var firebaseUser: dev.gitlive.firebase.auth.FirebaseUser? = null
//    var firebaseUser1: FirebaseUser? = null

    //var signedInUserName: String by remember { mutableStateOf("") }
    //var signedInUser: FBUser? by remember { mutableStateOf(null) }


    //Log.d("errorFB", "pred odhlasenim: ${firebaseAuth.currentUser}")
//    val onFirebaseResult: (Result<dev.gitlive.firebase.auth.FirebaseUser?>) -> Unit =
//        { result ->
//            if (result.isSuccess) {
//                val firebaseUser = result.getOrNull()
//                //signedInUserName = firebaseUser?.displayName ?: firebaseUser?.email ?: "Null User"
//                result.getOrNull().run {
//                    signedInUser = FBUser(
//                        result.getOrNull()?.uid,
//                        result.getOrNull()?.displayName,
//                        result.getOrNull()?.photoURL
//                    )
//                }
//            } else {
//                //signedInUserName = "Null User"
//                println("Error Result: ${result.exceptionOrNull()?.message}")
//            }
//        }

//    firebaseAuth.currentUser?.run {
//        signedInUser = FBUser(
//            id = uid,
//            displayName = displayName,
//            profilePicUrl = photoUrl?.toString()
//        )
//    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                HeadlineText(stringResource(id = R.string.app_name))

                Spacer(Modifier.padding(10.dp))

                Heading1("Prihlasenie")

                Spacer(Modifier.padding(30.dp))

                //Log.d("errorFB", " ${signedInUser?.profilePicUrl}")

//                Text(
//                    text = signedInUser?.displayName ?: "",
//                    style = MaterialTheme.typography.bodyLarge,
//                    textAlign = TextAlign.Start,
//                )

                //if (signedInUser == null && (firebaseAuth.currentUser == null)) {
                    //Log.d("errorFB", "po odhlaseni: ${firebaseAuth.currentUser}")

                    AuthUiHelperButtonsAndFirebaseAuth(
                        modifier = Modifier.fillMaxWidth(),
                        onFirebaseResult = { result ->
                            if (result.isSuccess) {
                                Log.d("errorFB", "success")
                            } else {
                                Log.d("errorFB", "error")
                            }
                        }
                    )
                //}
            }
        }
    }
}

//@Composable
//fun App() {
//
//    MaterialTheme {
//        Column(
//            Modifier.fillMaxSize().padding(20.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
//        ) {
//
//            var signedInUserName: String by remember { mutableStateOf("") }
//            val onFirebaseResult: (Result<dev.gitlive.firebase.auth.FirebaseUser?>) -> Unit = { result ->
//                if (result.isSuccess) {
//                    val firebaseUser = result.getOrNull()
//                    signedInUserName =
//                        firebaseUser?.displayName ?: firebaseUser?.email ?: "Null User"
//                } else {
//                    signedInUserName = "Null User"
//                    println("Error Result: ${result.exceptionOrNull()?.message}")
//                }
//
//            }
//            Text(
//                text = signedInUserName,
//                style = MaterialTheme.typography.bodyLarge,
//                textAlign = TextAlign.Start,
//            )
//
//            if (signedInUserName.isEmpty()) {
//                // ************************** UiHelper Text Buttons *************
//                //HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(16.dp))
//                AuthUiHelperButtonsAndFirebaseAuth(
//                    modifier = Modifier.width(IntrinsicSize.Max),
//                    onFirebaseResult = onFirebaseResult
//                )
//            }
//
//
//        }
//    }
//}

@Composable
fun AuthUiHelperButtonsAndFirebaseAuth(
    modifier: Modifier = Modifier,
    onFirebaseResult: (Result<dev.gitlive.firebase.auth.FirebaseUser?>) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {


        //Google Sign-In Button and authentication with Firebase
        GoogleButtonUiContainerFirebase(onResult = onFirebaseResult, linkAccount = false) {
            GoogleSignInButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                fontSize = 19.sp
            ) { this.onClick() }
        }

//        //Apple Sign-In Button and authentication with Firebase
//        AppleButtonUiContainer(onResult = onFirebaseResult, linkAccount = false) {
//            AppleSignInButton(modifier = Modifier.fillMaxWidth().height(44.dp)) { this.onClick() }
//        }

    }
}

@Composable
fun IconOnlyButtonsAndFirebaseAuth(
    modifier: Modifier = Modifier,
    onFirebaseResult: (Result<dev.gitlive.firebase.auth.FirebaseUser?>) -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {

        //Google Sign-In IconOnly Button and authentication with Firebase
        GoogleButtonUiContainerFirebase(onResult = onFirebaseResult, linkAccount = false) {
            GoogleSignInButtonIconOnly(onClick = { this.onClick() })
        }

        //Apple Sign-In IconOnly Button and authentication with Firebase
        AppleButtonUiContainer(onResult = onFirebaseResult, linkAccount = false) {
            AppleSignInButtonIconOnly(onClick = { this.onClick() })
        }
    }
}


@Preview
@Composable
fun PreviewLogin() {
    //LoginScreen()
}