package com.example.myapplication.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.Technique
import com.example.myapplication.data.TechniquesRepository
import com.example.myapplication.ui.components.TechniqueCard

@Composable
fun HomeScreen(
    onTechniqueClick: (Technique) -> Unit,
    modifier: Modifier = Modifier
) {
    // Stable reference to avoid recomposition
    val techniques by remember { 
        mutableStateOf(TechniquesRepository.getAllTechniques()) 
    }
    
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            // Hero Section
            HeroSection()
        }
        
        item {
            // Techniques Grid Section
            TechniquesGridSection(
                techniques = techniques,
                onTechniqueClick = onTechniqueClick
            )
        }
        
        item {
            // About Section
            AboutSection()
        }
        
        item {
            // Resources Section
            ResourcesSection()
        }
    }
}

@Composable
private fun HeroSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Brythee",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Text(
            text = "Boîte à outils contre l'anxiété",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Outils gratuits pour vous aider lorsque vous vous sentez anxieux. Ces exercices sont simples à suivre et ne nécessitent aucun équipement particulier, utilisez-les dès que votre esprit est trop agité ou lorsque vous avez besoin d'un moment de calme.",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun TechniquesGridSection(
    techniques: List<Technique>,
    onTechniqueClick: (Technique) -> Unit
) {
    Column {
        Text(
            text = "Toutes les techniques",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Techniques Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
modifier = Modifier.height(((techniques.size / 2 + techniques.size % 2) * 200).dp)
        ) {
            items(
                items = techniques,
                key = { it.id }
            ) { technique ->
                TechniqueCard(
                    technique = technique,
                    onClick = remember(technique.id) { 
                        { onTechniqueClick(technique) } 
                    }
                )
            }
        }
    }
}

@Composable
private fun AboutSection() {
    Column {
        Text(
            text = "Comprendre l'anxiété",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // What happens card
            InfoCard(
                icon = Icons.Default.Warning,
                title = "Ce qui se passe pendant l'anxiété",
                description = "Votre corps active la réponse « combat ou fuite », augmentant le rythme cardiaque, accélérant la respiration et libérant des hormones de stress comme l'adrénaline. Cela se déclenche souvent inutilement dans des situations quotidiennes, vous laissant tendu et sur le qui-vive.",
                backgroundColor = Color(0xFFFEF3C7),
                iconColor = Color(0xFFD97706),
                borderColor = Color(0xFFF59E0B)
            )
            
            // How techniques help card
            InfoCard(
                icon = Icons.Default.CheckCircle,
                title = "Comment ces techniques aident",
                description = "Ces techniques fondées sur des données scientifiques activent votre système nerveux parasympathique, qui contrebalance naturellement la réponse au stress. Une pratique régulière peut réduire à la fois la fréquence et l'intensité des sensations d'anxiété.",
                backgroundColor = Color(0xFFDCFCE7),
                iconColor = Color(0xFF16A34A),
                borderColor = Color(0xFF22C55E)
            )
            
            // Tips card
            TipsCard()
        }
    }
}

@Composable
private fun InfoCard(
    icon: ImageVector,
    title: String,
    description: String,
    backgroundColor: Color,
    iconColor: Color,
    borderColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Text(
                text = description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun TipsCard() {
    val tips = listOf(
        "Trouvez un endroit calme et confortable où vous ne serez pas interrompu",
        "Commencez par des techniques courtes (2-5 minutes) si vous vous sentez très anxieux",
        "Pratiquez régulièrement, même lorsque vous n'êtes pas anxieux, pour développer vos compétences",
        "Soyez patient avec vous-même, ces techniques deviennent plus faciles avec la pratique"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF60A5FA), RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDBEAFE))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "Conseils pour de meilleurs résultats",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Column {
                tips.forEach { tip ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = "•",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(end = 8.dp, top = 2.dp)
                        )
                        
                        Text(
                            text = tip,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResourcesSection() {
    Column {
        Text(
            text = "Ressources supplémentaires",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Professional help card
            ResourceCard(
                icon = Icons.Default.Person,
                title = "Quand demander de l'aide professionnelle",
                description = "Si l'anxiété impacte fortement votre vie quotidienne, votre travail ou vos relations, envisagez de consulter un professionnel de la santé mentale.",
                items = listOf(
                    "Les crises d'anxiété sont fréquentes ou sévères",
                    "Vous évitez des activités importantes",
                    "Les symptômes physiques persistent",
                    "Le sommeil ou l'appétit est fortement affecté"
                )
            )
            
            // Organizations card
            ResourceCard(
                icon = Icons.Default.Business,
                title = "Organisations de santé mentale",
                description = "Principales organisations offrant des informations et ressources fondées sur des preuves pour l'anxiété et la dépression :",
                items = listOf(
                    "Psycom – Santé Mentale Info : Organisme public d'information sur la santé mentale",
                    "Fondation de France - Santé Mentale : Soutient la recherche sur la santé mentale",
                    "Unafam : Association offrant du soutien aux familles"
                )
            )
        }
    }
}

@Composable
private fun ResourceCard(
    icon: ImageVector,
    title: String,
    description: String,
    items: List<String>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Text(
                text = description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Column {
                items.forEach { item ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = "•",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(end = 8.dp, top = 2.dp)
                        )
                        
                        Text(
                            text = item,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}