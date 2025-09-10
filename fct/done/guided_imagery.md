# 🌅 Imagerie guidée

## Métadonnées
- **ID** : guided‑imagery  
- **Nom affiché** : Imagerie guidée  
- **Catégorie** : Visualisation  
- **Durée** : 8 min  
- **Niveau d’anxiété visé** : faible à modéré  
- **Tags** : visualisation, relaxation, créativité, stress  
- **Contre‑indications** : éviter si les visualisations provoquent des souvenirs traumatiques ; adapter pour les personnes malvoyantes en utilisant des sensations auditives et corporelles  

---

## But & résultats attendus
- **Intention utilisateur** : créer un espace mental apaisant grâce à l’imagination pour réduire le stress et améliorer l’humeur  
- **Résultats immédiats** : respiration plus lente, diminution de la tension et sentiment de bien‑être  
- **Avec pratique régulière** : capacité accrue à se détendre, amélioration de la confiance en soi et de la résilience  

---

## Contexte psycho‑physio
L’imagerie guidée consiste à fermer les yeux, à respirer profondément puis à visualiser un lieu ou une scène paisible en ajoutant des détails sensoriels. Cette technique stimule la réponse de relaxation : elle ralentit la respiration, abaisse la pression artérielle et le rythme cardiaque【314968577812417†L151-L162】. Pour pratiquer, on imagine un paysage (plage, forêt, montagne) et on mobilise les cinq sens pour renforcer l’expérience【314968577812417†L265-L282】. L’imagerie est généralement considérée comme sûre et procure un sentiment de calme durable【314968577812417†L166-L189】.

---

## Quand pratiquer
- Lors d’une pause au travail pour décompresser  
- Avant un examen ou un entretien pour se recentrer  
- Le soir pour évacuer les pensées anxieuses  
- Après une séance d’activité physique pour prolonger la détente  

---

## Ce que vous remarquerez
- Sensation de voyager mentalement dans un endroit sûr  
- Rythme respiratoire plus lent et régulier  
- Détente musculaire et chaleur dans le corps  
- Mise à distance des préoccupations  

---

## Conseils pratiques
- **Posture** : assis·e ou allongé·e, dos soutenu, mains posées sur les cuisses ou le ventre  
- **Environnement** : endroit calme ; vous pouvez écouter un fond sonore neutre (bruit de vagues, forêt)  
- **Visualisation** : choisissez un lieu qui vous inspire ; ajoutez progressivement des détails (couleurs, sons, odeurs)  
- **Adaptabilité** : si vous avez des difficultés à visualiser, concentrez‑vous sur les sensations corporelles et auditives  

---

## Déroulé de la séance
- **Étapes de visualisation** :  
  | Étape                   | Durée approx. | Texte UI                         |  
  |------------------------|--------------|----------------------------------|  
  | Préparation            | 1 min        | Prenez une position confortable  |  
  | Respiration initiale   | 1 min        | Inspirez profondément            |  
  | Visualisation du lieu  | 4 min        | Imaginez votre lieu apaisant    |  
  | Ajout des sens         | 1 min        | Ajoutez sons, odeurs, textures  |  
  | Retour au présent      | 1 min        | Revenez doucement à l’instant   |  
  
- Laissez les images venir sans forcer  
- Si des pensées intrusives apparaissent, laissez‑les passer et revenez à la scène  
- La séance dure environ **8 minutes**  

---

## Interface utilisateur
- **Écran d’intro** : présentation de la technique, suggestions de lieux à imaginer et bouton *Commencer*  
- **Écran d’exercice** :  
  - Fond visuel doux ou neutre ; possibilité de changer l’ambiance sonore  
  - Indications textuelles et audio pour chaque étape  
  - Boutons : Pause / Reprendre / Arrêter  
- **Écran de fin** : message d’ancrage pour réintégrer l’instant présent et invitation à noter ses ressentis  

---

## Feedback sensoriel
- **Audio** : fond sonore personnalisable (mer, forêt, feu de cheminée) ; voix guidant la visualisation  
- **Haptique** : aucune vibration nécessaire, pour ne pas perturber l’immersion  

---

## Localisation (clés)
- `exercise.title` = « Imagerie guidée »  
- `exercise.step.prepare` = « Installez‑vous confortablement »  
- `exercise.step.breathe` = « Respirez profondément »  
- `exercise.step.visualise` = « Visualisez le lieu »  
- `exercise.step.senses` = « Ajoutez les sensations »  
- `exercise.step.return` = « Revenez au présent »  
- `exercise.completed.title` = « Séance terminée ! »  

---

## États & transitions
- **SELECTION → STARTED → (PAUSED ↔ STARTED) → COMPLETED**  

---

## Télémétrie (non PII)
- `exercise_started{id:guided‑imagery}`  
- `step_changed{name:visualisation,duration_ms:240000}`  
- `session_completed{duration_ms:480000}`  

---

## Tests d’acceptation
- Les indications textuelles et audio sont synchronisées avec les étapes  
- La personnalisation du fond sonore fonctionne sans couper les instructions  
- Le retour au présent inclut un signal visuel et audio doux  
- Interface lisible sur 360×640 dp, adaptable au mode sombre  