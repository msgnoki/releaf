# 🌲 Immersion Forêt Basée Nature

## Métadonnées
- **ID** : forest-immersion-nature
- **Nom affiché** : Immersion Forêt
- **Catégorie** : Visualisation Guidée
- **Durée** : 2-3min (reset rapide), 8min (standard), 15min (immersion profonde)
- **Niveau d'anxiété visé** : léger à élevé
- **Tags** : nature, biophilia, stress-relief, immersive, restoration
- **Contre-indications** : phobies liées à la nature (très rare)

---

## But & résultats attendus
- **Intention utilisateur** : évasion mentale, reconnexion avec nature, réduction stress urbain
- **Résultats immédiats** : sensation d'évasion, relaxation profonde, clarté mentale, apaisement
- **Avec pratique régulière** : réduction stress chronique, amélioration humeur, connexion nature renforcée

---

## Contexte psycho-physio
**Simulation exposition nature multisensorielle** utilisant capacités smartphone pour immersion complète. Active système parasympathique via réponse biophilie, réduit cortisol (-23% études), améliore activité ondes alpha, diminue ruminations. Effet thérapeutique prouvé équivalent à 20min marche en forêt.

---

## Quand pratiquer
- Lors de stress urbain intense ou sentiment d'enfermement
- En pause déjeuner pour reset mental
- Après journées difficiles pour récupération
- Quand besoin de perspective et clarté mentale

---

## Ce que vous remarquerez
- Sensation immédiate d'évasion et d'ouverture
- Respiration qui se régularise naturellement
- Diminution tension physique et mentale
- Sentiment de paix et de connexion

---

## Conseils pratiques
- **Posture** :
  - Assis confortablement ou allongé
  - Casque audio recommandé pour immersion sonore
  - Yeux fermés ou mi-clos selon préférence
- **Environnement** : espace calme, interruptions minimisées
- **Ouverture** : laisser imagination créer détails personnels

---

## Déroulé de la séance
- **Progression immersive** :

| Phase | Durée | Élément sensoriel | Description guidée |
|-------|-------|-------------------|-------------------|
| Arrivée | 1-2min | Sons forêt lointains | "Vous marchez vers une forêt, entendez les premiers bruissements..." |
| Entrée | 1-2min | Visuels verts, ombres dansantes | "Vous pénétrez sous la canopée, lumière filtrée..." |
| Sentier | 2-3min | Pas sur sol forestier, oiseaux | "Vos pieds foulent la mousse et les feuilles..." |
| Clairière | 2-4min | Lumière dorée, calme profond | "Vous découvrez une clairière baignée de soleil..." |
| Ruisseau | 2-3min | Eau qui coule, fraîcheur | "Un ruisseau cristallin coule doucement..." |
| Repos | 3-5min | Synthèse sensorielle | "Vous vous installez confortablement..." |
| Retour | 1min | Transition douce | "Vous gardez cette paix en revenant..." |

- **Éléments sensoriels intégrés** :
  - **Visuel** : transitions couleur écran (vert forêt, lumière dorée)
  - **Audio** : sons 3D spatialisés (vent, oiseaux, ruisseau)
  - **Haptique** : vibrations douces simulant brise, pas
  - **Guidage** : narration voix chaleureuse et apaisante

---

## Interface utilisateur
- **Écran d'intro** : aperçu forêt, choix durée/intensité, préparation casque
- **Écran d'immersion** :
  - Fond visuel évolutif (couleurs nature, lumières)
  - Contrôles discrets (pause/volume) masqués
  - Timer de progression subtil
  - Option sous-titres pour narration
- **Écran de transition** : retour graduel, intégration expérience

---

## Feedback sensoriel
- **Audio principal** :
  - Narration guidée voix professionnelle apaisante
  - Paysage sonore 3D : vent dans feuilles, chants oiseaux, ruisseau
  - Masquage ambiance urbaine
- **Visuels immersifs** :
  - Transitions couleur écran (vert forêt, brun terre, bleu ciel)
  - Jeux lumière/ombre subtils
  - Animations naturelles douces
- **Haptiques** :
  - Pulsations douces simulant brise
  - Patterns irréguliers imitant nature
  - Synchronisation avec respiration

---

## Localisation (clés)
- `exercise.title` = "Immersion Forêt"
- `exercise.intro.setup` = "Installez-vous confortablement et fermez les yeux"
- `exercise.scene.arrival` = "Vous vous approchez d'une forêt luxuriante..."
- `exercise.scene.clearing` = "Une clairière paisible s'ouvre devant vous..."
- `exercise.scene.stream` = "Un ruisseau cristallin coule doucement..."
- `exercise.transition.return` = "Gardez cette sensation de paix en revenant..."

---

## États & transitions
- **SETUP → ARRIVAL → ENTRY → PATH → CLEARING → STREAM → REST → RETURN → INTEGRATION**
- **Adaptabilité** : possibilité raccourcir/étendre phases selon durée choisie

---

## Télémétrie (non PII)
- `exercise_started{id:forest-immersion, duration_selected:480s, audio_quality:3d}`
- `scene_transition{from:entry, to:path, timestamp}`
- `immersion_rating{scene:clearing, engagement:high}`
- `session_completed{phases_completed:7, user_rating:peaceful}`
- `interruption_handled{scene:stream, resume_successful:true}`

---

## Tests d'acceptation
- Audio 3D immersif sans distorsion
- Transitions visuelles fluides et apaisantes
- Synchronisation parfaite audio-visuel-haptique
- Contrôles accessibles sans briser immersion
- Narration tempo adapté à relaxation
- Qualité sonore optimale casque/haut-parleurs
- Expérience cohérente sur durées différentes