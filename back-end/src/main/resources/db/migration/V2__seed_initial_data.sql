INSERT INTO clients (
    id,
    name,
    document_id,
    document_type,
    plan_type,
    active,
    balance,
    monthly_limit,
    monthly_used
) VALUES
    (
        '11111111-1111-1111-1111-111111111111',
        'BCB Padaria Central',
        '12345678901',
        'CPF',
        'PREPAID',
        TRUE,
        20.00,
        NULL,
        0.00
    ),
    (
        '22222222-2222-2222-2222-222222222222',
        'BCB Loja Exemplo LTDA',
        '12345678000199',
        'CNPJ',
        'POSTPAID',
        TRUE,
        NULL,
        50.00,
        0.00
    );

INSERT INTO recipients (id, name, phone) VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Ana Souza', '+5511999990001'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Carlos Lima', '+5511999990002'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'Marina Alves', '+5511999990003');

INSERT INTO conversations (id, client_id, recipient_id) VALUES
    ('90000000-0000-0000-0000-000000000001', '11111111-1111-1111-1111-111111111111', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'),
    ('90000000-0000-0000-0000-000000000002', '11111111-1111-1111-1111-111111111111', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
    ('90000000-0000-0000-0000-000000000003', '22222222-2222-2222-2222-222222222222', 'cccccccc-cccc-cccc-cccc-cccccccccccc');

INSERT INTO messages (
    id,
    conversation_id,
    client_id,
    content,
    priority,
    status,
    sent_by_id,
    sent_by_type,
    cost,
    queued_at,
    sent_at
) VALUES
    (
        '80000000-0000-0000-0000-000000000001',
        '90000000-0000-0000-0000-000000000001',
        '11111111-1111-1111-1111-111111111111',
        'Oi Ana, seu pedido saiu para entrega.',
        'NORMAL',
        'SENT',
        '11111111-1111-1111-1111-111111111111',
        'CLIENT',
        0.25,
        CURRENT_TIMESTAMP - INTERVAL '20 minutes',
        CURRENT_TIMESTAMP - INTERVAL '19 minutes'
    ),
    (
        '80000000-0000-0000-0000-000000000002',
        '90000000-0000-0000-0000-000000000001',
        '11111111-1111-1111-1111-111111111111',
        'Obrigado pelo aviso!',
        'NORMAL',
        'READ',
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        'USER',
        0.00,
        CURRENT_TIMESTAMP - INTERVAL '15 minutes',
        CURRENT_TIMESTAMP - INTERVAL '15 minutes'
    );
