package com.example.pdfloaderdemo.common

import android.view.View
import java.lang.ref.WeakReference

/**
 * Allows to set double click events on views.
 *
 * @param action The action to perform on click.
 * @param interval The maximum interval between two clicks, in milliseconds.
 */
class DoubleClickListener(private val action: (view: View) -> Unit, private val interval: Long = 800) : View.OnClickListener {

    companion object {

        /** The view that was clicked previously. */
        private var myPreviouslyClickedView: WeakReference<View>? = null

        /**
         * Check if the click was a second one or not.
         * @param view The view to check for.
         *
         * @return True if the click was a second one.
         */
        private fun isSecondClick(view: View) =
            myPreviouslyClickedView?.get() == view

    }

    /** Execute the click. */
    override fun onClick(view: View?) {
        if (view != null) {

            // Make sure this click is the second one
            if (isSecondClick(view)) {
                myPreviouslyClickedView?.clear()
                action(view)

            } else {

                // Set the previous view to this one but remove it after few moments
                myPreviouslyClickedView = WeakReference(view)
                view.postDelayed({ myPreviouslyClickedView?.clear() }, interval)
            }
        }
    }

}