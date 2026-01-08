-- Socle Technique & Administratif

CREATE TABLE IF NOT EXISTS Cabinet_Medical (
                                               idCabinet BIGSERIAL PRIMARY KEY,
                                               nom VARCHAR(255),
    email VARCHAR(255),
    logo VARCHAR(255),
    adresse VARCHAR(255),
    cin VARCHAR(255),
    tel1 VARCHAR(255),
    tel2 VARCHAR(255),
    siteWeb VARCHAR(255),
    instagram VARCHAR(255),
    facebook VARCHAR(255),
    description TEXT,
    dateCreation DATE,
    dateDerniereModification TIMESTAMP,
    modifiePar VARCHAR(255),
    creePar VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS Role (
                                    idRole BIGSERIAL PRIMARY KEY,
                                    libelle VARCHAR(50)
    );

CREATE TABLE IF NOT EXISTS Role_Privileges (
                                               idRole BIGINT,
                                               privilege VARCHAR(255),
    FOREIGN KEY (idRole) REFERENCES Role(idRole)
    );

CREATE TABLE IF NOT EXISTS Utilisateur (
                                           idUser BIGSERIAL PRIMARY KEY,
                                           nom VARCHAR(255),
    email VARCHAR(255),
    adresse VARCHAR(255),
    cin VARCHAR(255),
    tel VARCHAR(255),
    sexe VARCHAR(50),
    login VARCHAR(255),
    motDePasse VARCHAR(255),
    lastLoginDate DATE,
    dateNaissance DATE,
    dateRecrutement DATE,
    token VARCHAR(255),
    idCabinet BIGINT,
    idRole BIGINT,
    dateCreation DATE,
    dateDerniereModification TIMESTAMP,
    modifiePar VARCHAR(255),
    creePar VARCHAR(255),
    FOREIGN KEY (idCabinet) REFERENCES Cabinet_Medical(idCabinet),
    FOREIGN KEY (idRole) REFERENCES Role(idRole)
    );

CREATE TABLE IF NOT EXISTS Notification (
                                            idNotification BIGSERIAL PRIMARY KEY,
                                            titre VARCHAR(50),
    message TEXT,
    date DATE,
    time TIME,
    type VARCHAR(50),
    priorite VARCHAR(50),
    idUser BIGINT,
    FOREIGN KEY (idUser) REFERENCES Utilisateur(idUser)
    );

CREATE TABLE IF NOT EXISTS Statistique (
                                           idStatistique BIGSERIAL PRIMARY KEY,
                                           nom VARCHAR(255),
    categorie VARCHAR(50),
    chiffre DOUBLE PRECISION,
    dateCalcul DATE,
    idCabinet BIGINT,
    FOREIGN KEY (idCabinet) REFERENCES Cabinet_Medical(idCabinet)
    );

-- Ressources Humaines

CREATE TABLE IF NOT EXISTS Admin (
                                     idAdmin BIGSERIAL PRIMARY KEY,
                                     idUser BIGINT,
                                     FOREIGN KEY (idUser) REFERENCES Utilisateur(idUser)
    );

CREATE TABLE IF NOT EXISTS Staff (
                                     idStaff BIGSERIAL PRIMARY KEY,
                                     salaire DOUBLE PRECISION,
                                     prime DOUBLE PRECISION,
                                     soldConge INT,
                                     etat BOOLEAN,
                                     idUser BIGINT,
                                     FOREIGN KEY (idUser) REFERENCES Utilisateur(idUser)
    );

CREATE TABLE IF NOT EXISTS Medecin (
                                       idMedecin BIGSERIAL PRIMARY KEY,
                                       specialite VARCHAR(255),
    idUser BIGINT,
    FOREIGN KEY (idUser) REFERENCES Utilisateur(idUser)
    );

CREATE TABLE IF NOT EXISTS Secretaire (
                                          idSecretaire BIGSERIAL PRIMARY KEY,
                                          numCNSS VARCHAR(255),
    comission DOUBLE PRECISION,
    idUser BIGINT,
    FOREIGN KEY (idUser) REFERENCES Utilisateur(idUser)
    );

CREATE TABLE IF NOT EXISTS AgendaMensuel (
                                             idAgenda BIGSERIAL PRIMARY KEY,
                                             mois VARCHAR(50),
    etat BOOLEAN,
    idMedecin BIGINT,
    FOREIGN KEY (idMedecin) REFERENCES Medecin(idMedecin)
    );

CREATE TABLE IF NOT EXISTS Agenda_Jours_Non_Disponible (
                                                           idAgenda BIGINT,
                                                           jour DATE,
                                                           FOREIGN KEY (idAgenda) REFERENCES AgendaMensuel(idAgenda)
    );

-- Dossier Patient & Médical

CREATE TABLE IF NOT EXISTS Patient (
    idPatient BIGSERIAL PRIMARY KEY,
    nom VARCHAR(255),
    prenom VARCHAR(255),
    dateDeNaissance DATE,
    sexe VARCHAR(50),
    adresse VARCHAR(255),
    telephone VARCHAR(255),
    assurance VARCHAR(50),
    dateCreation DATE,
    dateDerniereModification TIMESTAMP,
    modifiePar VARCHAR(255),
    creePar VARCHAR(255)
    );


CREATE TABLE IF NOT EXISTS Antecedents (
                                           idAntecedent BIGSERIAL PRIMARY KEY,
                                           nom VARCHAR(255),
    catégorie VARCHAR(255),
    niveauDeRisque VARCHAR(50),
    idPatient BIGINT,
    FOREIGN KEY (idPatient) REFERENCES Patient(idPatient)
    );

CREATE TABLE IF NOT EXISTS RDV (
                                   idRDV BIGSERIAL PRIMARY KEY,
                                   date DATE,
                                   heure TIME,
                                   motif VARCHAR(255),
    statut VARCHAR(50),
    noteMedecin TEXT,
    idPatient BIGINT,
    idMedecin BIGINT,
    idSecretaire BIGINT,
    FOREIGN KEY (idPatient) REFERENCES Patient(idPatient),
    FOREIGN KEY (idMedecin) REFERENCES Medecin(idMedecin),
    FOREIGN KEY (idSecretaire) REFERENCES Secretaire(idSecretaire)
    );

CREATE TABLE IF NOT EXISTS Consultation (
                                            idConsultation BIGSERIAL PRIMARY KEY,
                                            date DATE,
                                            statut VARCHAR(50),
    observationMedecin TEXT,
    idRDV BIGINT,
    FOREIGN KEY (idRDV) REFERENCES RDV(idRDV)
    );

CREATE TABLE IF NOT EXISTS InterventionMédecin (
                                                   idIM BIGSERIAL PRIMARY KEY,
                                                   prixDePatient DOUBLE PRECISION,
                                                   idConsultation BIGINT,
                                                   idMedecin BIGINT,
                                                   FOREIGN KEY (idConsultation) REFERENCES Consultation(idConsultation),
    FOREIGN KEY (idMedecin) REFERENCES Medecin(idMedecin)
    );

CREATE TABLE IF NOT EXISTS Acte (
                                    idActe BIGSERIAL PRIMARY KEY,
                                    libellé VARCHAR(255),
    catégorie VARCHAR(255),
    prixDeBase DOUBLE PRECISION,
    description TEXT,
    code INT
    );

CREATE TABLE IF NOT EXISTS Acte_Intervention (
                                                 idActeIntervention BIGSERIAL PRIMARY KEY,
                                                 idActe BIGINT,
                                                 idIM BIGINT,
                                                 FOREIGN KEY (idActe) REFERENCES Acte(idActe),
    FOREIGN KEY (idIM) REFERENCES InterventionMédecin(idIM)
    );

CREATE TABLE IF NOT EXISTS Certificat (
                                          idCertif BIGSERIAL PRIMARY KEY,
                                          dateDébut DATE,
                                          dateFin DATE,
                                          durée INT,
                                          noteMedecin TEXT,
                                          idPatient BIGINT,
                                          idMedecin BIGINT,
                                          FOREIGN KEY (idPatient) REFERENCES Patient(idPatient),
    FOREIGN KEY (idMedecin) REFERENCES Medecin(idMedecin)
    );

-- Pharmacie

CREATE TABLE IF NOT EXISTS Médicament (
                                          idMct BIGSERIAL PRIMARY KEY,
                                          nom VARCHAR(255),
    laboratoire VARCHAR(255),
    type VARCHAR(255),
    forme VARCHAR(50),
    remboursable BOOLEAN,
    prixUnitaire DOUBLE PRECISION,
    description TEXT
    );

CREATE TABLE IF NOT EXISTS Ordannance (
                                          idOrd BIGSERIAL PRIMARY KEY,
                                          date DATE,
                                          idPatient BIGINT,
                                          idMedecin BIGINT,
                                          FOREIGN KEY (idPatient) REFERENCES Patient(idPatient),
    FOREIGN KEY (idMedecin) REFERENCES Medecin(idMedecin)
    );

CREATE TABLE IF NOT EXISTS Prescription (
                                            idPr BIGSERIAL PRIMARY KEY,
                                            quantité INT,
                                            fréquence VARCHAR(255),
    duréeEnJours INT,
    idOrd BIGINT,
    idMct BIGINT,
    FOREIGN KEY (idOrd) REFERENCES Ordannance(idOrd),
    FOREIGN KEY (idMct) REFERENCES Médicament(idMct)
    );

-- Finances

CREATE TABLE IF NOT EXISTS Facture (
                                       idFacture BIGSERIAL PRIMARY KEY,
                                       totaleFacture DOUBLE PRECISION,
                                       totalePayé DOUBLE PRECISION,
                                       reste DOUBLE PRECISION,
                                       statut VARCHAR(50),
    dateFacture TIMESTAMP,
    idConsultation BIGINT,
    FOREIGN KEY (idConsultation) REFERENCES Consultation(idConsultation)
    );

CREATE TABLE IF NOT EXISTS SituationFinanciere (
                                                   idSF BIGSERIAL PRIMARY KEY,
                                                   totaleDesActes DOUBLE PRECISION,
                                                   totalePayé DOUBLE PRECISION,
                                                   crédit DOUBLE PRECISION,
                                                   statut VARCHAR(50),
    enPromo VARCHAR(50),
    idFacture BIGINT,
    FOREIGN KEY (idFacture) REFERENCES Facture(idFacture)
    );

CREATE TABLE IF NOT EXISTS Revenue (
                                       idRevenue BIGSERIAL PRIMARY KEY,
                                       titre VARCHAR(255),
    description TEXT,
    montant DOUBLE PRECISION,
    date TIMESTAMP,
    idCabinet BIGINT,
    FOREIGN KEY (idCabinet) REFERENCES Cabinet_Medical(idCabinet)
    );

CREATE TABLE IF NOT EXISTS Charge (
                                      idCharge BIGSERIAL PRIMARY KEY,
                                      titre VARCHAR(255),
    description TEXT,
    montant DOUBLE PRECISION,
    date TIMESTAMP,
    idCabinet BIGINT,
    FOREIGN KEY (idCabinet) REFERENCES Cabinet_Medical(idCabinet)
    );