# Gestion Abonnements

Application console Java 8 permettant de centraliser la gestion d'abonnements personnels ou professionnels : suivi des échéances, détection des impayés et génération de rapports financiers.

## Contexte

Projet réalisé dans le cadre du **Brief 2 - Sprint 1** (YouCode/UM6P). L'objectif est de fournir un outil de suivi des abonnements et paiements, avec persistance en mémoire (collections Java) pour ce sprint.

## Fonctionnalités

- Création d'abonnements avec ou sans engagement
- Modification, suppression et résiliation d'un abonnement
- Consultation de la liste des abonnements
- Génération automatique des échéances de paiement
- Enregistrement, modification et suppression des paiements
- Détection des impayés et calcul du montant total impayé
- Calcul de la somme payée pour un abonnement
- Consultation des 5 derniers paiements
- Génération de rapports financiers (mensuel, annuel, impayés)

## Architecture

Le projet respecte une architecture en couches :

```
com.gestionabonnements
├── entites/        // Abonnement, AbonnementAvecEngagement, AbonnementSansEngagement, Paiement, Statut, StatutPaiement
├── DAO/            // AbonnementDAO, PaiementDAO (persistance en mémoire)
├── services/       // AbonnementService, PaiementService (logique métier)
└── presentation/   // MenuPrincipal (interface console)
```

## Technologies

- **Java 8** (Stream API, lambda, Optional, Collectors)
- **JDBC** (prévu en bonus pour la persistance PostgreSQL/MySQL)
- Persistance en mémoire via collections Java (`HashMap`) pour ce sprint

## Modèle de données

| Entité | Attributs principaux |
|---|---|
| Abonnement | id, nomService, montantMensuel, dateDebut, dateFin, statut, dureeEngagementMois (si avec engagement) |
| Paiement | idPaiement, idAbonnement, dateEcheance, datePaiement, typePaiement, statut |

Relation **1..n** entre `Abonnement` et `Paiement`.

## Prérequis

- JDK 8 ou supérieur
- IntelliJ IDEA (ou tout IDE compatible Java)

## Lancer l'application

1. Cloner le dépôt :
```bash
   git clone https://github.com/MhammedGabigabe/GestionAbonnements.git
```
2. Ouvrir le projet dans l'IDE.
3. Exécuter la classe `Main` (`src/main/java/com/gestionabonnements/Main.java`).
4. Naviguer dans le menu console via les options numérotées.

## Organisation des tâches

Lien Jira : https://mhammedgabigabe-1788862202791.atlassian.net/jira/software/projects/GES/boards/68/backlog

## Auteur

GABIGABE Mhammed — Développeur backend (intern), YouCode/UM6P Safi