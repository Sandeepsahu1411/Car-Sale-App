package com.example.salecar.presentation_layer.screens.common_component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String = "",
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    isEditable: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: (() -> Unit)? = null,
    onlyNumbers: Boolean = false,
    maxLines: Int = 1

) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordVisibility by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (!onlyNumbers || it.all { char -> char.isDigit() }) {
                onValueChange(it)
            }
        },
        singleLine = true,
        placeholder = {
            Text(
                text = placeholderText,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )

        },


        modifier = modifier
            .fillMaxWidth()
            .height(55.dp),
        maxLines = maxLines,
        readOnly = !isEditable,
        textStyle = TextStyle(fontSize = 18.sp),
        visualTransformation = if (isPassword && !passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = trailingIcon ?: if (isPassword) {
            {
                val icon =
                    if (passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (passwordVisibility) "Hide password" else "Show password"
                    )
                }
            }
        } else null,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.secondary,
            unfocusedTextColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.primary,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary,


            ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                onImeAction?.invoke()
            },
            onGo = { onImeAction?.invoke() }

        ),
        leadingIcon = leadingIcon
    )
}