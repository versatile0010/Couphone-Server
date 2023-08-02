-- 샘플 데이터 생성: category 테이블
INSERT INTO category (category_id, created_date, modified_date, name) VALUES (1, '2023-07-31 12:00:00', '2023-07-31 12:00:00', '카페');
INSERT INTO category (category_id, created_date, modified_date, name) VALUES (2, '2023-07-30 09:30:00', '2023-07-30 09:45:00', '음식점');

-- 샘플 데이터 생성: member 테이블
INSERT INTO member (member_id, created_date, modified_date, email, grade, name, password, phone_number, status) VALUES (1, '2023-07-31 12:30:00', '2023-07-31 12:30:00', 'member1@example.com', 'ROLE_MEMBER', '김회원', 'password1232','010-1111-1111', 'ACTIVE');
INSERT INTO member (member_id, created_date, modified_date, email, grade, name, password, phone_number, status) VALUES (2, '2023-07-30 10:30:00', '2023-07-30 10:30:00', 'member2@example.com', 'ROLE_MEMBER', '이회원', 'pass4563', '010-1111-1112', 'ACTIVE');
INSERT INTO member (member_id, created_date, modified_date, email, grade, name, password, phone_number, status) VALUES (3, '2023-07-29 10:30:00', '2023-07-29 10:30:00', 'member3@example.com', 'ROLE_ADMIN', '김관리자', 'pass4564', '010-1111-1113', 'ACTIVE');
INSERT INTO member (member_id, created_date, modified_date, email, grade, name, password, phone_number, status) VALUES (4, '2023-07-28 10:30:00', '2023-07-28 10:30:00', 'member4@example.com', 'ROLE_ADMIN', '이관리자', 'pass4565', '010-1111-1114', 'ACTIVE');

-- 샘플 데이터 생성: brand 테이블
INSERT INTO brand (brand_id, category_id, created_date, modified_date, brand_image_url, name, reward_description, status) VALUES (1, 1, '2023-07-31 12:15:00', '2023-07-31 12:20:00', 'https://example.com/images/brand1.jpg', '메가 커피', '따뜻한 아메리카노 한 잔 무료 증정', 'ACTIVE');
INSERT INTO brand (brand_id, category_id, created_date, modified_date, brand_image_url, name, reward_description, status) VALUES (2, 2, '2023-07-29 10:00:00', '2023-07-29 10:05:00', 'https://example.com/images/brand3.jpg', '버거킹', '주니어 와퍼 한 개 무료 증정', 'ACTIVE');
INSERT INTO brand (brand_id, category_id, created_date, modified_date, brand_image_url, name, reward_description, status) VALUES (3, 1, '2023-07-30 10:00:00', '2023-07-30 10:05:00', 'https://example.com/images/brand2.jpg', '컴포즈 커피', '아이스 아메리카노 한 잔 무료 증정', 'ACTIVE');

-- 샘플 데이터 생성: store 테이블
INSERT INTO store (store_id, latitude, longitude, brand_id, created_date, modified_date, city, name, status, street, zipcode) VALUES (1, 37.12345, -122.67890, 1, '2023-07-31 13:00:00', '2023-07-31 13:00:00', '서울특별시', '메가커피 건대입구점', 'ACTIVE', '건대입구로', '1111');
INSERT INTO store (store_id, latitude, longitude, brand_id, created_date, modified_date, city, name, status, street, zipcode) VALUES (2, 37.12345, -110.67890, 1, '2023-07-31 13:00:00', '2023-07-31 13:00:00', '서울특별시', '메가커피 세종대입구점', 'ACTIVE', '세종대입구로', '2222');
INSERT INTO store (store_id, latitude, longitude, brand_id, created_date, modified_date, city, name, status, street, zipcode) VALUES (3, 37.12345, -100.67890, 2, '2023-07-31 13:00:00', '2023-07-31 13:00:00', '서울특별시', '컴포즈커피 건대입구점', 'ACTIVE', '건대입구로', '1111');

-- 샘플 데이터 생성: coupon_item 테이블
INSERT INTO coupon_item (coupon_item_id, stamp_count, brand_id, created_date, member_id, modified_date, status) VALUES (1, 5, 1, '2023-07-31 12:45:00', 1, '2023-07-31 12:45:00', 'INACTIVE');
INSERT INTO coupon_item (coupon_item_id, stamp_count, brand_id, created_date, member_id, modified_date, status) VALUES (2, 3, 2, '2023-07-30 11:00:00', 1, '2023-07-30 11:00:00', 'EXPIRED');
INSERT INTO coupon_item (coupon_item_id, stamp_count, brand_id, created_date, member_id, modified_date, status) VALUES (3, 4, 2, '2023-07-29 11:00:00', 1, '2023-07-30 11:00:00', 'INACTIVE');
INSERT INTO coupon_item (coupon_item_id, stamp_count, brand_id, created_date, member_id, modified_date, status) VALUES (4, 1, 2, '2023-07-28 11:00:00', 2, '2023-07-30 11:00:00', 'INACTIVE');
INSERT INTO coupon_item (coupon_item_id, stamp_count, brand_id, created_date, member_id, modified_date, status) VALUES (5, 2, 1, '2023-07-27 11:00:00', 2, '2023-07-30 11:00:00', 'INACTIVE');
INSERT INTO coupon_item (coupon_item_id, stamp_count, brand_id, created_date, member_id, modified_date, status) VALUES (6, 10, 1, '2023-07-26 11:00:00', 2, '2023-07-30 11:00:00', 'ACTIVE');
