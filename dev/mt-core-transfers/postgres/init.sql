CREATE USER mt_core_transfers_admin PASSWORD 'mt_core_transfers_admin';
CREATE DATABASE mt_core_transfers OWNER mt_core_transfers_admin ENCODING 'UTF8' LC_COLLATE='ru_RU.utf8' LC_CTYPE='ru_RU.utf8' TEMPLATE='template0' CONNECTION LIMIT 10;
GRANT ALL PRIVILEGES ON DATABASE mt_core_transfers TO mt_core_transfers_admin;