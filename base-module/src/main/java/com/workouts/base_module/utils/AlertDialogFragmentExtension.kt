package com.workouts.base_module.utils

import android.os.Looper
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume

private val TAG = AlertDialogFragment::class.java.simpleName

/**
 * Show alert dialog fragment
 * @return [Unit] when pressing OK button, null otherwise
 */
suspend fun FragmentActivity.showAlertDialogSuspend(
    negativeText: String? = "Cancel",
    positiveText: String? = "OK",
    init: AlertDialogFragment.Builder.() -> Unit,
): Unit? {
    return suspendCancellableCoroutine { emitter ->
        check(Looper.getMainLooper() == Looper.myLooper())

        showAlertDialog {
            init()

            onCancel { emitter.resume(null) }

            negativeText?.let {
                negativeAction(it) { _, _ ->
                    emitter.resume(null)
                }
            }

            positiveText?.let {
                positiveAction(it) { _, _ ->
                    emitter.resume(Unit)
                }
            }
        }

        emitter.invokeOnCancellation {
            Dispatchers.Main.immediate.dispatch(EmptyCoroutineContext) {
                dismissAlertDialog()
            }
        }
    }
}

/**
 * Show alert dialog fragment
 * @return a Flow that emits [Unit] when pressing OK button, emits null otherwise.
 */
@OptIn(FlowPreview::class)
fun FragmentActivity.showAlertDialogAsFlow(
    negativeText: String? = "Cancel",
    positiveText: String? = "OK",
    init: AlertDialogFragment.Builder.() -> Unit,
): Flow<Unit?> = suspend {
    showAlertDialogSuspend(
        negativeText = negativeText,
        positiveText = positiveText,
        init = init
    )
}.asFlow()

/**
 * Show alert dialog
 */
fun FragmentActivity.showAlertDialog(
    init: AlertDialogFragment.Builder.() -> Unit
): AlertDialogFragment {
    dismissAlertDialog()

    return AlertDialogFragment.Builder()
        .apply(init)
        .build()
        .apply { show(supportFragmentManager, TAG) }
}

/**
 * Dismiss alert dialog
 */
fun FragmentActivity.dismissAlertDialog() {
    try {
        val dialogFragment =
            supportFragmentManager.findFragmentByTag(TAG) as? AlertDialogFragment
        dialogFragment?.cleanUp()
        dialogFragment?.dismissAllowingStateLoss()
        Timber.d("dismissAlertDialog tag=$TAG")
    } catch (e: Exception) {
        Timber.e(e, "dismissAlertDialog tag=$TAG")
    }
}

fun FragmentActivity.showDialogFragment(dialog: DialogFragment) {
    val tag = dialog::class.java.simpleName

    (supportFragmentManager.findFragmentByTag(tag) as? DialogFragment)?.dismissAllowingStateLoss()

    dialog.show(supportFragmentManager, tag)
}