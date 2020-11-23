create table client
(
    id     serial primary key,
    name   varchar,
    age    integer,
    gender varchar
);

INSERT INTO client (id, name, age, gender)
VALUES (1, 'joao', 25, 'M');
INSERT INTO client (id, name, age, gender)
VALUES (2, 'mateus', 35, 'M');
INSERT INTO client (id, name, age, gender)
VALUES (3, 'marcos', 45, 'M');
INSERT INTO client (id, name, age, gender)
VALUES (4, 'lucas', 55, 'M');
INSERT INTO client (id, name, age, gender)
VALUES (5, 'maria', 25, 'F');
INSERT INTO client (id, name, age, gender)
VALUES (6, 'suzana', 35, 'F');
INSERT INTO client (id, name, age, gender)
VALUES (7, 'ester', 45, 'F');