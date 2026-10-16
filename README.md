# TaskFlow : backend

API REST de gestion de tâches en **Spring Boot**, avec authentification **JWT**.
Elle alimente le frontend Angular de TaskFlow.

🔗 **Démo en ligne** : https://taskflow-frontend-three-eosin.vercel.app/
🎥 **Vidéo de démonstration** : https://www.youtube.com/watch?v=W_Tl1lVULcU
🖥️ **Frontend (Angular)** : https://github.com/kericheouafa/taskflow-frontend

> Le premier chargement peut être lent si le serveur est en veille.

## Sommaire

- [Fonctionnalités](#fonctionnalités)
- [Technologies](#technologies)
- [Structure du projet](#structure-du-projet)
- [Installation](#installation)
- [Authentification](#authentification)
- [Règles métier](#règles-métier)
- [Tests](#tests)
- [Pistes d'amélioration](#pistes-damélioration)
- [Auteure](#auteure)

## Fonctionnalités

- **Authentification** : inscription, connexion et renouvellement du token (JWT)
- **Gestion des tâches** : création, liste, suppression et changement de statut (À faire, En cours, Terminé)
- **Isolation des données** : chaque utilisateur ne voit et ne modifie que ses propres tâches
- **Validation** : titre de 3 à 100 caractères, priorité de 1 à 3, échéance dans le futur

## Technologies

| Partie | Outils |
|---|---|
| Langage et framework | Java 25, Spring Boot |
| Persistance | Spring Data JPA / Hibernate, PostgreSQL |
| Sécurité | Spring Security, JWT (jjwt), hachage des mots de passe |
| Tests | JUnit 5, Mockito, MockMvc |
| Outils et déploiement | Maven, Git, GitHub, Docker, Render |

## Structure du projet

```
src/main/java/com/taskflow/taskflow/
├── model/        entités JPA (Tache, Statut, Utilisateur)
├── repository/   accès aux données (Spring Data JPA)
├── service/      logique métier (TacheService)
└── security/     JWT (JwtService), contrôleur d'authentification
```

## Installation

### Prérequis

- Java 25 (Temurin) et Maven
- PostgreSQL, avec une base nommée `taskflow_db` (port 5432 par défaut)

### Configuration

Dans `application.properties`, renseignez :

- `spring.datasource.username` et `spring.datasource.password` pour la base de données
- `jwt.secret` : clé secrète de signature, **au moins 32 caractères** (exigence de l'algorithme HS256)
- `jwt.expiration` : durée de validité du token, en millisecondes

En production (Render), la connexion à la base est fournie par des variables d'environnement.

### Lancement

```bash
git clone https://github.com/kericheouafa/taskflow-springboot
cd taskflow-springboot
mvn spring-boot:run
```

Le serveur démarre sur `http://localhost:8080`.

## Authentification

| Méthode | Route | Rôle | Réponses |
|---|---|---|---|
| POST | `/api/auth/register` | création d'un compte, renvoie un token | 200, 409 (email déjà utilisé) |
| POST | `/api/auth/login` | connexion, renvoie un token | 200, 401 (identifiants incorrects) |
| POST | `/api/auth/refresh` | renvoie un nouveau token (utilisateur connecté) | 200 |

Le token est ensuite envoyé dans l'en-tête `Authorization` des requêtes protégées.

## Tâches

Toutes ces routes sont protégées : l'utilisateur est identifié par son token, et chaque requête ne concerne que **ses** tâches.

| Méthode | Route | Rôle |
|---|---|---|
| GET | `/taches` | liste des tâches de l'utilisateur connecté |
| POST | `/taches` | création d'une tâche (statut « A faire » ajouté automatiquement) |
| PUT | `/taches/{id}/statut/{idStatut}` | changement du statut d'une tâche |
| DELETE | `/taches/{id}` | suppression d'une tâche |

## Règles métier

- À la création, une tâche reçoit automatiquement le statut **« A faire »** et est rattachée à l'utilisateur connecté.
- L'identifiant d'une tâche à la création est toujours remis à zéro côté serveur : un client ne peut pas écraser une tâche existante en envoyant un `id`.
- La modification et la suppression passent par l'**email de l'utilisateur** : une tâche qui appartient à quelqu'un d'autre est traitée comme introuvable.
- Le message d'erreur du login est le même pour un email inconnu et un mauvais mot de passe, pour ne pas révéler quels emails existent.

## Tests

25 tests unitaires, avec **JUnit 5**, **Mockito** et **MockMvc**.

| Classe de test | Outils | Ce qui est testé |
|---|---|---|
| `JwtServiceTest` | JUnit 5 | génération du token, extraction de l'email, rejet d'un token falsifié, signé avec une autre clé ou expiré |
| `TacheServiceTest` | JUnit 5, Mockito | création, suppression et changement de statut, avec tous les cas d'erreur (tâche, utilisateur ou statut introuvable) |
| `AuthControllerTest` | JUnit 5, MockMvc | login (200 / 401), register (200 / 409), refresh du token |

```bash
./mvnw test
```

Pour une seule classe :

```bash
./mvnw test -Dtest=JwtServiceTest
```

### Choix techniques

- Les repositories sont remplacés par des **mocks** : les tests sont rapides et n'utilisent pas la vraie base.
- Dans chaque cas d'erreur, on vérifie aussi que rien n'est enregistré ni supprimé (`verify(..., never())`).
- Le projet tourne sous Java 25 : le plugin `maven-surefire-plugin` du `pom.xml` active l'option `-Dnet.bytebuddy.experimental=true`, car Mockito s'appuie sur Byte Buddy, qui ne supporte pas encore officiellement cette version.

## Pistes d'amélioration

- Champ description pour les tâches
- Modification du titre, de la priorité et de l'échéance d'une tâche existante
- Tests du filtre JWT et des contrôleurs de tâches

## Auteure

**Ouafa Keriche** — Ingénieure systèmes d'information, développeuse Full Stack (Java, Spring Boot, Angular).
GitHub : [kericheouafa](https://github.com/kericheouafa)
