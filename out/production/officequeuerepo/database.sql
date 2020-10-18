CREATE DATABASE office;

USE office;

CREATE TABLE request_type (
                              id int not null auto_increment primary key ,
                              tag_name varchar(30) not null,
                              avg_time int not null
);

CREATE TABLE request (
    id int not null auto_increment primary key ,
    counter int not null,
    ref_type int not null,
    date date not null,
    foreign key (ref_type) references request_type(id)
);

