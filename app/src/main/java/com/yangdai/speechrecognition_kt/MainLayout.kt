package com.yangdai.speechrecognition_kt

import android.Manifest
import android.content.Intent
import android.content.res.Configuration
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.util.Locale


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainLayout(
    viewModel: MainViewModel
) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val constraints = dynamicLayoutConstraint(isPortrait)

    val multiplePermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.RECORD_AUDIO
        )
    )
    val context = LocalContext.current
    val result = remember {
        mutableStateOf("无内容")
    }
    viewModel.myRecognitionListener.setResult(result)

    ConstraintLayout(
        constraints,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = result.value,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.layoutId("text"),
            textAlign = TextAlign.Center
        )

        ElevatedButton(
            onClick = {
                multiplePermissionState.launchMultiplePermissionRequest()

                when {
                    multiplePermissionState.allPermissionsGranted -> {
                        if (Utils.isNetworkAvailable(context)) {
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
                            viewModel.mySpeechRecognizer.startListening(recognitionIntent)
                        } else {
                            Toast.makeText(
                                context,
                                "无网络连接",
                                Toast.LENGTH_SHORT
                            ).show()
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
            modifier = Modifier.layoutId("button")
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_keyboard_voice_24),
                contentDescription = "",
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "语音识别")
        }
    }
}

/**
 * 横竖屏布局切换
 */
fun dynamicLayoutConstraint(isPortrait: Boolean): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        if (isPortrait) {
            constrain(button) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(text.bottom)
                centerHorizontallyTo(parent)
            }
            constrain(text) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(button.top)
                centerHorizontallyTo(parent)
            }
        } else {
            val guideline = createGuidelineFromStart(0.5f)
            constrain(button) {
                bottom.linkTo(parent.bottom)
                start.linkTo(guideline)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                centerVerticallyTo(parent)
            }
            constrain(text) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(guideline)
                bottom.linkTo(parent.bottom)
                centerVerticallyTo(parent)
            }
        }
    }
}