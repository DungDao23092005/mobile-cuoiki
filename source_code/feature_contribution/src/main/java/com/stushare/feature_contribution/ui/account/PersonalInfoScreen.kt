package com.stushare.feature_contribution.ui.account

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.stushare.feature_contribution.R
import com.stushare.feature_contribution.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("Dũng Đào") }
    var dob by remember { mutableStateOf("01/01/2000") }
    var gender by remember { mutableStateOf("Nam") }

    val backgroundColor = MaterialTheme.colorScheme.background
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.p_info_title), color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenPrimary)
            )
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Surface(modifier = Modifier.size(80.dp), shape = CircleShape, color = MaterialTheme.colorScheme.surfaceVariant) {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.padding(16.dp), tint = onSurfaceColor.copy(alpha = 0.5f))
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = name, onValueChange = { name = it },
                        label = { Text(stringResource(R.string.p_info_name_hint)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = onSurfaceColor, unfocusedTextColor = onSurfaceColor, focusedLabelColor = GreenPrimary)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = dob, onValueChange = { dob = it },
                        label = { Text(stringResource(R.string.p_info_dob_hint)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = onSurfaceColor, unfocusedTextColor = onSurfaceColor, focusedLabelColor = GreenPrimary)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(stringResource(R.string.p_info_gender_label), fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start), color = onSurfaceColor)
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.Start)) {
                        val male = stringResource(R.string.gender_male)
                        val female = stringResource(R.string.gender_female)
                        RadioButton(selected = gender == male, onClick = { gender = male }, colors = RadioButtonDefaults.colors(selectedColor = GreenPrimary))
                        Text(male, color = onSurfaceColor)
                        Spacer(modifier = Modifier.width(24.dp))
                        RadioButton(selected = gender == female, onClick = { gender = female }, colors = RadioButtonDefaults.colors(selectedColor = GreenPrimary))
                        Text(female, color = onSurfaceColor)
                    }
                    Spacer(modifier = Modifier.height(32.dp))

                    val successMsg = stringResource(R.string.p_info_save_success, name)
                    Button(
                        onClick = { Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show(); onBackClick() },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.p_info_save))
                    }
                }
            }
        }
    }
}