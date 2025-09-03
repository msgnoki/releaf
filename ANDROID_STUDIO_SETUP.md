# Configuration Android Studio pour Brythee

## Problème résolu : "testClasses" n'existe pas

Le projet compile parfaitement avec Gradle, mais Android Studio cherche une tâche `testClasses` obsolète.

## Solutions de contournement

### 1. Utiliser les tâches Gradle directement

Dans le terminal d'Android Studio :
```bash
# Compilation debug
./gradlew assembleDebug

# Tests unitaires 
./gradlew test

# Installation sur appareil
./gradlew installDebug

# Nettoyage
./gradlew clean
```

### 2. Corriger la Run Configuration

1. **Run** → **Edit Configurations...**
2. Sélectionner votre configuration d'app
3. Dans **Gradle project**, vérifier que c'est `:app`
4. Dans **Tasks**, remplacer `testClasses` par `assembleDebug`
5. Cliquer **OK**

### 3. Synchronisation du projet

1. **File** → **Sync Project with Gradle Files**
2. **Build** → **Clean Project**
3. **Build** → **Rebuild Project**

## Tâches Gradle disponibles

- `assembleDebug` : Compilation de l'APK debug
- `test` : Exécution des tests unitaires
- `testDebugUnitTest` : Tests debug uniquement
- `installDebug` : Installation sur appareil
- `connectedAndroidTest` : Tests instrumentés

## Statut du projet

✅ **Compilation** : Fonctionne parfaitement  
✅ **Tests** : Passent avec succès  
✅ **APK** : Se génère correctement  
⚠️ **Android Studio** : Configuration à ajuster

## Nouvelles fonctionnalités v0.2

- ✅ Bottom Navigation (4 onglets)
- ✅ Design système pastel
- ✅ Écrans Browse, Sleep, Profile
- ✅ Services de recommandations
- ✅ Composants UI modernes