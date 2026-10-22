package com.example.quizz

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Tableau de 5 questions statiques
    private val questions = listOf(
        Question(
            text = "Quelle est la forme géométrique affichée ci-dessus ?",
            imageResId = R.drawable.ic_triangle,
            choices = listOf("Carré", "Triangle", "Cercle"),
            correctAnswerIndex = 1,
            imageDescription = "Triangle bleu à 3 côtés",
        ),
        Question(
            text = "Quelle est la forme géométrique de cette image ?",
            imageResId = R.drawable.ic_square,
            choices = listOf("Rectangle", "Carré", "Étoile"),
            correctAnswerIndex = 1,
            imageDescription = "Carré rose à 4 côtés égaux",
        ),
        Question(
            text = "Quelle est cette forme géométrique parfaitement ronde ?",
            imageResId = R.drawable.ic_circle,
            choices = listOf("Cercle", "Triangle", "Carré"),
            correctAnswerIndex = 0,
            imageDescription = "Cercle vert parfaitement rond",
        ),
        Question(
            text = "Quelle forme comporte 5 branches sur ce dessin ?",
            imageResId = R.drawable.ic_star,
            choices = listOf("Triangle", "Rectangle", "Étoile"),
            correctAnswerIndex = 2,
            imageDescription = "Étoile jaune à 5 branches",
        ),
        Question(
            text = "Identifiez cette forme géométrique au format allongé :",
            imageResId = R.drawable.ic_rectangle,
            choices = listOf("Rectangle", "Carré", "Cercle"),
            correctAnswerIndex = 0,
            imageDescription = "Rectangle violet à 4 côtés",
        ),
    )

    // Variables d'état du jeu
    private var currentQuestionIndex = 0
    private var score = 0
    private var hasAnswered = false
    private var selectedChoiceIndex = -1
    private var isGameOver = false

    companion object {
        private const val KEY_INDEX = "KEY_CURRENT_INDEX"
        private const val KEY_SCORE = "KEY_SCORE"
        private const val KEY_HAS_ANSWERED = "KEY_HAS_ANSWERED"
        private const val KEY_SELECTED_CHOICE = "KEY_SELECTED_CHOICE"
        private const val KEY_GAME_OVER = "KEY_GAME_OVER"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuration des événements de clics
        setupListeners()

        // Restauration de l'état si disponible (rotation d'écran)
        if (savedInstanceState != null) {
            currentQuestionIndex = savedInstanceState.getInt(KEY_INDEX, 0)
            score = savedInstanceState.getInt(KEY_SCORE, 0)
            hasAnswered = savedInstanceState.getBoolean(KEY_HAS_ANSWERED, false)
            selectedChoiceIndex = savedInstanceState.getInt(KEY_SELECTED_CHOICE, -1)
            isGameOver = savedInstanceState.getBoolean(KEY_GAME_OVER, false)
        }

        if (isGameOver) {
            showFinalResult()
        } else {
            displayQuestion(currentQuestionIndex)
            if (hasAnswered && (selectedChoiceIndex != -1)) {
                restoreAnswerState(selectedChoiceIndex)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, currentQuestionIndex)
        outState.putInt(KEY_SCORE, score)
        outState.putBoolean(KEY_HAS_ANSWERED, hasAnswered)
        outState.putInt(KEY_SELECTED_CHOICE, selectedChoiceIndex)
        outState.putBoolean(KEY_GAME_OVER, isGameOver)
    }

    private fun setupListeners() {
        binding.btnChoix1.setOnClickListener { onChoiceSelected(0) }
        binding.btnChoix2.setOnClickListener { onChoiceSelected(1) }
        binding.btnChoix3.setOnClickListener { onChoiceSelected(2) }

        binding.btnNext.setOnClickListener {
            if (currentQuestionIndex < (questions.size - 1)) {
                currentQuestionIndex++
                hasAnswered = false
                selectedChoiceIndex = -1
                displayQuestion(currentQuestionIndex)
            } else {
                showFinalResult()
            }
        }

        binding.btnRejouer.setOnClickListener {
            restartGame()
        }
    }

    private fun displayQuestion(index: Int) {
        val q = questions[index]

        // Mise à jour de l'en-tête, du numéro et du score
        binding.txtNumQuestion.text = getString(R.string.question_number_format, index + 1, questions.size)
        updateScoreAndProgress()

        // Mise à jour de l'image et du texte
        binding.imgQuestion.setImageResource(q.imageResId)
        binding.imgQuestion.contentDescription = q.imageDescription
        binding.txtQuestion.text = q.text

        // Mise à jour des boutons de choix
        val buttons = listOf(binding.btnChoix1, binding.btnChoix2, binding.btnChoix3)
        for (i in buttons.indices) {
            buttons[i].text = q.choices[i]
        }
        setChoiceButtonsEnabled(enabled = true)

        // Cacher le feedback et le bouton suivant
        binding.txtFeedback.visibility = View.INVISIBLE
        binding.btnNext.visibility = View.GONE

        // Visibilité du layout quiz
        binding.layoutQuiz.visibility = View.VISIBLE
        binding.layoutResultat.visibility = View.GONE
    }

    private fun onChoiceSelected(choiceIndex: Int) {
        // Empêcher toute double validation ou attribution multiple de points
        if (hasAnswered) return

        hasAnswered = true
        selectedChoiceIndex = choiceIndex

        val q = questions[currentQuestionIndex]
        val isCorrect = (choiceIndex == q.correctAnswerIndex)

        if (isCorrect) {
            score++
            binding.txtFeedback.text = getString(R.string.feedback_correct)
            binding.txtFeedback.setTextColor(ContextCompat.getColor(this, R.color.correct_green))
        } else {
            val correctAnswerStr = q.choices[q.correctAnswerIndex]
            binding.txtFeedback.text = getString(R.string.feedback_incorrect_format, correctAnswerStr)
            binding.txtFeedback.setTextColor(ContextCompat.getColor(this, R.color.incorrect_red))
        }

        binding.txtFeedback.visibility = View.VISIBLE

        // Désactiver les 3 boutons de choix
        setChoiceButtonsEnabled(enabled = false)

        // Mettre à jour le score et la progression
        updateScoreAndProgress()

        // Mettre à jour le libellé du bouton suivant
        if (currentQuestionIndex < (questions.size - 1)) {
            binding.btnNext.text = getString(R.string.btn_next)
        } else {
            binding.btnNext.text = getString(R.string.btn_view_results)
        }
        binding.btnNext.visibility = View.VISIBLE
    }

    private fun restoreAnswerState(choiceIndex: Int) {
        setChoiceButtonsEnabled(enabled = false)

        val q = questions[currentQuestionIndex]
        val isCorrect = (choiceIndex == q.correctAnswerIndex)

        if (isCorrect) {
            binding.txtFeedback.text = getString(R.string.feedback_correct)
            binding.txtFeedback.setTextColor(ContextCompat.getColor(this, R.color.correct_green))
        } else {
            val correctAnswerStr = q.choices[q.correctAnswerIndex]
            binding.txtFeedback.text = getString(R.string.feedback_incorrect_format, correctAnswerStr)
            binding.txtFeedback.setTextColor(ContextCompat.getColor(this, R.color.incorrect_red))
        }

        binding.txtFeedback.visibility = View.VISIBLE

        if (currentQuestionIndex < (questions.size - 1)) {
            binding.btnNext.text = getString(R.string.btn_next)
        } else {
            binding.btnNext.text = getString(R.string.btn_view_results)
        }
        binding.btnNext.visibility = View.VISIBLE
    }

    private fun setChoiceButtonsEnabled(enabled: Boolean) {
        binding.btnChoix1.isEnabled = enabled
        binding.btnChoix2.isEnabled = enabled
        binding.btnChoix3.isEnabled = enabled
    }

    private fun updateScoreAndProgress() {
        binding.txtScore.text = getString(R.string.score_format, score, questions.size)

        // La progression indique le nombre de questions auxquelles l'utilisateur a répondu
        val answeredCount = if (hasAnswered) currentQuestionIndex + 1 else currentQuestionIndex
        binding.progressQuiz.progress = answeredCount
    }

    private fun showFinalResult() {
        isGameOver = true
        binding.layoutQuiz.visibility = View.GONE
        binding.layoutResultat.visibility = View.VISIBLE

        binding.txtFinalScore.text = getString(R.string.final_score_format, score, questions.size)
        binding.progressQuiz.progress = questions.size
        binding.txtScore.text = getString(R.string.score_format, score, questions.size)
    }

    private fun restartGame() {
        currentQuestionIndex = 0
        score = 0
        hasAnswered = false
        selectedChoiceIndex = -1
        isGameOver = false

        displayQuestion(0)
    }
}
