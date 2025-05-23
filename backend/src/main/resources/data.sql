-- data.sql -- Seeder file for initial development data

-- ==================================================
-- 0. Clear Existing Data (Respect Foreign Key Constraints)
-- ==================================================
DROP TRIGGER IF EXISTS trg_set_batch ON member_organization_role;
DROP FUNCTION IF EXISTS set_batch_based_on_first_year();

DELETE FROM member_organization_role;
DELETE FROM member;
DELETE FROM organization;
DELETE FROM role;

-- ==================================================
-- 1. Populate Roles Table (Security Roles)
-- ==================================================
INSERT INTO role (role_id, name) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_MEMBER') -- Represents a general member for security purposes
ON CONFLICT (name) DO NOTHING;

-- ==================================================
-- 2. Populate Organizations Table (5 Organizations with VALID UUIDs)
-- ==================================================
INSERT INTO organization (organization_id, organization_name) VALUES
('a1b2c3d4-e5f6-7777-8888-100000000001', 'Innovators Tech Guild'),
('a1b2c3d4-e5f6-7777-8888-100000000002', 'Community Builders Alliance'),
('a1b2c3d4-e5f6-7777-8888-100000000003', 'Future Leaders Network'),
('a1b2c3d4-e5f6-7777-8888-100000000004', 'Creative Minds Collective'),
('a1b2c3d4-e5f6-7777-8888-100000000005', 'Academic Scholars Society')
ON CONFLICT (organization_id) DO NOTHING;

-- ==================================================
-- 3. Populate Members Table (12 Users with VALID UUIDs)
-- ==================================================
-- REMEMBER TO REPLACE ALL '$2a$10$...' PASSWORD HASHES WITH ACTUAL VALID HASHES for each user
INSERT INTO member (member_id, first_name, last_name, gender, degree_program, email, password, created_at, updated_at) VALUES
('b2c3d4e5-f6a1-2222-3333-200000000001', 'Liam', 'Smith', 'Male', 'Computer Science', 'liam.smith@example.com', '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b2c3d4e5-f6a1-2222-3333-200000000002', 'Olivia', 'Jones', 'Female', 'Business Administration', 'olivia.jones@example.com', '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b2c3d4e5-f6a1-2222-3333-200000000003', 'Noah', 'Williams', 'Male', 'Mechanical Engineering', 'noah.williams@example.com', '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b2c3d4e5-f6a1-2222-3333-200000000004', 'Emma', 'Brown', 'Female', 'Psychology', 'emma.brown@example.com', '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b2c3d4e5-f6a1-2222-3333-200000000005', 'Oliver', 'Davis', 'Male', 'Civil Engineering', 'oliver.davis@example.com', '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b2c3d4e5-f6a1-2222-3333-200000000006', 'Ava', 'Miller', 'Female', 'Biology', 'ava.miller@example.com', '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b2c3d4e5-f6a1-2222-3333-200000000007', 'Elijah', 'Wilson', 'Male', 'Economics', 'elijah.wilson@example.com', '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b2c3d4e5-f6a1-2222-3333-200000000008', 'Sophia', 'Moore', 'Female', 'Political Science', 'sophia.moore@example.com', '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b2c3d4e5-f6a1-2222-3333-200000000009', 'Lucas', 'Taylor', 'Male', 'Mathematics', 'lucas.taylor@example.com', '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b2c3d4e5-f6a1-2222-3333-200000000010', 'Isabella', 'Anderson', 'Female', 'English Literature', 'isabella.anderson@example.com', '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b2c3d4e5-f6a1-2222-3333-200000000011', 'Mason', 'Thomas', 'Male', 'History', 'mason.thomas@example.com', '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b2c3d4e5-f6a1-2222-3333-200000000012', 'Mia', 'Jackson', 'Female', 'Fine Arts', 'mia.jackson@example.com', '$2a$10$K.iVjXVzJg7yVw.j6H8sR.uQY9zCjP0QkI8GqJkLwN9tO3rDxD0K', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (member_id) DO NOTHING;


-- ==================================================
-- 4. Populate MemberOrganizationRole Table (Join Table)
-- ==================================================
-- Using the VALID UUIDs defined above for organizations and members.
-- Using gen_random_uuid() for the PK of member_organization_role.

-- Organization 1: Innovators Tech Guild ('a1b2c3d4-e5f6-7777-8888-100000000001')
INSERT INTO member_organization_role (id, member_id, organization_id, role_id, batch, year, semester, position, status, committee) VALUES
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000001', 'a1b2c3d4-e5f6-7777-8888-100000000001', 1, 2025, 2025, 1, 'President', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000001', 'a1b2c3d4-e5f6-7777-8888-100000000001', 1, 2025, 2025, 2, 'President', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000002', 'a1b2c3d4-e5f6-7777-8888-100000000001', 1, 2025, 2025, 1, 'Treasurer', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000002', 'a1b2c3d4-e5f6-7777-8888-100000000001', 1, 2025, 2025, 2, 'Treasurer', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000003', 'a1b2c3d4-e5f6-7777-8888-100000000001', 1, 2025, 2025, 1, 'Secretary', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000003', 'a1b2c3d4-e5f6-7777-8888-100000000001', 1, 2025, 2025, 2, 'Secretary', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000004', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 1, 'Member', 'active', 'Tech Projects'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000004', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 2, 'Member', 'active', 'Tech Projects'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000005', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 1, 'Member', 'active', 'Workshops'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000005', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 2, 'Member', 'active', 'Workshops'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000006', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 1, 'Member', 'active', 'Outreach'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000006', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 2, 'Member', 'active', 'Outreach'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000007', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 1, 'Member', 'active', 'Mentorship'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000007', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 2, 'Member', 'active', 'Mentorship'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000008', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 1, 'Member', 'active', 'Hackathons'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000008', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 2, 'Member', 'active', 'Hackathons'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000009', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 1, 'Member', 'active', 'AI Research'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000009', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 2, 'Member', 'active', 'AI Research'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000010', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 1, 'Member', 'active', 'Web Dev'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000010', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 2, 'Member', 'active', 'Web Dev'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000011', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 1, 'Member', 'active', 'Mobile Dev'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000011', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 2, 'Member', 'active', 'Mobile Dev'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000012', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 1, 'Member', 'active', 'Cybersecurity'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000012', 'a1b2c3d4-e5f6-7777-8888-100000000001', 2, 2025, 2025, 2, 'Member', 'active', 'Cybersecurity');

-- Organization 2: Community Builders Alliance ('a1b2c3d4-e5f6-7777-8888-100000000002')
INSERT INTO member_organization_role (id, member_id, organization_id, role_id, batch, year, semester, position, status, committee) VALUES
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000004', 'a1b2c3d4-e5f6-7777-8888-100000000002', 1, 2025, 2025, 1, 'President', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000004', 'a1b2c3d4-e5f6-7777-8888-100000000002', 1, 2025, 2025, 2, 'President', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000005', 'a1b2c3d4-e5f6-7777-8888-100000000002', 1, 2025, 2025, 1, 'Treasurer', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000005', 'a1b2c3d4-e5f6-7777-8888-100000000002', 1, 2025, 2025, 2, 'Treasurer', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000006', 'a1b2c3d4-e5f6-7777-8888-100000000002', 1, 2025, 2025, 1, 'Secretary', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000006', 'a1b2c3d4-e5f6-7777-8888-100000000002', 1, 2025, 2025, 2, 'Secretary', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000007', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 1, 'Member', 'active', 'Volunteering'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000007', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 2, 'Member', 'active', 'Volunteering'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000008', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 1, 'Member', 'active', 'Event Planning'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000008', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 2, 'Member', 'active', 'Event Planning'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000009', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 1, 'Member', 'active', 'Fundraising'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000009', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 2, 'Member', 'active', 'Fundraising'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000010', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 1, 'Member', 'active', 'Local Partnerships'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000010', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 2, 'Member', 'active', 'Local Partnerships'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000011', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 1, 'Member', 'active', 'Social Media'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000011', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 2, 'Member', 'active', 'Social Media'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000012', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 1, 'Member', 'active', 'Member Recruitment'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000012', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 2, 'Member', 'active', 'Member Recruitment'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000001', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 1, 'Member', 'active', 'Advisory Board'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000001', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 2, 'Member', 'active', 'Advisory Board'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000002', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 1, 'Member', 'active', 'Community Engagement'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000002', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 2, 'Member', 'active', 'Community Engagement'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000003', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 1, 'Member', 'active', 'Policy Review'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000003', 'a1b2c3d4-e5f6-7777-8888-100000000002', 2, 2025, 2025, 2, 'Member', 'active', 'Policy Review');

-- Organization 3: Future Leaders Network ('a1b2c3d4-e5f6-7777-8888-100000000003')
INSERT INTO member_organization_role (id, member_id, organization_id, role_id, batch, year, semester, position, status, committee) VALUES
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000007', 'a1b2c3d4-e5f6-7777-8888-100000000003', 1, 2025, 2025, 1, 'President', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000007', 'a1b2c3d4-e5f6-7777-8888-100000000003', 1, 2025, 2025, 2, 'President', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000008', 'a1b2c3d4-e5f6-7777-8888-100000000003', 1, 2025, 2025, 1, 'Treasurer', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000008', 'a1b2c3d4-e5f6-7777-8888-100000000003', 1, 2025, 2025, 2, 'Treasurer', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000009', 'a1b2c3d4-e5f6-7777-8888-100000000003', 1, 2025, 2025, 1, 'Secretary', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000009', 'a1b2c3d4-e5f6-7777-8888-100000000003', 1, 2025, 2025, 2, 'Secretary', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000010', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 1, 'Member', 'active', 'Networking Events'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000010', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 2, 'Member', 'active', 'Networking Events'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000011', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 1, 'Member', 'active', 'Career Development'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000011', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 2, 'Member', 'active', 'Career Development'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000012', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 1, 'Member', 'active', 'Alumni Relations'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000012', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 2, 'Member', 'active', 'Alumni Relations'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000001', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 1, 'Member', 'active', 'Public Speaking Workshops'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000001', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 2, 'Member', 'active', 'Public Speaking Workshops'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000002', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 1, 'Member', 'active', 'Leadership Seminars'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000002', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 2, 'Member', 'active', 'Leadership Seminars'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000003', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 1, 'Member', 'active', 'Mentorship Program'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000003', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 2, 'Member', 'active', 'Mentorship Program'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000004', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 1, 'Member', 'active', 'Internship Coordination'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000004', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 2, 'Member', 'active', 'Internship Coordination'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000005', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 1, 'Member', 'active', 'Skill Workshops'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000005', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 2, 'Member', 'active', 'Skill Workshops'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000006', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 1, 'Member', 'active', 'Guest Speaker Series'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000006', 'a1b2c3d4-e5f6-7777-8888-100000000003', 2, 2025, 2025, 2, 'Member', 'active', 'Guest Speaker Series');

-- Organization 4: Creative Minds Collective ('a1b2c3d4-e5f6-7777-8888-100000000004')
INSERT INTO member_organization_role (id, member_id, organization_id, role_id, batch, year, semester, position, status, committee) VALUES
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000010', 'a1b2c3d4-e5f6-7777-8888-100000000004', 1, 2025, 2025, 1, 'President', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000010', 'a1b2c3d4-e5f6-7777-8888-100000000004', 1, 2025, 2025, 2, 'President', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000011', 'a1b2c3d4-e5f6-7777-8888-100000000004', 1, 2025, 2025, 1, 'Treasurer', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000011', 'a1b2c3d4-e5f6-7777-8888-100000000004', 1, 2025, 2025, 2, 'Treasurer', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000012', 'a1b2c3d4-e5f6-7777-8888-100000000004', 1, 2025, 2025, 1, 'Secretary', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000012', 'a1b2c3d4-e5f6-7777-8888-100000000004', 1, 2025, 2025, 2, 'Secretary', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000001', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 1, 'Member', 'active', 'Visual Arts'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000001', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 2, 'Member', 'active', 'Visual Arts'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000002', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 1, 'Member', 'active', 'Performing Arts'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000002', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 2, 'Member', 'active', 'Performing Arts'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000003', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 1, 'Member', 'active', 'Literary Arts'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000003', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 2, 'Member', 'active', 'Literary Arts'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000004', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 1, 'Member', 'active', 'Digital Media'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000004', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 2, 'Member', 'active', 'Digital Media'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000005', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 1, 'Member', 'active', 'Crafts & Design'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000005', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 2, 'Member', 'active', 'Crafts & Design'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000006', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 1, 'Member', 'active', 'Music Production'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000006', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 2, 'Member', 'active', 'Music Production'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000007', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 1, 'Member', 'active', 'Film & Video'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000007', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 2, 'Member', 'active', 'Film & Video'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000008', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 1, 'Member', 'active', 'Photography'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000008', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 2, 'Member', 'active', 'Photography'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000009', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 1, 'Member', 'active', 'Creative Writing'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000009', 'a1b2c3d4-e5f6-7777-8888-100000000004', 2, 2025, 2025, 2, 'Member', 'active', 'Creative Writing');

-- Organization 5: Academic Scholars Society ('a1b2c3d4-e5f6-7777-8888-100000000005')
INSERT INTO member_organization_role (id, member_id, organization_id, role_id, batch, year, semester, position, status, committee) VALUES
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000009', 'a1b2c3d4-e5f6-7777-8888-100000000005', 1, 2025, 2025, 1, 'President', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000009', 'a1b2c3d4-e5f6-7777-8888-100000000005', 1, 2025, 2025, 2, 'President', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000010', 'a1b2c3d4-e5f6-7777-8888-100000000005', 1, 2025, 2025, 1, 'Treasurer', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000010', 'a1b2c3d4-e5f6-7777-8888-100000000005', 1, 2025, 2025, 2, 'Treasurer', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000011', 'a1b2c3d4-e5f6-7777-8888-100000000005', 1, 2025, 2025, 1, 'Secretary', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000011', 'a1b2c3d4-e5f6-7777-8888-100000000005', 1, 2025, 2025, 2, 'Secretary', 'active', 'Executive'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000012', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 1, 'Member', 'active', 'Research Group Alpha'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000012', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 2, 'Member', 'active', 'Research Group Alpha'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000001', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 1, 'Member', 'active', 'Research Group Beta'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000001', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 2, 'Member', 'active', 'Research Group Beta'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000002', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 1, 'Member', 'active', 'Conference Planning'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000002', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 2, 'Member', 'active', 'Conference Planning'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000003', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 1, 'Member', 'active', 'Journal Club'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000003', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 2, 'Member', 'active', 'Journal Club'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000004', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 1, 'Member', 'active', 'Tutoring Program'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000004', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 2, 'Member', 'active', 'Tutoring Program'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000005', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 1, 'Member', 'active', 'Academic Competitions'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000005', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 2, 'Member', 'active', 'Academic Competitions'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000006', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 1, 'Member', 'active', 'Peer Review Group'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000006', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 2, 'Member', 'active', 'Peer Review Group'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000007', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 1, 'Member', 'active', 'Thesis Support Group'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000007', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 2, 'Member', 'active', 'Thesis Support Group'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000008', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 1, 'Member', 'active', 'Study Abroad Committee'),
(gen_random_uuid(), 'b2c3d4e5-f6a1-2222-3333-200000000008', 'a1b2c3d4-e5f6-7777-8888-100000000005', 2, 2025, 2025, 2, 'Member', 'active', 'Study Abroad Committee');


-- ==================================================
-- 5. Recreate Trigger for 'batch' in member_organization_role
-- ==================================================
-- CREATE OR REPLACE FUNCTION set_batch_based_on_first_year()
-- RETURNS TRIGGER AS $$
-- BEGIN
--     SELECT MIN(mor.year)
--     INTO NEW.batch
--     FROM member_organization_role mor
--     WHERE mor.member_id = NEW.member_id
--       AND mor.organization_id = NEW.organization_id;

--     IF NEW.batch IS NULL THEN
--         NEW.batch := NEW.year;
--     END IF;

--     RETURN NEW;
-- END;
-- $$ LANGUAGE plpgsql;

-- CREATE TRIGGER trg_set_batch
-- BEFORE INSERT ON member_organization_role
-- FOR EACH ROW
-- EXECUTE FUNCTION set_batch_based_on_first_year();