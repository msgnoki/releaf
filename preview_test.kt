@Preview(showBackground = true)
@Composable
private fun StartScreenPreview() {
    StartScreen(
        technique = Technique(
            id = "progressive-muscle-relaxation",
            icon = "accessibility",
            iconColor = "purple",
            tags = listOf("moderate-anxiety", "medium-time"),
            name = "Relaxation Musculaire Progressive",
            shortDescription = "Technique de relaxation par tension-relâchement",
            description = "L'anxiété crée souvent une tension physique. Cette technique vous aide à identifier et relâcher ces tensions en contractant puis détendant systématiquement chaque groupe musculaire.",
            duration = "20 min",
            category = TechniqueCategory.RELAXATION,
            durationMinutesStart = 8,
            durationMinutesEnd = 20
        ),
        onStartExercise = { }
    )
}