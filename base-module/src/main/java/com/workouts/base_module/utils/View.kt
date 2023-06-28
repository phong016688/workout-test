@file:Suppress("SpellCheckingInspection", "unused", "NOTHING_TO_INLINE")

package com.workouts.base_module.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible


fun View.hideKeyboard() {
  clearFocus()
  (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
    windowToken,
    0
  )
}

inline fun View.gone() {
  isGone = true
}

inline fun View.visible() {
  isVisible = true
}

inline fun View.invisible() {
  isInvisible = true
}

inline fun View.clicks(coolDown: Long = 300L, crossinline action: (view: View) -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        var lastTime = 0L
        override fun onClick(v: View) {
            val now = System.currentTimeMillis()
            if (now - lastTime > coolDown) {
                action(v)
                lastTime = now
            }
        }
    })
}

@SuppressLint("ClickableViewAccessibility")
fun handleHideKeyBoardWhenClick(view: View) {
    if (view !is EditText) {
        view.setOnTouchListener { v, event ->
            v.hideKeyboard()
            false
        }
    }
    if (view is ViewGroup) {
        for (i in 0 until view.childCount) {
            val innerView = view.getChildAt(i)
            handleHideKeyBoardWhenClick(innerView)
        }
    }
}