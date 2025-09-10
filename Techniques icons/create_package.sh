#!/bin/bash

# Script pour créer le package ZIP des vignettes Brythee
# Utilisation: ./create_package.sh

echo "🎨 Création du package Brythee Icons - Style Keith Haring"
echo "========================================================="

# Nom du fichier ZIP
ZIP_NAME="brythee_icons_keith_haring.zip"

# Supprimer l'ancien ZIP s'il existe
if [ -f "$ZIP_NAME" ]; then
    rm "$ZIP_NAME"
    echo "🗑️  Ancien ZIP supprimé"
fi

# Créer le nouveau ZIP
echo "📦 Création du fichier ZIP..."

zip -r "$ZIP_NAME" \
    *.svg \
    README.md \
    -x create_zip.py create_package.sh

# Vérifier la création
if [ -f "$ZIP_NAME" ]; then
    SIZE=$(du -h "$ZIP_NAME" | cut -f1)
    COUNT=$(unzip -l "$ZIP_NAME" | tail -1 | awk '{print $2}')
    
    echo "✅ Package créé avec succès!"
    echo "📁 Fichier: $ZIP_NAME"
    echo "📊 Taille: $SIZE"
    echo "🔢 Nombre de fichiers: $COUNT"
    echo ""
    echo "🚀 Prêt pour l'intégration dans l'application Android!"
    echo ""
    echo "📋 Contenu du package:"
    unzip -l "$ZIP_NAME"
else
    echo "❌ Erreur lors de la création du package"
    exit 1
fi
