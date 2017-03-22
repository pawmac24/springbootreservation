CREATE SEQUENCE reservation_seq START 1;
CREATE SEQUENCE person_seq START 1;

CREATE TABLE person (
  id bigint not null,
  name varchar(25) not null,
  pesel varchar(25) not null UNIQUE,
  surname varchar(25) not null,
  primary key (id)
);

CREATE TABLE reservation
(
  id bigint not null,
  comment varchar(255) not null,
  create_date timestamp not null,
  price numeric(19,2) not null,
  status integer not null,
  time_interval bigint not null,
  person_id bigint REFERENCES person (id),
  primary key (id)
);
