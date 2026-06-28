# Contact Tracker

Application Android (Kotlin + Jetpack Compose + Room/SQLite) pour le suivi des nouveaux contacts.

## Fonctionnalités
- Base de données locale SQLite (via Room)
- Liste des contacts avec recherche et statut de suivi (New / Contacted / In progress / Done)
- Ajout, modification, suppression d'un contact
- Import en masse depuis un fichier CSV (bouton en haut de la liste)
- Boutons d'action sur la fiche d'un contact : WhatsApp, SMS, appel, email
  - Le numéro local (ex: 077123456) est automatiquement converti au format international (241...)
    requis par WhatsApp
  - Le message envoyé est pré-rempli différemment selon que la personne a demandé
    une prière ou autre chose (modifiable avant l'envoi)

## Prérequis
- Android Studio (version récente, "Koala" ou plus récent recommandé)
- JDK 17 (généralement inclus avec Android Studio)
- Une connexion internet au premier lancement (pour télécharger les dépendances Gradle)

## Mise en route
1. Ouvre Android Studio
2. "Open" → sélectionne le dossier `ContactTracker` (celui qui contient ce README)
3. Laisse Android Studio synchroniser le projet (Gradle Sync). S'il propose d'utiliser
   le wrapper Gradle ou de le régénérer, accepte — c'est normal, le fichier binaire
   du wrapper n'est pas inclus dans cet export.
4. Connecte ton téléphone Android (mode développeur + débogage USB activé) ou lance un
   émulateur, puis clique sur "Run" (▶)

## Importer tes contacts
1. Transfère ton fichier `customers_template.csv` sur le téléphone (Drive, email, USB...)
2. Dans l'app, appuie sur l'icône d'import (en haut à droite de la liste)
3. Sélectionne le fichier CSV — tes contacts sont chargés automatiquement

## Pour aller plus loin
- Les couleurs/thème sont dans `ui/theme/Color.kt`
- Les champs du formulaire sont dans `ui/screens/AddEditContactScreen.kt`
- Les messages pré-remplis sont dans `util/MessageTemplates.kt`
- Le mapping des colonnes CSV est dans `data/CsvImporter.kt`

## Limites connues (premières version)
- Pas encore de tri/filtre par statut dans la liste (seulement recherche par nom)
- Pas de sauvegarde/export automatique de la base (à ajouter si besoin)
- Le numéro secondaire n'a pas encore son propre bouton WhatsApp/SMS dans le détail
  (facile à ajouter si tu en as besoin)
