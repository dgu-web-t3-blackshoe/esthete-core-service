CREATE TABLE IF NOT EXISTS genres
(
    genre_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    genre_uuid BINARY(16) NOT NULL,
    genre_name VARCHAR(255) NOT NULL,
    UNIQUE (genre_uuid)
);

INSERT INTO genres (`genre_id`, `genre_name`, `genre_uuid`) VALUES (1, 'Portrait', UNHEX(REPLACE('e4302d24-2199-11ee-9ef2-0a0027000003', '-', '')));
INSERT INTO genres (`genre_id`, `genre_name`, `genre_uuid`) VALUES (2, 'Landscape', UNHEX(REPLACE('e43030eb-2199-11ee-9ef2-0a0027000003', '-', '')));
INSERT INTO genres (`genre_id`, `genre_name`, `genre_uuid`) VALUES (3, 'Street', UNHEX(REPLACE('e4303240-2199-11ee-9ef2-0a0027000003', '-', '')));
INSERT INTO genres (`genre_id`, `genre_name`, `genre_uuid`) VALUES (4, 'Food', UNHEX(REPLACE('e43032aa-2199-11ee-9ef2-0a0027000003', '-', '')));
INSERT INTO genres (`genre_id`, `genre_name`, `genre_uuid`) VALUES (5, 'Travel', UNHEX(REPLACE('e43032fb-2199-11ee-9ef2-0a0027000003', '-', '')));
INSERT INTO genres (`genre_id`, `genre_name`, `genre_uuid`) VALUES (6, 'Fashion', UNHEX(REPLACE('e430334a-2199-11ee-9ef2-0a0027000003', '-', '')));
INSERT INTO genres (`genre_id`, `genre_name`, `genre_uuid`) VALUES (7, 'Architectural', UNHEX(REPLACE('e4303394-2199-11ee-9ef2-0a0027000003', '-', '')));
INSERT INTO genres (`genre_id`, `genre_name`, `genre_uuid`) VALUES (8, 'Night', UNHEX(REPLACE('e43033e6-2199-11ee-9ef2-0a0027000003', '-', '')));
INSERT INTO genres (`genre_id`, `genre_name`, `genre_uuid`) VALUES (9, 'Sports', UNHEX(REPLACE('e4303432-2199-11ee-9ef2-0a0027000003', '-', '')));
INSERT INTO genres (`genre_id`, `genre_name`, `genre_uuid`) VALUES (10, 'Journalism', UNHEX(REPLACE('e4303475-2199-11ee-9ef2-0a0027000003', '-', '')));
INSERT INTO genres (`genre_id`, `genre_name`, `genre_uuid`) VALUES (11, 'Wildlife', UNHEX(REPLACE('e43034b6-2199-11ee-9ef2-0a0027000003', '-', '')));
INSERT INTO genres (`genre_id`, `genre_name`, `genre_uuid`) VALUES (12, 'Fine-art', UNHEX(REPLACE('e43034f7-2199-11ee-9ef2-0a0027000003', '-', '')));