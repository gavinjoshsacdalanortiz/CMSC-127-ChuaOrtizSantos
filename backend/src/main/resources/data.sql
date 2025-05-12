-- data.sql -- Seeder file for initial development data

-- ==================================================
-- 1. Populate Roles Table
-- ==================================================
INSERT INTO role (role_id, name) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER')
ON CONFLICT (name) DO NOTHING;

-- ==================================================
-- 2. Populate Organizations Table
-- ==================================================
INSERT INTO organization (organization_id, organization_name) VALUES
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Stark Industries'),
('f81d4fae-7dec-11d0-a765-00a0c91e6bf6', 'Wayne Enterprises'),
('b4a1c2d3-e4f5-a6b7-c8d9-e0f1a2b3c4d5', 'Oscorp'),
('c5d2e3f4-a1b2-c3d4-e5f6-a1b2c3d4e5f6', 'LexCorp'),
('d6e3f4a5-b2c3-d4e5-f6a1-b2c3d4e5f6a1', 'Queen Consolidated')
ON CONFLICT (organization_id) DO NOTHING;

-- ==================================================
-- 3. Populate Users Table
-- ==================================================
-- 'password123' -> Alice
-- 'securepass'  -> Bob

INSERT INTO member (member_id, first_name, last_name, gender, degree_program, batch, email, password, created_at, updated_at) VALUES
(
    '4e5b6f70-1111-2222-3333-444444444444', -- Alice's UUID (Valid)
    'Alice',
    'Anderson',
    'Female',
    'CS',
    '2025',
    'alice@example.com',
    '$2a$10$VujCHM4n17SR6.VJ4ggh2eYGFwYV5sJwwwaZ9Cd6vdQBOpVANHBhW', -- Example HASHED 'password123'
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    '8a9b0c1d-aaaa-bbbb-cccc-dddddddddddd', -- Bob's UUID (Valid)
    'Bob',
    'Brown',
    'Male',
    'IT',
    '2024',
    'bob@example.com',
    '$2a$10$N0sPls7/.gJYYkNe6y7.UeALQDmlW9Q7dS97R01f4m7o6zLJMw1wC', -- Example HASHED 'securepass'
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    '3c4d5e6f-bbcc-ddee-ff00-112233445566', -- Carol's UUID (Valid)
    'Carol',
    'Davis',
    'Female',
    'EE',
    '2023',
    'carol@example.com',
    '$2a$10$abcdef1234567890ABCDE.FEDCBA0987654321abcdef', -- REPLACE WITH ACTUAL HASH for Carol's password
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    '079a180b-709f-427c-bc6b-036a11a4398d', -- David's UUID (NEWLY GENERATED AND VERIFIED)
    'David',
    'Miller',
    'Male',
    'ME',
    '2025',
    'david@example.com',
    '$2a$10$ghijkl1234567890GHIJK.KJIHG0987654321ghijkl', -- REPLACE WITH ACTUAL HASH for David's password
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    'b1d690a2-1128-43d7-91d6-cf99a2b7dfae', -- Eve's UUID (NEWLY GENERATED AND VERIFIED)
    'Eve',
    'Wilson',
    'Female',
    'BIO',
    '2024',
    'eve@example.com',
    '$2a$10$mnopqr1234567890MNOPQ.QPONM0987654321mnopqr', -- REPLACE WITH ACTUAL HASH for Eve's password
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
)
ON CONFLICT (member_id) DO NOTHING;


-- ==================================================
-- 4. Populate UserOrganizationRole Table (Join Table)
-- ==================================================
INSERT INTO member_organization_role (id, member_id, organization_id, role_id, position) VALUES

('111aaa11-bbbb-cccc-dddd-eeeeeeeeeeee', '4e5b6f70-1111-2222-3333-444444444444', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 1, 'President'),
('444ddd44-eeee-ffff-0000-111111111111', '4e5b6f70-1111-2222-3333-444444444444', 'f81d4fae-7dec-11d0-a765-00a0c91e6bf6', 1, 'Secretary'),
('555eee55-ffff-0000-1111-222222222222', '8a9b0c1d-aaaa-bbbb-cccc-dddddddddddd', 'b4a1c2d3-e4f5-a6b7-c8d9-e0f1a2b3c4d5', 2, 'Member'),

-- Carol in Stark Industries as Member
('666fff66-0000-1111-2222-333333333333', '3c4d5e6f-bbcc-ddee-ff00-112233445566', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 2, 'Engineer'),
-- Carol in Wayne Enterprises as Admin
('777aaa77-1111-2222-3333-444444444444', '3c4d5e6f-bbcc-ddee-ff00-112233445566', 'f81d4fae-7dec-11d0-a765-00a0c91e6bf6', 1, 'Director'),
-- David in Oscorp as Admin
('888bbb88-2222-3333-4444-555555555555', '079a180b-709f-427c-bc6b-036a11a4398d', 'b4a1c2d3-e4f5-a6b7-c8d9-e0f1a2b3c4d5', 1, 'VP'),
-- David in Stark Industries as Member
('999ccc99-3333-4444-5555-666666666666', '079a180b-709f-427c-bc6b-036a11a4398d', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 2, 'Consultant'),
-- Eve in LexCorp as Admin
('aaabbba0-4444-5555-6666-777777777777', 'b1d690a2-1128-43d7-91d6-cf99a2b7dfae', 'c5d2e3f4-a1b2-c3d4-e5f6-a1b2c3d4e5f6', 1, 'CEO'),
-- Eve in Queen Consolidated as Member
('bbbccca1-5555-6666-7777-888888888888', 'b1d690a2-1128-43d7-91d6-cf99a2b7dfae', 'd6e3f4a5-b2c3-d4e5-f6a1-b2c3d4e5f6a1', 2, 'Analyst')
ON CONFLICT (id) DO NOTHING;