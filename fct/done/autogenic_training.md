# 🌡️ Training autogène

## Métadonnées
- **ID** : autogenic‑training  
- **Nom affiché** : Training autogène  
- **Catégorie** : Auto‑hypnose  
- **Durée** : 12 min  
- **Niveau d’anxiété visé** : modéré à élevé  
- **Tags** : relaxation profonde, auto‑suggestion, stress, respiration  
- **Contre‑indications** : déconseillé en cas de troubles dissociatifs graves ou de problèmes cardiaques ; pratiquer sous supervision lors des premières séances  

---

## But & résultats attendus
- **Intention utilisateur** : induire un état de détente profonde en utilisant des suggestions verbales pour créer des sensations de lourdeur et de chaleur  
- **Résultats immédiats** : ralentissement du rythme cardiaque, respiration plus calme, sentiment de réconfort  
- **Avec pratique régulière** : meilleure gestion du stress, diminution des douleurs psychosomatiques, amélioration de la concentration  

---

## Contexte psycho‑physio
Le training autogène est une méthode de relaxation fondée sur des autosuggestions. Elle invite à se placer dans un endroit calme, à respirer lentement puis à répéter mentalement des formules telles que « Mon bras droit est lourd » ou « Mon cœur bat calmement ». Une session type débute par l’auto‑affirmation « Je suis complètement calme » puis se poursuit par des sensations de lourdeur et de chaleur dans chaque membre, et se termine par la conscience du rythme cardiaque【251420385434896†L252-L269】. Cette technique vise à activer le système parasympathique et à induire une relaxation profonde.

---

## Quand pratiquer
- En fin de journée pour dénouer les tensions accumulées  
- Avant une séance thérapeutique pour se mettre dans un état réceptif  
- Lors de périodes d’anxiété prolongée afin de réduire la tension globale  
- Avant de dormir pour favoriser le sommeil  

---

## Ce que vous remarquerez
- Sensations de lourdeur puis de chaleur dans les membres  
- Ralentissement perceptible du rythme cardiaque  
- Calme intérieur et concentration sur les mots répétés  
- Parfois un léger engourdissement, signe de relaxation profonde  

---

## Conseils pratiques
- **Position** : allongé·e sur le dos ou assis·e confortablement ; bras et jambes légèrement écartés  
- **Environnement** : pièce silencieuse avec lumière douce ; se couvrir pour ne pas avoir froid  
- **Formulations** : prononcez mentalement les phrases lentement ; répétez‑les environ six fois chacune  
- **Transition** : après la séance, étirez doucement vos membres avant de vous lever  

---

## Déroulé de la séance
- **Étapes d’auto‑suggestion** :  
  | Étape                      | Durée approx. | Texte UI                                   |  
  |---------------------------|--------------|--------------------------------------------|  
  | Calme initial             | 1 min        | « Je suis complètement calme »            |  
  | Lourdeur bras droit       | 1 min        | « Mon bras droit est lourd »              |  
  | Lourdeur bras gauche      | 1 min        | « Mon bras gauche est lourd »             |  
  | Lourdeur jambe droite     | 1 min        | « Ma jambe droite est lourde »            |  
  | Lourdeur jambe gauche     | 1 min        | « Ma jambe gauche est lourde »            |  
  | Chaleur membres           | 2 min        | « Mes membres sont chauds »               |  
  | Cœur calme                | 2 min        | « Mon cœur bat calmement et régulièrement » |  
  | Conclusion                | 3 min        | Respirez calmement, reprenez conscience    |  
  
- Répétez chaque formule environ six fois à un rythme lent  
- La séance complète dure environ **12 minutes**  

---

## Interface utilisateur
- **Écran d’intro** : description de la méthode, précautions et bouton *Commencer*  
- **Écran d’exercice** :  
  - Texte affichant la phrase à répéter ; progression indiquée par un compteur  
  - Option de voix qui répète la formule pour accompagner l’utilisateur  
  - Boutons : Pause / Reprendre / Arrêter  
- **Écran de fin** : message invitant à bouger progressivement et à observer les sensations après l’exercice  

---

## Feedback sensoriel
- **Audio** : voix posée récitant les phrases ; possibilité de fond sonore calme  
- **Haptique** : aucune vibration, pour ne pas perturber l’état de relaxation profonde  

---

## Localisation (clés)
- `exercise.title` = « Training autogène »  
- `exercise.step.calm` = « Je suis complètement calme »  
- `exercise.step.armRight` = « Mon bras droit est lourd »  
- `exercise.step.armLeft` = « Mon bras gauche est lourd »  
- `exercise.step.legRight` = « Ma jambe droite est lourde »  
- `exercise.step.legLeft` = « Ma jambe gauche est lourde »  
- `exercise.step.warmth` = « Mes membres sont chauds »  
- `exercise.step.heart` = « Mon cœur bat calmement »  
- `exercise.completed.title` = « Séance terminée ! »  

---

## États & transitions
- **SELECTION → STARTED → (PAUSED ↔ STARTED) → COMPLETED**  

---

## Télémétrie (non PII)
- `exercise_started{id:autogenic‑training}`  
- `step_completed{name:lourdeur_bras_droit,duration_ms:60000}`  
- … (répété pour chaque étape)  
- `session_completed{duration_ms:720000}`  

---

## Tests d’acceptation
- La phrase affichée correspond bien à l’étape en cours  
- L’utilisateur peut activer/désactiver la voix guidée  
- Pause/Reprise reprend au début de la phrase en cours  
- L’application avertit l’utilisateur de se relever lentement en fin de séance  