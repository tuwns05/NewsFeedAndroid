package com.example.newsfeed.ui.screen.authen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newsfeed.data.repository.AuthRepository
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onSuccess: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val repo = remember { AuthRepository() }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegister by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                scope.launch {
                    val success =
                        if (isRegister)
                            repo.register(email, password)
                        else
                            repo.login(email, password)

                    if (success) {
                        onSuccess()
                    }
                }
            }
        ) {
            Text(
                if (isRegister)
                    "Đăng ký"
                else
                    "Đăng nhập"
            )
        }

        TextButton(
            onClick = {
                isRegister = !isRegister
            }
        ) {
            Text(
                if (isRegister)
                    "Đã có tài khoản?"
                else
                    "Chưa có tài khoản?"
            )
        }
    }
}