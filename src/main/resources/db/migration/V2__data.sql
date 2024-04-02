INSERT INTO role(role)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN');

INSERT INTO "user"(created_at, email, password, updated_at, username)
VALUES (NOW(), 'admin@gmail.com', '$2a$10$U2PsxQGqVHGV92E1Di/ww.mZpOTmaO9yeZ1/gJKm543UxWCYd9mVi', NOW(), 'admin');