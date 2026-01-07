
CREATE TYPE continent_enum AS ENUM ('AFRICA', 'EUROPA', 'ASIA', 'AMERICA');
CREATE TYPE player_position_enum AS ENUM ('GK', 'DEF', 'MIDF', 'STR');


CREATE TABLE team (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      continent continent_enum NOT NULL
);

CREATE TABLE player (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        age INT NOT NULL,
                        position player_position_enum NOT NULL,
                        id_team INT,
                        CONSTRAINT fk_team FOREIGN KEY (id_team) REFERENCES team(id) ON DELETE SET NULL
);
