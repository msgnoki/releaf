# 🧘‍♂️ Body scan méditatif

## Métadonnées
- **ID** : body‑scan‑meditation  
- **Nom affiché** : Body scan  
- **Catégorie** : Méditation  
- **Durée** : 10 min  
- **Niveau d’anxiété visé** : faible à modéré  
- **Tags** : pleine conscience, relaxation, sommeil, bien‑être  
- **Contre‑indications** : en cas de douleur aiguë ou de traumatisme corporel, adapter la pratique ou consulter un professionnel  

---

## But & résultats attendus
- **Intention utilisateur** : développer la conscience corporelle et relâcher les tensions en observant chaque partie du corps  
- **Résultats immédiats** : sensation de calme, ancrage dans l’instant présent  
- **Avec pratique régulière** : amélioration du sommeil, réduction du stress, augmentation de l’attention aux signaux corporels  

---

## Contexte psycho‑physio
Le body scan est une pratique de pleine conscience qui consiste à porter son attention successivement sur chaque région du corps, à sentir les sensations présentes et à les accueillir sans jugement. On commence généralement par les pieds et on remonte jusqu’à la tête ou l’inverse. Cette observation attentive permet de relâcher les tensions et de calmer l’esprit【645249645396177†L262-L303】. Les recherches suggèrent que la méditation scannée améliore la qualité du sommeil et réduit l’anxiété et le stress【645249645396177†L196-L234】.

---

## Quand pratiquer
- Avant de dormir pour apaiser le mental  
- Au réveil pour commencer la journée en pleine conscience  
- Lors d’un moment de stress ou de surcharge émotionnelle  
- Après une activité sportive pour relâcher le corps  

---

## Ce que vous remarquerez
- Sensations subtiles (picotements, chaleur, lourdeur) dans différentes parties du corps  
- Pensées qui se calment progressivement  
- Sensation d’ancrage et de détente générale  
- Possibilité de bâillements ou de somnolence  

---

## Conseils pratiques
- **Posture** : allongé·e sur le dos, bras le long du corps, paumes tournées vers le ciel ; ou assis·e avec le dos droit  
- **Environnement** : lieu calme, sans interruption ; utiliser un tapis ou un coussin  
- **Attitude** : observer les sensations sans chercher à les modifier ; si l’attention se disperse, revenir doucement à la zone en cours  
- **Durée** : commencer avec 5 minutes et augmenter progressivement  

---

## Déroulé de la séance
- **Parcours corporel** :  
  | Zone                | Durée approx. | Texte UI                              |  
  |--------------------|--------------|---------------------------------------|  
  | Pieds et orteils    | 30 s         | Portez attention aux pieds            |  
  | Jambes (mollets, genoux) | 1 min      | Ressentez les jambes                 |  
  | Hanches et bassin   | 1 min        | Ressentez le bassin                  |  
  | Dos et abdomen      | 2 min        | Observez le dos et le ventre         |  
  | Poitrine et épaules | 1 min        | Ressentez la poitrine et les épaules |  
  | Bras et mains       | 1 min        | Portez attention aux bras et mains   |  
  | Cou et visage       | 2 min        | Observez le cou et le visage         |  
  | Corps complet       | 2 min        | Sentez l’ensemble du corps           |  
  
- Suivez l’ordre proposé ou adaptez selon vos besoins  
- Lorsque vous êtes distrait·e par des pensées, revenez simplement à la sensation  
- La séance peut durer **10 minutes** ou être prolongée  

---

## Interface utilisateur
- **Écran d’intro** : explication de la pratique et conseils (lieu calme, posture), bouton *Commencer*  
- **Écran d’exercice** :  
  - Silhouette humaine stylisée avec surbrillance de la zone actuelle  
  - Chronomètre ou indicateur de progression  
  - Boutons : Pause / Reprendre / Passer à la zone suivante / Arrêter  
- **Écran de fin** : message de clôture et suggestion de noter les sensations ressenties  

---

## Feedback sensoriel
- **Audio** : voix douce qui guide l’attention vers chaque zone ; option de sons de nature en arrière‑plan  
- **Haptique** : petite vibration lors du passage à une nouvelle zone  

---

## Localisation (clés)
- `exercise.title` = « Body scan »  
- `exercise.zone.feet` = « Pieds et orteils »  
- `exercise.zone.legs` = « Jambes »  
- `exercise.zone.hips` = « Hanches et bassin »  
- `exercise.zone.torso` = « Dos et abdomen »  
- `exercise.zone.chest` = « Poitrine et épaules »  
- `exercise.zone.arms` = « Bras et mains »  
- `exercise.zone.neck` = « Cou et visage »  
- `exercise.zone.fullbody` = « Corps complet »  
- `exercise.completed.title` = « Séance terminée ! »  

---

## États & transitions
- **SELECTION → STARTED → (PAUSED ↔ STARTED) → COMPLETED**  

---

## Télémétrie (non PII)
- `exercise_started{id:body‑scan‑meditation}`  
- `zone_changed{name:pieds,duration_ms:30000}`  
- … (répété pour chaque zone)  
- `session_completed{duration_ms:600000}`  

---

## Tests d’acceptation
- La silhouette affiche correctement la zone en surbrillance  
- Les minuteries respectent la durée définie avec une précision de ±200 ms  
- Pause/Reprise reprend à la bonne zone et au temps restant  
- Les textes restent lisibles sur écran 360×640 dp en français et en anglais  