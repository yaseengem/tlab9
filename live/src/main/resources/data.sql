-- Clear the tables before inserting new data
TRUNCATE TABLE courses RESTART IDENTITY CASCADE;
TRUNCATE TABLE subjects RESTART IDENTITY CASCADE;
TRUNCATE TABLE modules RESTART IDENTITY CASCADE;
TRUNCATE TABLE units RESTART IDENTITY CASCADE;
TRUNCATE TABLE topics RESTART IDENTITY CASCADE;

-- Insert data into courses table
INSERT INTO courses (course_name, course_code, description, created_by, created_at) VALUES
('Course 1', 'C001', 'Description for Course 1', 'Admin', '2023-01-01 10:00:00'),
('Course 2', 'C002', 'Description for Course 2', 'Admin', '2023-01-02 11:00:00'),
('Course 3', 'C003', 'Description for Course 3', 'Admin', '2023-01-03 12:00:00'),
('Course 4', 'C004', 'Description for Course 4', 'Admin', '2023-01-04 13:00:00'),
('Course 5', 'C005', 'Description for Course 5', 'Admin', '2023-01-05 14:00:00');

-- Insert data into subjects table
INSERT INTO subjects (subject_name, subject_code, description, created_by, created_at) VALUES
('Subject 1', 'S001', 'Description for Subject 1', 'Admin', '2023-01-01 10:30:00'),
('Subject 2', 'S002', 'Description for Subject 2', 'Admin', '2023-01-01 11:00:00'),
('Subject 3', 'S003', 'Description for Subject 3', 'Admin', '2023-01-02 11:30:00'),
('Subject 4', 'S004', 'Description for Subject 4', 'Admin', '2023-01-02 12:00:00'),
('Subject 5', 'S005', 'Description for Subject 5', 'Admin', '2023-01-03 12:30:00');

-- Insert data into modules table
INSERT INTO modules (module_name, module_code, description, created_by, created_at) VALUES
('Module 1', 'M001', 'Description for Module 1', 'Admin', '2023-01-01 10:45:00'),
('Module 2', 'M002', 'Description for Module 2', 'Admin', '2023-01-01 11:15:00'),
('Module 3', 'M003', 'Description for Module 3', 'Admin', '2023-01-02 11:45:00'),
('Module 4', 'M004', 'Description for Module 4', 'Admin', '2023-01-02 12:15:00'),
('Module 5', 'M005', 'Description for Module 5', 'Admin', '2023-01-03 12:45:00');

-- Insert data into units table
INSERT INTO units (unit_name, unit_code, description, created_by, created_at) VALUES
('Unit 1', 'U001', 'Description for Unit 1', 'Admin', '2023-01-01 11:00:00'),
('Unit 2', 'U002', 'Description for Unit 2', 'Admin', '2023-01-01 11:30:00'),
('Unit 3', 'U003', 'Description for Unit 3', 'Admin', '2023-01-02 12:00:00'),
('Unit 4', 'U004', 'Description for Unit 4', 'Admin', '2023-01-02 12:30:00'),
('Unit 5', 'U005', 'Description for Unit 5', 'Admin', '2023-01-03 13:00:00');

-- Insert data into topics table
INSERT INTO topics (topic_name, topic_code, content, head_image_url, created_by, created_at, updated_at, sequence_no, topic_type) VALUES
('Topic 1', 'T001', 'Content for Topic 1', 'http://example.com/image1.jpg', 'Admin', '2023-01-01 11:15:00', '2023-01-01 11:20:00', 1, 'Type1'),
('Topic 2', 'T002', 'Content for Topic 2', 'http://example.com/image2.jpg', 'Admin', '2023-01-01 11:45:00', '2023-01-01 11:50:00', 2, 'Type2'),
('Topic 3', 'T003', 'Content for Topic 3', 'http://example.com/image3.jpg', 'Admin', '2023-01-02 12:15:00', '2023-01-02 12:20:00', 3, 'Type3'),
('Topic 4', 'T004', 'Content for Topic 4', 'http://example.com/image4.jpg', 'Admin', '2023-01-02 12:45:00', '2023-01-02 12:50:00', 4, 'Type4'),
('Topic 5', 'T005', 'Content for Topic 5', 'http://example.com/image5.jpg', 'Admin', '2023-01-03 13:15:00', '2023-01-03 13:20:00', 5, 'Type5');