package com.example.salecar.presentation_layer.screens.common_component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

data class BottomNavigationItem(
    val title: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector

)

class BottomAppBarCutoutShape(
    private val fabDiameter: Float, private val fabMargin: Dp
) : Shape {
    override fun createOutline(
        size: Size, layoutDirection: LayoutDirection, density: Density
    ): Outline {
        val fabMarginPx = with(density) { fabMargin.toPx() }
        val notchRadius = fabDiameter / 2 + fabMarginPx
        val centerX = size.width / 2
        val notchDepth = fabDiameter / 2 + fabMarginPx * 0.6f

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(centerX - notchRadius, 0f)

            cubicTo(
                centerX - notchRadius * 0.66f,
                0f,
                centerX - notchRadius * 0.66f,
                notchDepth,
                centerX,
                notchDepth
            )
            cubicTo(
                centerX + notchRadius * 0.66f,
                notchDepth,
                centerX + notchRadius * 0.66f,
                0f,
                centerX + notchRadius,
                0f
            )

            lineTo(size.width, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()

        }

        return Outline.Generic(path)

    }
}

@Composable
fun CustomBottomBar(
    modifier: Modifier = Modifier, selectedItemIndex: Int, onItemSelected: (Int) -> Unit
) {
    val fabDiameterPx = with(LocalDensity.current) { 56.dp.toPx() }
    val cutoutShape = BottomAppBarCutoutShape(fabDiameter = fabDiameterPx, fabMargin = 26.dp)
    val isGestureNavigation = isGestureNavigationEnabled()

    val items = listOf(
        BottomNavigationItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavigationItem("WishList", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
        BottomNavigationItem("Massage", Icons.Filled.Message, Icons.Outlined.Message),
        BottomNavigationItem("Profile", Icons.Filled.Person, Icons.Outlined.Person)
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
//            .navigationBarsPadding()
            .then(if (!isGestureNavigation) Modifier.navigationBarsPadding() else Modifier)
            .height(64.dp)
            .clip(RoundedCornerShape(10.dp)),
        shape = cutoutShape,
        shadowElevation = 6.dp,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically

        ) {
            items.forEachIndexed { index, item ->
                if (index == 2) {
                    Spacer(modifier = Modifier.width(50.dp))
                }
                val icon =
                    if (selectedItemIndex == index) item.selectedIcon else item.unselectedIcon
                val color =
                    if (selectedItemIndex == index) MaterialTheme.colorScheme.primary else Color.Gray

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(imageVector = icon,
                        contentDescription = item.title,
                        tint = color,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable { onItemSelected(index) })
//                    if (selectedItemIndex == index)
                    AnimatedVisibility(visible = selectedItemIndex == index) {

                        Text(
                            text = item.title,
                            color = color,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun isGestureNavigationEnabled(): Boolean {
    val context = LocalContext.current
    val resources = context.resources
    val resourceId = resources.getIdentifier("config_navBarInteractionMode", "integer", "android")

    // 2 => Gesture Navigation, 1 => 2-Button Nav, 0 => 3-Button Nav
    return if (resourceId > 0) {
        resources.getInteger(resourceId) == 2
    } else {
        false
    }
}
