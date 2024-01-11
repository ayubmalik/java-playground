CREATE TABLE pharmacy
(
  id               SERIAL,
  code             CHAR(5)      NOT NULL,
  name             VARCHAR(128) NOT NULL,
  legal_start_date DATE         NOT NULL,
  legal_end_date   DATE,
  address_line_1   varchar(256) not null,
  address_line_2   varchar(256),
  address_line_3   varchar(255),
  town             varchar(128),
  county           varchar(128),
  postcode         varchar(10),
  PRIMARY KEY (id)
);
