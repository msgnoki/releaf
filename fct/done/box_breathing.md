# 🟦 Respiration carrée (box breathing)

## Métadonnées
- **ID** : breathing‑box  
- **Nom affiché** : Respiration carrée  
- **Catégorie** : Respiration  
- **Durée** : 4 min  
- **Niveau d’anxiété visé** : modéré à élevé  
- **Tags** : stress, concentration, anxiété, respiration  
- **Contre‑indications** : ne pas prolonger les pauses en cas de grossesse ou de problèmes cardio‑respiratoires ; interrompre si malaise  

---

## But & résultats attendus
- **Intention utilisateur** : retrouver rapidement un état d’équilibre en synchronisant l’inspiration, la pause, l’expiration et la pause suivante  
- **Résultats immédiats** : ralentissement de la respiration et du rythme cardiaque, regain de clarté mentale  
- **Avec pratique régulière** : meilleure gestion des émotions et du stress, amélioration de la concentration  

---

## Contexte psycho‑physio
La respiration carrée — ou *box breathing* — consiste à inspirer, retenir son souffle, expirer et retenir de nouveau, chaque phase durant le même laps de temps. Les protocoles recommandent des cycles de quatre secondes par phase【693028334085208†L3-L12】. Une respiration profonde et régulière active la réponse de relaxation du corps, ralentit le rythme cardiaque et abaisse la tension artérielle【693028334085208†L24-L27】. Cette technique est utilisée pour gérer les crises de panique et retrouver son calme【158490261124790†L364-L376】.

---

## Quand pratiquer
- Avant une présentation importante pour calmer les nerfs  
- Pendant une crise d’angoisse ou une montée de panique  
- En milieu de journée pour retrouver concentration et clarté  
- Après une altercation afin de réguler les émotions  

---

## Ce que vous remarquerez
- Rythme respiratoire régulier  
- Pensées plus claires et sensations d’ancrage  
- Diminution progressive du stress  
- Possibles sensations de chaleur ou de légers étourdissements au début  

---

## Conseils pratiques
- **Posture** :  
  - Assis·e : dos droit, épaules relâchées, pieds bien ancrés  
  - Debout : pieds écartés largeur des hanches, genoux légèrement fléchis  
- **Environnement** : endroit calme de préférence ; utiliser un casque si nécessaire  
- **Régularité** : pratiquer une à deux fois par jour pour intégrer le rythme  
- **Adaptation** : si 4 secondes semblent longues, commencez avec 3 s et augmentez progressivement【158490261124790†L420-L432】  

---

## Déroulé de la séance
- **Pattern respiratoire** :  
  | Phase   | Durée approx. | Texte UI           |  
  |---------|---------------|--------------------|  
  | inhale  | 4 s           | Inspirez 4 s       |  
  | hold1   | 4 s           | Pause 4 s          |  
  | exhale  | 4 s           | Expirez 4 s        |  
  | hold2   | 4 s           | Pause 4 s          |  
  
- Répétez ce carré respiratoire pendant **4 minutes**  
- Ajustez la durée selon votre confort  

---

## Interface utilisateur
- **Écran d’intro** : titre, explication du concept de carré, durée et bouton *Commencer*  
- **Écran d’exercice** :  
  - Animation représentant un carré ; le contour se colore progressivement sur chaque côté pour symboliser les phases  
  - Compteur du temps écoulé et du temps restant  
  - Contrôles : Pause / Reprendre / Recommencer / Arrêter  
- **Écran de fin** : message de félicitations et possibilité de réviser le carré respiratoire en cas de besoin  

---

## Feedback sensoriel
- **Audio** : léger son ou voix pour compter chaque seconde ; silence pendant les pauses  
- **Haptique** : vibration courte au début de chaque phase  

---

## Localisation (clés)
- `exercise.title` = « Respiration carrée »  
- `exercise.phase.inhale` = « Inspirez »  
- `exercise.phase.hold1` = « Retenez »  
- `exercise.phase.exhale` = « Expirez »  
- `exercise.phase.hold2` = « Retenez »  
- `exercise.completed.title` = « Exercice terminé ! »  

---

## États & transitions
- **SELECTION → STARTED → (PAUSED ↔ STARTED) → COMPLETED**  

---

## Télémétrie (non PII)
- `exercise_started{id:breathing‑box}`  
- `phase_changed{phase:inhale,duration_ms:4000}`  
- `phase_changed{phase:hold1,duration_ms:4000}`  
- `phase_changed{phase:exhale,duration_ms:4000}`  
- `phase_changed{phase:hold2,duration_ms:4000}`  
- `session_completed{duration_ms:240000}`  

---

## Tests d’acceptation
- La progression visuelle sur le carré est synchronisée avec le temps (±200 ms)  
- Pause/Reprise reprend la phase en cours sans décalage  
- La durée totale d’environ 4 min est respectée  
- Les textes s’affichent lisiblement en français et en anglais sur un écran 360×640 dp  