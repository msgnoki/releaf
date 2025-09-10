# 🌬️ Respiration 4-7-8 (Souffle Relaxant)

## Métadonnées
- **ID** : breathing-478-relaxing
- **Nom affiché** : Respiration 4-7-8
- **Catégorie** : Respiration
- **Durée** : 2-3min (4-6 cycles), 5-10min (8-15 cycles), 15-20min (sessions étendues)
- **Niveau d'anxiété visé** : modéré à élevé
- **Tags** : stress, anxiety, sleep, beginner-intermediate
- **Contre-indications** : grossesse, maladie cardiovasculaire sévère, troubles respiratoires

---

## But & résultats attendus
- **Intention utilisateur** : réduction rapide du stress et de l'anxiété, préparation au sommeil
- **Résultats immédiats** : activation du système parasympathique, ralentissement cardiaque, apaisement mental
- **Avec pratique régulière** : amélioration qualité sommeil, réduction tension artérielle, résilience stress accrue

---

## Contexte psycho-physio
Pattern respiratoire structuré basé sur les techniques pranayama adaptées par le Dr Andrew Weil. **Active le système parasympathique** via stimulation vagale, réduit cortisol et activité sympathique, augmente variabilité cardiaque, améliore sensibilité baroréflexe.

---

## Quand pratiquer
- Avant le coucher pour faciliter l'endormissement
- Pendant une montée d'anxiété ou de stress
- Avant une présentation ou un événement stressant
- Lors de réveils nocturnes pour se rendormir

---

## Ce que vous remarquerez
- Sensation de lourdeur et de détente progressives
- Ralentissement du rythme cardiaque
- Esprit qui se calme et pensées qui ralentissent
- Facilitation de l'endormissement

---

## Conseils pratiques
- **Posture** :
  - Assis : dos droit mais détendu, pieds au sol
  - Allongé : position confortable, bras relâchés
- **Environnement** : espace calme, lumière tamisée, vêtements confortables
- **Régularité** : 2 fois par jour, idéalement matin et soir

---

## Déroulé de la séance
- **Pattern respiratoire** :
  | Phase | Durée | Action | Texte UI |
  |-------|-------|--------|----------|
  | Préparation | - | Placer langue derrière dents supérieures | Placez votre langue derrière vos dents supérieures |
  | Expiration initiale | - | Expirer complètement par bouche avec "whoosh" | Expirez complètement par la bouche |
  | Inspiration | 4 temps | Inspirer par nez silencieusement | Inspirez par le nez (1...2...3...4) |
  | Rétention | 7 temps | Retenir souffle sans effort | Retenez votre souffle (1...2...3...4...5...6...7) |
  | Expiration | 8 temps | Expirer par bouche avec "whoosh" | Expirez par la bouche (1...2...3...4...5...6...7...8) |

- **Cycles** : 4-8 cycles pour débutants, jusqu'à 15 cycles avec l'expérience
- **Progression** : commencer par 4 cycles, augmenter graduellement

---

## Interface utilisateur
- **Écran d'intro** : titre, durée estimée, bénéfices principaux, contre-indications, bouton *Commencer*
- **Écran d'exercice** :
  - Cercle animé expansion/contraction synchronisé
  - Compteur visuel des temps (4-7-8)
  - Indicateur de phase (Inspirez/Retenez/Expirez)
  - Compteur de cycles effectués
  - Boutons Pause/Reprendre/Recommencer/Arrêter
- **Écran de fin** : message de félicitations, ressenti post-exercice, suggestion prochaine session

---

## Feedback sensoriel
- **Audio** : guidance vocale optionnelle "Inspirez...retenez...expirez" avec timing précis
- **Visuel** : cercle respiratoire avec changements de couleur par phase
- **Haptique** : vibrations douces au changement de phase
- **Biofeedback** : monitoring cardiaque optionnel si disponible

---

## Localisation (clés)
- `exercise.title` = "Respiration 4-7-8"
- `exercise.phase.prepare` = "Placez votre langue derrière vos dents supérieures"
- `exercise.phase.exhale_initial` = "Expirez complètement par la bouche"
- `exercise.phase.inhale` = "Inspirez par le nez en comptant jusqu'à 4"
- `exercise.phase.hold` = "Retenez votre souffle en comptant jusqu'à 7"
- `exercise.phase.exhale` = "Expirez par la bouche en comptant jusqu'à 8"
- `exercise.completed.title` = "Exercice terminé !"

---

## États & transitions
- **SETUP → STARTED → (PAUSED ↔ STARTED) → COMPLETED**
- **Transitions spéciales** : possibilité d'arrêt anticipé si inconfort

---

## Télémétrie (non PII)
- `exercise_started{id:breathing-478-relaxing, duration_selected:300}`
- `phase_changed{phase:inhale|hold|exhale, cycle_number:n}`
- `session_paused{timestamp, cycle_number}`
- `session_completed{total_cycles:n, duration_ms:300000}`
- `early_exit{reason:discomfort|interruption, cycle_number:n}`

---

## Tests d'acceptation
- Le timing 4-7-8 est précis à ±0.1 seconde
- L'animation du cercle est fluide et synchronisée
- Les vibrations sont douces et non perturbantes
- Pause/Reprise maintient le cycle en cours
- L'audio de guidance est clair et apaisant
- Interface lisible sur écrans 360×640dp minimum