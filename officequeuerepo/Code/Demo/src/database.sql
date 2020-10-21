DROP DATABASE IF EXISTS office;

CREATE DATABASE office;

USE office;

CREATE TABLE request
(
    id            int         not null auto_increment primary key,
    ref_counter   int         not null,
    type_tag_name varchar(30) not null,
    date          date        not null
);

-- Testing data --
INSERT INTO request (ref_counter, type_tag_name, date)
VALUES (1, 'Bollette', '2020-10-18');
INSERT INTO request (ref_counter, type_tag_name, date)
VALUES (1, 'Pacchi', '2020-10-18');
INSERT INTO request (ref_counter, type_tag_name, date)
VALUES (2, 'Ricariche', '2020-10-18');
INSERT INTO request (ref_counter, type_tag_name, date)
VALUES (1, 'Bollette', '2020-09-18');
INSERT INTO request (ref_counter, type_tag_name, date)
VALUES (1, 'Pacchi', '2020-10-17');
INSERT INTO request (ref_counter, type_tag_name, date)
VALUES (1, 'Pacchi', '2020-10-18');

