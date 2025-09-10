#!/usr/bin/env python3
"""
Script pour créer un fichier ZIP contenant toutes les vignettes Brythee
"""

import os
import zipfile
from pathlib import Path

def create_brythee_icons_zip():
    """Crée un fichier ZIP avec toutes les vignettes SVG"""
    
    # Chemin du dossier courant
    current_dir = Path(__file__).parent
    
    # Nom du fichier ZIP
    zip_filename = "brythee_icons_keith_haring.zip"
    
    # Liste des fichiers SVG à inclure
    svg_files = [
        "respiration_2_minutes.svg",
        "respiration_guidee.svg", 
        "respiration_anti_stress.svg",
        "respiration_diaphragmatique.svg",
        "respiration_carree.svg",
        "respiration_4_7_8.svg",
        "respiration_consciente.svg",
        "relaxation_musculaire_progressive.svg",
        "therapie_sonore.svg",
        "training_autogene.svg",
        "auto_hypnose_autogene.svg",
        "ancrage_5_4_3_2_1.svg",
        "bulles_anti_stress.svg",
        "balle_anti_stress.svg",
        "body_scan.svg",
        "meditation_bienveillante.svg",
        "meditation_conscience_souffle.svg",
        "visualisation_paisible.svg",
        "immersion_foret.svg",
        "etiquetage_pensees.svg",
        "etoile_apaisante.svg",
        "lune_relaxante.svg",
        "soleil_meditatif.svg",
        "vagues_zen.svg",
        "papillon_liberte.svg",
        "nuage_pensees.svg",
        "fleur_lotus.svg",
        "bougie_relaxation.svg",
        "plume_legere.svg"
    ]
    
    # Créer le fichier ZIP
    with zipfile.ZipFile(zip_filename, 'w', zipfile.ZIP_DEFLATED) as zipf:
        # Ajouter le README
        readme_path = current_dir / "README.md"
        if readme_path.exists():
            zipf.write(readme_path, "README.md")
            print(f"✅ Ajouté: README.md")
        
        # Ajouter tous les fichiers SVG
        for svg_file in svg_files:
            svg_path = current_dir / svg_file
            if svg_path.exists():
                zipf.write(svg_path, svg_file)
                print(f"✅ Ajouté: {svg_file}")
            else:
                print(f"❌ Manquant: {svg_file}")
    
    # Vérifier la taille du fichier ZIP
    zip_size = os.path.getsize(zip_filename)
    print(f"\n🎉 Fichier ZIP créé: {zip_filename}")
    print(f"📦 Taille: {zip_size / 1024:.1f} KB")
    print(f"📁 Nombre de fichiers: {len(svg_files) + 1} (SVG + README)")
    
    return zip_filename

if __name__ == "__main__":
    print("🎨 Création du package Brythee Icons - Style Keith Haring")
    print("=" * 60)
    
    zip_file = create_brythee_icons_zip()
    
    print("\n" + "=" * 60)
    print(f"✨ Package créé avec succès: {zip_file}")
    print("🚀 Prêt pour l'intégration dans l'application Android!")
