package com.ds9712.ds_9712.fragments.main

import android.content.Context
import android.os.Bundle
import androidx.annotation.NavigationRes
import androidx.navigation.fragment.NavHostFragment
import com.ds9712.ds_9712.ui.main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

class MainNavHostFragment : NavHostFragment() {
    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onAttach(context: Context) {
        childFragmentManager.fragmentFactory = (activity as MainActivity).fragmentFactory
        super.onAttach(context)
    }

    companion object {
        private const val KEY_GRAPH_ID = "android-support-nav:fragment:graphId"

        @JvmStatic
        fun create(
            @NavigationRes graphId: Int = 0
        ): MainNavHostFragment {
            var bundle: Bundle? = null
            if (graphId != 0) {
                bundle = Bundle()
                bundle.putInt(KEY_GRAPH_ID, graphId)
            }
            val result = MainNavHostFragment()
            if (bundle != null) {
                result.arguments = bundle
            }
            return result

        }
    }
}