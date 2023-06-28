package com.workouts.base_module.presentation

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.workouts.base_module.utils.handleHideKeyBoardWhenClick

abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding>(@LayoutRes layoutRes: Int) : AppCompatActivity(layoutRes) {
    protected abstract val binding: VB
    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
        bindViewModel()
        handleHideKeyBoardWhenClick(binding.root)
    }

    @MainThread
    protected abstract fun setupViews()

    @MainThread
    protected abstract fun bindViewModel()
}