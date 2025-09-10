# 💖 Méditation bienveillante (Metta)

## Métadonnées
- **ID** : loving‑kindness‑meditation  
- **Nom affiché** : Méditation bienveillante  
- **Catégorie** : Méditation  
- **Durée** : 10 min  
- **Niveau d’anxiété visé** : faible à modéré  
- **Tags** : compassion, bienveillance, stress, émotion  
- **Contre‑indications** : peut faire remonter des émotions intenses ; interrompre si malaise émotionnel  

---

## But & résultats attendus
- **Intention utilisateur** : cultiver la bienveillance envers soi‑même et les autres pour réduire le stress et améliorer son bien‑être émotionnel  
- **Résultats immédiats** : sentiment d’ouverture et de chaleur, diminution des pensées négatives  
- **Avec pratique régulière** : augmentation de l’empathie et de la compassion, diminution du stress et des émotions négatives【822582405626986†L404-L410】  

---

## Contexte psycho‑physio
La méditation bienveillante, ou *Metta*, repose sur la répétition de phrases de souhaits positifs en commençant par soi‑même puis en élargissant progressivement à des proches, des connaissances et toutes les créatures. Les participants sont invités à s’asseoir confortablement, à se concentrer sur la respiration et à choisir une phrase telle que « Que je sois heureux·se, que je sois en sécurité, que je sois en paix ». Puis la même phrase est dirigée vers un ami, un voisin, une personne neutre, une personne difficile et finalement tous les êtres【822582405626986†L348-L372】. Cette pratique favorise des émotions positives et réduit la rumination【822582405626986†L404-L410】.

---

## Quand pratiquer
- En début de journée pour installer un état d’esprit bienveillant  
- Après un conflit ou une journée éprouvante pour apaiser les relations  
- Lorsque l’on ressent de la colère ou de la tristesse envers soi ou un autre  
- Avant de dormir pour cultiver un sentiment de gratitude  

---

## Ce que vous remarquerez
- Sentiment de chaleur au niveau du cœur  
- Apparition d’émotions (joie, tristesse, gratitude)  
- Perception plus douce de soi‑même et des autres  
- Possibles résistances ou inconforts, qui diminueront avec la pratique  

---

## Conseils pratiques
- **Posture** : assis·e sur un coussin ou une chaise, dos droit, mains sur les genoux  
- **Phrase** : choisissez une formulation qui vous parle ; par exemple : « Que je sois heureux·se, que je sois en sécurité, que je sois en paix »  
- **Progression** : commencez par vous adresser la phrase, puis élargissez successivement à : un·e ami·e, un·e voisin·e ou collègue, une personne neutre, une personne avec qui vous avez des difficultés, puis tous les êtres  
- **Visualisation** : imaginez une lumière qui émane de votre cœur et se diffuse vers les autres  

---

## Déroulé de la séance
- **Étapes de bienveillance** :  
  | Étape                   | Durée approx. | Texte UI                                         |  
  |------------------------|--------------|--------------------------------------------------|  
  | Bienveillance pour soi | 2 min        | Répétez la phrase pour vous : « Que je sois heureux » |  
  | Pour un·e ami·e         | 2 min        | Répétez pour un proche : « Qu’il/elle soit heureux·se » |  
  | Pour une connaissance   | 2 min        | Répétez pour une connaissance                    |  
  | Pour une personne difficile | 2 min        | Répétez pour une personne difficile             |  
  | Pour tous les êtres     | 2 min        | Envoyez la phrase à tous les êtres              |  
  
- Répétez chaque phrase intérieurement, en vous concentrant sur la sensation dans le cœur  
- La séance dure environ **10 minutes**  

---

## Interface utilisateur
- **Écran d’intro** : définition de la pratique, suggestions de phrases et bouton *Commencer*  
- **Écran d’exercice** :  
  - Texte affichant la phrase actuelle et la cible (soi, ami, connaissance, etc.)  
  - Visualisation d’une lumière qui s’étend depuis le centre de l’écran  
  - Boutons : Pause / Reprendre / Arrêter  
- **Écran de fin** : message de gratitude et encouragement à pratiquer régulièrement  

---

## Feedback sensoriel
- **Audio** : voix apaisante répétant les phrases ; fond musical doux  
- **Haptique** : léger tapotement au passage d’une étape pour marquer le changement de destinataire  

---

## Localisation (clés)
- `exercise.title` = « Méditation bienveillante »  
- `exercise.step.self` = « Que je sois heureux·se, en sécurité, en paix »  
- `exercise.step.friend` = « Qu’il/elle soit heureux·se, en sécurité, en paix »  
- `exercise.step.acquaintance` = « Qu’il/elle soit heureux·se »  
- `exercise.step.difficult` = « Qu’il/elle soit heureux·se »  
- `exercise.step.all` = « Que tous les êtres soient heureux »  
- `exercise.completed.title` = « Séance terminée ! »  

---

## États & transitions
- **SELECTION → STARTED → (PAUSED ↔ STARTED) → COMPLETED**  

---

## Télémétrie (non PII)
- `exercise_started{id:loving‑kindness‑meditation}`  
- `step_completed{name:self,duration_ms:120000}`  
- … (répété pour chaque étape)  
- `session_completed{duration_ms:600000}`  

---

## Tests d’acceptation
- La phrase affichée change bien selon la cible (soi, ami, etc.)  
- La visualisation lumineuse suit l’expansion suggérée  
- Pause/Reprise ne provoque pas de saut d’étape  
- Les textes sont lisibles et inclusifs (utilisation du point médian) sur 360×640 dp  