-- Create table: categories
CREATE TABLE IF NOT EXISTS categories (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL UNIQUE
);

-- Create table: transactions
CREATE TABLE IF NOT EXISTS transactions (
                              id SERIAL PRIMARY KEY,
                              description VARCHAR(255),
                              amount NUMERIC(19, 2) NOT NULL,
                              date DATE NOT NULL,
                              category_id INTEGER REFERENCES categories(id)
);

-- Create table: budgets
CREATE TABLE IF NOT EXISTS budgets (
                         id SERIAL PRIMARY KEY,
                         amount NUMERIC(19, 2) NOT NULL,
                         budget_month INTEGER NOT NULL,
                         budget_year INTEGER NOT NULL,
                         category_id INTEGER REFERENCES categories(id),
                         CONSTRAINT unique_category_month_year UNIQUE (category_id, budget_month, budget_year)
);
