package com.google.samples.apps.sunflower

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class OKRPeriod(val title: String) {
    WEEK("周 OKR"),
    MONTH("月 OKR"),
    YEAR("年 OKR")
}

data class KR(val title: String, var done: Boolean = false)
data class Objective(val title: String, val krs: MutableList<KR>)

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
    var currentPeriod by remember { mutableStateOf(OKRPeriod.WEEK) }

    val weekOKR = remember {
        Objective(
            "本周家庭目标",
            mutableListOf(
                KR("全家运动 3 次"),
                KR("孩子英语打卡 5 天"),
                KR("家庭会议一次")
            )
        )
    }

    val monthOKR = remember {
        Objective(
            "本月家庭目标",
            mutableListOf(
                KR("完成一次短途旅行"),
                KR("整理家庭照片"),
                KR("家庭读书计划")
            )
        )
    }

    val yearOKR = remember {
        Objective(
            "年度家庭目标",
            mutableListOf(
                KR("家庭年度旅行"),
                KR("孩子英语提升计划"),
                KR("家庭理财规划")
            )
        )
    }

    val currentOKR = when (currentPeriod) {
        OKRPeriod.WEEK -> weekOKR
        OKRPeriod.MONTH -> monthOKR
        OKRPeriod.YEAR -> yearOKR
    }

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

            PeriodSelector(currentPeriod) {
                currentPeriod = it
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("🎯 ${currentOKR.title}", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(8.dp))

            currentOKR.krs.forEach { kr ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(kr.title)
                    Checkbox(
                        checked = kr.done,
                        onCheckedChange = { kr.done = it }
                    )
                }
            }
        }
    }
}

@Composable
fun PeriodSelector(current: OKRPeriod, onSelect: (OKRPeriod) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OKRPeriod.values().forEach { period ->
            FilterChip(
                selected = current == period,
                onClick = { onSelect(period) },
                label = { Text(period.title) }
            )
        }
    }
}
