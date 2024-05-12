package com.example.fastmath.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.fastmath.R
import com.example.fastmath.databinding.FragmentChooseLevelBinding
import com.example.fastmath.databinding.FragmentGameBinding
import com.example.fastmath.domain.entity.Level


class ChooseLevelFragment : Fragment() {


    private var _binding: FragmentChooseLevelBinding? = null
    private val binding: FragmentChooseLevelBinding
        get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onLevelClickListeners()
    }

    private fun onLevelClickListeners() {
        with (binding) {
            buttonHard.setOnClickListener {
                launchGameFragment(Level.HARD)
            }
            buttonMedium.setOnClickListener {
                launchGameFragment(Level.MEDIUM)
            }
            buttonEasy.setOnClickListener {
                launchGameFragment(Level.EASY)
            }
            buttonTest.setOnClickListener {
                launchGameFragment(Level.TEST)
            }
        }
    }

    private fun launchGameFragment(level: Level) {
       requireActivity().supportFragmentManager.beginTransaction()
           .replace(R.id.main_container, GameFragment.newInstance(level))
           .addToBackStack(GameFragment.NAME)
           .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val NAME = "ChooseLevelFragment"

        @JvmStatic
        fun newInstance(): ChooseLevelFragment {
            return ChooseLevelFragment()
        }
    }
}