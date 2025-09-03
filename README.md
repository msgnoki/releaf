# Calm - Application Android

Application Android portée depuis la version web Nuxt.js pour la gestion du stress et de l'anxiété.

## 🌟 Fonctionnalités

### Techniques de Relaxation (9 techniques disponibles)
- **Respiration 2 Minutes** - Exercice de respiration simple avec animation interactive
- **Ancrage 5-4-3-2-1** - Technique sensorielle pour revenir au moment présent
- **Respiration Guidée** - Modèles de respiration structurés
- **Relaxation Musculaire Progressive** - Exercices de tension/détente musculaire
- **Visualisation Paisible** - Imagerie mentale guidée
- **Étiquetage des Pensées** - Technique de pleine conscience
- **Bulles Anti-Stress** - Activité interactive ludique
- **Thérapie Sonore** - Fréquences de relaxation
- **Balle Anti-Stress** - Exercice tactile virtuel

### Interface Utilisateur
- **Design Material 3** - Interface moderne et accessible
- **Navigation intuitive** - Navigation fluide entre les écrans
- **Animations interactives** - Animation de respiration en temps réel
- **Cartes techniques** - Vue d'ensemble claire avec tags et durées
- **Écrans d'exercices** - Interface dédiée pour chaque technique

## 🏗️ Architecture

### Technologies utilisées
- **Kotlin** - Langage principal
- **Jetpack Compose** - Interface utilisateur moderne
- **Navigation Component** - Gestion de la navigation
- **Material 3** - Design system Google
- **Coroutines** - Programmation asynchrone
- **Kotlinx Serialization** - Sérialisation des données

### Structure du projet
```
app/src/main/java/com/example/myapplication/
├── data/
│   └── Technique.kt              # Modèles de données et repository
├── ui/
│   ├── components/
│   │   ├── TechniqueCard.kt      # Carte d'affichage des techniques
│   │   └── BreathingAnimation.kt # Animation interactive de respiration
│   ├── screens/
│   │   ├── HomeScreen.kt         # Écran principal avec grille
│   │   ├── TechniqueDetailScreen.kt # Détails d'une technique
│   │   ├── ExerciseScreen.kt     # Écran d'exercice générique
│   │   └── BreathingExerciseScreen.kt # Écran respiration spécialisé
│   ├── navigation/
│   │   └── CalmNavigation.kt     # Configuration de la navigation
│   └── theme/                    # Thème Material 3
└── MainActivity.kt               # Activité principale
```

## 🎨 Fonctionnalités Techniques

### Système de Tags
- **high-anxiety** / **moderate-anxiety** - Niveau d'anxiété ciblé
- **short-time** / **medium-time** / **long-time** - Durée de l'exercice

### Animation de Respiration
- Animation fluide en temps réel
- Phases de respiration guidées (inspiration, rétention, expiration)
- Indicateurs visuels du rythme
- Compteur de cycles

### Navigation
- Navigation par type d'écran (Home → Détail → Exercice)
- Gestion du back stack
- Techniques similaires suggérées

## 🚀 Installation et Compilation

### Prérequis
- Android Studio Koala (2024.1.1) ou plus récent
- JDK 11 ou plus récent
- SDK Android 24+ (minimum)
- SDK Android 36+ (cible)

### Compilation
```bash
# Cloner et ouvrir dans Android Studio
cd /home/msgnoki/AndroidStudioProjects/MyApplication

# Compiler le projet
./gradlew assembleDebug

# Installer sur un émulateur/appareil
./gradlew installDebug
```

## 📱 Guide d'utilisation

### Écran Principal
1. **Grille des techniques** - 9 techniques organisées en grille 2x2
2. **Cartes techniques** - Nom, durée, icône et tags pour chaque technique
3. **Navigation intuitive** - Tap sur une carte pour voir les détails

### Écran de Détail
1. **Informations complètes** - Description détaillée de la technique
2. **Tags et durée** - Classification claire
3. **Bouton d'exercice** - Lancement direct de la session
4. **Techniques similaires** - Suggestions basées sur les tags

### Écran d'Exercice
1. **Timer interactif** - Compte à rebours avec progression
2. **Instructions dynamiques** - Guide selon la technique choisie
3. **Contrôles** - Play/Pause et Reset
4. **Animation (respiration)** - Visualisation du rythme respiratoire

## 🔄 Portage depuis la version Web

### Équivalences techniques
| Web (Nuxt.js/Vue.js) | Android (Compose) |
|----------------------|-------------------|
| `pages/*.vue` | `ui/screens/*Screen.kt` |
| `components/*.vue` | `ui/components/*.kt` |
| `composables/useTechniques.js` | `data/TechniquesRepository` |
| `i18n/locales/` | `res/values/strings.xml` |
| Vue Router | Navigation Compose |
| Tailwind CSS | Material 3 Theme |

### Fonctionnalités adaptées
- **PWA → App native** - Interface adaptée aux standards Android
- **Animations CSS → Compose** - Animations Material Design
- **Internationalisation** - Système Android strings.xml
- **Navigation** - Navigation Component avec back stack
- **État local** - Compose State et LaunchedEffect

## 🎯 Points d'Extension

### Nouvelles Techniques
1. Ajouter une nouvelle technique dans `TechniquesRepository`
2. Créer l'écran d'exercice spécialisé si nécessaire
3. Mettre à jour la navigation si besoin

### Internationalisation
1. Créer `res/values-en/strings.xml` pour l'anglais
2. Adapter les descriptions dans `TechniquesRepository`
3. Utiliser `stringResource()` dans les Composables

### Persistance
1. Ajouter Room Database pour l'historique
2. Implémenter les préférences utilisateur
3. Statistiques d'utilisation des techniques

## 📋 État du Portage

✅ **Complété**
- Structure de données des 9 techniques
- Interface utilisateur complète (Home, Détail, Exercice)
- Navigation entre écrans
- Animation interactive de respiration
- Design Material 3 cohérent
- Gestion des états et timers

🔄 **En cours / Améliorations possibles**
- Compilation (nécessite configuration JDK)
- Exercices spécialisés pour toutes les techniques
- Sons et vibrations pour certains exercices
- Sauvegarde des sessions et statistiques
- Tests unitaires et d'intégration

L'application Android reproduit fidèlement l'expérience de la version web avec des améliorations spécifiques à Android (animations natives, Material Design, navigation mobile optimisée).