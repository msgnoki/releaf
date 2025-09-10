# 💪 Relaxation musculaire progressive

## Métadonnées
- **ID** : progressive‑muscle‑relaxation  
- **Nom affiché** : Relaxation musculaire progressive  
- **Catégorie** : Relaxation corporelle  
- **Durée** : 10 min  
- **Niveau d’anxiété visé** : modéré à élevé  
- **Tags** : stress, tension, sommeil, décontraction  
- **Contre‑indications** : éviter en cas de blessure musculaire ou d’inflammation importante ; arrêter en cas de douleur  

---

## But & résultats attendus
- **Intention utilisateur** : relâcher les tensions physiques et apaiser l’esprit en alternant contraction et relaxation des groupes musculaires  
- **Résultats immédiats** : sensation de détente corporelle, diminution de l’agitation mentale  
- **Avec pratique régulière** : meilleure perception corporelle, réduction des douleurs liées au stress, sommeil de meilleure qualité  

---

## Contexte psycho‑physio
La relaxation musculaire progressive consiste à contracter volontairement un groupe musculaire pendant environ 5 secondes puis à relâcher complètement pendant 10 à 20 secondes. En suivant un ordre précis du bas vers le haut du corps, elle permet de prendre conscience des tensions et de les relâcher【719466411385979†L243-L266】. Cette pratique améliore la sensation de relaxation générale et réduit l’anxiété【719466411385979†L299-L304】. Pour les débutants, il est recommandé de pratiquer dans un endroit calme, avec des vêtements confortables et de ne pas retenir sa respiration【719466411385979†L273-L281】.

---

## Quand pratiquer
- Avant de se coucher pour préparer le sommeil  
- Après une journée stressante ou un effort physique  
- Lors de migraines de tension ou de crispations musculaires  
- À tout moment où une détente corporelle est nécessaire  

---

## Ce que vous remarquerez
- Sensation de lourdeur agréable dans les membres  
- Réduction des tensions musculaires  
- Respiration plus profonde et régulière  
- Amélioration du bien‑être et de la concentration  

---

## Conseils pratiques
- **Posture** : allongé·e sur le dos ou assis·e confortablement, les bras le long du corps  
- **Environnement** : pièce calme, température agréable, lumière douce  
- **Respiration** : respirez lentement et régulièrement ; ne retenez pas votre souffle pendant la contraction  
- **Progression** : si 5 secondes sont trop longues, commencez par 3 s de contraction puis augmentez  

---

## Déroulé de la séance
- **Groupes musculaires et timing** :  
  | Groupe             | Phase contraction | Phase détente | Texte UI                          |  
  |-------------------|-------------------|--------------|-----------------------------------|  
  | Pieds             | 5 s              | 15 s         | Contractez les pieds              |  
  | Mollets           | 5 s              | 15 s         | Contractez les mollets            |  
  | Cuisses           | 5 s              | 15 s         | Contractez les cuisses            |  
  | Mains             | 5 s              | 15 s         | Serrez les poings                 |  
  | Bras              | 5 s              | 15 s         | Contractez les bras               |  
  | Épaules           | 5 s              | 15 s         | Levez les épaules                 |  
  | Cou et nuque      | 5 s              | 15 s         | Contractez la nuque               |  
  | Visage            | 5 s              | 15 s         | Plissez le visage                 |  
  
- Faites chaque groupe l’un après l’autre en suivant l’ordre indiqué  
- Entre chaque contraction, concentrez‑vous sur la sensation de relâchement  
- La séance complète dure environ **10 minutes**  

---

## Interface utilisateur
- **Écran d’intro** : description de la technique, conseils de posture et bouton *Commencer*  
- **Écran d’exercice** :  
  - Liste animée des groupes musculaires ; le groupe actif est mis en surbrillance  
  - Compteur indiquant le temps de contraction et de détente  
  - Boutons : Pause / Reprendre / Passer le groupe / Arrêter  
- **Écran de fin** : message de félicitations et rappel des bénéfices  

---

## Feedback sensoriel
- **Audio** : narration douce pour annoncer chaque groupe musculaire et guider la respiration  
- **Haptique** : vibration longue pour signaler le début de la contraction, vibration courte pour la détente  

---

## Localisation (clés)
- `exercise.title` = « Relaxation musculaire progressive »  
- `exercise.group.feet` = « Contractez les pieds »  
- `exercise.group.calves` = « Contractez les mollets »  
- `exercise.group.thighs` = « Contractez les cuisses »  
- `exercise.group.hands` = « Serrez les poings »  
- `exercise.group.arms` = « Contractez les bras »  
- `exercise.group.shoulders` = « Levez les épaules »  
- `exercise.group.neck` = « Contractez la nuque »  
- `exercise.group.face` = « Plissez le visage »  
- `exercise.completed.title` = « Séance terminée ! »  

---

## États & transitions
- **SELECTION → STARTED → (PAUSED ↔ STARTED) → COMPLETED**  

---

## Télémétrie (non PII)
- `exercise_started{id:progressive‑muscle‑relaxation}`  
- `group_started{name:pieds,duration_ms:5000}`  
- `group_relaxed{name:pieds,duration_ms:15000}`  
- … (répété pour chaque groupe)  
- `session_completed{duration_ms:600000}`  

---

## Tests d’acceptation
- Les durées de contraction/détente respectent ±200 ms de précision  
- L’application affiche en surbrillance le bon groupe musculaire à chaque étape  
- Pause/Reprise reprend exactement au début du temps restant  
- Les textes restent lisibles et accessibles en modes clair et sombre sur 360×640 dp  