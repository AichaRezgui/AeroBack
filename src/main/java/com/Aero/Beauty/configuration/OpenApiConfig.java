package com.Aero.Beauty.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "AeroBeauty API",
                version = "1.0",
                description = """
        ⭐ **AeroBeauty API – Documentation Officielle**

        Bienvenue sur l'API d’AeroBeauty, une plateforme dédiée à la gestion complète
        d’une boutique e-commerce spécialisée dans les produits de beauté.

        ### 🔧 Fonctionnalités principales
        - 🔐 **Authentification des utilisateurs** (connexion, inscription, session basée sur JWT)
        - 🛍️ **Gestion des produits** (catalogue, recherche, détails)
        - 🛒 **Gestion du panier** (ajout, suppression, modification des quantités)
        - 📦 **Création et gestion des commandes**

        ### 🔒 Sécurité
        L’API utilise un système d’authentification JWT sécurisé :  
        - Le jeton est stocké dans un **cookie HTTPOnly** pour empêcher l’accès JavaScript  
        - Le cookie peut être configuré en **SameSite** et **Secure**  
        - Les endpoints sensibles nécessitent une authentification

        Cette documentation vous permet de tester tous les endpoints et de visualiser
        les schémas des données directement via Swagger UI.
        """
        )
)
public class OpenApiConfig {}
