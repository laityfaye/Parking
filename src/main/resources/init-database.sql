-- Création de la table cars si elle n'existe pas
CREATE TABLE IF NOT EXISTS cars (
    id SERIAL PRIMARY KEY,
    license_plate VARCHAR(20) NOT NULL UNIQUE,
    entry_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Création d'une table pour l'historique des entrées/sorties
CREATE TABLE IF NOT EXISTS parking_log (
    id SERIAL PRIMARY KEY,
    license_plate VARCHAR(20) NOT NULL,
    entry_time TIMESTAMP,
    exit_time TIMESTAMP,
    duration_minutes INT
);

-- Création d'une fonction pour enregistrer les sorties
CREATE OR REPLACE FUNCTION log_car_exit()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO parking_log (license_plate, entry_time, exit_time, duration_minutes)
    VALUES (OLD.license_plate, OLD.entry_time, NOW(), 
            EXTRACT(EPOCH FROM (NOW() - OLD.entry_time))/60);
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

-- Création d'un trigger pour enregistrer les sorties
CREATE OR REPLACE TRIGGER car_exit_trigger
AFTER DELETE ON cars
FOR EACH ROW
EXECUTE FUNCTION log_car_exit();