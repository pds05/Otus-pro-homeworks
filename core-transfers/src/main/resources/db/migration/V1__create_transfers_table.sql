create table transfers (
    id varchar(36) primary key,
    client_id varchar(10),
    target_client_id varchar(10),
    source_account varchar(12),
    target_account varchar(12),
    amount numeric(8, 2),
    message varchar(255),
    created_at timestamp default now(),
    updated_at timestamp default now()
);
