package com.stushare.feature_contribution.ui.upload

import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stushare.feature_contribution.R
import com.stushare.feature_contribution.ui.theme.GreenPrimary

@Composable
fun UploadScreen(
    viewModel: UploadViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val isUploading by viewModel.isUploading.collectAsStateWithLifecycle()
    
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedFileName by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    
    // String resources for logic
    val noFileStr = stringResource(R.string.upload_no_file)
    if (selectedFileName.isEmpty()) selectedFileName = noFileStr

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            selectedUri = it
            val cursor = context.contentResolver.query(it, null, null, null, null)
            cursor?.use { c ->
                if (c.moveToFirst()) {
                    val nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex >= 0) selectedFileName = c.getString(nameIndex)
                }
            }
            if (title.isEmpty()) {
                title = selectedFileName.substringBeforeLast(".")
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uploadEvent.collect { result ->
            when (result) {
                is UploadViewModel.UploadResult.Success -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    title = ""
                    description = ""
                    selectedFileName = noFileStr
                    selectedUri = null
                }
                is UploadViewModel.UploadResult.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(GreenPrimary, RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp))
            ) {
                IconButton(onClick = onBackClick, modifier = Modifier.padding(top = 32.dp, start = 16.dp)) {
                    Text("🔙", fontSize = 24.sp, color = Color.White)
                }
            }

            Card(
                modifier = Modifier.padding(16.dp).offset(y = (-40).dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        stringResource(R.string.upload_header),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = onSurfaceColor,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(stringResource(R.string.upload_selected_file), color = onSurfaceColor.copy(alpha = 0.6f))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(selectedFileName, color = onSurfaceColor)
                    }

                    Button(
                        onClick = { filePickerLauncher.launch(arrayOf("application/pdf", "application/msword")) },
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.upload_choose_btn))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text(stringResource(R.string.upload_title_hint)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = onSurfaceColor,
                            unfocusedTextColor = onSurfaceColor,
                            focusedLabelColor = GreenPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text(stringResource(R.string.upload_desc_hint)) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = onSurfaceColor,
                            unfocusedTextColor = onSurfaceColor,
                            focusedLabelColor = GreenPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        OutlinedButton(
                            onClick = { 
                                title = ""; description = ""; selectedUri = null; selectedFileName = noFileStr 
                            },
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                        ) {
                            Text(stringResource(R.string.cancel))
                        }

                        val errorMsg = stringResource(R.string.upload_error_msg)
                        Button(
                            onClick = {
                                if (selectedUri != null && title.isNotEmpty()) {
                                    viewModel.handleUploadClick(title, description)
                                } else {
                                    Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                                }
                            },
                            enabled = !isUploading && selectedUri != null,
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                        ) {
                            Text(if (isUploading) stringResource(R.string.upload_btn_loading) else stringResource(R.string.upload_btn))
                        }
                    }
                }
            }
        }
        
        if (isUploading) {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GreenPrimary)
            }
        }
    }
}