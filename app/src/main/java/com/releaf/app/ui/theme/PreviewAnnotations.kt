package com.releaf.app.ui.theme

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Annotations d'aperçu personnalisées pour Releaf
 * Ces annotations combinent plusieurs configurations d'aperçu courantes
 */

@Preview(
    name = "Light Mode",
    showBackground = true,
    group = "Light"
)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    group = "Dark"
)
annotation class ReleafPreviews

@Preview(
    name = "Phone Portrait",
    showBackground = true,
    widthDp = 360,
    heightDp = 640
)
@Preview(
    name = "Phone Landscape", 
    showBackground = true,
    widthDp = 640,
    heightDp = 360
)
@Preview(
    name = "Tablet",
    showBackground = true,
    widthDp = 800,
    heightDp = 1280
)
annotation class ReleafDevicePreviews

@Preview(
    name = "Normal Font",
    showBackground = true,
    fontScale = 1.0f,
    group = "Font Scales"
)
@Preview(
    name = "Large Font",
    showBackground = true,
    fontScale = 1.3f,
    group = "Font Scales"
)
@Preview(
    name = "Extra Large Font",
    showBackground = true,
    fontScale = 1.8f,
    group = "Font Scales"
)
annotation class ReleafFontPreviews

@Preview(
    name = "French",
    showBackground = true,
    locale = "fr-FR",
    group = "Locales"
)
@Preview(
    name = "English",
    showBackground = true,
    locale = "en-US",
    group = "Locales"
)
@Preview(
    name = "Spanish",
    showBackground = true,
    locale = "es-ES",
    group = "Locales"
)
annotation class ReleafLocalePreviews

@ReleafPreviews
@ReleafDevicePreviews
annotation class ReleafCompletePreviews