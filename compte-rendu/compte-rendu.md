# Compte Rendu du TP - Application Android Quiz en Images

**Projet** : Quiz Android en Images  
**Langage** : Kotlin  
**Architecture** : Single Activity (`MainActivity`) avec ViewBinding et Layout XML  
**Thème choisi** : Formes Géométriques (Triangle, Carré, Cercle, Étoile, Rectangle)  

---

## 1. Présentation de l'interface et des widgets utilisés

L'interface est définie dans un fichier unique `activity_main.xml` sans utiliser de Fragments ni Jetpack Compose.
Elle utilise les widgets Android suivants :

- **`MaterialCardView`** : En-tête fixe coloré contenant le titre et le score, offrant une lisibilité optimale.
- **`TextView` (`txt_score`)** : Affiche le score en temps réel sous le format `"Score : X/5"`.
- **`ProgressBar` (`progress_quiz`)** : Barre horizontale configurée de 0 à 5 indiquant le nombre de questions auxquelles l'utilisateur a répondu.
- **`TextView` (`txt_num_question`)** : Affiche l'intitulé du numéro de question (ex: `"Question 1 sur 5"`).
- **`ImageView` (`img_question`)** : Affiche l'image associée à la question sans déformation avec `scaleType="fitCenter"`.
- **`TextView` (`txt_question`)** : Intitulé de la question courante.
- **`Button` (`btn_choix1`, `btn_choix2`, `btn_choix3`)** : Les 3 choix de réponse cliquables.
- **`TextView` (`txt_feedback`)** : Zone de message indiquant si la réponse est bonne (texte vert) ou mauvaise (texte rouge avec rappel de la bonne réponse).
- **`Button` (`btn_next`)** : Bouton permettant de passer à la question suivante (`"Question suivante"`) ou d'afficher le résultat final (`"Voir le résultat"`).
- **`LinearLayout` (`layout_resultat`)** : Zone de résultat final affichée à la fin du quiz contenant le score final et le bouton `btn_rejouer`.
- **`ScrollView`** : Conteneur scrollable garantissant l'accessibilité sur les écrans de petite taille.

---

## 2. Structure des données et tableau de questions

Les questions sont modélisées via la data class `Question` ([Question.kt](../app/src/main/java/com/example/quizz/Question.kt)) :

```kotlin
data class Question(
    val text: String,
    @param:DrawableRes val imageResId: Int,
    val choices: List<String>,
    val correctAnswerIndex: Int,
    val imageDescription: String
)
```

Un tableau statique de 5 questions est défini dans `MainActivity.kt` :
1. **Triangle** (`R.drawable.ic_triangle`) : Choix `["Carré", "Triangle", "Cercle"]`, Bonne réponse : index 1.
2. **Carré** (`R.drawable.ic_square`) : Choix `["Rectangle", "Carré", "Étoile"]`, Bonne réponse : index 1.
3. **Cercle** (`R.drawable.ic_circle`) : Choix `["Cercle", "Triangle", "Carré"]`, Bonne réponse : index 0.
4. **Étoile** (`R.drawable.ic_star`) : Choix `["Triangle", "Rectangle", "Étoile"]`, Bonne réponse : index 2.
5. **Rectangle** (`R.drawable.ic_rectangle`) : Choix `["Rectangle", "Carré", "Cercle"]`, Bonne réponse : index 0.

---

## 3. Réponses aux questions explicites de l'énoncé

### Q1. Comment représentez-vous une question et sa bonne réponse ?
Une question est représentée par une `data class Question` contenant l'intitulé (`text`), l'identifiant de ressource drawable (`imageResId`), la liste des 3 choix (`choices`) et l'indice entier `correctAnswerIndex` (0, 1 ou 2) qui désigne la réponse exacte dans la liste des choix.

### Q2. Comment le clic sur un bouton permet-il de savoir quel choix a été sélectionné ?
Chaque bouton possède un écouteur de clic (`setOnClickListener`) rattaché dans la méthode `setupListeners()`. L'écouteur transmet un indice fixe (0 pour `btn_choix1`, 1 pour `btn_choix2` et 2 pour `btn_choix3`) à la fonction centrale `onChoiceSelected(choiceIndex)`.

### Q3. Comment empêchez-vous l'attribution de plusieurs points pour une question ?
L'attribution multiple est bloquée grâce à :
1. Une variable booléenne d'état `hasAnswered`. Dès le premier clic, la fonction `onChoiceSelected` vérifie `if (hasAnswered) return` puis bascule immédiatement `hasAnswered = true`.
2. Désactivation immédiate des 3 boutons de choix via `setChoiceButtonsEnabled(false)` (`isEnabled = false`).

### Q4. Quelle différence faites-vous entre le score et la progression ?
- **Score** : Représente uniquement le nombre de **bonnes réponses** données (de 0 à 5). Il n'augmente que si le choix sélectionné est correct.
- **Progression** : Représente le nombre total de **questions auxquelles l'utilisateur a répondu** (de 0 à 5). La barre de progression avance d'une unité à chaque réponse donnée, que la réponse soit juste ou fausse.

### Q5. Comment détectez-vous la dernière question et affichez-vous le résultat ?
La détection de la dernière question s'effectue en comparant l'indice courant `currentQuestionIndex` à `questions.size - 1` (soit 4) :
- Lorsque la dernière question est validée, le libellé du bouton d'action devient `"Voir le résultat"`.
- Lors du clic sur ce bouton, la fonction `showFinalResult()` masque le conteneur du quiz (`layout_quiz.visibility = View.GONE`) et affiche le conteneur du résultat final (`layout_resultat.visibility = View.VISIBLE`) avec le score obtenu et le bouton `"Rejouer"`.

---

## 4. Tests réalisés et résultats

Tous les scénarios de test obligatoires définis dans l'énoncé ont été validés :

| Scénario de test | Procédure | Résultat attendu | Statut |
| :--- | :--- | :--- | :--- |
| **Nouvelle partie** | Lancement de l'application | Q1 affichée, 3 choix, score `0/5`, progression `0/5` | **SUCCÈS** |
| **Bonne réponse** | Clic sur la bonne réponse | Score `+1`, progression `+1`, feedback vert | **SUCCÈS** |
| **Mauvaise réponse** | Clic sur une mauvaise réponse | Score inchangé, progression `+1`, feedback rouge | **SUCCÈS** |
| **Clics répétés** | Clics multiples sur les choix | Boutons désactivés, aucun point supplémentaire | **SUCCÈS** |
| **Navigation** | Clic sur "Question suivante" | Passage à Q+1, choix réactivés, score conservé | **SUCCÈS** |
| **Score parfait** | 5 bonnes réponses | Score final `5/5`, progression `5/5` | **SUCCÈS** |
| **Score nul** | 5 mauvaises réponses | Score final `0/5`, progression `5/5` | **SUCCÈS** |
| **Rejouer** | Clic sur "Rejouer" à la fin | Retour à Q1, score et progression remis à `0/5` | **SUCCÈS** |
| **Rotation d'écran** | Tourner l'appareil pendant le quiz | Question, score et état conservés sans réinitialisation | **SUCCÈS** |
