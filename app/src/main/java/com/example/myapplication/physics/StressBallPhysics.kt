package com.example.myapplication.physics

import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.*
import org.jbox2d.collision.shapes.CircleShape
import kotlin.math.abs

/**
 * Moteur physique Box2D pour la balle anti-stress
 * Fournit une physique réaliste avec inertie, friction, rebonds et déformation
 */
class StressBallPhysics(
    private val containerWidth: Float,
    private val containerHeight: Float,
    private val ballRadius: Float = 50f
) {
    // Monde Box2D avec gravité légère vers le bas
    private val world = World(Vec2(0f, 2f))
    
    // Corps de la balle
    private lateinit var ballBody: Body
    
    // Corps des murs pour les collisions
    private lateinit var walls: List<Body>
    
    // État de déformation pour les effets visuels
    var squashX = 1f
        private set
    var squashY = 1f
        private set
    
    // Velocité précédente pour calculer la déformation
    private var previousVelocity = Vec2(0f, 0f)
    
    init {
        createBall()
        createWalls()
    }
    
    /**
     * Crée la balle avec ses propriétés physiques
     */
    private fun createBall() {
        // Définition du corps
        val ballDef = BodyDef().apply {
            type = BodyType.DYNAMIC
            position.set(containerWidth / 2f, containerHeight / 2f)
            linearDamping = 0.1f // Friction de l'air
            angularDamping = 0.2f
            allowSleep = false // Garde la balle toujours active
        }
        
        ballBody = world.createBody(ballDef)
        
        // Forme circulaire
        val circle = CircleShape().apply {
            radius = ballRadius
        }
        
        // Propriétés du matériau
        val fixtureDef = FixtureDef().apply {
            shape = circle
            density = 1.5f // Densité pour le poids
            friction = 0.4f // Friction avec les surfaces
            restitution = 0.7f // Coefficient de rebond (0-1)
        }
        
        ballBody.createFixture(fixtureDef)
    }
    
    /**
     * Crée les murs invisibles pour les collisions
     */
    private fun createWalls() {
        walls = listOf(
            createWall(containerWidth / 2f, 0f, containerWidth, 1f), // Bas
            createWall(containerWidth / 2f, containerHeight, containerWidth, 1f), // Haut
            createWall(0f, containerHeight / 2f, 1f, containerHeight), // Gauche
            createWall(containerWidth, containerHeight / 2f, 1f, containerHeight) // Droite
        )
    }
    
    /**
     * Crée un mur statique
     */
    private fun createWall(x: Float, y: Float, width: Float, height: Float): Body {
        val wallDef = BodyDef().apply {
            type = BodyType.STATIC
            position.set(x, y)
        }
        
        val wall = world.createBody(wallDef)
        
        val wallShape = org.jbox2d.collision.shapes.PolygonShape().apply {
            setAsBox(width / 2f, height / 2f)
        }
        
        val wallFixture = FixtureDef().apply {
            shape = wallShape
            friction = 0.3f
            restitution = 0.8f // Rebond des murs
        }
        
        wall.createFixture(wallFixture)
        return wall
    }
    
    /**
     * Met à jour la simulation physique
     */
    fun step(deltaTime: Float) {
        // Simulation Box2D
        world.step(deltaTime, 6, 2)
        
        // Calcul de la déformation basée sur la vitesse
        calculateSquash()
    }
    
    /**
     * Calcule la déformation de la balle basée sur la vélocité et les collisions
     */
    private fun calculateSquash() {
        val velocity = ballBody.linearVelocity
        val speed = velocity.length()
        
        // Calcul de l'impact basé sur le changement de vélocité
        val deltaVx = abs(velocity.x - previousVelocity.x)
        val deltaVy = abs(velocity.y - previousVelocity.y)
        
        // Facteurs de déformation
        val maxSquash = 0.3f // Déformation maximale
        val speedFactor = (speed / 100f).coerceAtMost(1f)
        
        // Déformation horizontale (impacts verticaux)
        val targetSquashX = if (deltaVy > 10f) {
            1f - (deltaVy / 50f * maxSquash).coerceAtMost(maxSquash)
        } else {
            1f
        }
        
        // Déformation verticale (impacts horizontaux)
        val targetSquashY = if (deltaVx > 10f) {
            1f - (deltaVx / 50f * maxSquash).coerceAtMost(maxSquash)
        } else {
            1f
        }
        
        // Lissage de la déformation
        val lerpFactor = 0.3f
        squashX = lerp(squashX, targetSquashX, lerpFactor)
        squashY = lerp(squashY, targetSquashY, lerpFactor)
        
        // Retour progressif à la forme normale
        if (deltaVx < 5f && deltaVy < 5f) {
            squashX = lerp(squashX, 1f, 0.1f)
            squashY = lerp(squashY, 1f, 0.1f)
        }
        
        previousVelocity.set(velocity.x, velocity.y)
    }
    
    /**
     * Interpolation linéaire
     */
    private fun lerp(start: Float, end: Float, factor: Float): Float {
        return start + (end - start) * factor
    }
    
    /**
     * Applique une force à la balle (pour le drag)
     */
    fun applyForce(forceX: Float, forceY: Float) {
        ballBody.applyForceToCenter(Vec2(forceX, forceY))
    }
    
    /**
     * Applique une impulsion instantanée (pour les lancers)
     */
    fun applyImpulse(impulseX: Float, impulseY: Float) {
        ballBody.applyLinearImpulse(Vec2(impulseX, impulseY), ballBody.worldCenter)
    }
    
    /**
     * Définit la position de la balle
     */
    fun setBallPosition(x: Float, y: Float) {
        ballBody.setTransform(Vec2(x, y), ballBody.angle)
    }
    
    /**
     * Obtient la position actuelle de la balle
     */
    fun getBallPosition(): Pair<Float, Float> {
        val pos = ballBody.position
        return Pair(pos.x, pos.y)
    }
    
    /**
     * Obtient la rotation actuelle de la balle
     */
    fun getBallRotation(): Float {
        return ballBody.angle
    }
    
    /**
     * Obtient la vélocité actuelle
     */
    fun getBallVelocity(): Pair<Float, Float> {
        val vel = ballBody.linearVelocity
        return Pair(vel.x, vel.y)
    }
    
    /**
     * Arrête complètement la balle
     */
    fun stopBall() {
        ballBody.linearVelocity.set(0f, 0f)
        ballBody.angularVelocity = 0f
    }
    
    /**
     * Réinitialise la balle au centre
     */
    fun resetBall() {
        setBallPosition(containerWidth / 2f, containerHeight / 2f)
        stopBall()
        squashX = 1f
        squashY = 1f
        previousVelocity.set(0f, 0f)
    }
    
    /**
     * Libère les ressources Box2D
     */
    fun dispose() {
        // Box2D se charge automatiquement du nettoyage
    }
}