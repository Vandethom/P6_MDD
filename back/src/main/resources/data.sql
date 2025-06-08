
-- ========================================
-- NETTOYAGE DE LA BASE DE DONNÉES
-- ========================================
-- Suppression des données dans l'ordre correct (contraintes FK)

-- 1. Suppression des commentaires (dépendent des articles et users)
DELETE FROM comments;

-- 2. Suppression des abonnements (dépendent des users et themes)
DELETE FROM subscriptions;

-- 3. Suppression de la table de liaison article_themes
DELETE FROM article_themes;

-- 4. Suppression des articles (dépendent des users et themes)
DELETE FROM articles;

-- 5. Suppression des utilisateurs
DELETE FROM users;

-- 6. Suppression des thèmes (en dernier car référencés par articles)
DELETE FROM themes;

-- Reset des auto-increment (MySQL)
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE themes AUTO_INCREMENT = 1;
ALTER TABLE articles AUTO_INCREMENT = 1;
ALTER TABLE comments AUTO_INCREMENT = 1;
ALTER TABLE subscriptions AUTO_INCREMENT = 1;

-- ========================================
-- CRÉATION DES TABLES SI NÉCESSAIRE
-- ========================================

-- Création de la table de liaison article_themes si elle n'existe pas
CREATE TABLE IF NOT EXISTS article_themes (
    article_id BIGINT NOT NULL,
    theme_id BIGINT NOT NULL,
    PRIMARY KEY (article_id, theme_id),
    FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,
    FOREIGN KEY (theme_id) REFERENCES themes(id) ON DELETE CASCADE
);

-- ========================================
-- POPULATION DES DONNÉES DE TEST
-- ========================================

-- THÈMES
INSERT INTO themes (id, title, description, created_at) VALUES 
(1, 'Java', 'Tout sur le langage Java : syntaxe, frameworks, bonnes pratiques et nouveautés. Apprenez Spring Boot, Maven, et les design patterns essentiels.', CURRENT_TIMESTAMP),
(2, 'Angular', 'Framework frontend moderne pour créer des applications web dynamiques. TypeScript, RxJS, directives, services et routing expliqués.', CURRENT_TIMESTAMP),
(3, 'TypeScript', 'Superset de JavaScript qui ajoute le typage statique. Interfaces, génériques, décorateurs et intégration avec les frameworks modernes.', CURRENT_TIMESTAMP),
(4, 'Spring Boot', 'Framework Java pour créer des applications robustes rapidement. Auto-configuration, starter dependencies et microservices.', CURRENT_TIMESTAMP),
(5, 'Clean Code', 'Principes et pratiques pour écrire du code maintenable et lisible. SOLID, refactoring, tests unitaires et architecture propre.', CURRENT_TIMESTAMP),
(6, 'Clean Architecture', 'Architecture en couches pour des applications maintenables. Séparation des responsabilités, dependency inversion et ports/adapters.', CURRENT_TIMESTAMP);

-- UTILISATEURS (mots de passe: "password123" hashé avec BCrypt)
INSERT INTO users (id, username, email, password, created_at) VALUES 
(1, 'john.doe', 'john.doe@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', CURRENT_TIMESTAMP),
(2, 'jane.smith', 'jane.smith@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', CURRENT_TIMESTAMP),
(3, 'developer.pro', 'dev.pro@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', CURRENT_TIMESTAMP),
(4, 'alice.martin', 'alice.martin@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', CURRENT_TIMESTAMP),
(5, 'bob.wilson', 'bob.wilson@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', CURRENT_TIMESTAMP);

-- ARTICLES (avec des dates factices pour tester le tri)
INSERT INTO articles (id, title, content, author_id, theme_id, created_at, updated_at) VALUES 
(1, 'Introduction à Spring Boot 3', 
'Spring Boot 3 apporte de nombreuses nouveautés avec le support de Java 17 et GraalVM. Cette version introduit des améliorations significatives en termes de performance et de sécurité. 

Dans cet article, nous explorerons les nouvelles fonctionnalités principales :
- Support natif de GraalVM pour des temps de démarrage ultra-rapides
- Migration vers Jakarta EE 9
- Nouvelles annotations pour l''observabilité
- Amélioration du support des microservices

Ces changements rendent Spring Boot encore plus adapté aux architectures cloud-native et aux déploiements containerisés.', 
1, 4, '2024-01-15 10:30:00', '2024-01-15 10:30:00'),

(2, 'Les composants Angular : Guide complet', 
'Les composants sont le cœur d''Angular. Ils encapsulent la logique métier, le template HTML et les styles CSS dans une unité réutilisable.

Structure d''un composant :
- @Component decorator avec selector, template et styles
- Classe TypeScript avec propriétés et méthodes
- Cycle de vie avec OnInit, OnDestroy, etc.
- Communication parent-enfant via @Input et @Output

Best practices :
- Utiliser OnPush change detection quand possible
- Séparer la logique métier dans des services
- Implémenter les interfaces de cycle de vie appropriées', 
2, 2, '2024-02-20 14:15:00', '2024-02-20 14:15:00'),

(3, 'TypeScript : Types avancés et Génériques', 
'TypeScript offre un système de types puissant qui va bien au-delà des types primitifs. Les types avancés permettent de créer des APIs type-safe et expressives.

Types utilitaires :
- Partial<T> : rend toutes les propriétés optionnelles
- Required<T> : rend toutes les propriétés obligatoires  
- Pick<T, K> : sélectionne certaines propriétés
- Omit<T, K> : exclut certaines propriétés

Génériques :
- Fonctions génériques pour la réutilisabilité
- Contraintes avec extends
- Types conditionnels avec ternaire
- Mapped types pour la transformation', 
3, 3, '2024-03-10 09:45:00', '2024-03-10 09:45:00'),

(4, 'Principes SOLID en Java', 
'Les principes SOLID sont fondamentaux pour écrire du code maintenable et extensible. Chaque principe adresse un aspect spécifique de la conception orientée objet.

S - Single Responsibility : Une classe ne doit avoir qu''une seule raison de changer
O - Open/Closed : Ouvert à l''extension, fermé à la modification
L - Liskov Substitution : Les objets dérivés doivent être substituables
I - Interface Segregation : Préférer plusieurs interfaces spécifiques
D - Dependency Inversion : Dépendre d''abstractions, pas de concrétions

Ces principes, appliqués avec discernement, améliorent significativement la qualité du code.', 
1, 5, '2024-04-05 16:20:00', '2024-04-05 16:20:00'),

(5, 'Architecture hexagonale avec Spring', 
'L''architecture hexagonale, ou ports et adaptateurs, sépare clairement la logique métier des détails techniques. Cette approche facilite les tests et la maintenance.

Structure :
- Domain : entités métier et règles de gestion
- Application : cas d''usage et services applicatifs  
- Infrastructure : adaptateurs pour bases de données, APIs, etc.
- Interfaces : ports d''entrée et de sortie

Avantages :
- Indépendance vis-à-vis des frameworks
- Facilité de test avec des mocks
- Évolutivité et maintenabilité accrues', 
4, 6, '2024-05-12 11:10:00', '2024-05-12 11:10:00'),

(6, 'Java 21 : Les nouveautés', 
'Java 21 est une version LTS qui apporte des fonctionnalités attendues depuis longtemps. Virtual Threads et Pattern Matching révolutionnent la programmation Java.

Nouveautés principales :
- Virtual Threads (Project Loom) pour la concurrence
- Pattern Matching for switch (finalisé)
- Record Patterns pour la déstructuration
- String Templates (preview)
- Sequenced Collections

Ces fonctionnalités modernisent Java et le rendent plus expressif tout en conservant la compatibilité ascendante.', 
5, 1, '2024-06-18 13:55:00', '2024-06-18 13:55:00'),

(7, 'RxJS et la programmation réactive en Angular', 
'RxJS est au cœur d''Angular pour gérer l''asynchrone et les événements. La programmation réactive change la façon de penser les flux de données.

Concepts clés :
- Observable : flux de données dans le temps
- Operators : map, filter, mergeMap, switchMap
- Subjects : BehaviorSubject, ReplaySubject
- Error handling avec catchError et retry

Patterns utiles :
- Gestion d''état avec services et BehaviorSubject
- Debouncing pour les recherches
- Combinaison de flux avec combineLatest', 
2, 2, '2024-07-22 08:30:00', '2024-07-22 08:30:00'),

(8, 'Tests unitaires avec JUnit 5', 
'JUnit 5 modernise l''écriture de tests en Java avec de nouvelles annotations et fonctionnalités. Les tests deviennent plus expressifs et maintenables.

Nouveautés :
- @DisplayName pour des noms explicites
- @ParameterizedTest pour les tests paramétrés
- @TestInstance pour le cycle de vie
- Assertions améliorées avec assertAll
- Extensions personnalisées

Best practices :
- Arrange, Act, Assert (AAA pattern)
- Un seul concept testé par test
- Noms de tests explicites', 
3, 1, '2024-08-14 15:40:00', '2024-08-14 15:40:00');

-- ABONNEMENTS (users abonnés à différents thèmes)
INSERT INTO subscriptions (id, user_id, theme_id, created_at) VALUES
(1, 1, 1, CURRENT_TIMESTAMP), -- john.doe -> Java
(2, 1, 4, CURRENT_TIMESTAMP), -- john.doe -> Spring Boot  
(3, 1, 5, CURRENT_TIMESTAMP), -- john.doe -> Clean Code
(4, 2, 2, CURRENT_TIMESTAMP), -- jane.smith -> Angular
(5, 2, 3, CURRENT_TIMESTAMP), -- jane.smith -> TypeScript
(6, 3, 1, CURRENT_TIMESTAMP), -- developer.pro -> Java
(7, 3, 5, CURRENT_TIMESTAMP), -- developer.pro -> Clean Code
(8, 3, 6, CURRENT_TIMESTAMP), -- developer.pro -> Clean Architecture
(9, 4, 6, CURRENT_TIMESTAMP), -- alice.martin -> Clean Architecture
(10, 4, 4, CURRENT_TIMESTAMP), -- alice.martin -> Spring Boot
(11, 5, 1, CURRENT_TIMESTAMP), -- bob.wilson -> Java
(12, 5, 2, CURRENT_TIMESTAMP); -- bob.wilson -> Angular

-- COMMENTAIRES (avec des dates factices)
INSERT INTO comments (id, content, user_id, article_id, created_at) VALUES
(1, 'Excellent article ! Spring Boot 3 semble vraiment prometteur avec GraalVM. As-tu déjà testé les performances en production ?', 2, 1, '2024-01-16 09:15:00'),
(2, 'Merci pour ce guide complet. La partie sur les composants réactifs est particulièrement utile pour mon projet actuel.', 3, 2, '2024-02-21 16:30:00'),
(3, 'Les types conditionnels de TypeScript sont un vrai game-changer. Peux-tu donner un exemple concret d''utilisation ?', 1, 3, '2024-03-11 11:20:00'),
(4, 'SOLID est effectivement fondamental. J''ajouterais que l''application de ces principes doit rester pragmatique selon le contexte.', 4, 4, '2024-04-06 14:45:00'),
(5, 'L''architecture hexagonale a transformé notre façon de structurer les projets. Plus jamais de couplage fort avec les frameworks !', 2, 5, '2024-05-13 08:25:00'),
(6, 'Virtual Threads vont révolutionner la concurrence en Java. Enfin une alternative simple aux CompletableFuture !', 1, 6, '2024-06-19 17:10:00'),
(7, 'RxJS peut être déroutant au début, mais une fois maîtrisé, c''est un outil incroyablement puissant pour gérer les flux complexes.', 4, 7, '2024-07-23 12:40:00'),
(8, 'JUnit 5 est un vrai plaisir à utiliser. Les tests paramétrés ont grandement simplifié notre suite de tests.', 5, 8, '2024-08-15 10:20:00'),
(9, '@john.doe Pour GraalVM, j''ai testé en préproduction et le gain en temps de démarrage est impressionnant, surtout pour les microservices.', 1, 1, '2024-01-17 13:30:00'),
(10, 'Pourrais-tu faire un article de suivi sur les tests d''intégration avec Spring Boot 3 ?', 3, 1, '2024-01-18 15:45:00'),
(11, 'La gestion d''état avec NgRx serait un excellent complément à cet article sur RxJS.', 5, 7, '2024-07-24 09:15:00'),
(12, 'Très bon rappel sur SOLID. Dans notre équipe, nous organisons des code reviews spécifiquement sur ces principes.', 3, 4, '2024-04-07 16:20:00'),
(13, 'L''exemple d''architecture hexagonale est clair. As-tu des recommandations de ressources pour approfondir ?', 2, 5, '2024-05-14 11:35:00'),
(14, 'Java 21 LTS arrive à point nommé. Les Virtual Threads vont simplifier tant de choses dans nos applications serveur.', 4, 6, '2024-06-20 14:50:00'),
(15, 'Excellent point sur le debouncing ! C''est souvent oublié mais tellement important pour les performances UX.', 1, 7, '2024-07-25 18:25:00');
