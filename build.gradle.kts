plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false // <-- ADICIONADO
    alias(libs.plugins.google.gms.google.services) apply false
}
