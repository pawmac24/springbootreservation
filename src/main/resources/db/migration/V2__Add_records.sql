INSERT INTO person (id, name, pesel, surname) values (nextval('person_seq'), 'Andrzej', '80092205517', 'Markowski');

INSERT INTO reservation
(id, comment, create_date, person_id, price, status, time_interval) values
(nextval('reservation_seq'),'comment1', '2015-09-08 12:30:00.000', (SELECT id from person where pesel='80092205517'), 100.00, 0, 1800);

INSERT INTO reservation
(id, comment, create_date, person_id, price, status, time_interval) values
(nextval('reservation_seq'),'comment2', '2015-09-08 13:00:00.000', (SELECT id from person where pesel='80092205517'), 200.00, 0, 2400);

INSERT INTO reservation
(id, comment, create_date, person_id, price, status, time_interval) values
(nextval('reservation_seq'),'comment ...', '2015-09-08 13:00:00.000', (SELECT id from person where pesel='80092205517'), 200.00, 2, 2400);

INSERT INTO reservation
(id, comment, create_date, person_id, price, status, time_interval) values
(nextval('reservation_seq'),'comment ...', (select current_timestamp - (10 * interval '1 minute')), (SELECT id from person where pesel='80092205517'), 350.00, 0, 2400);

-------------------------------

INSERT INTO person (id, name, pesel, surname) values (nextval('person_seq'), 'Pawel', '75040201624', 'Kurski');

INSERT INTO reservation
(id, comment, create_date, person_id, price, status, time_interval) values
(nextval('reservation_seq'),'comment ...', '2015-09-20 11:00:00.000', (SELECT id from person where pesel='75040201624'), 300.00, 0, 2400);

INSERT INTO reservation
(id, comment, create_date, person_id, price, status, time_interval) values
(nextval('reservation_seq'),'comment ...', '2015-09-15 08:30:00.000', (SELECT id from person where pesel='75040201624'), 400.00, 1, 2400);
