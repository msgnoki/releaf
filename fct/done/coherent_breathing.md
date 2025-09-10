# 🫶 Respiration cohérente (coherent breathing)

## Métadonnées
- **ID** : coherent‑breathing  
- **Nom affiché** : Respiration cohérente  
- **Catégorie** : Respiration  
- **Durée** : 7 min  
- **Niveau d’anxiété visé** : faible à modéré  
- **Tags** : cohérence cardiaque, respiration lente, relaxation, stress  
- **Contre‑indications** : interrompre en cas d’étourdissements ; adapter la durée pour les personnes souffrant d’asthme ou de problèmes respiratoires  

---

## But & résultats attendus
- **Intention utilisateur** : réguler le système nerveux autonome en respirant à un rythme constant d’environ cinq respirations par minute  
- **Résultats immédiats** : sensations de calme et d’équilibre, diminution du stress  
- **Avec pratique régulière** : amélioration de la variabilité de la fréquence cardiaque, régulation des émotions et meilleure gestion du stress  

---

## Contexte psycho‑physio
La respiration cohérente se caractérise par des inhalations et expirations de même durée, généralement de quatre à six secondes, pour atteindre environ cinq respirations par minute. Cette méthode invite d’abord à compter le rythme naturel de la respiration, puis à inspirer pendant quatre secondes et expirer pendant quatre secondes, avant d’allonger à cinq puis six secondes【730425392005983†L277-L303】. Ce rythme lent et régulier calme le système nerveux et contribue à réduire l’anxiété et le stress.

---

## Quand pratiquer
- En début de journée pour installer un rythme calme  
- Avant une réunion stressante ou un examen  
- Pendant une pause pour se recentrer  
- En fin de journée pour éliminer les tensions  

---

## Ce que vous remarquerez
- Respiration profonde et régulière  
- Baisse progressive de la fréquence cardiaque  
- Sensation d’équilibre et de détente  
- Possibles légers étourdissements si le rythme est trop long au départ  

---

## Conseils pratiques
- **Position** : assis·e, dos droit, pieds au sol ; une main sur le ventre pour sentir les mouvements  
- **Progression** : commencez par des cycles de 4 s et augmentez à 5 s puis 6 s si confortable【730425392005983†L277-L303】  
- **Attention** : concentrez‑vous sur une respiration diaphragmatique ; laissez le ventre se soulever à l’inspiration et retomber à l’expiration  
- **Régularité** : pratiquez 2 fois par jour pour en ressentir les effets durables  

---

## Déroulé de la séance
- **Phases de respiration** :  
  | Phase          | Durée approx. | Durée par cycle | Texte UI                        |  
  |---------------|--------------|-----------------|---------------------------------|  
  | Évaluation     | 1 min        | -               | Comptez votre rythme naturel     |  
  | 4 s / 4 s      | 2 min        | 4 s in / 4 s out | Inspirez 4 s, expirez 4 s        |  
  | 5 s / 5 s      | 2 min        | 5 s in / 5 s out | Inspirez 5 s, expirez 5 s        |  
  | 6 s / 6 s      | 2 min        | 6 s in / 6 s out | Inspirez 6 s, expirez 6 s        |  
  
- Placez une main sur votre ventre pour vérifier que la respiration est abdominale  
- La séance dure environ **7 minutes**  

---

## Interface utilisateur
- **Écran d’intro** : présentation de la cohérence cardiaque, conseils de posture et bouton *Commencer*  
- **Écran d’exercice** :  
  - Animation (bulle ou sphère) qui monte et descend au rythme de la respiration  
  - Indicateur de la durée de chaque cycle (4 s, 5 s, 6 s)  
  - Boutons : Pause / Reprendre / Arrêter  
- **Écran de fin** : message indiquant les bénéfices et encouragement à pratiquer régulièrement  

---

## Feedback sensoriel
- **Audio** : ton doux ou musique de fond qui marque les inspirations et expirations ; adaptation du tempo selon la durée  
- **Haptique** : vibration douce au début de chaque inspiration  

---

## Localisation (clés)
- `exercise.title` = « Respiration cohérente »  
- `exercise.phase.evaluate` = « Comptez votre rythme naturel »  
- `exercise.phase.four` = « Inspirez 4 s, expirez 4 s »  
- `exercise.phase.five` = « Inspirez 5 s, expirez 5 s »  
- `exercise.phase.six` = « Inspirez 6 s, expirez 6 s »  
- `exercise.completed.title` = « Séance terminée ! »  

---

## États & transitions
- **SELECTION → STARTED → (PAUSED ↔ STARTED) → COMPLETED**  

---

## Télémétrie (non PII)
- `exercise_started{id:coherent‑breathing}`  
- `phase_changed{name:4s,duration_ms:120000}`  
- `phase_changed{name:5s,duration_ms:120000}`  
- `phase_changed{name:6s,duration_ms:120000}`  
- `session_completed{duration_ms:420000}`  

---

## Tests d’acceptation
- L’animation respire au rythme 4 s → 5 s → 6 s sans à-coups  
- La progression s’affiche clairement et la voix s’adapte à la durée  
- Pause/Reprise reprend précisément la durée restante dans la phase en cours  
- Les textes sont lisibles et clairs sur 360×640 dp  