# 🫁 Respiration 4–7–8

## Métadonnées
- **ID** : breathing‑478  
- **Nom affiché** : Respiration 4–7–8  
- **Catégorie** : Respiration  
- **Durée** : 4 min  
- **Niveau d’anxiété visé** : modéré à élevé  
- **Tags** : stress, sommeil, anxiété, débutant  
- **Contre‑indications** : interrompre en cas d’étourdissements ou de problèmes respiratoires, ne pas retenir la respiration en cas de grossesse ou de maladie cardio‑respiratoire  

---

## But & résultats attendus
- **Intention utilisateur** : calmer une montée de stress ou favoriser l’endormissement à l’aide d’un rythme respiratoire guidé  
- **Résultats immédiats** : ralentissement du rythme cardiaque, détente corporelle, apaisement mental  
- **Avec pratique régulière** : amélioration de la gestion du stress et du sommeil, ancrage d’une respiration lente et consciente  

---

## Contexte psycho‑physio
La méthode 4–7–8 consiste à inspirer pendant 4 secondes, retenir son souffle pendant 7 secondes puis expirer pendant 8 secondes. Ce schéma ralentit la respiration et stimule le système parasympathique, ce qui aide à réduire la fréquence cardiaque et à calmer l’esprit【468066847796661†L386-L398】. Des répétitions régulières favorisent une détente profonde et un sentiment de calme【468066847796661†L431-L437】.

---

## Quand pratiquer
- Avant de dormir pour faciliter l’endormissement  
- Lors d’une crise d’angoisse ou de stress au travail  
- Après une activité physique intense pour retrouver un rythme calme  
- En préparation d’une prise de parole ou d’un examen  

---

## Ce que vous remarquerez
- Respiration plus longue et régulière  
- Sensation de chaleur et de lourdeur dans le corps  
- Diminution de la fréquence cardiaque  
- Possibles légers vertiges au début, qui disparaissent avec la pratique  

---

## Conseils pratiques
- **Posture** :  
  - Assis : dos droit, épaules relâchées, pieds au sol  
  - Allongé : bras le long du corps, paumes vers le haut  
- **Environnement** : endroit calme, lumière tamisée, vêtements confortables  
- **Régularité** : pratiquer chaque soir ou lorsqu’un stress se manifeste  
- **Sécurité** : si vous ressentez des vertiges ou un malaise, revenez à une respiration naturelle et interrompez l’exercice  

---

## Déroulé de la séance
- **Pattern respiratoire** :  
  | Phase   | Durée approx. | Texte UI                          |  
  |---------|---------------|-----------------------------------|  
  | inhale  | 4 s           | Inspirez 4 s                      |  
  | hold    | 7 s           | Retenez 7 s                       |  
  | exhale  | 8 s           | Expirez 8 s                       |  
  
- Répétez le cycle en continu pendant **4 minutes**  
- Pas de rétention à l’expiration  

---

## Interface utilisateur
- **Écran d’introduction** : affiche le titre « Respiration 4–7–8 », les bénéfices et la durée, avec un bouton *Commencer*  
- **Écran d’exercice** :  
  - Animation visuelle indiquant la phase active (cercle qui se remplit à l’inspiration, se fige en rétention puis se vide à l’expiration)  
  - Compteur numérique pour les secondes  
  - Boutons : Pause / Reprendre / Recommencer / Arrêter  
- **Écran de fin** : message *« Exercice terminé ! »* et suggestions pour répéter l’exercice régulièrement  

---

## Feedback sensoriel
- **Audio** : option d’une voix douce comptant les secondes ou d’un bip discret pour marquer les transitions  
- **Haptique** : vibration brève au changement de phase (inspiration → rétention → expiration)  

---

## Localisation (clés)
- `exercise.title` = « Respiration 4–7–8 »  
- `exercise.phase.inhale` = « Inspirez pendant 4 secondes »  
- `exercise.phase.hold` = « Retenez pendant 7 secondes »  
- `exercise.phase.exhale` = « Expirez pendant 8 secondes »  
- `exercise.completed.title` = « Exercice terminé ! »  

---

## États & transitions
- **SELECTION → STARTED → (PAUSED ↔ STARTED) → COMPLETED**  

---

## Télémétrie (non PII)
- `exercise_started{id:breathing‑478}`  
- `phase_changed{phase:inhale,duration_ms:4000}`  
- `phase_changed{phase:hold,duration_ms:7000}`  
- `phase_changed{phase:exhale,duration_ms:8000}`  
- `session_completed{duration_ms:240000}`  

---

## Tests d’acceptation
- Le minuteur de 4–7–8 indique des durées précises (±200 ms)  
- La pause / reprise reprend exactement la phase en cours  
- L’exercice dure ~4 min avec des cycles continus  
- Les textes en FR/EN s’affichent correctement sur un écran 360×640 dp sans troncation  