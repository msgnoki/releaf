# 👣 Technique d’ancrage 5‑4‑3‑2‑1

## Métadonnées
- **ID** : grounding‑54321  
- **Nom affiché** : Ancrage 5‑4‑3‑2‑1  
- **Catégorie** : Pleine conscience  
- **Durée** : 5 min  
- **Niveau d’anxiété visé** : modéré à élevé  
- **Tags** : ancrage, crise de panique, anxiété, sensorialité  
- **Contre‑indications** : adapter pour les personnes ayant un handicap sensoriel (vue, ouïe, odorat, goût) ; interrompre si la recherche d’éléments accentue l’anxiété  

---

## But & résultats attendus
- **Intention utilisateur** : ramener son attention dans le moment présent en sollicitant les cinq sens afin de diminuer l’intensité d’une crise d’angoisse  
- **Résultats immédiats** : réduction de la dissociation et du sentiment de panique, respiration plus régulière  
- **Avec pratique régulière** : meilleure capacité à se recentrer rapidement lors de situations stressantes  

---

## Contexte psycho‑physio
L’outil d’ancrage 5‑4‑3‑2‑1 consiste à lister mentalement ou à voix haute cinq choses que vous entendez, quatre choses que vous voyez, trois choses que vous pouvez toucher, deux choses que vous pouvez sentir et une chose que vous pouvez goûter ou imaginer【556157660991350†L532-L544】. Cette séquence sollicite les différentes modalités sensorielles, ce qui détourne l’attention de l’angoisse et facilite le retour au présent.

---

## Quand pratiquer
- En plein milieu d’une crise de panique ou d’une attaque de panique  
- Après un cauchemar pour se reconnecter à la réalité  
- Lors d’un voyage en transport si l’anxiété augmente  
- Avant un examen ou un entretien pour réduire la nervosité  

---

## Ce que vous remarquerez
- Stabilisation de la respiration et du rythme cardiaque  
- Attention portée sur l’environnement immédiat plutôt que sur les pensées anxieuses  
- Augmentation de la conscience sensorielle  
- Diminution progressive des sensations d’irréalité ou de panique  

---

## Conseils pratiques
- **Position** : asseyez‑vous ou tenez‑vous debout, pieds bien ancrés au sol  
- **Progression** : prenez le temps de nommer chaque élément ; si un sens est difficile à mobiliser, compensez avec un autre (par exemple imaginer une odeur)  
- **Adaptation** : pour des enfants ou des personnes malentendantes/malvoyantes, substituez le sens concerné par une sensation corporelle (température, position)  
- **Répétition** : pratiquer régulièrement afin d’automatiser la séquence et l’utiliser rapidement lors des crises  

---

## Déroulé de la séance
- **Séquence sensorielle** :  
  | Étape | Nombre d’éléments | Sens sollicité | Texte UI                                 |  
  |-------|------------------|----------------|------------------------------------------|  
  | 1     | 5                | Ouïe          | Citez 5 sons que vous entendez           |  
  | 2     | 4                | Vue           | Citez 4 objets que vous voyez            |  
  | 3     | 3                | Toucher       | Touchez 3 textures autour de vous        |  
  | 4     | 2                | Odorat        | Décrivez 2 odeurs présentes              |  
  | 5     | 1                | Goût          | Identifiez 1 goût (ou imaginez‑en un)    |  
  
- Respectez l’ordre proposé ; il crée un rythme rassurant  
- La séance dure environ **5 minutes** selon la vitesse à laquelle vous identifiez les éléments  

---

## Interface utilisateur
- **Écran d’intro** : explication de la méthode et recommandations d’utilisation, bouton *Commencer*  
- **Écran d’exercice** :  
  - Indicateur d’étape avec un cercle ou une barre de progression  
  - Texte invitant à nommer les éléments ; possibilité de saisir des mots ou simplement de cliquer sur “Suivant”  
  - Boutons : Passer l’étape, Pause/Reprendre, Arrêter  
- **Écran de fin** : message de retour à soi et de félicitations pour l’ancrage  

---

## Feedback sensoriel
- **Audio** : voix rassurante guidant l’utilisateur dans la séquence ; option de sons ambiants neutres  
- **Haptique** : vibration douce pour signaler le passage à l’étape suivante  

---

## Localisation (clés)
- `exercise.title` = « Ancrage 5‑4‑3‑2‑1 »  
- `exercise.step.hear` = « Citez cinq sons »  
- `exercise.step.see` = « Citez quatre choses »  
- `exercise.step.touch` = « Touchez trois textures »  
- `exercise.step.smell` = « Décrivez deux odeurs »  
- `exercise.step.taste` = « Identifiez un goût »  
- `exercise.completed.title` = « Exercice terminé ! »  

---

## États & transitions
- **SELECTION → STARTED → (PAUSED ↔ STARTED) → COMPLETED**  

---

## Télémétrie (non PII)
- `exercise_started{id:grounding‑54321}`  
- `step_completed{name:hear,count:5}`  
- `step_completed{name:see,count:4}`  
- `step_completed{name:touch,count:3}`  
- `step_completed{name:smell,count:2}`  
- `step_completed{name:taste,count:1}`  
- `session_completed{duration_ms:300000}`  

---

## Tests d’acceptation
- Les transitions d’étape sont fluides et accompagnées d’un signal audio/haptique  
- La possibilité de passer une étape fonctionne sans bloquer la progression  
- L’exercice est accessible en mode hors ligne et fonctionne en 360×640 dp  
- Les textes en français/anglais restent lisibles et ne sont pas tronqués  