create table authors
(
    id          serial
        primary key,
    name        varchar(255) not null,
    nationality varchar(255) not null,
    birthyear   integer check (birthyear>0 and birthyear<2026)
);

alter table authors
    owner to postgres;

create table books
(
    id             serial
        primary key,
    title          varchar(255) not null,
    isbn           varchar(50) unique,
    author_id      integer
        references authors,
    publish_year   integer check (publish_year>0 and publish_year <=2026),
    book_type      varchar(20)  not null,
    download_url   varchar(500),
    file_size      double precision,
    shelf_location varchar(100),
    weight         double precision,
    available      boolean default true
);

alter table books
    owner to postgres;


--authors first because of foreign key--
INSERT INTO authors (name, nationality, birthyear) VALUES
                                                       ('George Orwell', 'British', 1903),
                                                       ('J.K. Rowling', 'British', 1965),
                                                       ('J.R.R. Tolkien', 'British', 1892),
                                                       ('Fyodor Dostoevsky', 'Russian', 1821),
                                                       ('Harper Lee', 'American', 1926),
                                                       ('Gabriel García Márquez', 'Colombian', 1927),
                                                       ('Paulo Coelho', 'Brazilian', 1947),
                                                       ('Jane Austen', 'British', 1775),
                                                       ('Dan Brown', 'American', 1964),
                                                       ('Stephen King', 'American', 1947);
--ebooks--
INSERT INTO books
(title, isbn, author_id, publish_year, book_type, download_url, file_size, available)
VALUES
    ('1984', '9780451524935', 1, 1949, 'EBOOK',
     'https://ebooks.example.com/1984', 1.2, true),

    ('Animal Farm', '9780451526342', 1, 1945, 'EBOOK',
     'https://ebooks.example.com/animal_farm', 0.9, true),

    ('Harry Potter and the Philosopher''s Stone', '9780747532699', 2, 1997, 'EBOOK',
     'https://ebooks.example.com/hp1', 2.5, true),

    ('Harry Potter and the Chamber of Secrets', '9780747538493', 2, 1998, 'EBOOK',
     'https://ebooks.example.com/hp2', 2.7, true),

    ('The Hobbit', '9780547928227', 3, 1937, 'EBOOK',
     'https://ebooks.example.com/hobbit', 1.8, true),

    ('Crime and Punishment', '9780140449136', 4, 1866, 'EBOOK',
     'https://ebooks.example.com/crime_punishment', 2.2, true),

    ('One Hundred Years of Solitude', '9780060883287', 6, 1967, 'EBOOK',
     'https://ebooks.example.com/solitude', 2.4, true),

    ('The Alchemist', '9780062315007', 7, 1988, 'EBOOK',
     'https://ebooks.example.com/alchemist', 1.1, true),

    ('Pride and Prejudice', '9780141439518', 8, 1813, 'EBOOK',
     'https://ebooks.example.com/pride_prejudice', 1.0, true),

    ('The Da Vinci Code', '9780307474278', 9, 2003, 'EBOOK',
     'https://ebooks.example.com/da_vinci_code', 2.6, true);
--printed books--
INSERT INTO books
(title, isbn, author_id, publish_year, book_type, shelf_location, weight, available)
VALUES
    ('To Kill a Mockingbird', '9780061120084', 5, 1960, 'PRINTED',
     'A1-01', 0.45, true),

    ('The Lord of the Rings: The Fellowship of the Ring', '9780261102354', 3, 1954, 'PRINTED',
     'A2-03', 0.75, true),

    ('The Lord of the Rings: The Two Towers', '9780261102361', 3, 1954, 'PRINTED',
     'A2-04', 0.78, true),

    ('The Lord of the Rings: The Return of the King', '9780261102378', 3, 1955, 'PRINTED',
     'A2-05', 0.80, true),

    ('The Shining', '9780307743657', 10, 1977, 'PRINTED',
     'B1-02', 0.60, true),

    ('It', '9781501142970', 10, 1986, 'PRINTED',
     'B1-03', 1.10, true),

    ('The Da Vinci Code', '9780307474278', 9, 2003, 'PRINTED',
     'C3-01', 0.55, true),

    ('Harry Potter and the Prisoner of Azkaban', '9780747542155', 2, 1999, 'PRINTED',
     'A3-02', 0.50, true),

    ('The Alchemist', '9780061122415', 7, 1988, 'PRINTED',
     'C1-04', 0.40, true),

    ('Pride and Prejudice', '9780141040349', 8, 1813, 'PRINTED',
     'A4-01', 0.42, true);