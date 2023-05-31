package com.example.empowerplant

import android.os.Bundle
import android.os.ProxyFileDescriptorCallback
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.*
import com.example.empowerplant.ui.theme.EmpowerPlantTheme
import com.example.empowerplant.utils.Action
import com.example.empowerplant.utils.EmpowerPlantController
import dalvik.system.BaseDexClassLoader
import io.sentry.SentryOptions
import io.sentry.android.core.SentryAndroid
import io.sentry.android.navigation.SentryNavigationListener
import io.sentry.protocol.SentryId


class MainActivity : ComponentActivity() {
    private var screen = "empowerplant_screen"
    private val sentryNavListener = SentryNavigationListener()
    private var navController: NavHostController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val openDialog = remember { mutableStateOf(false)  }
            initializeSentry(openDialog)
            navController = rememberNavController()
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BackButton(navController!!)

                userFeedback(openDialog)
                NavHost(navController = navController!!, startDestination = screen) {
                    composable(route = "list_app_screen") {
                        ListApp(navController = navController!!)
                    }
                    composable(route = "empowerplant_screen") {
                        EmpowerPlant(navController = navController!!)
                    }
                }
            }
        }
    }

    private fun initializeSentry(openDialog: MutableState<Boolean>) {
        SentryAndroid.init(this) { options ->
            options.beforeSend = SentryOptions.BeforeSendCallback {event, hint ->
                val currentException = event.exceptions?.get(0);
                if (currentException != null && currentException.type!!.endsWith("ItemDeliveryProcessException")) {
                    openDialog.value = true
                }
                event
            }
        }
    }

    private fun launchUserFeedback(eventID : SentryId){

    }

    @Composable
    fun userFeedback(openDialog : MutableState<Boolean>){
        if (openDialog.value) {

            AlertDialog(
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    openDialog.value = false
                },
                title = {
                    Text(text = "Oops, Checkout Failed!")
                },
                text = {
                    TextField(value = "", onValueChange = {})
                },
                confirmButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.addToCartBtn),
                            contentColor = Color.Black
                        ),
                        onClick = {
                            openDialog.value = false
                        }) {
                        Text("Submit")
                    }
                },
                dismissButton = {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.addToCartBtn),
                            contentColor = Color.Black
                        ),
                        onClick = {
                            openDialog.value = false
                        }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        navController?.addOnDestinationChangedListener(sentryNavListener)
    }

    override fun onPause() {
        super.onPause()
        navController?.removeOnDestinationChangedListener(sentryNavListener)
    }

    @Composable
    fun BackButton(navController: NavController?) {
        Log.d("screen", this.screen)
        var screenVal = if (this.screen == "list_app_screen") "Go to EmpowerPlant" else "Go to ListApp"
        val text by remember {
            mutableStateOf(screenVal)
        }
        Log.d("screen_val", screenVal)
        Button(
            onClick = {
                navBarOnClick(navController)
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("navButton"),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(text)
        }
    }

    private fun navBarOnClick(navController: NavController?) {
        this.screen = if (this.screen == "list_app_screen") "empowerplant_screen" else "list_app_screen"
        navController?.navigate(this.screen)
    }
}

