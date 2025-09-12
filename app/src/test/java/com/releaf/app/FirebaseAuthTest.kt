package com.releaf.app

import org.junit.Test
import org.junit.Assert.*

/**
 * Test de base pour vérifier l'intégration Firebase
 */
class FirebaseAuthTest {
    
    @Test
    fun firebase_auth_classes_exist() {
        // Vérifier que les classes Firebase Auth peuvent être référencées
        // Ceci confirme que les dépendances Firebase sont correctement configurées
        val firebaseAuthClass = com.google.firebase.auth.FirebaseAuth::class.java
        val firebaseUserClass = com.google.firebase.auth.FirebaseUser::class.java
        
        assertNotNull("FirebaseAuth class should be available", firebaseAuthClass)
        assertNotNull("FirebaseUser class should be available", firebaseUserClass)
        assertEquals("FirebaseAuth class should be properly loaded", 
                     "FirebaseAuth", firebaseAuthClass.simpleName)
        assertEquals("FirebaseUser class should be properly loaded",
                     "FirebaseUser", firebaseUserClass.simpleName)
    }
    
    @Test
    fun firebase_auth_repository_exists() {
        // Ce test vérifie que notre FirebaseAuthRepository existe
        val repositoryClass = com.releaf.app.data.repository.FirebaseAuthRepository::class.java
        
        assertNotNull("FirebaseAuthRepository class should exist", repositoryClass)
        assertEquals("FirebaseAuthRepository should have correct name",
                     "FirebaseAuthRepository", repositoryClass.simpleName)
    }
}