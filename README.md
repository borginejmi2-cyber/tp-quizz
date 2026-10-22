# Quiz Android en Images - Formes Géométriques

Ce projet est une application Android développée en Kotlin dans le cadre du **TP Android Quiz en Images**.
L'application propose un jeu de quiz de 5 questions illustrées sur le thème des formes géométriques.

## 📱 Présentation de l'application
L'utilisateur observe une image représentant une forme géométrique (triangle, carré, cercle, étoile, rectangle) et choisit la réponse parmi trois boutons.
- **Score** : +1 point pour chaque bonne réponse (visible en haut à droite).
- **Progression** : Une `ProgressBar` horizontale indique le nombre de questions traitées (de 0 à 5).
- **Interaction** : Désactivation immédiate des boutons après réponse pour empêcher la double validation.
- **Écran final** : Présentation du score final et bouton **Rejouer** pour réinitialiser le jeu.
- **Rotation d'écran** : Conservation de l'état grâce à `onSaveInstanceState`.

---

## 🛠️ Configuration et Prérequis
- **Android Studio** : Version récente (Ladybug / 2024.2.1+)
- **SDK Android minimum** : API 24 (Android 7.0)
- **SDK Target** : API 37
- **Langage** : Kotlin 2.x
- **UI Framework** : Android XML Views (avec ViewBinding enabled, sans Jetpack Compose ni Fragments)

---

## 🚀 Comment ouvrir et exécuter le projet
1. Ouvrez **Android Studio**.
2. Sélectionnez **Open** et choisissez le dossier du projet `Quizz`.
3. Laissez Gradle synchroniser le projet (`Sync Project with Gradle Files`).
4. Lancez l'application sur un émulateur ou appareil Android via le bouton **Run** (`Shift + F10`).

---

## 📁 Structure du projet
```text
Quizz/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/quizz/
│   │   │   ├── Question.kt           # Modèle de données de question
│   │   │   └── MainActivity.kt       # Logique applicative et gestion des événements
│   │   └── res/
│   │       ├── drawable/             # Vector Drawables XML (ic_triangle, ic_square, etc.)
│   │       ├── layout/activity_main.xml # Layout XML principal
│   │       └── values/               # strings.xml, colors.xml
├── captures/                         # Captures d'écran de démonstration
├── compte-rendu/                     # Compte rendu rédigé du TP (compte-rendu.md)
└── README.md                         # Guide du projet
```

---

## 📋 Documentation et Rendu
- **Compte Rendu** : Consulter [`compte-rendu/compte-rendu.md`](compte-rendu/compte-rendu.md) pour le rapport détaillé et les réponses aux questions de l'énoncé.
- **Captures d'écran** : Les images illustrant les scénarios de test sont situées dans le dossier [`captures/`](captures/).
