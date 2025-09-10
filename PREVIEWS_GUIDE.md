# Guide d'utilisation des aperçus @Preview dans Releaf

Ce guide explique comment utiliser les aperçus Compose dans Android Studio pour développer et tester l'interface de l'application Releaf.

## 📋 Ce qui a été implémenté

### ✅ Écrans avec aperçus
- **HomeScreen** - 5 aperçus différents
- **LoginScreen** - 6 aperçus avec différents états
- **BrowseScreen** - 6 aperçus pour les différentes tailles d'écran
- **TechniqueCard** - 4 aperçus pour les différents composants

### ✅ Annotations personnalisées
- `@ReleafPreviews` - Mode clair/sombre
- `@ReleafDevicePreviews` - Différentes tailles d'écran
- `@ReleafFontPreviews` - Différentes tailles de police
- `@ReleafLocalePreviews` - Différentes langues
- `@ReleafCompletePreviews` - Combinaison complète

## 🚀 Comment utiliser les aperçus

### 1. Ouvrir les aperçus dans Android Studio

1. **Ouvrez un fichier avec aperçus** (ex: `HomeScreen.kt`)
2. **Basculez vers l'onglet "Design"** en haut à droite de l'éditeur
3. **Sélectionnez "Split"** pour voir le code et les aperçus simultanément

### 2. Navigation entre les aperçus

- **Mode Focus** : Concentrez-vous sur un seul aperçu pour économiser les ressources
- **Mode Grille** : Affichez plusieurs aperçus simultanément 
- **Mode Liste** : Affichez les aperçus en liste verticale

### 3. Interactivité des aperçus

#### Mode interactif
1. **Cliquez sur l'icône "Démarrer le mode interactif"** sur un aperçu
2. **Interagissez** avec les composants comme sur un vrai appareil
3. **Testez** les animations, clics, et saisies

#### Exécution sur appareil
1. **Cliquez sur l'icône "Exécuter l'aperçu"** 
2. **Sélectionnez votre émulateur/appareil**
3. **L'aperçu s'exécute** dans une nouvelle activité

## 🔧 Aperçus disponibles par écran

### HomeScreen (`/ui/screens/HomeScreen.kt`)
- `HomeScreenPreview` - Vue normale
- `HomeScreenLightDarkPreview` - Modes clair/sombre
- `HomeScreenDifferentSizesPreview` - Différentes tailles d'écran
- `HomeScreenLandscapePreview` - Mode paysage
- `HomeScreenLargeFontPreview` - Police agrandie

### LoginScreen (`/ui/screens/auth/LoginScreen.kt`)
- `LoginScreenPreview` - État normal
- `LoginScreenLoadingPreview` - État de chargement
- `LoginScreenWithErrorPreview` - Avec message d'erreur
- `LoginScreenLightDarkPreview` - Modes clair/sombre
- `LoginScreenSizesPreview` - Différentes tailles
- `LoginScreenLandscapePreview` - Mode paysage

### BrowseScreen (`/ui/screens/BrowseScreen.kt`)
- `BrowseScreenPreview` - Vue normale
- `BrowseScreenGridViewPreview` - Vue grille
- `BrowseScreenLightDarkPreview` - Modes clair/sombre
- `BrowseScreenSizesPreview` - Différentes tailles
- `BrowseScreenLandscapePreview` - Mode paysage
- `BrowseScreenLargeFontPreview` - Police agrandie

### TechniqueCard (`/ui/components/TechniqueCard.kt`)
- `TechniqueCardPreview` - Carte simple
- `TechniqueCardLongTitlePreview` - Avec titre long
- `TechniqueCardGridPreview` - Grille de cartes
- `TechniqueCardDifferentSizesPreview` - Différentes tailles

## 🎨 Personnalisation des aperçus

### Créer de nouveaux aperçus

```kotlin
@Preview(
    name = "Mon aperçu personnalisé",
    showBackground = true,
    widthDp = 320,
    heightDp = 480,
    fontScale = 1.2f
)
@Composable
fun MonAperçuPersonnalisé() {
    MyApplicationTheme {
        MonComposable()
    }
}
```

### Utiliser les annotations personnalisées

```kotlin
@ReleafPreviews  // Modes clair/sombre
@Composable
fun MonAperçu() {
    MyApplicationTheme {
        MonComposable()
    }
}
```

### Paramètres d'aperçu disponibles

- `name` - Nom de l'aperçu
- `showBackground` - Afficher l'arrière-plan
- `widthDp`, `heightDp` - Dimensions
- `fontScale` - Échelle de la police (0.5f à 2.0f)
- `locale` - Langue ("fr-FR", "en-US", etc.)
- `uiMode` - Mode sombre avec `Configuration.UI_MODE_NIGHT_YES`
- `device` - Appareil spécifique ("id:pixel_4")

## 💡 Bonnes pratiques

### ✅ À faire
- **Utilisez `MyApplicationTheme`** dans tous les aperçus
- **Testez différents états** (chargement, erreur, vide)
- **Vérifiez les modes clair/sombre**
- **Testez avec de gros textes** (accessibilité)
- **Utilisez le mode interactif** pour les animations

### ❌ À éviter
- **Ne pas oublier le thème** - Toujours wrapper avec `MyApplicationTheme`
- **Éviter les données réelles** - Utilisez des données d'exemple
- **Ne pas avoir trop d'aperçus** - Limite pour les performances
- **Éviter les appels réseau** - Les aperçus n'ont pas accès au réseau

## 🏗 Architecture des aperçus

### Fichiers créés/modifiés
```
app/src/main/java/com/example/myapplication/
├── ui/theme/PreviewAnnotations.kt          # Annotations personnalisées
├── ui/screens/HomeScreen.kt                # ✅ Aperçus ajoutés
├── ui/screens/auth/LoginScreen.kt          # ✅ Aperçus ajoutés  
├── ui/screens/BrowseScreen.kt              # ✅ Aperçus ajoutés
└── ui/components/TechniqueCard.kt          # ✅ Aperçus ajoutés
```

### Annotations personnalisées créées
- `@ReleafPreviews` - Light/Dark
- `@ReleafDevicePreviews` - Tailles d'écran
- `@ReleafFontPreviews` - Échelles de police
- `@ReleafLocalePreviews` - Langues
- `@ReleafCompletePreviews` - Tout combiné

## 🚀 Prochaines étapes

### Écrans à ajouter
- `TechniqueDetailScreen` - Page de détail des techniques
- `ExerciseScreen` - Écrans d'exercices
- `ProfileScreen` - Profil utilisateur
- Écrans de respiration spécialisés

### Composants à ajouter
- `BreathingAnimation` - Animations de respiration
- `SearchBar` - Barre de recherche
- `CategoryChip` - Puces de catégorie
- `BottomNavigation` - Navigation inférieure

## 📱 Avantages des aperçus

1. **Développement plus rapide** - Pas besoin d'émulateur
2. **Tests visuels instantanés** - Voir les changements en temps réel
3. **Tests multi-appareils** - Différentes tailles simultanément
4. **Économie de ressources** - Moins de mémoire que l'émulateur
5. **Tests d'accessibilité** - Police agrandie, contrastes
6. **Itération rapide** - Modifier et voir immédiatement

---

🎉 **Les aperçus sont maintenant configurés et prêts à utiliser !**

Pour toute question ou amélioration, consultez la documentation officielle Android : 
https://developer.android.com/develop/ui/compose/tooling/previews