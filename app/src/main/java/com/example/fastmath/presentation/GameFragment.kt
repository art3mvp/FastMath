package com.example.fastmath.presentation

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Resources.Theme
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
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

    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[GameViewModel::class.java]
    }


    private val options by lazy {
        mutableListOf<TextView>().apply {
            this.add(binding.textViewOption1)
            this.add(binding.textViewOption2)
            this.add(binding.textViewOption3)
            this.add(binding.textViewOption4)
            this.add(binding.textViewOption5)
            this.add(binding.textViewOption6)
        }
    }

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

        observeViewModel()
        setOnClickListeners()
        viewModel.startGame(level)

    }

    private fun observeViewModel() {
        viewModel.question.observe(viewLifecycleOwner) {
            binding.textViewTotal.text = it.total.toString()
            binding.textViewVisibleNumber.text = it.visibleNumber.toString()
            for (index in 0 until options.size) {
                options[index].text = it.options[index].toString()
            }
        }
        viewModel.percentOfCorrectAnswers.observe(viewLifecycleOwner) {
            binding.progressBar.setProgress(it, true)
        }

        viewModel.enoughCorrectAnswers.observe(viewLifecycleOwner) {
            binding.textViewAnswersProgress.setTextColor(getColorByState(it))
        }

        viewModel.enoughPercent.observe(viewLifecycleOwner) {
            val color = getColorByState(it)
            binding.progressBar.progressTintList = ColorStateList.valueOf(color)
        }
        viewModel.formattedTime.observe(viewLifecycleOwner) {
            binding.textViewTimer.text = it
        }
        viewModel.progressOfCorrectAnswers.observe(viewLifecycleOwner) {
            binding.textViewAnswersProgress.text = it
        }
        viewModel.minPercent.observe(viewLifecycleOwner) {
            binding.progressBar.secondaryProgress = it
        }
        viewModel.gameResult.observe(viewLifecycleOwner) {
            launchGameFinishFragment(it)
        }
    }

    private fun getColorByState(goodState: Boolean): Int {
        val colorResId = if (goodState) {
            ContextCompat.getColor(requireContext(), R.color.green_correct)
        } else {
            ContextCompat.getColor(requireContext(), R.color.red_progress)
        }
        return colorResId
    }

    private fun setOnClickListeners() {
        binding.textViewOption1.setOnClickListener {
            val valueClicked = binding.textViewOption1.text.toString().toInt()
            viewModel.chooseAnswer(valueClicked)
        }
        binding.textViewOption2.setOnClickListener {
            val valueClicked =  binding.textViewOption2.text.toString().toInt()
            viewModel.chooseAnswer(valueClicked)
        }
        binding.textViewOption3.setOnClickListener {
            val valueClicked =  binding.textViewOption3.text.toString().toInt()
            viewModel.chooseAnswer(valueClicked)

        }
        binding.textViewOption4.setOnClickListener {
            val valueClicked =  binding.textViewOption4.text.toString().toInt()
            viewModel.chooseAnswer(valueClicked)

        }
        binding.textViewOption5.setOnClickListener {
            val valueClicked =  binding.textViewOption5.text.toString().toInt()
            viewModel.chooseAnswer(valueClicked)

        }
        binding.textViewOption6.setOnClickListener {
            val valueClicked =  binding.textViewOption6.text.toString().toInt()
            viewModel.chooseAnswer(valueClicked)
        }
    }


    private fun launchGameFinishFragment(gameResult: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, GameFinishFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
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