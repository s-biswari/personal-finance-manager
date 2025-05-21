-- Insert Categories
INSERT INTO categories (name) VALUES
                                  ('Groceries'),
                                  ('Rent'),
                                  ('Utilities'),
                                  ('Entertainment');

-- Insert Transactions
INSERT INTO transactions (description, amount, date, category_id) VALUES
                                                                      ('Supermarket shopping', 150.00, '2025-05-01', 1),
                                                                      ('Monthly Rent', 1200.00, '2025-05-03', 2),
                                                                      ('Electricity Bill', 100.50, '2025-05-05', 3),
                                                                      ('Netflix Subscription', 15.99, '2025-05-10', 4);

-- Insert Budgets
INSERT INTO budgets (amount, budget_month, budget_year, category_id) VALUES
                                                           (500.00, 5, 2025, 1),
                                                           (1300.00, 5, 2025, 2),
                                                           (200.00, 5, 2025, 3),
                                                           (100.00, 5, 2025, 4);
