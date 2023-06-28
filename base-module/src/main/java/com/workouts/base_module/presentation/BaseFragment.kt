package com.workouts.base_module.presentation

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.workouts.databinding.LayoutLoadingBinding

abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding>(@LayoutRes layoutRes: Int) :
    Fragment(layoutRes) {
    protected abstract val binding: VB
    protected abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupData()
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        bindViewModel()
    }

    @MainThread
    protected abstract fun setupViews()

    @MainThread
    protected abstract fun bindViewModel()

    @MainThread
    protected open fun setupData() = Unit

    fun showLoading() {
        val loadingView = LayoutLoadingBinding.inflate(layoutInflater, null, false).root
        loadingView.tag = LOADING_VIEW_TAG
        (binding.root as? ViewGroup)?.addView(
            loadingView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    fun hideLoading() {
        (binding.root as? ViewGroup)?.apply {
            removeView(findViewWithTag(LOADING_VIEW_TAG))
        }
    }

    companion object{
        const val LOADING_VIEW_TAG = "LoadingViewTag"
    }
}
