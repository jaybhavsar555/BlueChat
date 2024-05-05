package com.example.bluechat.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxScopeInstance.align
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Switch
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun BluetoothOnOffScreen(
//    state: BluetoothUiState,
    onOnOffClick: (Boolean) -> Unit,
) {
    AppBarr(onOnOffClick)
}


@Composable
fun BluetoothSwitchButton(onOnOffClick: (Boolean) -> Unit) {
    val mCheckedState = remember { mutableStateOf(false) }

    Switch(
        checked = mCheckedState.value,
        onCheckedChange = {
            mCheckedState.value = it
//            if (it) {
////                val intent = Intent(this@BluetoothOnOffScreenActivity, ProfileScreen::class.java)
////                startActivity(intent)
//            } else {
//                null
//            }
            onOnOffClick(it)
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color(0xFF96B3ED),
            uncheckedThumbColor = Color(0xFF96B3ED),
            checkedTrackColor = Color(0xFFD9D9D9),
            uncheckedTrackColor = Color(0xFFFFFF)

        ),
//         thumbContent = {
//             Icon(
//                 imageVector =  Icons.Filled.Check,
//                 contentDescription = null,
//                 modifier = Modifier.size(SwitchDefaults.IconSize)
//             )
//         }


    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarr(onOnOffClick: (Boolean) -> Unit) {
    Scaffold(
        topBar = {

            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF96B3ED),
                    titleContentColor = Color(0xFF4D87F9),
                ),
                title = {
                    Text("BlueChat",
                        fontWeight = FontWeight.Bold
                        )
                }
            )
        }, content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                OnOffScreenBody(onOnOffClick)
            }
        }
    )
}

@Composable
fun OnOffScreenBody(onOnOffClick: (Boolean) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(), // Fills the entire screen
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(15.dp),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Text(
                    "Turn on bluethooth to chat with people near you ",
                    fontSize = 13.sp,
                )
                BluetoothSwitchButton(onOnOffClick)
            }
        }
    }
}

