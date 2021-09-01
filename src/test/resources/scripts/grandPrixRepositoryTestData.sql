insert into circuit (id, name, location, country, latitude, longitude, altitude, url)
values ('1', 'Albert Park Grand Prix Circuit', 'Melbourne', 'Australia', '-37.8497', '144.968', '10',
        'http://en.wikipedia.org/wiki/Melbourne_Grand_Prix_Circuit');
insert into circuit (id, name, location, country, latitude, longitude, altitude, url)
values ('2', 'Sepang International Circuit', 'Kuala Lumpur', 'Malaysia', '2.76083', '101.738', '18',
        'http://en.wikipedia.org/wiki/Sepang_International_Circuit');
insert into circuit (id, name, location, country, latitude, longitude, altitude, url)
values ('3', 'Bahrain International Circuit', 'Sakhir', 'Bahrain', '26.0325', '50.5106', '7',
        'http://en.wikipedia.org/wiki/Bahrain_International_Circuit');


insert into grand_prix
(id, year, round, circuit_id, name, date, localization, country, time, url, driver_name)
values ('1', '2009', '1', '1', 'Australian Grand Prix', '2009-03-29', 'Melbourne', 'Australia', '06:00:00',
        'http://en.wikipedia.org/wiki/2009_Australian_Grand_Prix', 'Jenson Button');
insert into grand_prix
(id, year, round, circuit_id, name, date, localization, country, time, url, driver_name)
values ('2', '2009', '2', '2', 'Malaysian Grand Prix', '2009-04-05', 'Kuala Lumpur', 'Malaysia', '09:00:00',
        'http://en.wikipedia.org/wiki/2009_Malaysian_Grand_Prix', 'Jenson Button');
insert into grand_prix
(id, year, round, circuit_id, name, date, localization, country, time, url, driver_name)
values ('3', '2010', '1', '3', 'Bahrain Grand Prix', '2010-03-14', 'Sakhir', 'Bahrain', '12:00:00',
        'http://en.wikipedia.org/wiki/2010_Bahrain_Grand_Prix', 'Fernando Alonso');

insert into race_result (id, grand_prix_id, driver_number, driver_name, team_name, starting_grid_position,
                         finishing_position, points, laps, time, time_in_milliseconds, fastest_lap_time,
                         fastest_lap_speed, status, ranking, driver_id)
values ('1', '1', '44', 'Lewis Hamilton', 'McLaren', '1', '1', '10', '58', '1:34:50.616', '5690616', '1:27.452',
        '218.300', 'Finished', '2', '1');
insert into race_result (id, grand_prix_id, driver_number, driver_name, team_name, starting_grid_position,
                         finishing_position, points, laps, time, time_in_milliseconds, fastest_lap_time,
                         fastest_lap_speed, status, ranking, driver_id)
values ('2', '1', NULL, 'Lewis Hamilton', 'BMW Sauber', '5', '2', '8', '58', '+5.478', '5696094', '1:27.739', '217.586',
        'Finished', '3', '1');
insert into race_result (id, grand_prix_id, driver_number, driver_name, team_name, starting_grid_position,
                         finishing_position, points, laps, time, time_in_milliseconds, fastest_lap_time,
                         fastest_lap_speed, status, ranking, driver_id)
values ('3', '2', '88', 'Robert Kubica', 'BMW Sauber', '4', '2', '8', '56', '+19.570', '5498125', '1:35.921',
        '208.033', 'Finished', '6', '9');
insert into race_result (id, grand_prix_id, driver_number, driver_name, team_name, starting_grid_position,
                         finishing_position, points, laps, time, time_in_milliseconds, fastest_lap_time,
                         fastest_lap_speed, status, ranking, driver_id)
values ('4', '3', '88', 'Robert Kubica', 'BMW Sauber', '4', '2', '8', '56', '+19.570', '5498125', '1:35.921',
        '208.033', 'Finished', '6', '9');


insert into qualification_result (id, grand_prix_id, driver_number, driver_name, team_name, result, q1time, q2time,
                                  q3time, driver_id)
values ('1', '1', '22', 'Lewis Hamilton', 'McLaren', '1', '1:26.572', '1:25.187', '1:26.714', '1');

insert into qualification_result (id, grand_prix_id, driver_number, driver_name, team_name, result, q1time, q2time,
                                  q3time, driver_id)
values ('9', '1', '12', 'Timo Glock', 'Toyota', '9', '1:26.919', '1:26.164', '1:29.593', '10');

insert into qualification_result (id, grand_prix_id, driver_number, driver_name, team_name, result, q1time, q2time,
                                  q3time, driver_id)
values ('71', '2', '22', 'Lewis Hamilton', 'McLaren', '5', '1:21.366', '1:20.825', '1:22.096', '1');





