create table if not exists clients (
  id varchar(10) primary key,
  username varchar(50) unique
);
create table if not exists transfers (
    id varchar(36) primary key,
    client_id varchar(10),
    target_client_id varchar(10),
    source_account varchar(12),
    target_account varchar(12),
    amount int,
    message varchar(255),
    CONSTRAINT fk_transfers_clients FOREIGN KEY (client_id) references CLIENTS (ID) ON UPDATE CASCADE ON DELETE SET NULL
);
create table if not exists accounts (
    id serial primary key,
    account varchar (12) unique,
    client_id varchar (10),
    funds numeric (10,2),
    is_blocked smallint,
    CONSTRAINT fk_accounts_clients FOREIGN KEY (client_id) references CLIENTS (ID) ON UPDATE CASCADE ON DELETE CASCADE
);

insert into clients (id, username) values
('1000000001', 'Bob'),
('1000000002', 'Leo');

insert into accounts (account, client_id, funds, is_blocked) values
('880979287826', '1000000001', 1000, 0),
('757189807296', '1000000001', 500, 0),
('800269685404', '1000000001', 0, 1),
('640009574186', '1000000002', 100, 0),
('122195913711', '1000000002', 300, 0);

insert into transfers (id, client_id, target_client_id, source_account, target_account, amount, message) values
('bde76ffa-f133-4c23-9bca-03618b2a94b2', '1000000001', '1000000002', '000000000001', '000000000002', 100, 'Тестовый перевод'),
('32ebb2eb-ed35-4baa-b500-b7f6535e4c88', '1000000002', '1000000001', '000000000002', '000000000001', 50, 'Обратный тестовый перевод');