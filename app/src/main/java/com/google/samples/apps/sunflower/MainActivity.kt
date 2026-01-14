package com.google.samples.apps.sunflower

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                OKRApp()
            }
        }
    }
}

@Composable
fun OKRApp() {
    var selectedTab by remember { mutableStateOf(0) }

    val tabs = listOf("周 OKR", "月 OKR", "年 OKR")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("家庭 OKR") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (selectedTab) {
                0 -> OKRPage("本周家庭目标", listOf("运动 3 次", "英语打卡 5 天", "家庭会议一次"))
                1 -> OKRPage("本月家庭目标", listOf("短途旅行", "家庭整理日", "家庭读书计划"))
                2 -> OKRPage("年度家庭目标", listOf("年度旅行", "英语提升计划", "家庭理财规划"))
            }
        }
    }
}

@Composable
fun OKRPage(title: String, krs: List<String>) {
    Column {
        Text("🎯 $title", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        krs.forEach { kr ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(kr)
                Checkbox(checked = false, onCheckedChange = {})
            }
        }
    }
}
