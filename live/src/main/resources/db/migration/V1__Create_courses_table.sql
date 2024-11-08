CREATE TABLE courses (
    course_id BIGSERIAL PRIMARY KEY,
    course_name VARCHAR(255) NOT NULL,
    course_code VARCHAR(255) NOT NULL,
    intro VARCHAR(8000),
    content VARCHAR(8000),
    created_by TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_active BOOLEAN DEFAULT FALSE NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
    head_video_url VARCHAR(255),
    head_image_url VARCHAR(255),
    stage DECIMAL(5, 5)
);

-- Create users table
CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    user_code VARCHAR(50),
    intro VARCHAR(8000),
    content VARCHAR(8000),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_active BOOLEAN DEFAULT FALSE NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
    head_video_url VARCHAR(255),
    head_image_url VARCHAR(255),
    stage DECIMAL(5, 5)
);

-- Create units table
CREATE TABLE units (
    unit_id BIGSERIAL PRIMARY KEY,
    unit_name VARCHAR(255),
    unit_code VARCHAR(50),
    intro VARCHAR(8000),
    content VARCHAR(8000),
    created_by TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_active BOOLEAN DEFAULT FALSE NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
    head_video_url VARCHAR(255),
    head_image_url VARCHAR(255),
    module_id BIGINT,
    seq_no INTEGER,
    stage DECIMAL(5, 5)
);

-- Create topics table
CREATE TABLE topics (
    topic_id BIGSERIAL PRIMARY KEY,
    topic_name VARCHAR(255),
    topic_code VARCHAR(50),
    intro VARCHAR(8000),
    content VARCHAR(8000),
    created_by TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_active BOOLEAN DEFAULT FALSE NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
    head_video_url VARCHAR(255),
    head_image_url VARCHAR(255),
    unit_id BIGINT,
    seq_no INTEGER,
    topic_type VARCHAR(50),
    stage DECIMAL(5, 5)
);

-- Create subjects table
CREATE TABLE subjects (
    subject_id BIGSERIAL PRIMARY KEY,
    subject_name VARCHAR(255),
    subject_code VARCHAR(50),
    intro VARCHAR(8000),
    content VARCHAR(8000),
    created_by TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_active BOOLEAN DEFAULT FALSE NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
    head_video_url VARCHAR(255),
    head_image_url VARCHAR(255),
    course_id BIGINT,
    seq_no INTEGER,
    stage DECIMAL(5, 5)
);

-- Create modules table
CREATE TABLE modules (
    module_id BIGSERIAL PRIMARY KEY,
    module_name VARCHAR(255),
    module_code VARCHAR(50),
    intro VARCHAR(8000),
    content VARCHAR(8000),
    created_by TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_active BOOLEAN DEFAULT FALSE NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE NOT NULL,
    head_video_url VARCHAR(255),
    head_image_url VARCHAR(255),
    subject_id BIGINT,
    seq_no INTEGER,
    stage DECIMAL(5, 5)
);