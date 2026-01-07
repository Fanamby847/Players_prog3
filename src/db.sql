CREATE DATABASE mini_football_db;

CREATE USER mini_football_user WITH PASSWORD '

GRANT CREATE TABLE , SELECT ,INSERT ,UPTADE 
,DELETE ON DATABASE mini_football_db TO mini_football_user;

-- Création de la base de données
CREATE DATABASE mini_football_db;

-- Création de l'utilisateur
CREATE USER mini_football_db_manager WITH PASSWORD 'password';

-- Privilèges
GRANT ALL PRIVILEGES ON DATABASE mini_football_db TO mini_football_db_manager;
