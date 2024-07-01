/*
 * Copyright 2022 emo Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.qhplus.emo.photo.ui.picker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.qhplus.emo.ui.core.CheckBox
import cn.qhplus.emo.ui.core.CheckStatus
import cn.qhplus.emo.ui.core.PressWithAlphaBox
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PhotoPickCheckBox(pickIndex: Int) {
    val config = LocalPhotoPickerConfig.current
    val strokeWidth = with(LocalDensity.current) {
        2.dp.toPx()
    }
    AnimatedVisibility(
        visible = pickIndex < 0,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = config.commonIconNormalTintColor,
                radius = (size.minDimension - strokeWidth) / 2.0f,
                style = Stroke(strokeWidth)
            )
        }
    }
    AnimatedVisibility(
        visible = pickIndex >= 0,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(config.commonIconCheckedTintColor),
            contentAlignment = Alignment.Center
        ) {
            if (transition.targetState != EnterExitState.PostExit) {
                Text(
                    text = "${pickIndex + 1}",
                    color = config.commonIconCheckedTextColor,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun PhotoPickRadio(
    checked: Boolean,
    ratioSize: Dp = 18.dp,
    strokeWidthDp: Dp = 1.6.dp
) {
    Box(modifier = Modifier.size(ratioSize)) {
        val strokeWidth = with(LocalDensity.current) {
            strokeWidthDp.toPx()
        }
        val config = LocalPhotoPickerConfig.current
        AnimatedVisibility(
            visible = !checked,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Canvas(modifier = Modifier.size(ratioSize)) {
                drawCircle(
                    color = config.commonIconNormalTintColor,
                    radius = (size.minDimension - strokeWidth) / 2.0f,
                    style = Stroke(strokeWidth)
                )
            }
        }
        AnimatedVisibility(
            visible = checked,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Canvas(modifier = Modifier.size(ratioSize)) {
                drawCircle(
                    color = config.commonIconCheckedTintColor,
                    radius = (size.minDimension - strokeWidth) / 2.0f,
                    style = Stroke(strokeWidth)
                )

                drawCircle(
                    color = config.commonIconCheckedTintColor,
                    radius = (size.minDimension - strokeWidth * 4) / 2.0f
                )
            }
        }
    }
}

@Composable
fun OriginOpenButton(
    modifier: Modifier = Modifier,
    isOriginOpenFlow: StateFlow<Boolean>,
    onToggleOrigin: (toOpen: Boolean) -> Unit
) {
    val isOriginOpen by isOriginOpenFlow.collectAsState()
    Row(
        modifier = modifier.clickable(
            interactionSource = remember {
                MutableInteractionSource()
            },
            indication = null
        ) {
            onToggleOrigin.invoke(!isOriginOpen)
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.spacedBy(5.dp)
    ) {
        PhotoPickRadio(isOriginOpen)
        Text(
            "原图",
            fontSize = 17.sp,
            color = LocalPhotoPickerConfig.current.commonTextButtonTextColor
        )
    }
}

@Composable
fun PickCurrentCheckButton(
    modifier: Modifier = Modifier,
    isPicked: Boolean,
    onPicked: (toPick: Boolean) -> Unit
) {
    val config = LocalPhotoPickerConfig.current
    Row(
        modifier = modifier.clickable(
            interactionSource = remember {
                MutableInteractionSource()
            },
            indication = null
        ) {
            onPicked.invoke(!isPicked)
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.spacedBy(5.dp)
    ) {
        CheckBox(
            size = 18.dp,
            status = if (isPicked) CheckStatus.Checked else CheckStatus.None,
            tint = if (isPicked) config.commonIconCheckedTintColor else config.commonIconNormalTintColor,
            background = if (isPicked) config.commonIconNormalTintColor else Color.Transparent
        )
        Text(
            "选择",
            fontSize = 17.sp,
            color = LocalPhotoPickerConfig.current.commonTextButtonTextColor
        )
    }
}

@Composable
internal fun CommonTextButton(
    modifier: Modifier,
    enable: Boolean,
    text: String,
    onClick: () -> Unit
) {
    PressWithAlphaBox(
        enable = enable,
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
            .then(modifier),
        onClick = {
            onClick()
        }
    ) {
        Text(
            text,
            fontSize = 17.sp,
            color = LocalPhotoPickerConfig.current.commonTextButtonTextColor,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
internal fun CommonButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    text: String,
    onClick: () -> Unit
) {
    val config = LocalPhotoPickerConfig.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()
    val bgColor = when {
        !enabled -> config.commonButtonDisableBgColor
        isPressed.value -> config.commonButtonPressBgColor
        else -> config.commonButtonNormalBgColor
    }
    val textColor = when {
        !enabled -> config.commonButtonDisabledTextColor
        isPressed.value -> config.commonButtonPressedTextColor
        else -> config.commonButtonNormalTextColor
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bgColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled
            ) {
                onClick()
            }
            .padding(start = 10.dp, end = 10.dp, top = 3.dp, bottom = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 17.sp,
            color = textColor
        )
    }
}

@Composable
internal fun CommonPickerTip(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text(
            text,
            fontSize = 16.sp,
            color = LocalPhotoPickerConfig.current.tipTextColor,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
