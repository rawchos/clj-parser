---
title: clj-parser.README
created: '2021-02-28T20:12:59.131Z'
modified: '2021-03-01T21:58:56.942Z'
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
Birth dates in files are expected to be in the following format: `2021-03-01`
