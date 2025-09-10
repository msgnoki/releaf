# 🌬️ Respiration consciente (mindful breathing)

## Métadonnées
- **ID** : mindful‑breathing  
- **Nom affiché** : Respiration consciente  
- **Catégorie** : Méditation  
- **Durée** : 6 min  
- **Niveau d’anxiété visé** : faible à modéré  
- **Tags** : pleine conscience, respiration, concentration, apaisement  
- **Contre‑indications** : prudence en cas de trouble anxieux sévère ou de traumatisme lié à la respiration ; arrêter si malaise  

---

## But & résultats attendus
- **Intention utilisateur** : entraîner l’attention à se poser sur la respiration afin de développer la présence et de réduire les ruminations  
- **Résultats immédiats** : respiration plus profonde, calme intérieur, diminution des pensées stressantes  
- **Avec pratique régulière** : amélioration de la concentration, meilleure gestion des émotions et ancrage dans l’instant présent  

---

## Contexte psycho‑physio
La respiration consciente invite à observer le souffle tel qu’il est sans chercher à le modifier : sensations de l’air qui entre et qui sort, mouvements du ventre et de la poitrine. Un script de la Veterans Administration décrit comment se mettre dans une position confortable, laisser flotter les pensées comme des feuilles sur un ruisseau et ramener l’attention vers le souffle à chaque distraction【29910524358215†L6-L43】. En notant les différences entre l’inspiration et l’expiration et en observant les sensations corporelles, on apprend à ancrer son esprit dans le présent【29910524358215†L45-L70】.

---

## Quand pratiquer
- Au réveil pour démarrer la journée en conscience  
- Pendant une pause au travail pour réduire le stress  
- Avant une réunion ou un entretien pour se recentrer  
- En fin de journée pour décompresser  

---

## Ce que vous remarquerez
- Sensation de calme et d’ancrage  
- Pensées moins envahissantes  
- Perception claire des mouvements du corps liés à la respiration  
- Parfois des pensées intrusives qui reviennent ; cela fait partie de la pratique  

---

## Conseils pratiques
- **Posture** : assis·e sur une chaise ou un coussin, dos droit mais détendu, mains posées sur les cuisses  
- **Environnement** : endroit calme ; éteindre les notifications  
- **Attitude** : accueillir sans jugement chaque pensée ou sensation ; revenir au souffle avec bienveillance  
- **Durée** : commencez par 5 minutes et prolongez selon vos besoins  

---

## Déroulé de la séance
- **Étapes de présence** :  
  | Étape                    | Durée approx. | Texte UI                           |  
  |-------------------------|--------------|------------------------------------|  
  | Installation            | 1 min        | Installez‑vous confortablement     |  
  | Observation du souffle  | 4 min        | Observez chaque inspiration/expiration |  
  | Retour et ouverture     | 1 min        | Étendez votre attention au corps   |  
  
- Laissez les pensées passer comme des nuages, revenez à votre respiration  
- La séance dure environ **6 minutes**  

---

## Interface utilisateur
- **Écran d’intro** : explication de la respiration consciente, conseils de posture et bouton *Commencer*  
- **Écran d’exercice** :  
  - Animation subtile (par exemple, un cercle qui se dilate et se contracte au rythme de la respiration)  
  - Texte qui rappelle de ramener l’attention au souffle  
  - Boutons : Pause / Reprendre / Arrêter  
- **Écran de fin** : message invitant à poursuivre l’observation quelques instants avant de reprendre ses activités  

---

## Feedback sensoriel
- **Audio** : cloche douce au début et à la fin ; option de voix guidant l’observation  
- **Haptique** : pas de vibration pour favoriser une attention stable  

---

## Localisation (clés)
- `exercise.title` = « Respiration consciente »  
- `exercise.step.setup` = « Installez‑vous confortablement »  
- `exercise.step.observe` = « Observez votre souffle »  
- `exercise.step.finish` = « Étendez votre attention au corps »  
- `exercise.completed.title` = « Séance terminée ! »  

---

## États & transitions
- **SELECTION → STARTED → (PAUSED ↔ STARTED) → COMPLETED**  

---

## Télémétrie (non PII)
- `exercise_started{id:mindful‑breathing}`  
- `step_changed{name:observe,duration_ms:240000}`  
- `session_completed{duration_ms:360000}`  

---

## Tests d’acceptation
- L’animation est fluide et synchronisée avec le chrono  
- La cloche retentit au début et à la fin sans déranger l’utilisateur  
- Pause/Reprise fonctionne sans perte de progression  
- Les textes restent lisibles en français/anglais sur un écran 360×640 dp  