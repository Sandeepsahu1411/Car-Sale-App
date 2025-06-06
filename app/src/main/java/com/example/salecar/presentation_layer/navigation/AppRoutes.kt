package com.example.salecar.presentation_layer.navigation

import kotlinx.serialization.Serializable

sealed class SubNavigation {

    @Serializable
    object MainHomeScreen : SubNavigation()

    @Serializable
    object StartupScreen : SubNavigation()
}

sealed class Routes {

    @Serializable
    object SplashScreenRoute : Routes()

    @Serializable
    object LoginScreeRoute : Routes()

    @Serializable
    object SignUpScreenRoute : Routes()

    @Serializable
    object HomeScreenRoute : Routes()

    @Serializable
    object MassageScreenRoute : Routes()

    @Serializable
    object WishListScreenRoute : Routes()

    @Serializable
    object ProfileScreenRoute : Routes()

    @Serializable
    object NotificationScreenRoute : Routes()

    @Serializable
    data class AddScreenRoute(
        val id: String? = null
    ) : Routes()
//    object AddScreenRoute : Routes()

    @Serializable
    object StartScreenRoute : Routes()

    @Serializable
    object SearchScreenRoute : Routes()

    @Serializable
    data class ProductDetailScreenRoute(
        val id: String
    ) : Routes()

    @Serializable
    object SettingScreenRoute : Routes()


}