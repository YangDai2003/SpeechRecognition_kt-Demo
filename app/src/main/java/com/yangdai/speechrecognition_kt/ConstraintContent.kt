package com.yangdai.speechrecognition_kt

import android.Manifest
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.util.Locale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ConstraintContent(
    viewModel: MainViewModel
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (btSpeech, tvResult) = createRefs()

        val multiplePermissionState = rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.RECORD_AUDIO
            )
        )

        val context = LocalContext.current
        val result = remember {
            mutableStateOf("无内容")
        }
        val myRecognitionListener = MyRecognitionListener(result)

        Text(
            text = result.value,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier
                .constrainAs(tvResult) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(btSpeech.top)
                    centerHorizontallyTo(parent)
                }
                .padding(16.dp),
            textAlign = TextAlign.Center
        )

        Button(
            onClick = {
                multiplePermissionState.launchMultiplePermissionRequest()

                when {
                    multiplePermissionState.allPermissionsGranted -> {
                        when {
                            Utils.isNetworkAvailable(context) -> {
                                val recognitionIntent =
                                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                                recognitionIntent.putExtra(
                                    RecognizerIntent.EXTRA_PARTIAL_RESULTS,
                                    true
                                )
                                recognitionIntent.putExtra(
                                    RecognizerIntent.EXTRA_LANGUAGE,
                                    Locale.CHINA
                                )

                                viewModel.mySpeechRecognizer.setRecognitionListener(
                                    myRecognitionListener
                                )
                                viewModel.mySpeechRecognizer.startListening(recognitionIntent)
                            }

                            else -> {
                                Toast.makeText(
                                    context,
                                    "无网络连接",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    }

                    multiplePermissionState.shouldShowRationale -> {
                        Toast.makeText(
                            context,
                            "请同意全部权限",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            modifier = Modifier.constrainAs(btSpeech) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(tvResult.bottom)
                centerHorizontallyTo(parent)
            }
        ) {
            Text(text = "语音识别")
        }
    }
}
