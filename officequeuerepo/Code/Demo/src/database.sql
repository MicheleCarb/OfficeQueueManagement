DROP DATABASE IF EXISTS office;

CREATE DATABASE office;

USE office;

CREATE TABLE request_type
(
    id       int         not null auto_increment primary key,
    tag_name varchar(30) not null,
    avg_time int         not null
);

CREATE TABLE request
(
    id          int  not null auto_increment primary key,
    ref_counter int  not null,
    ref_type    int  not null,
    date        date not null,
    foreign key (ref_type) references request_type (id)
);

-- Testing data --
INSERT INTO request_type (tag_name, avg_time)
VALUES ('Shipping', 12.4);
INSERT INTO request_type (tag_name, avg_time)
VALUES ('Invoices', 30.4);
INSERT INTO request_type (tag_name, avg_time)
VALUES ('Credit Card', 50.4);

INSERT INTO request (ref_counter, ref_type, date)
VALUES (1, 1, '2020-10-18');
INSERT INTO request (ref_counter, ref_type, date)
VALUES (1, 2, '2020-10-18');
INSERT INTO request (ref_counter, ref_type, date)
VALUES (2, 3, '2020-10-18');
INSERT INTO request (ref_counter, ref_type, date)
VALUES (1, 1, '2020-09-18');
INSERT INTO request (ref_counter, ref_type, date)
VALUES (1, 2, '2020-10-17');
INSERT INTO request (ref_counter, ref_type, date)
VALUES (1, 2, '2020-10-18');

