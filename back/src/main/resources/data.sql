-- Création de la table de liaison article_themes si elle n'existe pas
CREATE TABLE IF NOT EXISTS article_themes (
    article_id BIGINT NOT NULL,
    theme_id BIGINT NOT NULL,
    PRIMARY KEY (article_id, theme_id),
    FOREIGN KEY (article_id) REFERENCES articles(id) ON DELETE CASCADE,
    FOREIGN KEY (theme_id) REFERENCES themes(id) ON DELETE CASCADE
);

-- Insertion des thèmes au démarrage de l'application
INSERT INTO themes (id, title, description, created_at) 
SELECT 1, 'Java', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed non risus. Suspendisse lectus tortor, dignissim sit amet, adipiscing nec, ultricies sed, dolor. Cras elementum ultrices diam.', CURRENT_TIMESTAMP
FROM dual 
WHERE NOT EXISTS (SELECT 1 FROM themes WHERE id = 1);

INSERT INTO themes (id, title, description, created_at) 
SELECT 2, 'Angular', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum volutpat pretium libero. Cras id dui. Aenean ut eros et nisl sagittis vestibulum. Nullam nulla eros.', CURRENT_TIMESTAMP
FROM dual 
WHERE NOT EXISTS (SELECT 1 FROM themes WHERE id = 2);

INSERT INTO themes (id, title, description, created_at) 
SELECT 3, 'TypeScript', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero.', CURRENT_TIMESTAMP
FROM dual 
WHERE NOT EXISTS (SELECT 1 FROM themes WHERE id = 3);

INSERT INTO themes (id, title, description, created_at) 
SELECT 4, 'Totoro', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce neque. Suspendisse faucibus, nunc et pellentesque egestas, lacus ante convallis tellus, vitae iaculis lacus elit id tortor.', CURRENT_TIMESTAMP
FROM dual 
WHERE NOT EXISTS (SELECT 1 FROM themes WHERE id = 4);

INSERT INTO themes (id, title, description, created_at) 
SELECT 5, 'Clean Code', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh.', CURRENT_TIMESTAMP
FROM dual 
WHERE NOT EXISTS (SELECT 1 FROM themes WHERE id = 5);

INSERT INTO themes (id, title, description, created_at) 
SELECT 6, 'Clean Architecture', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis.', CURRENT_TIMESTAMP
FROM dual 
WHERE NOT EXISTS (SELECT 1 FROM themes WHERE id = 6);
