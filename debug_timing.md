# Instructions pour vérifier les paramètres Android

## Vérifications à faire sur votre appareil/émulateur :

### 1. Options développeur (le plus probable)
- Aller dans **Paramètres > Options pour les développeurs**
- Vérifier ces paramètres :
  - **Échelle d'animation des fenêtres** : doit être à 1x (pas 0.5x ou "Désactivé")
  - **Échelle d'animation des transitions** : doit être à 1x  
  - **Échelle de durée d'animation** : doit être à 1x
  - **Forcer l'accélération matérielle GPU** : doit être DÉSACTIVÉ
  - **Simulation d'affichage de format plus petit** : doit être DÉSACTIVÉ

### 2. Modes d'économie d'énergie
- **Économiseur de batterie** : DÉSACTIVÉ pendant les tests
- **Mode performance** : sur "Équilibré" ou "Performance"

### 3. Paramètres d'accessibilité
- **Supprimer les animations** : DÉSACTIVÉ
- **Durée des animations** : Normal

### 4. Si c'est un émulateur
- **Performances de l'émulateur** : Mode graphique à "Hardware GLES 2.0"
- **RAM allouée** : Au moins 2GB
- **Vitesse d'émulation** : "As fast as possible" peut causer des problèmes de timing

## Test simple pour confirmer le diagnostic :
Créez un chronomètre simple avec votre téléphone et lancez l'exercice de respiration. Si l'écart est significatif, c'est bien un problème de configuration Android.
