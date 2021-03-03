---
title: clj-parser.README
created: '2021-02-28T20:12:59.131Z'
modified: '2021-03-03T17:51:50.764Z'
---

# clj-parser

## Description
This is a basic app that parses records based on 3 different delimited formats: pipeline, comma, and space. There's a command line component that takes a file as a parameter and then parses and displays the records in that file. The REST API component provides a way to parse a single line and display existing records based on sort order.

## Formats
  * pipe-delimited
    `LastName | FirstName | Email | FavoriteColor | DateOfBirth`
  * comma-delimited
    `LastName, FirstName, Email, FavoriteColor, DateOfBirth`
  * space-delimited
    `LastName FirstName Email FavoriteColor DateOfBirth`
    
## Run Tests
```
bin/run-tests
```

## Caveats
 * Birth dates in files are expected to be in the following format: `2021-03-01`
 * Birth dates are being output in M/d/yyyy format
 * I also took some liberties in Avengers birthdays

## Command Line
There are sample files located in the `resources/samples` directory. To process a file, run `bin/parse-file resources/samples/{file.txt}` and the output will be displayed to the screen. You can pass in as many files as you like but each will need to have a size greater than 0. I chose to output the entire record, field names and all.

## REST API
 * Listens on port 8080
 * POST /api/records
   * Body should contain `{"data": "pipe, comma, or space delimited record"}`
   * Returns 409 conflict if this exact record already exists
   * Returns 400 bad request if unable to parse
 * GET /api/records/email
   * Returns current records sorted by email (descending) then last name (ascending)
 * GET /api/records/birthdate
   * Returns current records sorted by birthdate ascending
 * GET /api/records/name
   * Returns current records sorted by last name descending
   
## Running the API
To run the API, you can do either of the following:
 * `clj -M:run`
 * `make run`
