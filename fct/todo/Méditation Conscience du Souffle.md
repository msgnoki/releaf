# 🧘 Méditation Conscience du Souffle

## Métadonnées
- **ID** : meditation-breath-awareness
- **Nom affiché** : Méditation Conscience du Souffle
- **Catégorie** : Méditation et Pleine Conscience
- **Durée** : 2-3min (débutant), 5-10min (intermédiaire), 15-20min (avancé)
- **Niveau d'anxiété visé** : léger à modéré
- **Tags** : mindfulness, focus, attention, beginner-friendly, daily-practice
- **Contre-indications** : aucune majeure, adapter si claustrophobie

---

## But & résultats attendus
- **Intention utilisateur** : développer l'attention et la présence, calmer l'esprit agité
- **Résultats immédiats** : esprit plus calme, attention focalisée, réduction bavardage mental
- **Avec pratique régulière** : concentration accrue, régulation émotionnelle, résilience stress, développement mindfulness

---

## Contexte psycho-physio
**Entraînement attention focalisée** utilisant respiration naturelle comme ancre conscience moment présent. Active système parasympathique, réduit cortisol, augmente variabilité cardiaque, renforce réseaux attention préfrontaux. Développe capacité métacognitive d'observation sans jugement.

---

## Quand pratiquer
- Le matin pour commencer la journée centré
- En pause déjeuner pour reset mental
- Avant situations stressantes pour se recentrer
- Le soir pour transition vers détente

---

## Ce que vous remarquerez
- Esprit qui divague naturellement au début
- Moments de calme et de clarté
- Respiration qui se régularise naturellement
- Sensation de présence accrue au fil du temps

---

## Conseils pratiques
- **Posture** :
  - Assis : dos droit mais détendu, mains sur cuisses
  - Pieds au sol, épaules relâchées
  - Éviter positions qui endorment
- **Environnement** : espace calme, téléphone silencieux, position confortable
- **Attitude** : bienveillance envers soi, pas de performance à atteindre

---

## Déroulé de la séance
- **Phases de la méditation** :

| Phase | Durée | Action | Instructions |
|-------|-------|--------|-------------|
| Installation | 1min | Se positionner | Trouvez une position confortable, fermez les yeux |
| Centrage | 30s | 3 respirations profondes | Prenez trois respirations profondes pour vous ancrer |
| Observation | Variable | Attention au souffle naturel | Portez attention aux sensations de la respiration |
| Retour attention | Continu | Ramener l'attention si divagation | Quand l'esprit divague, revenez doucement au souffle |
| Intégration | 30s | Transition | Prenez un moment pour intégrer cette expérience |

- **Points d'attention** :
  - Sensations aux narines (air qui entre/sort)
  - Mouvement du ventre qui se soulève/s'abaisse
  - Température de l'air (frais à l'inspiration, chaud à l'expiration)
  - Pause naturelle entre inspiration et expiration

---

## Interface utilisateur
- **Écran d'intro** : explication concept, choix durée, conseils posture
- **Écran de méditation** :
  - Fond apaisant (couleurs douces, nature)
  - Timer discret en périphérie
  - Rappels textuels subtils si option activée
  - Indicateur respiration optionnel (cercle léger)
  - Sons nature optionnels (rivière, vent, silence)
- **Écran de fin** : moment de transition douce, feedback sur expérience

---

## Feedback sensoriel
- **Audio** : 
  - Guidance initiale douce pour installation
  - Rappels occasionnels "revenez au souffle" si option activée
  - Sons nature très discrets en arrière-plan
  - Cloche douce de fin
- **Visuel** : 
  - Animations subtiles non distrayantes
  - Couleurs apaisantes, transitions douces
- **Haptique** : vibration très légère pour fin de session

---

## Localisation (clés)
- `exercise.title` = "Méditation Conscience du Souffle"
- `exercise.instruction.setup` = "Installez-vous confortablement et fermez les yeux"
- `exercise.instruction.breathing` = "Portez attention aux sensations de votre respiration naturelle"
- `exercise.instruction.wandering` = "Si votre esprit divague, revenez doucement au souffle"
- `exercise.reminder.gentle` = "Revenez au souffle"
- `exercise.completed.title` = "Méditation terminée"

---

## États & transitions
- **SETUP → CENTERING → MEDITATION → (WANDERING → RETURN)* → INTEGRATION → COMPLETED**
- **Gestion divagation** : pas d'interruption, rappels très doux si demandés

---

## Télémétrie (non PII)
- `exercise_started{id:meditation-breath-awareness, duration_selected:600}`
- `reminder_shown{type:gentle_return, timestamp}`
- `session_completed{duration_actual:605s, interruptions:0}`
- `feedback_given{experience_rating:peaceful|restless|focused}`

---

## Tests d'acceptation
- Interface non distrayante, couleurs apaisantes
- Timer précis mais discret
- Transitions douces entre phases
- Sons optionnels ajustables en volume
- Rappels subtils non intrusifs
- Guidance initiale claire pour débutants
- Pas de stimulation excessive ou distrayante