DROP TABLE IF EXISTS transaction_t;

CREATE TABLE transaction_t (
 id                     INT NOT NULL,
 account_id             INT NOT NULL,
 date                   TIMESTAMP NOT NULL,
 description            VARCHAR(60) NOT NULL,
 amount                 INT NOT NULL,
 authorisation          BOOLEAN NOT NULL,
 PRIMARY KEY (id)
) ENGINE InnoDB;


INSERT INTO transaction_t (id, account_id, date, description, amount, authorisation)
VALUES
(1, 1001001, DATE_SUB(CURDATE(), INTERVAL 2 DAY), 'London LGW coffee shop', 10, false)
,(2, 1001001, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'Menara RAK coffee shop', 50, false)
,(3, 1001001, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'Palm Plaza', 150, true)
;
