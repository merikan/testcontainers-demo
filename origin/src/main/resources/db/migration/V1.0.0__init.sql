CREATE TABLE todo (
  id                    BIGINT NOT NULL AUTO_INCREMENT,
  title                         VARCHAR(255),
  note                          VARCHAR(255),
  owner                         VARCHAR(255),
  finished                      BIT NOT NULL,
  PRIMARY KEY (id)
)
/*! DEFAULT CHARACTER SET utf8mb4 */
/*! COLLATE utf8mb4_unicode_ci */
    ENGINE = InnoDB;
