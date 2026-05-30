CREATE TABLE IF NOT EXISTS capacidad (
      id           BIGINT AUTO_INCREMENT PRIMARY KEY,
      nombre       VARCHAR(50) NOT NULL UNIQUE,
      descripcion  VARCHAR(90) NOT NULL
    );

CREATE TABLE IF NOT EXISTS capacidad_tecnologia (
       capacidad_id   BIGINT NOT NULL,
       tecnologia_id  BIGINT NOT NULL,
       PRIMARY KEY (capacidad_id, tecnologia_id)
    );