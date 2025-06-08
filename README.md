# MDD - Monde de Dév

## 🌟 Présentation du Projet

MDD (Monde de Dév) est une plateforme de réseau social dédiée aux développeurs. Cette application full-stack permet aux utilisateurs de s'abonner à des thèmes de développement, de lire et publier des articles, et d'interagir via un système de commentaires.

### 🎯 Fonctionnalités Principales

- **🔐 Authentification** : Inscription, connexion avec JWT
- **📝 Gestion d'Articles** : Création, lecture, modification, suppression d'articles
- **🏷️ Thèmes** : Abonnement à des thèmes de développement
- **💬 Commentaires** : Système de commentaires sur les articles
- **👤 Profil Utilisateur** : Gestion du profil et des abonnements
- **📱 Responsive Design** : Interface adaptative avec Angular Material

## 🛠️ Stack Technique

### Backend (Spring Boot)
- **Framework** : Spring Boot 3.2.3
- **Langage** : Java 21
- **Base de données** : MySQL
- **Sécurité** : Spring Security + JWT
- **ORM** : JPA/Hibernate
- **Build** : Maven

### Frontend (Angular)
- **Framework** : Angular 14.1.3
- **UI Library** : Angular Material
- **Langage** : TypeScript
- **Build** : Angular CLI
- **Styles** : SCSS

## 📋 Prérequis

- **Java** 21 ou supérieur
- **Node.js** 16 ou supérieur
- **npm** ou **yarn**
- **MySQL** 8.0 ou supérieur
- **Angular CLI** (`npm install -g @angular/cli`)

## 🚀 Installation et Lancement

### 1. Cloner le Repository

```bash
git clone <repository-url>
cd P6-Full-Stack-reseau-dev
```

### 2. Configuration de la Base de Données

Créer une base de données MySQL :

```sql
CREATE DATABASE mdd_db;
```

### 3. Backend (Spring Boot)

```bash
cd back

# Configuration de la base de données
# Éditer src/main/resources/application.properties avec vos paramètres MySQL

# Lancement
mvn spring-boot:run
```

Le backend sera accessible sur `http://localhost:8080`

### 4. Frontend (Angular)

```bash
cd front

# Installation des dépendances
npm install

# Lancement en mode développement
ng serve
```

Le frontend sera accessible sur `http://localhost:4200`

## 🔧 Configuration

### Backend - application.properties

```properties
# Base de données
spring.datasource.url=jdbc:mysql://localhost:3306/mdd_db?createDatabaseIfNotExist=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_password

# JWT
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000

# Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Frontend - environment.ts

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api'
};
```

## 📚 API Documentation

### 🔐 Authentification
- `POST /api/auth/register` - Inscription
- `POST /api/auth/login` - Connexion

### 📝 Articles
- `GET /api/articles` - Liste des articles
- `GET /api/articles/{id}` - Détail d'un article
- `POST /api/articles` - Créer un article
- `PUT /api/articles/{id}` - Modifier un article
- `DELETE /api/articles/{id}` - Supprimer un article
- `GET /api/articles/user/{userId}` - Articles d'un utilisateur
- `GET /api/articles/subscriptions/{userId}` - Articles des abonnements

### 🏷️ Thèmes
- `GET /api/themes` - Liste des thèmes
- `GET /api/themes/{id}` - Détail d'un thème
- `GET /api/themes/{id}/articles` - Articles d'un thème

### 📝 Abonnements
- `GET /api/users/{userId}/subscriptions` - Abonnements d'un utilisateur
- `POST /api/users/{userId}/subscriptions` - S'abonner à un thème

### 💬 Commentaires
- `GET /api/comments/article/{articleId}` - Commentaires d'un article
- `POST /api/comments` - Créer un commentaire

## 🏗️ Architecture

### Structure Backend
```
back/
├── src/main/java/com/openclassrooms/mddapi/
│   ├── controllers/     # Contrôleurs REST
│   ├── dto/            # Data Transfer Objects
│   ├── models/         # Entités JPA
│   ├── repositories/   # Repositories Spring Data
│   ├── services/       # Logique métier
│   └── security/       # Configuration sécurité
└── src/main/resources/
    ├── application.properties
    └── data.sql        # Données d'initialisation
```

### Structure Frontend
```
front/src/app/
├── components/         # Composants réutilisables
│   ├── article-form/
│   └── header/
├── pages/             # Pages de l'application
│   ├── auth/
│   ├── home/
│   ├── themes/
│   └── user-profile/
├── services/          # Services Angular
├── models/            # Modèles TypeScript
├── guards/            # Guards de routing
└── interceptors/      # Intercepteurs HTTP
```

## 🔒 Sécurité

- **Authentification JWT** : Tokens sécurisés avec expiration
- **Authorization** : Guards Angular + Spring Security
- **CORS** : Configuration pour communication cross-origin
- **Validation** : Validation côté client et serveur
- **Protection CSRF** : Protection contre les attaques CSRF

## 🤝 Contribution

1. Fork le projet
2. Créer une branche feature (`git checkout -b feature/AmazingFeature`)
3. Commit les changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request
