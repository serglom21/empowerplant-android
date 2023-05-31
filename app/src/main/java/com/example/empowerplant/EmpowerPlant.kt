package com.example.empowerplant

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.empowerplant.ui.theme.EmpowerPlantTheme
import com.example.empowerplant.utils.EmpowerPlantController
import org.json.JSONArray
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.empowerplant.utils.CartController


var cartItems = JSONArray()
val epc = EmpowerPlantController()
val cartController = CartController()

@Composable
fun EmpowerPlant(
    navController: NavController
) {
    EmpowerPlantTheme() {
        val offset = remember { mutableStateOf(0f) }
        Column(modifier = Modifier.scrollable(
            orientation = Orientation.Vertical,
            state = rememberScrollableState { delta ->
                offset.value = offset.value + delta // update the state
                delta // indicate that we consumed all the pixels available
            }
        )) {
            val (count, updateCount) = remember {
                mutableStateOf(
                    0
                )
            }
            Row(
                modifier = Modifier
                    .background(colorResource(id = R.color.navBar))
                    .fillMaxWidth()
                    .padding(
                        vertical = 15.dp
                    )
            ) {
                Column() {
                    Text(
                        text = "Empower Plant",
                        color = Color.White,
                        modifier = Modifier.padding(start = 15.dp),
                        fontSize = 18.sp
                    )
                }

                Column(
                    modifier = Modifier.width(250.dp).height(40.dp).fillMaxWidth().padding(start = 150.dp),
                    horizontalAlignment = Alignment.End
                ) {

                    BottomNavigation {
                        BottomNavigationItem(
                            selected = false,
                            onClick = { cartController.checkout() },
                            modifier = Modifier.background(color = colorResource(id = R.color.navBar)),
                            icon = {
                                BadgedBox(
                                    badge = { Badge { Text(count.toString()) }},
                                ) {
                                    Icon(
                                        Icons.Filled.ShoppingCart,
                                        contentDescription = "Favorite"
                                    )
                                }
                            },
                        )
                    }
                }
            }
            buildItems(updateCount, count)

            /*
            Row() {
                CircularProgressIndicator(
                    modifier = Modifier.drawBehind {
                        drawCircle(
                            Color.Blue,
                            radius = size.width / 2 - 5.dp.toPx() / 2,
                            style = Stroke(5.dp.toPx())
                        ) },
                    color = Color.LightGray,
                    strokeWidth = 5.dp
                )
            }*/
            //buildItems(products)
        }

    }

    /*LaunchedEffect(true) {
        products = epc.fetchProducts()
    }*/

    //products = JSONArray(EmpowerPlantController().fetchProducts())


}

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    object Error : Result<Nothing>()
    class Success<T>(t: T?) : Result<T>()
}

@Composable
fun loadProducts(): State<Result<JSONArray>> {
   return produceState<Result<JSONArray>>(initialValue = Result.Loading){
       val epc = EmpowerPlantController()
       epc.fetchProducts()

       value = if (epc.getProducts() == null) {
           Result.Error
       } else {
           Result.Success(epc.getProducts())
       }
   }
}

@Composable
fun buildItems(updateCount: (Int) -> Unit, count: Int){
    var products = epc.fetchProducts()
    if (products != null) {
        for (i in 0 until products.length()) {
            Row(
                modifier = Modifier
                    .drawBehind {

                        val strokeWidth = Stroke.DefaultMiter
                        val y = size.height - strokeWidth / 2

                        drawLine(
                            Color.LightGray,
                            Offset(0f, y),
                            Offset(size.width, y),
                            strokeWidth
                        )
                    }){
                val imageModifier = Modifier
                    .padding(start = 20.dp, top = 20.dp, bottom = 10.dp)
                    .width(90.dp)
                val product = products.getJSONObject(i)
                AsyncImage(
                    model = product.get("imgcropped").toString(),
                    contentDescription = stringResource(id = R.string.navbar_description),
                    modifier = imageModifier
                )
                Column(modifier = Modifier.padding(start = 20.dp, top = 10.dp)) {
                    Text(product.get("title").toString(), color = colorResource(id = R.color.purple_500), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("SKU: " + product.get("id").toString())
                    Text("PRICE: " + product.get("price").toString())
                }
                Row(horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = { addItemToCart(products[i], count, updateCount) },
                        modifier = Modifier.padding(top = 50.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = colorResource(id = R.color.addToCartBtn)
                        )
                    ) {
                        Text(text = "Add to cart")
                    }
                }
            }
        }
    }
}

fun addItemToCart(item : Any, count: Int, updateCount : (Int) -> Unit) {
    updateCount(count + 1)
    cartController.addItem(item)
}