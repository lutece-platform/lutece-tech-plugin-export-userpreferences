
--
-- Structure for table exportuserpreferences_key
--

DROP TABLE IF EXISTS exportuserpreferences_key;
CREATE TABLE exportuserpreferences_key (
pref_key varchar(50) NOT NULL default '',
to_export SMALLINT NOT NULL,
PRIMARY KEY (pref_key)
);
