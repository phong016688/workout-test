@file:Suppress("SpellCheckingInspection", "unused", "NOTHING_TO_INLINE")

package com.workouts.base_module.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.widget.Toast
import androidx.annotation.AnyRes
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import java.util.Locale

inline val Context.isDarkThemeOn: Boolean
    get() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

@Px
@Suppress("NOTHING_TO_INLINE")
inline fun Context.dpToPx(dp: Float): Int = (dp * resources.displayMetrics.density).toInt()

@Px
@Suppress("NOTHING_TO_INLINE")
inline fun Context.dpToPx(dp: Int): Int = dpToPx(dp.toFloat())

inline val Context.isOrientationPortrait get() = this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

@ColorInt
inline fun Context.getColorCompat(@ColorRes id: Int) = ContextCompat.getColor(this, id)

inline fun Context.getDrawableCompat(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)

/**
 * Retrieve a color from the current [android.content.res.Resources.Theme].
 */
@ColorInt
@SuppressLint("Recycle")
inline fun Context.themeColor(@AttrRes themeAttrId: Int): Int =
    obtainStyledAttributes(intArrayOf(themeAttrId)).use { it.getColor(0, Color.TRANSPARENT) }

/**
 * Retrieve a color from the [resId].
 */
@ColorInt
@SuppressLint("Recycle")
inline fun Context.themeColor(@StyleRes resId: Int, @AttrRes themeAttrId: Int): Int =
    obtainStyledAttributes(resId, intArrayOf(themeAttrId)).use { it.getColor(0, Color.TRANSPARENT) }

/**
 * Get uri from any resource type
 * @receiver Context
 * @param resId - Resource id
 * @return - Uri to resource by given id or null
 */
inline fun Context.uriFromResourceId(@AnyRes resId: Int): Uri? {
    return runCatching {
        val res = this@uriFromResourceId.resources
        Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + res.getResourcePackageName(resId) +
                    '/' + res.getResourceTypeName(resId) +
                    '/' + res.getResourceEntryName(resId)
        )
    }.getOrNull()
}

inline fun Context.toast(
    @StringRes messageRes: Int,
    short: Boolean = true,
) = this.toast(getString(messageRes), short)

inline fun Context.toast(
    message: String,
    short: Boolean = true,
) =
    Toast.makeText(
        this,
        message,
        if (short) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
    ).apply { show() }!!

@SuppressLint("Recycle")
inline fun Context.themeInterpolator(@AttrRes attr: Int): Interpolator {
    return AnimationUtils.loadInterpolator(
        this,
        obtainStyledAttributes(intArrayOf(attr)).use {
            it.getResourceId(0, android.R.interpolator.fast_out_slow_in)
        }
    )
}

inline val Context.currentLocale: Locale
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        resources.configuration.locales[0]
    } else {
        @Suppress("DEPRECATION")
        resources.configuration.locale
    }
