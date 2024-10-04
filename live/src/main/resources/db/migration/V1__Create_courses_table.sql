-- Create courses table
CREATE TABLE courses (
    course_id BIGSERIAL PRIMARY KEY,
    course_name VARCHAR(255) NOT NULL,
    course_code VARCHAR(255) NOT NULL,
    description TEXT,
    created_by TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_active BOOLEAN DEFAULT FALSE NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
    head_video_url VARCHAR(255),
    head_image_url VARCHAR(255)
);

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    role VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Create units table
CREATE TABLE units (
    unit_id BIGSERIAL PRIMARY KEY,
    unit_name VARCHAR(255),
    description TEXT,
    sequence_no INTEGER,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by TEXT,
    head_video_url VARCHAR(255),
    head_image_url VARCHAR(255),
    module_id BIGINT,
    unit_code VARCHAR(50)
);

-- Create topics table
CREATE TABLE topics (
    topic_id BIGSERIAL PRIMARY KEY,
    topic_name VARCHAR(255) NOT NULL,
    topic_type VARCHAR(50) NOT NULL,
    content TEXT,
    description TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    topic_code VARCHAR(50),
    sequence_no INTEGER,
    created_by TEXT,
    head_video_url VARCHAR(255),
    head_image_url VARCHAR(255),
    unit_id BIGINT
);

-- Create subjects table
CREATE TABLE subjects (
    subject_id BIGSERIAL PRIMARY KEY,
    subject_name VARCHAR(255),
    description TEXT,
    sequence_no INTEGER,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by TEXT,
    head_video_url VARCHAR(255),
    head_image_url VARCHAR(255),
    course_id BIGINT,
    subject_code VARCHAR(50)
);

-- Create modules table
CREATE TABLE modules (
    module_id BIGSERIAL PRIMARY KEY,
    module_name VARCHAR(255),
    description TEXT,
    sequence_no INTEGER,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    created_by TEXT,
    head_video_url VARCHAR(255),
    head_image_url VARCHAR(255),
    subject_id BIGINT,
    module_code VARCHAR(50)
);