package com.example.fastmath.presentation

import android.health.connect.datatypes.units.Percentage
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.fastmath.R
import com.example.fastmath.databinding.FragmentGameFinishBinding
import com.example.fastmath.domain.entity.GameResult


class GameFinishFragment : Fragment() {

    private var _binding: FragmentGameFinishBinding? = null
    private lateinit var gameResult: GameResult
    private val binding: FragmentGameFinishBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishBinding == null")




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().getParcelable(KEY_GAME_RESULT, GameResult::class.java)?.let {
            gameResult = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGameFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        setUpOnClickListeners()

    }

    private fun setUpOnClickListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    retryGame()
                }
            })
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun bindViews() {
        setResultBrainEmotion(gameResult.winner)

        binding.textViewRequiredPercentage.text = String.format(
            stringResIdToString(R.string.required_correct_percentage_s),
            gameResult.gameSettings.minPercentOfRightAnswers.toString()
        )
        binding.textViewRequiredScore.text = String.format(
            stringResIdToString(R.string.required_score_s),
            gameResult.gameSettings.minCountOfRightAnswers.toString()
        )
        binding.textViewUserScore.text = String.format(
            stringResIdToString(R.string.your_score_s),
            gameResult.countOfRightAnswers.toString()
        )
        binding.textViewUserPercentage.text = String.format(
            stringResIdToString(R.string.your_percentage_s),
            (gameResult.countOfRightAnswers / gameResult.countOfQuestion.toDouble() * 100).toInt()
        )
    }


    private fun stringResIdToString(stringResId: Int): String {
        return ContextCompat.getString(requireContext(), stringResId)
    }


    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setResultBrainEmotion(winner: Boolean) {
        val drawableResId = if (winner) {
            ContextCompat.getDrawable(requireContext(), R.drawable.happy_brain)
        } else {
            ContextCompat.getDrawable(requireContext(), R.drawable.sad_brain)
        }
        binding.emojiResult.setImageDrawable(drawableResId)
    }

    companion object {

        private const val KEY_GAME_RESULT = "game_result"

        @JvmStatic
        fun newInstance(gameResult: GameResult) =
            GameFinishFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
    }
}