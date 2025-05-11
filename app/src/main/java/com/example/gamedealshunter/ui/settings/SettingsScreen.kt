package com.example.gamedealshunter.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

import androidx.navigation.NavController



import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import kotlinx.coroutines.launch


import org.koin.androidx.compose.koinViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    vm: SettingsViewModel = koinViewModel()
) {

    val discount by vm.minDiscount.collectAsState()
    val interval  by vm.interval.collectAsState()
    var dInput by remember { mutableStateOf(discount.toString()) }
    var iInput by remember { mutableStateOf(interval.toString()) }
    val focus = LocalFocusManager.current
    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(

        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snack) }
    ) { inner ->

        Column(
            modifier = Modifier
                .padding(inner)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {


            OutlinedTextField(
                value = dInput,
                onValueChange = { dInput = it },
                label = { Text("Мин. скидка %") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = iInput,
                onValueChange = { iInput = it },
                label = { Text("Частота проверки (часы)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    focus.clearFocus()
                    val min = dInput.toIntOrNull() ?: return@Button
                    val hrs = iInput.toIntOrNull() ?: return@Button
                    vm.save(min, hrs)
                    scope.launch { snack.showSnackbar("Сохранено") }
                    navController.navigateUp()
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Сохранить") }
        }
    }
}