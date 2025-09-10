# 💪 Relaxation Musculaire Progressive (PMR Jacobson)

## Métadonnées
- **ID** : pmr-jacobson-complete
- **Nom affiché** : Relaxation Musculaire Progressive
- **Catégorie** : Relaxation Musculaire
- **Durée** : 15-20min (complète), 10-12min (abrégée), 5min (express)
- **Niveau d'anxiété visé** : léger à élevé
- **Tags** : tension, stress, sleep, muscle-relief, anxiety
- **Contre-indications** : blessures musculaires récentes, tensions extrêmes sans avis médical

---

## But & résultats attendus
- **Intention utilisateur** : libération des tensions musculaires, réduction du stress physique
- **Résultats immédiats** : détente musculaire profonde, diminution tensions, relaxation générale
- **Avec pratique régulière** : amélioration conscience corporelle, réduction douleurs chroniques, qualité sommeil

---

## Contexte psycho-physio
Technique développée par Edmund Jacobson utilisant **cycles tension-relâchement systématiques**. Active le système parasympathique via contractions volontaires suivies de relâchement, réduit cortisol, améliore conscience proprioceptive, diminue activité musculaire résiduelle.

---

## Quand pratiquer
- En fin de journée pour évacuer les tensions accumulées
- Avant le coucher pour faciliter l'endormissement
- Lors de douleurs musculaires liées au stress
- En période de forte charge de travail

---

## Ce que vous remarquerez
- Sensation de lourdeur et de chaleur dans les muscles
- Contraste marqué entre tension et relâchement
- Détente progressive du corps entier
- Esprit plus calme et apaisé

---

## Conseils pratiques
- **Posture** :
  - Allongé : position confortable, bras le long du corps
  - Assis : dos soutenu, pieds au sol, mains sur cuisses
- **Environnement** : espace calme, température confortable, vêtements amples
- **Régularité** : quotidienne idéalement, au moins 3-4 fois par semaine

---

## Déroulé de la séance
- **Progression systématique** (version complète 16 groupes) :

| Ordre | Groupe musculaire | Tension (5-7s) | Relâchement (15-20s) |
|-------|-------------------|----------------|----------------------|
| 1 | Pied droit | Serrer orteils, pointer pied | Laisser retomber naturellement |
| 2 | Mollet droit | Fléchir pied vers soi | Détendre complètement |
| 3 | Cuisse droite | Contracter quadriceps | Relâcher totalement |
| 4 | Pied gauche | Serrer orteils, pointer pied | Laisser retomber naturellement |
| 5 | Mollet gauche | Fléchir pied vers soi | Détendre complètement |
| 6 | Cuisse gauche | Contracter quadriceps | Relâcher totalement |
| 7 | Fessiers | Serrer les fesses | Laisser s'affaisser |
| 8 | Abdomen | Rentrer le ventre | Laisser se détendre |
| 9 | Main droite | Fermer poing serré | Ouvrir et laisser pendre |
| 10 | Avant-bras droit | Fléchir poignet | Détendre complètement |
| 11 | Bras droit | Contracter biceps | Relâcher le long du corps |
| 12 | Main gauche | Fermer poing serré | Ouvrir et laisser pendre |
| 13 | Avant-bras gauche | Fléchir poignet | Détendre complètement |
| 14 | Bras gauche | Contracter biceps | Relâcher le long du corps |
| 15 | Épaules/cou | Hausser épaules vers oreilles | Laisser retomber |
| 16 | Visage | Froncer sourcils, serrer mâchoire | Détendre traits du visage |

- **Version abrégée** (4 groupes) :
  1. Membres inférieurs ensemble (30s)
  2. Torse et abdomen (30s)
  3. Bras et mains ensemble (30s)
  4. Cou, épaules et visage (30s)

---

## Interface utilisateur
- **Écran d'intro** : choix version (complète/abrégée/express), démonstration mouvements
- **Écran d'exercice** :
  - Diagramme corporel avec zone active surlignée
  - Instructions claires texte + audio
  - Timer phase tension/relâchement
  - Progression globale (groupe X/16)
  - Boutons Pause/Reprendre/Recommencer
- **Écran de fin** : évaluation niveau détente, encouragements, planification prochaine session

---

## Feedback sensoriel
- **Audio** : instructions guidées avec voix calme et rassurante
- **Visuel** : diagramme anatomique avec zones en cours
- **Haptique** : vibration douce début/fin phases
- **Timing** : sons doux transition entre groupes musculaires

---

## Localisation (clés)
- `exercise.title` = "Relaxation Musculaire Progressive"
- `exercise.instruction.prepare` = "Installez-vous confortablement"
- `exercise.instruction.tense` = "Contractez {muscle_group} pendant {duration} secondes"
- `exercise.instruction.release` = "Relâchez et détendez-vous pendant {duration} secondes"
- `exercise.muscle_groups.right_foot` = "pied droit"
- `exercise.muscle_groups.face` = "muscles du visage"
- `exercise.completed.title` = "Relaxation terminée !"

---

## États & transitions
- **INTRO → PREPARATION → GROUP_TENSION → GROUP_RELEASE → NEXT_GROUP → COMPLETION**
- **Transitions spéciales** : retour arrière possible, ajustement intensité

---

## Télémétrie (non PII)
- `exercise_started{id:pmr-jacobson, version:complete|abbreviated|express}`
- `muscle_group_completed{group:right_foot, tension_duration:7s, release_duration:20s}`
- `session_paused{current_group:5, timestamp}`
- `session_completed{total_duration:1200s, groups_completed:16}`
- `intensity_adjusted{group:shoulders, new_level:moderate}`

---

## Tests d'acceptation
- Timer précis pour phases tension/relâchement (±0.5s)
- Diagramme corporel met en évidence la zone active
- Audio synchronisé avec les phases visuelles
- Possibilité de répéter un groupe si nécessaire
- Instructions claires pour chaque groupe musculaire
- Adaptation automatique si session interrompue