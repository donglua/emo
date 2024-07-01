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

package cn.qhplus.emo.scheme.impl

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry
import cn.qhplus.emo.scheme.SchemeTransition

object SchemeTransitionProviders {
    private val push = PushSchemeTransitionProvider()
    private val map = mutableMapOf<Int, SchemeTransitionProvider>().apply {
        put(SchemeTransition.PUSH, push)
        put(SchemeTransition.PRESENT, PresentSchemeTransitionProvider())
        put(SchemeTransition.SCALE, ScaleSchemeTransitionProvider())
        put(SchemeTransition.PUSH_THEN_STILL, PushThenStillSchemeTransitionProvider())
    }
    fun put(type: Int, provider: SchemeTransitionProvider) {
        if (type <= 0) {
            throw RuntimeException("type must be a positive number.")
        }
        map[type] = provider
    }

    fun get(type: Int): SchemeTransitionProvider {
        return map[type] ?: push
    }
}

@OptIn(ExperimentalAnimationApi::class)
interface SchemeTransitionProvider {
    fun activityEnterRes(): Int
    fun activityExitRes(): Int
    fun enterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?
    fun exitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?
    fun popEnterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)?
    fun popExitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)?
}

@OptIn(ExperimentalAnimationApi::class)
val SlideInRight: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition by lazy {
    {
        slideIn(tween(durationMillis = 300)) { IntOffset(it.width, 0) }
    }
}

@OptIn(ExperimentalAnimationApi::class)
val SlideInLeft: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition by lazy {
    {
        slideIn(tween(durationMillis = 300)) { IntOffset(-it.width, 0) }
    }
}

@OptIn(ExperimentalAnimationApi::class)
val SlideInBottom: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition by lazy {
    {
        targetState

        slideIn(tween(durationMillis = 300)) { IntOffset(0, it.height) }
    }
}

@OptIn(ExperimentalAnimationApi::class)
val SlideInTop: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition by lazy {
    {
        slideIn(tween(durationMillis = 300)) { IntOffset(0, -it.height) }
    }
}

@OptIn(ExperimentalAnimationApi::class)
val ScaleIn: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition by lazy {
    {
        scaleIn(tween(durationMillis = 300), 0.8f) + fadeIn(tween(durationMillis = 300), 0f)
    }
}

@OptIn(ExperimentalAnimationApi::class)
val FadeIn: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition by lazy {
    {
        fadeIn(tween(durationMillis = 300), 0f)
    }
}

@OptIn(ExperimentalAnimationApi::class)
val StillIn: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition by lazy {
    {
        scaleIn(tween(durationMillis = 300), 1f)
    }
}

@OptIn(ExperimentalAnimationApi::class)
val SlideOutLeft: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition by lazy {
    {
        slideOut(tween(durationMillis = 300, delayMillis = 50)) { IntOffset(-it.width, 0) }
    }
}

@OptIn(ExperimentalAnimationApi::class)
val SlideOutRight: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition by lazy {
    {
        slideOut(tween(durationMillis = 300, delayMillis = 50)) { IntOffset(it.width, 0) }
    }
}

@OptIn(ExperimentalAnimationApi::class)
val SlideOutTop: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition by lazy {
    {
        slideOut(tween(durationMillis = 300, delayMillis = 50)) { IntOffset(0, -it.height) }
    }
}

@OptIn(ExperimentalAnimationApi::class)
val ScaleOut: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition by lazy {
    {
        scaleOut(
            tween(durationMillis = 300, delayMillis = 50),
            0.8f
        ) + fadeOut(tween(durationMillis = 300, delayMillis = 50), 0f)
    }
}

@OptIn(ExperimentalAnimationApi::class)
val FadeOut: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition by lazy {
    {
        fadeOut(tween(durationMillis = 300, delayMillis = 50))
    }
}

@OptIn(ExperimentalAnimationApi::class)
val StillOut: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition by lazy {
    {
        scaleOut(tween(durationMillis = 300, delayMillis = 50), 1f)
    }
}

@OptIn(ExperimentalAnimationApi::class)
val SlideOutBottom: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition by lazy {
    {
        slideOut(tween(durationMillis = 300, delayMillis = 50)) { IntOffset(0, it.height) }
    }
}

@OptIn(ExperimentalAnimationApi::class)
open class PushSchemeTransitionProvider : SchemeTransitionProvider {

    override fun activityEnterRes(): Int {
        return R.anim.slide_in_right
    }

    override fun activityExitRes(): Int {
        return R.anim.slide_out_left
    }

    override fun enterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? {
        return SlideInRight
    }

    override fun exitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? {
        return SlideOutLeft
    }

    override fun popEnterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? {
        return SlideInLeft
    }

    override fun popExitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? {
        return SlideOutRight
    }
}

@OptIn(ExperimentalAnimationApi::class)
open class PushThenStillSchemeTransitionProvider : SchemeTransitionProvider {

    override fun activityEnterRes(): Int {
        return R.anim.slide_in_right
    }

    override fun activityExitRes(): Int {
        return R.anim.slide_out_left
    }

    override fun enterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? {
        return SlideInRight
    }

    override fun exitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? {
        return StillOut
    }

    override fun popEnterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? {
        return StillIn
    }

    override fun popExitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? {
        return SlideOutRight
    }
}

@OptIn(ExperimentalAnimationApi::class)
open class PresentSchemeTransitionProvider : SchemeTransitionProvider {

    override fun activityEnterRes(): Int {
        return R.anim.slide_in_bottom
    }

    override fun activityExitRes(): Int {
        return R.anim.slide_still
    }

    override fun enterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? {
        return SlideInBottom
    }

    override fun exitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? {
        return StillOut
    }

    override fun popEnterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? {
        return StillIn
    }

    override fun popExitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? {
        return SlideOutBottom
    }
}

@OptIn(ExperimentalAnimationApi::class)
open class ScaleSchemeTransitionProvider : SchemeTransitionProvider {

    override fun activityEnterRes(): Int {
        return R.anim.scale_enter
    }

    override fun activityExitRes(): Int {
        return R.anim.slide_still
    }

    override fun enterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? {
        return ScaleIn
    }

    override fun exitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? {
        return StillOut
    }

    override fun popEnterTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?)? {
        return StillIn
    }

    override fun popExitTransition(): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?)? {
        return ScaleOut
    }
}
