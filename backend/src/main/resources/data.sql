-- data.sql -- Seeder file for initial development data
-- ==================================================
-- 0. Clear Existing Data (Respect Foreign Key Constraints)
-- ==================================================
DROP TRIGGER IF EXISTS trg_set_batch ON member_organization_role;

DROP FUNCTION IF EXISTS set_batch_based_on_first_year ();

DROP TABLE IF EXISTS fee;

DROP TABLE IF EXISTS member_organization_role;

DROP TABLE IF EXISTS member;

DROP TABLE IF EXISTS organization;

DROP TABLE IF EXISTS role;

DROP TYPE IF EXISTS role_name_enum;

DROP TYPE IF EXISTS member_status_enum;

DO '
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_type WHERE typname = ''role_name_enum''
    ) THEN
        CREATE TYPE role_name_enum AS ENUM (''ROLE_MEMBER'', ''ROLE_ADMIN'');
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_type WHERE typname = ''member_status_enum''
    ) THEN
        CREATE TYPE member_status_enum AS ENUM (''active'', ''inactive'', ''suspended'', ''expelled'', ''alumni'');
    END IF;
END;
';

CREATE TABLE
    IF NOT EXISTS member (
        member_id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
        first_name VARCHAR(255) NOT NULL,
        last_name VARCHAR(255) NOT NULL,
        gender VARCHAR(255),
        degree_program VARCHAR(255) NOT NULL,
        email VARCHAR(100) UNIQUE NOT NULL,
        password VARCHAR(255) NOT NULL,
        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE
    IF NOT EXISTS organization (
        organization_id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
        organization_name VARCHAR(255) NOT NULL
    );

CREATE TABLE
    IF NOT EXISTS role (
        role_id SERIAL PRIMARY KEY,
        name role_name_enum UNIQUE NOT NULL CONSTRAINT chk_role_name CHECK (name IN ('ROLE_MEMBER', 'ROLE_ADMIN'))
    );

CREATE TABLE
    IF NOT EXISTS member_organization_role (
        id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
        member_id UUID NOT NULL,
        organization_id UUID NOT NULL,
        role_id INTEGER,
        position VARCHAR(255),
        batch INTEGER NOT NULL,
        year INTEGER NOT NULL,
        semester INTEGER NOT NULL,
        status member_status_enum NOT NULL,
        committee VARCHAR(255),
        CONSTRAINT fk_member_organization_role_member FOREIGN KEY (member_id) REFERENCES member (member_id),
        CONSTRAINT fk_member_organization_role_organization FOREIGN KEY (organization_id) REFERENCES organization (organization_id),
        CONSTRAINT fk_member_organization_role_role FOREIGN KEY (role_id) REFERENCES role (role_id),
        CONSTRAINT chk_member_organization_role_status CHECK (
            status IN (
                'active',
                'inactive',
                'suspended',
                'expelled',
                'alumni'
            )
        )
    );

CREATE TABLE
    IF NOT EXISTS fee (
        fee_id UUID PRIMARY KEY DEFAULT gen_random_uuid (),
        amount NUMERIC NOT NULL,
        semester INTEGER NOT NULL,
        year INTEGER NOT NULL,
        due_date DATE NOT NULL,
        date_paid DATE,
        member_id UUID NOT NULL,
        organization_id UUID NOT NULL,
        CONSTRAINT fk_fee_member FOREIGN KEY (member_id) REFERENCES member (member_id),
        CONSTRAINT fk_fee_organization FOREIGN KEY (organization_id) REFERENCES organization (organization_id)
    );

-- ==================================================
-- 1. Populate Roles Table
-- ==================================================
INSERT INTO
    role (role_id, name)
VALUES
    (1, 'ROLE_ADMIN'),
    (2, 'ROLE_MEMBER') ON CONFLICT (name) DO NOTHING;

INSERT INTO
    organization (organization_id, organization_name)
VALUES
    (
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        'Innovators Tech Guild'
    ),
    (
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        'Community Builders Alliance'
    ) ON CONFLICT (organization_id) DO NOTHING;

-- ==================================================
-- 3. Populate Members Table (12 Users - using your new valid UUIDs)
-- ==================================================
INSERT INTO
    member (
        member_id,
        first_name,
        last_name,
        gender,
        degree_program,
        email,
        password,
        created_at,
        updated_at
    )
VALUES
    (
        'b2c3d4e5-f6a1-2222-3333-200000000001',
        'Liam',
        'Smith',
        'Male',
        'Computer Science',
        'liam.smith@example.com',
        '$2a$12$FmtIllXX4TQ/UlRZNiscrug65F2p8ABgMlJOcGmFC9.Imns.nxByW',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'b2c3d4e5-f6a1-2222-3333-200000000002',
        'Olivia',
        'Jones',
        'Female',
        'Business Administration',
        'olivia.jones@example.com',
        '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'b2c3d4e5-f6a1-2222-3333-200000000003',
        'Noah',
        'Williams',
        'Male',
        'Mechanical Engineering',
        'noah.williams@example.com',
        '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'b2c3d4e5-f6a1-2222-3333-200000000004',
        'Emma',
        'Brown',
        'Female',
        'Psychology',
        'emma.brown@example.com',
        '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'b2c3d4e5-f6a1-2222-3333-200000000005',
        'Oliver',
        'Davis',
        'Male',
        'Civil Engineering',
        'oliver.davis@example.com',
        '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'b2c3d4e5-f6a1-2222-3333-200000000006',
        'Ava',
        'Miller',
        'Female',
        'Biology',
        'ava.miller@example.com',
        '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'b2c3d4e5-f6a1-2222-3333-200000000007',
        'Elijah',
        'Wilson',
        'Male',
        'Economics',
        'elijah.wilson@example.com',
        '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'b2c3d4e5-f6a1-2222-3333-200000000008',
        'Sophia',
        'Moore',
        'Female',
        'Political Science',
        'sophia.moore@example.com',
        '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'b2c3d4e5-f6a1-2222-3333-200000000009',
        'Lucas',
        'Taylor',
        'Male',
        'Mathematics',
        'lucas.taylor@example.com',
        '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'b2c3d4e5-f6a1-2222-3333-200000000010',
        'Isabella',
        'Anderson',
        'Female',
        'English Literature',
        'isabella.anderson@example.com',
        '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'b2c3d4e5-f6a1-2222-3333-200000000011',
        'Mason',
        'Thomas',
        'Male',
        'History',
        'mason.thomas@example.com',
        '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ), -- Mason
    (
        'b2c3d4e5-f6a1-2222-3333-200000000012',
        'Mia',
        'Jackson',
        'Female',
        'Fine Arts',
        'mia.jackson@example.com',
        '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K',
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ) -- Mia
    ON CONFLICT (member_id) DO NOTHING;

-- ==================================================
-- 5. Populate Fees Table
-- ==================================================
INSERT INTO
    fee (
        fee_id,
        amount,
        semester,
        year,
        due_date,
        date_paid,
        member_id,
        organization_id
    )
VALUES
    (
        gen_random_uuid (),
        75.00,
        1,
        2024,
        '2025-02-28',
        '2025-02-10',
        'b2c3d4e5-f6a1-2222-3333-200000000001',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'
    ),
    (
        gen_random_uuid (),
        75.00,
        2,
        2024,
        '2025-09-30',
        '2025-09-15',
        'b2c3d4e5-f6a1-2222-3333-200000000001',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'
    ),
    (
        gen_random_uuid (),
        75.00,
        1,
        2024,
        '2025-02-28',
        '2025-02-20',
        'b2c3d4e5-f6a1-2222-3333-200000000002',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'
    ),
    (
        gen_random_uuid (),
        75.00,
        2,
        2024,
        '2025-09-30',
        NULL,
        'b2c3d4e5-f6a1-2222-3333-200000000002',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'
    ),
    (
        gen_random_uuid (),
        75.00,
        1,
        2024,
        '2025-02-28',
        NULL,
        'b2c3d4e5-f6a1-2222-3333-200000000003',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'
    ),
    (
        gen_random_uuid (),
        75.00,
        2,
        2024,
        '2025-09-30',
        NULL,
        'b2c3d4e5-f6a1-2222-3333-200000000003',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'
    ),
    (
        gen_random_uuid (),
        75.00,
        1,
        2024,
        '2025-02-28',
        '2025-02-10',
        'b2c3d4e5-f6a1-2222-3333-200000000001',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'
    ),
    (
        gen_random_uuid (),
        75.00,
        2,
        2024,
        '2025-09-30',
        '2025-09-15',
        'b2c3d4e5-f6a1-2222-3333-200000000001',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'
    ),
    (
        gen_random_uuid (),
        75.00,
        1,
        2024,
        '2025-02-28',
        '2025-02-20',
        'b2c3d4e5-f6a1-2222-3333-200000000002',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'
    ),
    (
        gen_random_uuid (),
        75.00,
        2,
        2024,
        '2025-09-30',
        '2025-10-07',
        'b2c3d4e5-f6a1-2222-3333-200000000002',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'
    ),
    (
        gen_random_uuid (),
        75.00,
        1,
        2024,
        '2025-02-28',
        '2025-03-07',
        'b2c3d4e5-f6a1-2222-3333-200000000003',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11'
    ),
    (
        gen_random_uuid (),
        75.00,
        1,
        2024,
        '2025-02-28',
        '2025-02-12',
        'b2c3d4e5-f6a1-2222-3333-200000000004',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6'
    ),
    (
        gen_random_uuid (),
        75.00,
        2,
        2024,
        '2025-09-30',
        '2025-09-18',
        'b2c3d4e5-f6a1-2222-3333-200000000004',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6'
    ),
    (
        gen_random_uuid (),
        75.00,
        1,
        2024,
        '2025-02-28',
        '2025-02-25',
        'b2c3d4e5-f6a1-2222-3333-200000000005',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6'
    ),
    (
        gen_random_uuid (),
        75.00,
        2,
        2024,
        '2025-09-30',
        NULL,
        'b2c3d4e5-f6a1-2222-3333-200000000005',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6'
    ),
    (
        gen_random_uuid (),
        75.00,
        1,
        2024,
        '2025-02-28',
        NULL,
        'b2c3d4e5-f6a1-2222-3333-200000000006',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6'
    ),
    (
        gen_random_uuid (),
        75.00,
        2,
        2024,
        '2025-09-30',
        '2025-09-20',
        'b2c3d4e5-f6a1-2222-3333-200000000006',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6'
    ),
    (
        gen_random_uuid (),
        75.00,
        1,
        2024,
        '2025-02-28',
        '2025-02-12',
        'b2c3d4e5-f6a1-2222-3333-200000000004',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6'
    ),
    (
        gen_random_uuid (),
        75.00,
        2,
        2024,
        '2025-09-30',
        '2025-09-18',
        'b2c3d4e5-f6a1-2222-3333-200000000004',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6'
    ),
    (
        gen_random_uuid (),
        75.00,
        1,
        2024,
        '2025-02-28',
        '2025-02-25',
        'b2c3d4e5-f6a1-2222-3333-200000000005',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6'
    ),
    (
        gen_random_uuid (),
        75.00,
        2,
        2024,
        '2025-09-30',
        '2025-10-07',
        'b2c3d4e5-f6a1-2222-3333-200000000005',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6'
    ),
    (
        gen_random_uuid (),
        75.00,
        1,
        2024,
        '2025-02-28',
        '2025-03-07',
        'b2c3d4e5-f6a1-2222-3333-200000000006',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6'
    ),
    (
        gen_random_uuid (),
        75.00,
        2,
        2024,
        '2025-09-30',
        '2025-09-20',
        'b2c3d4e5-f6a1-2222-3333-200000000006',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6'
    ) ON CONFLICT (fee_id) DO NOTHING;

-- Organization 1: Innovators Tech Guild ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11')
-- 12 Memberships for AY 2024, 1st Semester (Fall)
INSERT INTO
    member_organization_role (
        id,
        member_id,
        organization_id,
        role_id,
        batch,
        year,
        semester,
        position,
        status,
        committee
    )
VALUES
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000001',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        1,
        2024,
        2024,
        1,
        'President',
        'active',
        'Executive'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000002',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        1,
        2024,
        2024,
        1,
        'Treasurer',
        'active',
        'Executive'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000003',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        1,
        2024,
        2024,
        1,
        'Secretary',
        'active',
        'Executive'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000004',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2024,
        2024,
        1,
        'Member',
        'active',
        'Tech Projects'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000005',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2024,
        2024,
        1,
        'Member',
        'active',
        'Workshops'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000006',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2024,
        2024,
        1,
        'Member',
        'active',
        'Outreach'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000007',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2024,
        2024,
        1,
        'Member',
        'active',
        'Mentorship'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000008',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2024,
        2024,
        1,
        'Member',
        'active',
        'Hackathons'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000009',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2024,
        2024,
        1,
        'Member',
        'active',
        'AI Research'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000010',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2024,
        2024,
        1,
        'Member',
        'active',
        'Web Dev'
    ),
    -- Mason: Inactive Member in Innovators Tech Guild for a *past* semester (AY 2022, 2nd Sem)
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000011',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2024,
        2024,
        1,
        NULL,
        'inactive',
        NULL
    ),
    -- Mia: Alumni in Innovators Tech Guild for a *past* semester (AY 2021, 1st Sem)
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000012',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2020,
        2020,
        1,
        'Member',
        'active',
        'AI Research'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000012',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2020,
        2020,
        2,
        'Member',
        'active',
        'AI Research'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000012',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        NULL,
        2020,
        2021,
        2,
        NULL,
        'alumni',
        NULL
    ),
    -- 12 Memberships for AY 2024, 2nd Semester (Spring) - some changes in roles/positions
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000002',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        1,
        2024,
        2024,
        2,
        'President',
        'active',
        'Executive'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000001',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        1,
        2024,
        2024,
        2,
        'Treasurer',
        'active',
        'Executive'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000003',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        1,
        2024,
        2024,
        2,
        'Secretary',
        'active',
        'Executive'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000004',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2024,
        2024,
        2,
        'Member',
        'active',
        'Tech Projects'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000005',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2024,
        2024,
        2,
        'Member',
        'active',
        'Workshops'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000006',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2024,
        2024,
        2,
        'Member',
        'active',
        'Outreach'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000007',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2024,
        2024,
        2,
        'Member',
        'active',
        'Mentorship'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000008',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2024,
        2024,
        2,
        'Member',
        'active',
        'Hackathons'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000009',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2024,
        2024,
        2,
        'Member',
        'active',
        'AI Research'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000010',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2024,
        2024,
        2,
        'Member',
        'active',
        'Web Dev'
    ),
    -- Mason remains inactive for this org in Spring 2024
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000011',
        'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
        2,
        2024,
        2024,
        2,
        NULL,
        'inactive',
        NULL
    );

-- Mia remains alumni for this org in Spring 2024
-- -- Organization 2: Community Builders Alliance ('f81d4fae-7dec-11d0-a765-00a0c91e6bf6')
-- -- 12 Memberships for AY 2024, 1st Semester (Fall)
INSERT INTO
    member_organization_role (
        id,
        member_id,
        organization_id,
        role_id,
        batch,
        year,
        semester,
        position,
        status,
        committee
    )
VALUES
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000001',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        1,
        2024,
        2024,
        1,
        'President',
        'active',
        'Executi'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000002',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        1,
        2024,
        2024,
        1,
        'Treasurer',
        'active',
        'Board'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000003',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        1,
        2024,
        2024,
        1,
        'Secretary',
        'active',
        'Events'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000004',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2024,
        2024,
        1,
        'Member',
        'active',
        'Community Service'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000005',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2024,
        2024,
        1,
        'Member',
        'active',
        'Fundraising Drives'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000006',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2024,
        2024,
        1,
        'Member',
        'active',
        'Local Partnerships'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000007',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2024,
        2024,
        1,
        'Member',
        'active',
        'Awareness Campaigns'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000008',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2024,
        2024,
        1,
        'Member',
        'active',
        'Social Media Outreach'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000009',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2024,
        2024,
        1,
        'Member',
        'active',
        'Recruitment'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000010',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2024,
        2024,
        1,
        'Member',
        'active',
        'Grant Writing'
    ),
    -- Mason: Active Member in Community Builders Alliance for Fall
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000011',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2024,
        2024,
        1,
        'Historian',
        'active',
        'Archives'
    ),
    -- Mia: Alumni in Community Builders Alliance for a past semester (AY 2021, 2nd Sem)
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000012',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2021,
        2021,
        2,
        'Past President',
        'alumni',
        'Advisory Council'
    ),
    -- 12 Memberships for AY 2024, 2024, 2nd Semester (Spring)
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000001',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        1,
        2024,
        2024,
        2,
        'President',
        'active',
        'Executive'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000003',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        1,
        2024,
        2024,
        2,
        'Treasurer',
        'active',
        'Executive'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000002',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        1,
        2024,
        2024,
        2,
        'Secretary',
        'active',
        'Executive'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000004',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2024,
        2024,
        2,
        'Member',
        'active',
        'Community Service'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000005',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2024,
        2024,
        2,
        'Member',
        'active',
        'Fundraising Drives'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000006',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2024,
        2024,
        2,
        'Member',
        'active',
        'Local Partnerships'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000007',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2024,
        2024,
        2,
        'Member',
        'active',
        'Awareness Campaigns'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000008',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2024,
        2024,
        2,
        'Member',
        'active',
        'Social Media Outreach'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000009',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2024,
        2024,
        2,
        'Member',
        'active',
        'Recruitment'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000010',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2024,
        2024,
        2,
        'Member',
        'active',
        'Grant Writing'
    ),
    -- Mason: Still active Member in Community Builders Alliance for Spring
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000011',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2024,
        2024,
        2,
        'Member',
        'active',
        'Archives'
    ),
    -- Mia remains alumni
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000012',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2020,
        2020,
        1,
        'Member',
        'active',
        'Recruitment'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000012',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        2,
        2020,
        2020,
        2,
        'Member',
        'active',
        'Recruitment'
    ),
    (
        gen_random_uuid (),
        'b2c3d4e5-f6a1-2222-3333-200000000012',
        'f81d4fae-7dec-11d0-a765-00a0c91e6bf6',
        NULL,
        2020,
        2021,
        2,
        NULL,
        'alumni',
        NULL
    );

-- ==================================================
-- 6. Recreate Trigger for 'batch' in member_organization_role
-- ==================================================
-- Create function (replace if exists)
CREATE
OR REPLACE FUNCTION set_batch_based_on_first_year () RETURNS TRIGGER AS '
BEGIN
    SELECT MIN(mor.year)
    INTO NEW.batch
    FROM member_organization_role mor
    WHERE mor.member_id = NEW.member_id
      AND mor.organization_id = NEW.organization_id;

    IF NEW.batch IS NULL THEN
        NEW.batch := NEW.year;
    END IF;

    RETURN NEW;
END;
' LANGUAGE plpgsql;

DO '
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_trigger t
        JOIN pg_class c ON t.tgrelid = c.oid
        WHERE t.tgname = ''trg_set_batch''
          AND c.relname = ''member_organization_role''
    ) THEN
        EXECUTE ''
            CREATE TRIGGER trg_set_batch
            BEFORE INSERT ON member_organization_role
            FOR EACH ROW
            EXECUTE FUNCTION set_batch_based_on_first_year()
        '';
    END IF;
END;
';