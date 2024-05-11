package com.example.fastmath.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.fastmath.R
import com.example.fastmath.data.GameRepositoryImpl
import com.example.fastmath.databinding.FragmentGameBinding
import com.example.fastmath.domain.entity.GameResult
import com.example.fastmath.domain.entity.GameSettings
import com.example.fastmath.domain.entity.Level
import java.io.Serializable


private const val LEVEL_TYPE = "level_type"

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")


    private lateinit var level: Level

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        viewModel = GameViewModel(requireActivity().application)






    }

    private fun launchGameFinishFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    private fun setContentOnView() {
        binding.textViewTotal
    }

    private fun parseArgs() {
        requireArguments().getParcelable(LEVEL_TYPE, Level::class.java)?.let {
            level = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val NAME = "GameFragment"

        @JvmStatic
        fun newInstance(level: Level): GameFragment =
            GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(LEVEL_TYPE, level)
                }
            }
    }
}