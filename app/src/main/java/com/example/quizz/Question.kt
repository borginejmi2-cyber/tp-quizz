package com.example.quizz

import androidx.annotation.DrawableRes

/**
 * Modèle de données modélisant une question du quiz.
 *
 * @property text Intitulé de la question
 * @property imageResId Identifiant de ressource drawable (ex: R.drawable.ic_triangle)
 * @property choices Liste exacte de 3 choix de réponse
 * @property correctAnswerIndex Indice (0, 1 ou 2) correspondant au choix correct dans [choices]
 * @property imageDescription Description textuelle pour l'accessibilité
 */
data class Question(
    val text: String,
    @param:DrawableRes val imageResId: Int,
    val choices: List<String>,
    val correctAnswerIndex: Int,
    val imageDescription: String
)
