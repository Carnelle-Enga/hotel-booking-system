CREATE TYPE room_type AS ENUM (
    'SIMPLE',
    'DOUBLE',
    'SUITE'
);

CREATE TABLE rooms (

                       id BIGSERIAL PRIMARY KEY,

                       numero VARCHAR(50) NOT NULL UNIQUE,

                       room_type room_type NOT NULL,

                       prix_par_nuit DECIMAL(10,2) NOT NULL,

                       disponible BOOLEAN NOT NULL DEFAULT true
);