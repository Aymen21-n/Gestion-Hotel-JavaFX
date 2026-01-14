# Gestion d'Hôtel et Services (JavaFX + Hibernate)

Application de gestion hôtelière en Java 21 avec interface JavaFX (FXML) et persistance SQL Server via Hibernate/JPA.

## Prérequis
- Java 21
- Maven 3.9+
- SQL Server 2019+

## Configuration Base de Données
1. Créer la base :
   ```sql
   CREATE DATABASE hotel_db;
   ```
2. Mettre à jour les identifiants dans `src/main/resources/hibernate.cfg.xml` :
   - `hibernate.connection.url`
   - `hibernate.connection.username`
   - `hibernate.connection.password`

Hibernate est configuré en `hbm2ddl.auto=update` pour créer/mettre à jour les tables.

## Lancement
1. Compiler :
   ```bash
   mvn clean test
   ```
2. Démarrer l'application :
   ```bash
   mvn javafx:run
   ```

## Structure du projet
- `com.hotel.domain` : entités JPA (Hotel, Chambre, Reservation, Facture, etc.)
- `com.hotel.repository` : accès aux données
- `com.hotel.service` : logique métier et validations
- `com.hotel.ui` : contrôleurs JavaFX
- `com.hotel.config` : Hibernate + seed

## Données de démarrage
Au lancement, si la base est vide :
- 1 hôtel
- 3 chambres
- 1 administrateur (`admin@hotel.tn` / `admin123`)
- 2 services
- 2 clients

## Notes
- Héritage `Service` : stratégie `SINGLE_TABLE` pour simplifier le schéma et les requêtes.
