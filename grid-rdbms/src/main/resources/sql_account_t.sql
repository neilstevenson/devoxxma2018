DROP TABLE IF EXISTS account_t;

CREATE TABLE account_t (
 id                     INT NOT NULL,
 firstname              VARCHAR(60) NOT NULL,
 lastname               VARCHAR(60) NOT NULL,
 opened                 TIMESTAMP NOT NULL,
 actual_balance	        INT NOT NULL,
 effective_balance      INT NOT NULL,
 credit_limit           INT NOT NULL,
 PRIMARY KEY (id)
) ENGINE InnoDB;


INSERT INTO account_t (id, firstname, lastname, opened, actual_balance, effective_balance, credit_limit)
VALUES
(1001001, 'Neil', 'Stevenson', DATE_SUB(CURDATE(), INTERVAL 13 DAY), 100, 100, 1000)
,(1001002, 'John', 'Doe', DATE_SUB(CURDATE(), INTERVAL 12 DAY), 400, 400, 1000)
,(1001003, 'Jane', 'Doe', DATE_SUB(CURDATE(), INTERVAL 11 DAY), 300, 300, 1000)
;
