INSERT INTO users (id, name, email) VALUES (1, 'Alice Smith', 'alice@example.com'),
                                           (2, 'Bob Johnson', 'bob@example.com'),
                                           (3, 'Charlie Brown', 'charlie@example.com');

INSERT INTO requests (id, description, requestor_id, created)
VALUES (1, 'Request for a drill', 2, TIMESTAMP '2024-01-10 10:00:00'),
       (2, 'Need a ladder for painting', 3, TIMESTAMP '2024-01-15 15:30:00'),
       (3, 'Anybody has an extra oven?', 2, TIMESTAMP '2024-05-01 10:00:00');

INSERT INTO items (id, name, description, is_available, owner_id, request_id)
VALUES (1, 'Drill', 'Electric drill with variable speed', TRUE, 1, 1),
       (2, 'Ladder', '10-foot aluminum ladder', TRUE, 1, 2),
       (3, 'Hammer', 'Standard claw hammer', FALSE, 1, NULL);

INSERT INTO bookings (id, start_date_time, end_date_time, item_id, booker_id, status)
VALUES    (1, TIMESTAMP '2024-02-01 09:00:00', TIMESTAMP '2024-02-01 17:00:00', 1, 2, 'approved'),
          (2, TIMESTAMP '2024-02-05 14:00:00', TIMESTAMP '2024-02-06 14:00:00', 2, 3, 'waiting'),
          (3, TIMESTAMP '2024-03-01 08:00:00', TIMESTAMP '2024-03-01 12:00:00', 3, 2, 'rejected');

INSERT INTO comments (id, text, item_id, author_id, created)
VALUES (1, 'Worked great, thank you!', 1, 2, TIMESTAMP '2024-02-02 10:00:00'),
       (2, 'Very stable ladder. Helped a lot.', 2, 3, TIMESTAMP '2024-02-07 09:00:00');
