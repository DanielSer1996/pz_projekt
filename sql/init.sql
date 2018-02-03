CREATE TABLE CAR_PARTS_CATEGORY_DICT
(   
    CATEGORY_ID NUMBER(5, 0) NOT NULL,
    CATEGORY_NAME VARCHAR2(100 BYTE) NOT NULL,
    CATEGORY_DESCRIPTION VARCHAR2(400 BYTE),
    CONSTRAINT CAR_PARTS_CATEGORY_DICT_PK PRIMARY KEY (CATEGORY_ID)
);

COMMENT ON TABLE CAR_PARTS_CATEGORY_DICT IS 'SŁOWNIK KATEGORII CZĘŚCI SAMOCHODOWYCH';
COMMENT ON COLUMN CAR_PARTS_CATEGORY_DICT.CATEGORY_ID IS 'KLUCZ GŁÓWNY';
COMMENT ON COLUMN CAR_PARTS_CATEGORY_DICT.CATEGORY_NAME IS 'NAZWA KATEGORII';
COMMENT ON COLUMN CAR_PARTS_CATEGORY_DICT.CATEGORY_DESCRIPTION IS 'OPIS KATEGORII';


CREATE TABLE CAR_PART
(
    CAR_PART_ID NUMBER(10, 0) NOT NULL,
    CAR_PART_NAME VARCHAR2(100 BYTE) NOT NULL,
    CAR_PART_PRODUCER VARCHAR2(32 BYTE),
    CAR_PART_PRODUCER_MODEL_CODE VARCHAR2(24 BYTE),
    CAR_PART_PRICE NUMBER(10,2),
    CATEGORY_ID NUMBER(5,0),
    CAR_PART_IMG_URI VARCHAR2(100 BYTE),
    CONSTRAINT CAR_PART_PK PRIMARY KEY (CAR_PART_ID),
    CONSTRAINT CATEGORY_ID_FK
    FOREIGN KEY (CATEGORY_ID) REFERENCES CAR_PARTS_CATEGORY_DICT(CATEGORY_ID)
);

COMMENT ON TABLE CAR_PART IS 'TABELA Z CZĘŚCIAMI SAMOCHODOWYMI';
COMMENT ON COLUMN CAR_PART.CAR_PART_ID IS 'KLUCZ GŁÓWNY';
COMMENT ON COLUMN CAR_PART.CAR_PART_NAME IS 'NAZWA KATALOGOWA CZĘŚCI';
COMMENT ON COLUMN CAR_PART.CAR_PART_PRODUCER IS 'PRODUCENT CZĘŚCI';
COMMENT ON COLUMN CAR_PART.CAR_PART_PRODUCER_MODEL_CODE IS 'MODEL (OZNACZENIE PRODUCENTA)';
COMMENT ON COLUMN CAR_PART.CAR_PART_PRICE IS 'CENA CZĘŚCI';
COMMENT ON COLUMN CAR_PART.CATEGORY_ID IS 'KLUCZ OBCY DO KATEGORII CZĘŚCI';
COMMENT ON COLUMN CAR_PART.CAR_PART_IMG_URI IS 'ŚCIEŻKA DO PLIKU Z OBRAZEM';


CREATE TABLE CAR_DICT
(
    CAR_ID NUMBER(10, 0) NOT NULL,
    CAR_BRAND VARCHAR2(32 BYTE) NOT NULL,
    CAR_MODEL VARCHAR2(32 BYTE) NOT NULL,
    CAR_PRODUCTION_START_DATE DATE NOT NULL,
    CAR_PRODUCTION_END_DATE DATE NOT NULL,
    CAR_IMAGE_URI VARCHAR2(100 BYTE),
    CONSTRAINT CAR_DICT_PK PRIMARY KEY (CAR_ID)
);

COMMENT ON TABLE CAR_DICT IS 'SŁOWNIK MODELI SAMOCHODÓW';
COMMENT ON COLUMN CAR_DICT.CAR_ID IS 'KLUCZ GŁÓWNY';
COMMENT ON COLUMN CAR_DICT.CAR_BRAND IS 'MARKA SAMOCHODU';
COMMENT ON COLUMN CAR_DICT.CAR_MODEL IS 'MODEL SAMOCHODU';
COMMENT ON COLUMN CAR_DICT.CAR_PRODUCTION_START_DATE IS 'DATA POCZĄTKU PRODUKCJI MODELU';
COMMENT ON COLUMN CAR_DICT.CAR_PRODUCTION_END_DATE IS 'DATA KOŃCA PRODUKCJI MODELU';
COMMENT ON COLUMN CAR_DICT.CAR_IMAGE_URI IS 'ŚCIEŻKA DO PLIKU Z OBRAZEM';


CREATE TABLE CAR_PART_REL
(
    CAR_ID NUMBER(10, 0) NOT NULL,
    CAR_PART_ID NUMBER(10, 0) NOT NULL,
    CONSTRAINT CAR_FK FOREIGN KEY (CAR_ID)
    REFERENCES CAR_DICT(CAR_ID),
    CONSTRAINT CAR_PART_FK FOREIGN KEY (CAR_PART_ID)
    REFERENCES CAR_PART(CAR_PART_ID)
);

COMMENT ON TABLE CAR_PART_REL IS 'RELACJA - DANA CZĘŚĆ PASUJE DO DANEGO MODELU';
COMMENT ON COLUMN CAR_PART_REL.CAR_ID IS 'KLUCZ OBCY DO MODELU SAMOCHODU';
COMMENT ON COLUMN CAR_PART_REL.CAR_PART_ID IS 'KLUCZ OBCY DO CZĘŚCI';

CREATE TABLE USERS
(
    LOGIN VARCHAR2(64 BYTE) PRIMARY KEY,
    STORED_PASSWORD VARCHAR2(400 BYTE)
);

COMMENT ON TABLE USERS IS 'Tabela zawierająca loginy i zakodowane hasła';
COMMENT ON COLUMN USERS.LOGIN IS 'Login admina (unikalny)';
COMMENT ON COLUMN USERS.STORED_PASSWORD IS 'Zaszyfrowane hasło';


