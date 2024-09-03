package com.example.batteryalarm

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.batteryalarm.ui.theme.BatteryAlarmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BatteryAlarmTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BatteryStatusScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

    @Composable
    fun BatteryStatusScreen(modifier: Modifier) {
        val context = LocalContext.current
        var batteryPercentage by remember { mutableIntStateOf(0) }
        var batteryImage by remember { mutableIntStateOf(R.drawable.battery_full) }

        val batteryStatusReceiver = remember {
            BatteryStatusReceiver { batteryPct ->
                batteryPercentage = batteryPct
                batteryImage = if (batteryPct <= 20) {
                    R.drawable.battery_low
                } else {
                    R.drawable.battery_full
                }
            }
        }

        DisposableEffect(context) {
            val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            context.registerReceiver(batteryStatusReceiver, filter)
            onDispose {
                context.unregisterReceiver(batteryStatusReceiver)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = batteryImage),
                contentDescription = "Battery Status",
                modifier = Modifier.size(128.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${batteryPercentage}%",
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp)
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun BatteryStatusScreenPreview() {
        BatteryStatusScreen(modifier = Modifier)
    }