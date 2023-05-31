package com.example.empowerplant

sealed class Screen(val route: String) {
    object ListApp : Screen("list_app_screen")
    object EmpowerPlant : Screen("empowerplant_screen")
}