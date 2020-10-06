package com.example.runningapp.ui.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.runningapp.R
import com.example.runningapp.other.Constants.KEY_NAME
import com.example.runningapp.other.Constants.KEY_TOGGLE_FIRST_TIME
import com.example.runningapp.other.Constants.KEY_WEIGHT
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_setup.*
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment: Fragment(R.layout.fragment_setup) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @set:Inject
    var isFirstTime = true

    @set:Inject
    var name = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!isFirstTime) {
            setToolBarTitle(name)
            goToRunFragmentAndRemovingSetupFragment(savedInstanceState)
        }

        tvContinue.setOnClickListener {
            val success = writePersonalDataToSharedPreferences()
            if(!success) {
                Snackbar.make(
                    requireView(),
                    "Please enter all the details!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
            else {
                goToRunFragmentAndRemovingSetupFragment(savedInstanceState)
            }
        }
    }

    private fun goToRunFragmentAndRemovingSetupFragment(savedInstanceState: Bundle?) {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.setupFragment, true)
            .build()
        findNavController().navigate(
            R.id.action_setupFragment_to_runFragment,
            savedInstanceState,
            navOptions
        )
    }

    private fun setToolBarTitle(name: String) {
        val toolbarText = "Let's go, $name!"
        requireActivity().tvToolbarTitle.text = toolbarText
    }

    private fun writePersonalDataToSharedPreferences(): Boolean {
        val name = etName.text.toString()
        val weight = etWeight.text.toString()
        if(name.isEmpty() || weight.isEmpty()) {
            return false
        }

        sharedPreferences.edit()
            .putString(KEY_NAME, name)
            .putFloat(KEY_WEIGHT, weight.toFloat())
            .putBoolean(KEY_TOGGLE_FIRST_TIME, false)
            .apply()

        setToolBarTitle(name)
        return true
    }
}