DROP TABLE IF EXISTS address_t;

CREATE TABLE address_t (
 id                     INT NOT NULL,
 line1                  VARCHAR(60) NOT NULL,
-- line2                  VARCHAR(60),
 PRIMARY KEY (id)
) ENGINE InnoDB;

INSERT INTO address_t (id, line1)
VALUES
(1001001, 'Room 101')
,(1001002, 'Room 102')
,(1001003, 'Room 103')
;

