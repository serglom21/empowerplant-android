package com.example.empowerplant

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.empowerplant.ui.theme.EmpowerPlantTheme
import com.example.empowerplant.utils.Action

@Composable
fun ListApp(
    navController: NavController
) {
    EmpowerPlantTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavBarImage()
            heading(txt = "Unhandled Error + Attachment")
            actionBtn(
                txt = "ArithmeticException",
                action = { Action.arithmeticException() })
            actionBtn(txt = "RTE and Strip PII", action = { Action.stripPII() })
            heading(txt = "Handled Error + Attachment")
            actionBtn(
                txt = "ArrayIndexOutOfBoundsException",
                action = { Action.handledError() })
            heading(txt = "ANR")
            actionBtn(
                txt = "Application Not Responding",
                action = { Action.anrException() })
            heading(txt = "NDK/C++")
            actionBtn(txt = "Native Crash - SIGSEGV", action = { Action.nativeCrash() })
            actionBtn(txt = "Native Message", action = { Action.nativeMessage() })
        }
    }
}

@Composable
fun actionBtn(txt: String, action: () -> Unit) {
    Button(onClick = action,
        modifier = Modifier
            .width(300.dp)
            .testTag(txt.replace(" ", "") + "Btn"),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorResource(id = R.color.btnpurple),
            contentColor = Color.White
        )
    ) {
        Text(text = txt)
    }
}

@Composable
fun NavBarImage() {
    val imageModifier = Modifier
        .fillMaxWidth()
        .height(160.dp)
    Image(
        painter = painterResource(id = R.drawable.navigation_bar),
        contentDescription = stringResource(id = R.string.navbar_description),
        modifier = imageModifier
    )
}

@Composable
fun heading(txt : String) {
    Text(
        txt,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(
            top = 20.dp
        )
    )
}

