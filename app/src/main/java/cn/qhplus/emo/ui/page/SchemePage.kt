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

package cn.qhplus.emo.ui.page

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import cn.qhplus.emo.EmoScheme
import cn.qhplus.emo.MainActivity
import cn.qhplus.emo.config.SchemeConst
import cn.qhplus.emo.config.runQuietly
import cn.qhplus.emo.config.schemeBuilder
import cn.qhplus.emo.config.webSchemeBuilder
import cn.qhplus.emo.scheme.ComposeScheme
import cn.qhplus.emo.scheme.SchemeBoolArg
import cn.qhplus.emo.scheme.SchemeFloatArg
import cn.qhplus.emo.scheme.SchemeIntArg
import cn.qhplus.emo.scheme.SchemeLongArg
import cn.qhplus.emo.scheme.SchemeStringArg
import cn.qhplus.emo.scheme.SchemeTransition
import cn.qhplus.emo.scheme.model
import cn.qhplus.emo.scheme.readAction
import cn.qhplus.emo.scheme.readTransition
import cn.qhplus.emo.ui.CommonItem
import kotlinx.serialization.Serializable

@ComposeScheme(
    action = SchemeConst.SCHEME_ACTION_SCHEME,
    alternativeHosts = [MainActivity::class],
    transition = SchemeTransition.PUSH_THEN_STILL
)
@Composable
fun SchemePage(navBackStackEntry: NavBackStackEntry) {
    Log.i("SchemePage", "action = ${navBackStackEntry.readAction()}, transition = ${navBackStackEntry.readTransition()}")
    OnlyBackListPage(
        title = "Scheme"
    ) {
        item {
            CommonItem("For Activity") {
                schemeBuilder(SchemeConst.SCHEME_ACTION_SCHEME_ACTIVITY).runQuietly()
            }
        }

        item {
            CommonItem("For Composable") {
                schemeBuilder(SchemeConst.SCHEME_ACTION_MODAL).forceNewHost().runQuietly()
            }
        }

        item {
            CommonItem("With Argument") {
                schemeBuilder(SchemeConst.SCHEME_ACTION_SCHEME_ARG)
                    .arg("str", "hehe")
                    .arg("int", 100)
                    .arg("xxx", "xx")
                    .runQuietly()
            }
        }

        item {
            CommonItem("With Model Argument") {
                schemeBuilder(SchemeConst.SCHEME_ACTION_SCHEME_MODEL)
                    .model(DataArg(str = "haha"))
                    .runQuietly()
            }
        }

        item {
            CommonItem("Host Special Argument") {
                schemeBuilder(SchemeConst.SCHEME_ACTION_SCHEME_HOST_ARG)
                    .arg("aa", "hehe")
                    .runQuietly()
            }
        }

        item {
            CommonItem("Special Arg, Type = 1") {
                schemeBuilder(SchemeConst.SCHEME_ACTION_SCHEME_SPECIAL_ARG)
                    .arg("type", 1)
                    .runQuietly()
            }
        }

        item {
            CommonItem("Special Arg, Type = 2") {
                schemeBuilder(SchemeConst.SCHEME_ACTION_SCHEME_SPECIAL_ARG)
                    .arg("type", 2)
                    .runQuietly()
            }
        }

        item {
            CommonItem("Alpha Transition") {
                schemeBuilder(SchemeConst.SCHEME_ACTION_SCHEME_ALPHA).runQuietly()
            }
        }

        item {
            CommonItem("Slide From Bottom") {
                schemeBuilder(SchemeConst.SCHEME_ACTION_SCHEME_SLIDE_BOTTOM).runQuietly()
            }
        }

        item {
            CommonItem("Batch") {
                val schemes = listOf(
                    schemeBuilder(SchemeConst.SCHEME_ACTION_PHOTO).toString(),
                    webSchemeBuilder("file:///android_asset/demo.html", "JsBridge").toString(),
                    schemeBuilder(SchemeConst.SCHEME_ACTION_MODAL).toString(),
                    schemeBuilder(SchemeConst.SCHEME_ACTION_SCHEME_ACTIVITY).toString(),
                    schemeBuilder(SchemeConst.SCHEME_ACTION_PERMISSION).toString()
                )
                EmoScheme.batchHandleQuietly(schemes)
            }
        }
    }
}

@Serializable
data class DataArg(
    val i: Int = 3,
    val l: Long = 4,
    val b: Boolean = true,
    val str: String = "xixi"
)

@ComposeScheme(
    action = SchemeConst.SCHEME_ACTION_SCHEME_MODEL,
    alternativeHosts = [MainActivity::class]
)
@Composable
fun SchemeModelPage(arg: DataArg){
    OnlyBackListPage(
        title = "SchemeArg"
    ) {
        item {
            CommonItem(arg.toString()) {
            }
        }
    }
}

@ComposeScheme(
    action = SchemeConst.SCHEME_ACTION_SCHEME_ARG,
    alternativeHosts = [MainActivity::class]
)
@SchemeStringArg(name = "str", default = "xx")
@SchemeIntArg(name = "int", default = 3)
@SchemeLongArg(name = "long", default = 3)
@SchemeFloatArg(name = "float", default = 3.14f)
@SchemeBoolArg(name = "bool", default = false)
@Composable
fun SchemeArgPage(navBackStackEntry: NavBackStackEntry) {
    val stringArg = navBackStackEntry.arguments?.getString("str")
    val intArg = navBackStackEntry.arguments?.getInt("int")
    val longArg = navBackStackEntry.arguments?.getLong("long")
    val boolArg = navBackStackEntry.arguments?.getBoolean("bool")
    val floatArg = navBackStackEntry.arguments?.getFloat("float")
    OnlyBackListPage(
        title = "SchemeArg"
    ) {
        item {
            CommonItem("str = $stringArg, int = $intArg, long = $longArg, bool = $boolArg, float = $floatArg") {
            }
        }
    }
}

@ComposeScheme(
    action = SchemeConst.SCHEME_ACTION_SCHEME_HOST_ARG,
    alternativeHosts = [MainActivity::class]
)
@SchemeStringArg(name = "aa", default = "hehe")
@Composable
fun SchemeHostArgPage(navBackStackEntry: NavBackStackEntry) {
    val aaArg = navBackStackEntry.arguments?.getString("aa")
    OnlyBackListPage(
        title = "SchemeHostArg($aaArg)"
    ) {
        item {
            CommonItem("Matched, Use current activity") {
                schemeBuilder(SchemeConst.SCHEME_ACTION_SCHEME_HOST_ARG)
                    .arg("aa", aaArg ?: "")
                    .runQuietly()
            }
        }

        item {
            CommonItem("Not matched, Use new activity") {
                schemeBuilder(SchemeConst.SCHEME_ACTION_SCHEME_HOST_ARG)
                    .arg("aa", "${System.currentTimeMillis()}")
                    .runQuietly()
            }
        }
    }
}

@ComposeScheme(
    action = SchemeConst.SCHEME_ACTION_SCHEME_ALPHA,
    alternativeHosts = [MainActivity::class],
    transition = SchemeTransition.SCALE
)
@Composable
fun SchemeAlphaTransitionPage() {
    OnlyBackListPage(
        title = "SchemeAlphaTransition"
    ) {
    }
}

@ComposeScheme(
    action = SchemeConst.SCHEME_ACTION_SCHEME_SLIDE_BOTTOM,
    alternativeHosts = [MainActivity::class],
    transition = SchemeTransition.PRESENT
)
@Composable
fun SchemeSlideFromBottomPage() {
    OnlyBackListPage(
        title = "SchemeSlideFromBottom"
    ) {
    }
}

@ComposeScheme(
    action = SchemeConst.SCHEME_ACTION_SCHEME_SPECIAL_ARG,
    alternativeHosts = [MainActivity::class]
)
@SchemeIntArg(name = "type", special = true, default = 1)
@Composable
fun SchemeSpecialArgType1(navBackStackEntry: NavBackStackEntry) {
    OnlyBackListPage(
        title = "SchemeSpecialArg(Type = 1)"
    ) {
    }
}

@ComposeScheme(
    action = SchemeConst.SCHEME_ACTION_SCHEME_SPECIAL_ARG,
    alternativeHosts = [MainActivity::class]
)
@SchemeIntArg(name = "type", special = true, default = 2)
@Composable
fun SchemeSpecialArgType2(navBackStackEntry: NavBackStackEntry) {
    OnlyBackListPage(
        title = "SchemeSpecialArg(Type = 2)"
    ) {
    }
}
