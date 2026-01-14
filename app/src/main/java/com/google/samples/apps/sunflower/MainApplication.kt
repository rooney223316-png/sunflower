package com.google.samples.apps.sunflower

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class KR(val title: String, var done: Boolean = false)
data class O(val title: String, val krs: MutableList<KR>)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                OKRScreen()
            }
        }
    }
}

@Composable
fun OKRScreen() {
    var objectives by remember {
        mutableStateOf(
            mutableListOf(
                O(
                    "2026 家庭年度目标",
                    mutableListOf(
                        KR("全家每周运动 3 次"),
                        KR("孩子英语每日打卡"),
                        KR("家庭年度旅行一次")
                    )
                )
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("家庭 OKR") })
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            items(objectives) { obj ->
                Text(text = "🎯 ${obj.title}", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))

                obj.krs.forEach { kr ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(kr.title)
                        Checkbox(
                            checked = kr.done,
                            onCheckedChange = { kr.done = it }
                        )
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp))
            }
        }
    }
}
